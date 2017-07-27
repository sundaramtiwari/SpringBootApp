package com.sundaram.rest.thirdparty;

import java.io.InputStream;
import java.util.Collections;

import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisher.Reviews;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.api.services.androidpublisher.model.Review;
import com.google.api.services.androidpublisher.model.ReviewsListResponse;
import com.sundaram.rest.RestPathConstants;
import com.sundaram.service.WebhookService;

/**
 * @author sundaram
 *
 */
@RestController
@RequestMapping(RestPathConstants.API + RestPathConstants.VERSION_1 + RestPathConstants.SOCIAL)
public class RestSocialAPI {

	private static final Logger LOGGER = Logger.getLogger(RestSocialAPI.class);

	private static final String PLAYSTORE_KEY_JSON = "no-broker-192ec4f01749.json";

	@Autowired
	protected WebhookService webhookService;

	@RequestMapping(value="/fb/save", method = RequestMethod.POST, produces = "application/json")
	public String saveFBId(@QueryParam(value = "fbId") String fbId) {
		
		String response = webhookService.save(fbId, null);

		JSONObject responseJson = new JSONObject();
		responseJson.put("data", response);
		return responseJson.toString();

	}
	/**
	 * @param appName
	 * @return reviews json
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/google/reviews/{appName}", method = RequestMethod.GET, produces = "application/json")
	public String getProductDetails(@PathVariable(value = "appName") String appName) {

		LOGGER.info("Fetching playstore reviews for app: " + appName);
		JSONObject jsonObject = new JSONObject();

		try {
			HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

			InputStream playStoreJsonStream = ClassLoader.getSystemResourceAsStream(PLAYSTORE_KEY_JSON);
			GoogleCredential credential = GoogleCredential.fromStream(playStoreJsonStream)
					.createScoped(Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER));

			AndroidPublisher init = new AndroidPublisher.Builder(httpTransport, jsonFactory, credential)
					.setApplicationName(appName).build();

			Reviews reviews = init.reviews();
			reviews.list(appName).put("serviceAccountScope",
					Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER));
			ReviewsListResponse execute = reviews.list(appName).execute();

			java.util.List<Review> reviewList = execute.getReviews();
			// TokenPagination tokenPagination = execute.getTokenPagination();

			jsonObject.put("reviewList", reviewList);

		} catch (Exception e) {
			LOGGER.error("Error occured in fetching play-store reviews.", e);
		}

		return jsonObject.toString();
	}
}
