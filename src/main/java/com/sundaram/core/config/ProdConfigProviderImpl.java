package com.sundaram.core.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ProdConfigProviderImpl implements IConfigProvider {

	@Override
	public Map<String, String> getRedisSettings() {

		// Provide prod redis IP here
		String redisHostIP = "";
		Map< String, String> settings = new HashMap<String, String>();
		settings.put(REDIS_CLUSTER_MODE, "true");
		settings.put(REDIS_NODE1_HOST, redisHostIP);
		settings.put(REDIS_NODE1_PORT, "7000");
		
		settings.put(REDIS_NODE2_HOST, redisHostIP);
		settings.put(REDIS_NODE2_PORT, "7001");
		
		settings.put(REDIS_NODE3_HOST, redisHostIP);
		settings.put(REDIS_NODE3_PORT, "7002");
		return settings;
	}

	@Override
	public Boolean isCacheEnabled() {
		return false;
	}
}
