package com.sundaram.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.sundaram.core.config.ConfigProviderFactory;
import com.sundaram.core.config.LocalConfigProviderImpl;
import com.sundaram.core.config.ProdConfigProviderImpl;

@SpringBootApplication
@ComponentScan
public class NotificationAppContextListener implements ServletContextListener{

	private static final Logger LOGGER = Logger.getLogger( NotificationAppContextListener.class );

	@Value("${spring.profiles.active}")
	public String activeProfile;

	@Override
	public void contextInitialized(ServletContextEvent paramServletContextEvent) {
		LOGGER.info( activeProfile );

		if( activeProfile.contains("production")){
			LOGGER.info("Config profiles is set as prod");
			ConfigProviderFactory.getConfigProvider().init( new ProdConfigProviderImpl());
		} else{
			LOGGER.info("Config profiles is set as dev");
			ConfigProviderFactory.getConfigProvider().init( new LocalConfigProviderImpl());
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent paramServletContextEvent) {
		// TODO Auto-generated method stub
		
	}


	
	
}
