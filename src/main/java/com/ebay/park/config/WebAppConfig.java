/*
 * Copyright eBay, 2014
 */
package com.ebay.park.config;

import com.ebay.park.controller.AdminController;
import com.ebay.park.controller.ModerationController;
import com.ebay.park.controller.ModerationModeController;
import com.ebay.park.service.admin.AdminInterceptor;
import com.ebay.park.service.moderation.ModerationInterceptor;
import com.ebay.park.service.profile.interceptor.UserInterceptor;
import com.ebay.park.service.session.interceptor.SessionInterceptor;
import com.ebay.park.version.interceptor.ApiVersionInterceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author juan.pizarro
 * 
 */
@EnableWebMvc
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {

	private static final String ALL = "/**";

	@Value("${session.interceptor.exclude_path_patterns}")
	private String excludePathPattens;
	
	@Value("${apiVersion.interceptor.exclude_path_patterns}")
	private String apiVersionInterceptorPathPatternsToExclude;
	
	@Value("${profile.interceptor.base_path}")
	private String profileInterceptorBasePath;

	@Value("${profile.interceptor.include_path_patterns}")
	private String profileInterceptorPathPatternsToInclude;

	@Value("${profile.interceptor.exclude_path_patterns}")
	private String profileInterceptorPathPatternsToExclude;

	@Value("${moderation.interceptor.exclude_path_patterns}")
	private String moderationInterceptorPathPatternsToExclude;
	
	@Bean
	public AsyncHandlerInterceptor sessionInterceptor() {
		return new SessionInterceptor();
	}

	@Bean
	public UserInterceptor userInterceptor() {
		return new UserInterceptor();
	}

	@Bean
	public ModerationInterceptor moderationInterceptor() {
		return new ModerationInterceptor();
	}

	@Bean
	public AdminInterceptor adminInterceptor() {
		return new AdminInterceptor();
	}

	@Bean
	public ApiVersionInterceptor apiVersionInterceptor() {
		return new ApiVersionInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		ApiVersionInterceptor apiVersionInterceptor = apiVersionInterceptor();

		InterceptorRegistration apiVersionRegis = registry.addInterceptor(
				apiVersionInterceptor).addPathPatterns(ALL);
		for (String pattern : apiVersionInterceptorPathPatternsToExclude.split(",")) {
			apiVersionRegis.excludePathPatterns(pattern);
		}
		
		InterceptorRegistration registration = registry.addInterceptor(
				sessionInterceptor()).addPathPatterns(ALL);
		for (String pattern : excludePathPattens.split(",")) {
			registration.excludePathPatterns(pattern);
		}

		UserInterceptor userInterceptor = userInterceptor();
		userInterceptor.setBasePath(profileInterceptorBasePath);

		InterceptorRegistration userInterceptorReg = registry
				.addInterceptor(userInterceptor);
		for (String pattern : profileInterceptorPathPatternsToInclude
				.split(",")) {
			userInterceptorReg.addPathPatterns(pattern);
		}
		for (String pattern : profileInterceptorPathPatternsToExclude
				.split(",")) {
			userInterceptorReg.excludePathPatterns(pattern);
		}

		// Moderation Interceptor
		ModerationInterceptor moderationInterceptor = moderationInterceptor();
		InterceptorRegistration moderationInterceptorReg = registry
				.addInterceptor(moderationInterceptor);
		moderationInterceptorReg
		.addPathPatterns(ModerationController.MODERATION_PATH + "/**");
		moderationInterceptorReg
		.addPathPatterns(ModerationModeController.MODERATION_MODE_PATH + "/**");
		for (String pattern : moderationInterceptorPathPatternsToExclude
				.split(",")) {
			moderationInterceptorReg
			.excludePathPatterns(ModerationController.MODERATION_PATH
					+ pattern);
		}

		// Admin Interceptor
		AdminInterceptor adminInterceptor = adminInterceptor();
		InterceptorRegistration adminInterceptorReg = registry
				.addInterceptor(adminInterceptor);
		adminInterceptorReg.addPathPatterns(AdminController.ADMIN_PATH + "/**");

	}

}