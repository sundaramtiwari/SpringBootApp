package com.sundaram.core.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Config provider factory.
 * This class initialize config with other servers.
 * 
 * @author sundaram
 *
 */
@Component
public class ConfigProviderFactory {

	private static final Logger LOGGER = Logger.getLogger( ConfigProviderFactory.class );

	private boolean isInitialized = false;
	
	private static final ConfigProviderFactory _factory = new ConfigProviderFactory();

	private static final List<IConfigInitializer> customInitializers = new ArrayList<IConfigInitializer>();

	private IConfigProvider provider;

	// Redis client
	private JedisCommands commands;
	
	private JedisPool jedisPool;
	
	public void addConfigInitializer( IConfigInitializer configInitializer){
		customInitializers.add(configInitializer);
	}

	
	// return singleton instance
	public static ConfigProviderFactory getConfigProvider() {
		return _factory;
	}

	// Singleton instance
	private ConfigProviderFactory() {}

	/**
	 * Initialize the config based on config provider.
	 * 
	 * @param configProvider configProvider i.e. prod or local
	 */
	public void init(IConfigProvider configProvider) {
		if (isInitialized) {
			throw new RuntimeException("Connection provider is already initialized");
		}
		this.provider = configProvider;

		if (configProvider.isCacheEnabled()) {
			_initRedisConfig(configProvider.getRedisSettings());
		}

		for (IConfigInitializer configInitializer : customInitializers) {
			configInitializer.init(configProvider);
		}

		this.isInitialized = true;
		LOGGER.info("External Configs has been initialized");
	}
	


	/**
	 * Destroy the other server config
	 */
	public void destory()
	{
		if( !isConfigInitalized() ){
			return;
		}
		if( isRedisClusterMode() ){
			try {
				((JedisCluster) commands).close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			((Jedis) commands).close();
		}
		
	}

	/**
	 * returns true/false based whether config initialized or not.
	 * @return
	 */
	public boolean isConfigInitalized()
	{
		return this.isInitialized;
	}

	/**
	 * Redis config client
	 * @param settings
	 */
	private void _initRedisConfig( Map<String, String> settings){
		// Cache settings
		if( isRedisClusterMode()){
			Set<HostAndPort> nodes = new HashSet<HostAndPort>(
					Arrays.asList(
							//cache1
							new HostAndPort(settings.get(IConfigProvider.REDIS_NODE1_HOST), Integer.valueOf(settings.get(IConfigProvider.REDIS_NODE1_PORT))),
							// cache1
							new HostAndPort(settings.get(IConfigProvider.REDIS_NODE2_HOST), Integer.valueOf(settings.get(IConfigProvider.REDIS_NODE2_PORT))),
							// cache2
							new HostAndPort(settings.get(IConfigProvider.REDIS_NODE3_HOST), Integer.valueOf(settings.get(IConfigProvider.REDIS_NODE3_PORT)))));
			commands =  new JedisCluster(nodes);
		}else{
			JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), settings.get(IConfigProvider.REDIS_SINGLE_HOST), 
					Integer.valueOf(settings.get(IConfigProvider.REDIS_SINGLE_PORT)),
					Integer.valueOf(settings.get(IConfigProvider.REDIS_TIME_OUT_IN_SEC)));
			this.jedisPool = jedisPool;
		}
		LOGGER.debug("Jedis client has been initialized");
		
	}
	
	/**
	 * returns true if redis is running in cluster mode.
	 * @return
	 */
	public boolean isRedisClusterMode()
	{
		String isCluster = provider.getRedisSettings().get(IConfigProvider.REDIS_CLUSTER_MODE);
		if( StringUtils.isNotBlank( isCluster ) && "true".equals(isCluster)){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Preconfigured redis client
	 * @return
	 */
	public JedisCommands getRedisClient(){
		if( isConfigInitalized() ){
			if( isRedisClusterMode() ){
				return commands;	
			}else{
				return getJedisPool().getResource();
			}
		}
		throw new RuntimeException("Config is not yet initalized. Call init()");
	}
	
	public JedisPool getJedisPool() {
		return jedisPool;
	}
}
