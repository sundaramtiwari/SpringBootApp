package com.sundaram.util;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JsonUtil {

	public static final ObjectMapper MAPPER = new ObjectMapper();

	public static Object parse( String jsonString) {
		Object obj = null;
		try {
			obj = new JSONParser().parse(jsonString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static String writeValueAsString(Object obj) {
		String value = null;
		try {
			value = MAPPER.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return value;
	}
}
