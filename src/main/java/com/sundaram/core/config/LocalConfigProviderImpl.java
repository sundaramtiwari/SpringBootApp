package com.sundaram.core.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class LocalConfigProviderImpl implements IConfigProvider {
	
	@Override
	public Map<String, String> getRedisSettings() {
		Map< String, String> settings = new HashMap<String, String>();
		settings.put(REDIS_CLUSTER_MODE, "false");
		settings.put(REDIS_SINGLE_HOST, "localhost");
		settings.put(REDIS_SINGLE_PORT, "6379");
		settings.put(REDIS_TIME_OUT_IN_SEC, "5000");
		return settings;
	}

	@Override
	public Boolean isCacheEnabled() {
		return false;
	}
}
