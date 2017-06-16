package com.sundaram.cache;

import org.springframework.stereotype.Component;

import com.sundaram.core.config.ConfigProviderFactory;

import redis.clients.jedis.JedisCommands;

/**
 * Client to be used for redis command executions.
 * 
 * @author sundaram
 *
 */
@Component
public class RedisClient{

	private final ThreadLocal<JedisCommands> redisResource = new ThreadLocal<JedisCommands>();
	
	private static final RedisClient CLIENT = new RedisClient();
	
	
	public static RedisClient build(){
		return CLIENT;
	};
	
	public JedisCommands getClient() {
		JedisCommands commands =  redisResource.get();
		if( commands == null ){
			JedisCommands newCommand = ConfigProviderFactory.getConfigProvider().getRedisClient();
			setClient(newCommand);
			return newCommand;
		}
		
		return commands;
	}
	
	public void setClient( JedisCommands commands) {
		redisResource.set(commands);
	}
}
