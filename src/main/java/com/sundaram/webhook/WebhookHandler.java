/**
 * 
 */
package com.sundaram.webhook;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.sundaram.entity.FBEntity;
import com.sundaram.rest.RestPathConstants;
import com.sundaram.service.WebhookService;
import com.sundaram.util.JsonUtil;

/**
 * @author sundaram
 *
 */
@RestController
@RequestMapping(RestPathConstants.API + RestPathConstants.VERSION_1)
public class WebhookHandler {

	private static final Logger LOGGER = Logger.getLogger(WebhookHandler.class);

	@Lazy
	@Autowired
	protected RestTemplate restTemplate;

	@Autowired
	protected WebhookService webhookService;

	@RequestMapping(value="/fb/bot/create", method = RequestMethod.GET)
	public void getFBEntity(){
		webhookService.createFBEntityTable();
	}

	
	/**
	 * Webhook to fetch facebook Id and name of the user.
	 * 
	 * @param fbId
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/fb/bot", method = RequestMethod.GET, produces = "application/json")
	public String getFBEntity( @RequestParam(value = "fbId") String fbId) {

		LOGGER.info(String.format("getFBEntity called for fbId: ", fbId));

		if (fbId == null) {
			LOGGER.warn("fbId missing, cannot fetch FB details.");
			return "Missing mandatory param: fbId";
		}

		FBEntity fbEntity = webhookService.findFBEntityById(fbId);

		return JsonUtil.writeValueAsString(fbEntity);
	}

	@RequestMapping(value="/fb/bot", method = RequestMethod.POST, consumes = "application/json")
	public String saveFBEntity( @RequestBody String fbStr) {
		String response = "Internal error occured.";

		try {
			JSONObject fbJson = (JSONObject) new JSONParser().parse(fbStr);
			LOGGER.info("FB Entity Json: " + fbJson);

			String id = (String) fbJson.get("id");
			String name = (fbJson.get("name") == null || fbJson.get("name").equals("undefined")) ? null : (String) fbJson.get("name");

			response = webhookService.save(id, name);

		} catch (Exception e) {
			e.printStackTrace();
			return response;
		}

		return response;
	}
}
