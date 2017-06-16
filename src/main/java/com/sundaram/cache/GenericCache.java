package com.sundaram.cache;



/**
 * 
 * 
 * Generic cache implementation
 * @author sundaram
 *
 */
public interface GenericCache {

	String PRODUCT_CACHE = "product";

	/**
	 * Prepares the actual cache key by object key.
	 * Actually we use namespace:key as actual cache
	 *  
	 * @param key
	 * @return actual cache key
	 */
	public String getCacheKey( String key);
	
	/**
	 * Removes entity with key from cache
	 *  
	 * @param key
	 */
	public void remove( String key );
	
	/**
	 * 
	 * Is cache exist by entityKey
	 * 
	 * @param entityKey
	 * @return true/false
	 */
	public boolean exists(String key);
	
	/**
	 * find value from cache by key
	 * @param key
	 * @return
	 */
	public String getFromCache( String key);

	/**
	 * save value in cache by key
	 * @param key              look-up key 
	 * @param value           value to be cached
	 */
	public void saveInCache( String key, String value);

}
