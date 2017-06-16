package com.sundaram.core.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author sundaram
 *
 */
@Component
public class MyApplicationContextProvider implements ApplicationContextAware{

	public static ApplicationContext context;
	public static final Map<String, Object> TEST_MAP = new HashMap<String, Object>();


	public static ApplicationContext getApplicationContext() {
		return context;
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		MyApplicationContextProvider.context = context;    
	}


	/**
	 * Returns the desired bean from spring application context.
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean( Class<T> clazz){
		if( context == null ){
			T bean = (T) TEST_MAP.get(clazz.getName());
			if( bean != null ){
				return bean;
			}
			throw new RuntimeException("App context is not initialized");
		}

		return context.getBean(clazz);
	}
	
	/**
	 * 
	 * For test purpose.
	 * @param bean
	 */
	public static void setBean(Object bean){
		TEST_MAP.put(bean.getClass().getName(), bean);
	}
}
