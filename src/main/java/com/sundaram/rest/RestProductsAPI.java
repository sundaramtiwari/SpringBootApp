/**
 * 
 */
package com.sundaram.rest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sundaram.entity.Product;
import com.sundaram.entity.Product.CurrencyCode;
import com.sundaram.entity.Product.CurrentPrice;
import com.sundaram.service.ProductService;
import com.sundaram.util.JsonUtil;

/**
 * @author sundaram
 *
 */
@RestController
@RequestMapping(RestPathConstants.API + RestPathConstants.VERSION_1)
public class RestProductsAPI {

	private static final Logger LOGGER = Logger.getLogger(RestProductsAPI.class);

	@Lazy
	@Autowired
	protected RestTemplate restTemplate;

	@Autowired
	protected ProductService productService;

	@RequestMapping(value="/products/{productId}", method = RequestMethod.GET, produces = "application/json")
	public String getProductDetails( @PathVariable(value = "productId") Integer productId ) throws JsonProcessingException {

		LOGGER.info(String.format("getProductDetails called for productId: ", productId));

		if (productId == null) {
			LOGGER.warn("ProductId missing, cannot fetch product details.");
			return "Missing mandatory param: productId";
		}

		/**
		 *  Retrieve the product name from an external API:
		 *  However, since API is not available, I'm hitting a sample api and logging the response.
		 */
		String productStr = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", String.class);

		if (StringUtils.isNotBlank(productStr)) {
			JSONObject productJson = (JSONObject) JsonUtil.parse(productStr);
			LOGGER.info("Product Json: " + productJson);
		}

		// Fetch product details from DB and Cache
		Product product = productService.findProductById(productId);

		return JsonUtil.writeValueAsString(product);
	}

	@RequestMapping(value="/products/{productId}", method = RequestMethod.POST, consumes = "application/json")
	public String addProduct( 
			@RequestBody String productStr,
			@PathVariable(value = "productId") Integer productId ) {
		String resposne = "Internal error occured.";

		try {
			JSONObject productJson = (JSONObject) new JSONParser().parse(productStr);
			LOGGER.info("Product Json: " + productJson);

			Long id = (Long) productJson.get("id");
			String name = productJson.get("name") == null ? null : (String) productJson.get("name");
			Double value = productJson.get("value") == null ? null : (Double) productJson.get("value");
			String currencyCode = productJson.get("currencyCode") == null ? null : (String) productJson.get("currencyCode");

			Product product = productService.findProductById(id.intValue());

			if (product.getId() != null) {
				return "Product already exists with Id: " + id;
			}

			product = new Product().setName(name).setId(id.intValue());
			CurrentPrice currentPrice = product.new CurrentPrice().setValue(value)
					.setCurrencyCode(Product.CurrencyCode.valueOf(currencyCode));

			product.setCurrentPrice(currentPrice);

			resposne = productService.save(product);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return resposne;
	}

	@RequestMapping(value="/products/{productId}", method = RequestMethod.PUT, consumes = "application/json")
	public String updateProduct(
			@RequestBody String productStr,
			@PathVariable(value = "productId") Integer productId ) {
		String resposne = "Internal error occured.";

		try {
			JSONObject productJson = (JSONObject) new JSONParser().parse(productStr);
			LOGGER.info("Product Json: " + productJson);

			Long id = (Long) productJson.get("id");
			String name = productJson.get("name") == null ? null : (String) productJson.get("name");
			Double value = productJson.get("value") == null ? null : (Double) productJson.get("value");
			String currencyCode = productJson.get("currencyCode") == null ? null : (String) productJson.get("currencyCode");

			Product product = productService.findProductById(id.intValue());
			
			if (product.getId() == null) {
				return "Product does not exists with Id: " + id;
			} else if (name == null && value == null && currencyCode == null) {
				return "Nothing to update.";
			} else {
				// Update the product information
				if (StringUtils.isNotBlank(name))
					product.setName(name);
				
				if (StringUtils.isNotBlank(currencyCode))
					product.setCurrentPrice(product.getCurrentPrice().setCurrencyCode(CurrencyCode.valueOf(currencyCode)));
				
				if (value != null) {
					product.setCurrentPrice(product.getCurrentPrice().setValue(value));
				}

				resposne = productService.update(product);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return resposne;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
