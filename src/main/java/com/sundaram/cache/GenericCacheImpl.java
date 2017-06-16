package com.sundaram.cache;

import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.JedisCommands;


/**
 * 
 * Cache implementation class which is based on redis
 * 
 * @author sundaram
 *
 * @param <E>		Element which needs to be cached
 */
public class GenericCacheImpl implements GenericCache{

	protected final String namespace;
	
	// One Month
	protected static final int ONE_MONTH = 30*24*3600;

	protected static final int INFINITE = -1;
	
	protected final int EXPIRY_TIME;
	
	/**
	 * Namespace for redis key
	 * @param namespace
	 */
	public GenericCacheImpl(String namespace ) {
		this( namespace, INFINITE);
	}

	/**
	 * Provide expireTimeInSeconds as -1 if you don't want to enable expire.
	 * 
	 * @param namespace
	 * @param expireTimeInSeconds
	 */
	public GenericCacheImpl(String namespace, int expireTimeInSeconds ) {
		this.namespace = namespace;
		this.resource = RedisClient.build().getClient();
		this.EXPIRY_TIME = expireTimeInSeconds;
	}
	
	protected JedisCommands resource;
	
	/**
	 * 
	 * Generates a cache key with namespace.
	 * 
	 * @param entityKey
	 * @return
	 */
	public String getCacheKey( String entityKey){
		return new StringBuilder().append( namespace ).append(":").append(entityKey).toString();
	}
	
	public void remove(String entityKey) {
		String cacheKey = getCacheKey(entityKey);
		resource.del(cacheKey);
	}
	
	public boolean exists(String entityKey) {
		String cacheKey = getCacheKey(entityKey);
		return resource.exists(cacheKey);
	}
	
	public String getFromCache( String entityKey){
		String cacheKey = getCacheKey(entityKey);
		String cachedObjectJSON = resource.get(cacheKey);
		if( StringUtils.isEmpty(cachedObjectJSON)){
			return null;
		}
		return cachedObjectJSON;
	}
	
	public void saveInCache( String entityKey, String value){
		String cacheKey = getCacheKey(entityKey);
		resource.set(cacheKey, value);
		if ( EXPIRY_TIME != -1 ) {
			resource.expire(cacheKey, EXPIRY_TIME);
		}
	}
	
	
	
}
