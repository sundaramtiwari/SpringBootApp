package com.sundaram.core.config;


/**
 * 
 * Custom holder to initialize configuration based on env 
 * @author sundaram
 *
 */
public interface IConfigInitializer {

	/**
	 * Initialize the config
	 * 
	 * @param configProvider     Setting holder based on environment
	 */
	public void init(IConfigProvider configProvider);

}
