/*
 * Copyright eBay, 2014
 */
package com.ebay.park.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author lucia.masola
 * 
 */
@Configuration
@EnableCaching(proxyTargetClass=true)
public class SpringInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext arg0) throws ServletException {
	}

}