package com.sundaram.controller;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	private static final Logger LOGGER = Logger.getLogger(HomeController.class);

	@RequestMapping("/")
	public String index(Map<String, Object> model) {
		UUID uuid = UUID.randomUUID();
        String user_ref = StringUtils.replace(uuid.toString(), "-", "");
		model.put("user_ref", user_ref);
		LOGGER.info("user_ref: " + user_ref);
		return "index2";
	}
}
