/*
 * Copyright eBay, 2014
 */
package com.ebay.park.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

/**
 * @author juan.pizarro
 */
@Configuration
public class SocialConfig {

	@Value("${social.facebook.app_secret}")
	private String appSecret;

	@Value("${social.facebook.app_id}")
	private String appId;

	@PostConstruct
	private void init() {
		// hack for the login of facebook.
		try {
			String[] fieldsToMap = { "id", "about", "age_range", "bio", "birthday", "context", "cover",
					"currency", "devices", "education", "email", "favorite_athletes", "favorite_teams", "first_name",
					"gender", "hometown", "inspirational_people", "installed", "install_type", "is_verified",
					"languages", "last_name", "link", "locale", "location", "meeting_for", "middle_name", "name",
					"name_format", "political", "quotes", "payment_pricepoints", "relationship_status", "religion",
					"security_settings", "significant_other", "sports", "test_group", "timezone", "third_party_id",
					"updated_time", "verified", "viewer_can_send_gift", "website", "work" };

			Field field = Class.forName("org.springframework.social.facebook.api.UserOperations")
					.getDeclaredField("PROFILE_FIELDS");
			field.setAccessible(true);

			Field modifiers = field.getClass().getDeclaredField("modifiers");
			modifiers.setAccessible(true);
			modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			field.set(null, fieldsToMap);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Bean
	public ConnectionFactoryLocator connectionFactoryLocator() {
		ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
		registry.addConnectionFactory(new FacebookConnectionFactory(appId, appSecret));
		return registry;
	}

}