package com.sundaram.service;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sundaram.cache.GenericCache;
import com.sundaram.cache.GenericCacheImpl;
import com.sundaram.dao.ProductDao;
import com.sundaram.entity.Product;
import com.sundaram.entity.Product.CurrencyCode;
import com.sundaram.entity.Product.CurrentPrice;
import com.sundaram.util.JsonUtil;

@Service
public class ProductService {

	@Value("${cache.enabled}")
	protected Boolean cacheEnabled;

	@Autowired
	protected ProductDao productDao;

	public Product findProductById(Integer productId) {
		Product product = productDao.findProductById(productId);
		CurrentPrice currentPrice = product.new CurrentPrice();

		// Flag for accessing cache.
		if (cacheEnabled) {
			try {
				GenericCache productCache = new GenericCacheImpl(GenericCache.PRODUCT_CACHE);
				String productCacheStr = productCache.getFromCache(String.valueOf(productId));
				JSONObject currencyJson = (JSONObject) JsonUtil.parse(productCacheStr);
				currentPrice.setCurrencyCode(CurrencyCode.valueOf((String) currencyJson.get("currency_code")));
				currentPrice.setValue((Double) currencyJson.get("value"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		product.setCurrentPrice(currentPrice);

		return product;
	}

	public String save(Product product) {
		String response = productDao.save(product);

		if (response.contains("Saved product") && cacheEnabled) {
			GenericCache productCache = new GenericCacheImpl(GenericCache.PRODUCT_CACHE);
			productCache.saveInCache(String.valueOf(product.getId()),
				JsonUtil.writeValueAsString(product.getCurrentPrice()));
		}
		
		return response;
	}

	public String update(Product product) {
		String response = productDao.update(product);

		if (response.contains("Updated product") && cacheEnabled) {
			try {
				GenericCache productCache = new GenericCacheImpl(GenericCache.PRODUCT_CACHE);
				productCache.saveInCache(String.valueOf(product.getId()),
						JsonUtil.writeValueAsString(product.getCurrentPrice()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return response;
	}

}
