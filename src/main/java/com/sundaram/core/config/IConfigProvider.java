package com.sundaram.core.config;

import java.util.Map;


/**
 * Configuration provider
 * @author sundaram
 *
 */
public interface IConfigProvider {

	// true or false
	String REDIS_CLUSTER_MODE = "redis.cluster.mode";

	String REDIS_SINGLE_HOST = "redis.host";
	String REDIS_SINGLE_PORT = "redis.port";
	String REDIS_TIME_OUT_IN_SEC = "redis.timeout";

	String REDIS_NODE1_HOST = "redis.host1.address";
	String REDIS_NODE1_PORT = "redis.host1.port";
	String REDIS_NODE2_HOST = "redis.host2.address";
	String REDIS_NODE2_PORT = "redis.host2.port";
	String REDIS_NODE3_HOST = "redis.host3.address";
	String REDIS_NODE3_PORT = "redis.host3.port";

	/**
	 * Redis config settings
	 * 
	 * @return
	 */
	public Map<String, String> getRedisSettings();

	public Boolean isCacheEnabled();
}
