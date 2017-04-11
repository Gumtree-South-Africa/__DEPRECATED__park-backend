/*
 * Copyright eBay, 2014
 */
package com.ebay.park.config;

import com.google.code.ssm.Cache;
import com.google.code.ssm.CacheFactory;
import com.google.code.ssm.config.DefaultAddressProvider;
import com.google.code.ssm.providers.CacheConfiguration;
import com.google.code.ssm.providers.spymemcached.MemcacheClientFactoryImpl;
import com.google.code.ssm.spring.ExtendedSSMCacheManager;
import com.google.code.ssm.spring.SSMCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Configures needed classes for memcached.
 * 
 * @author lucia.masola
 */
@Configuration
@EnableCaching
public class MemcachedConfig {

	@Value("${memcached.cacheName}")
	private String cacheName;
	
	@Value("${memcached.notTemplateName}")
	private String notificationCacheName;

	@Value("${memcached.address}")
	private String cacheAddress;

	@Value("${memcached.defaultTTL}")
	private int cacheTTL;

	@Bean
	public CacheFactory sessionCacheFactory() {
		CacheFactory cacheFactory = new CacheFactory();
		cacheFactory.setCacheName(cacheName);

		MemcacheClientFactoryImpl cacheClientFactory = new MemcacheClientFactoryImpl();
		cacheFactory.setCacheClientFactory(cacheClientFactory);

		DefaultAddressProvider addressProvider = new DefaultAddressProvider();
		addressProvider.setAddress(cacheAddress);
		cacheFactory.setAddressProvider(addressProvider);

		CacheConfiguration cacheConfiguration = new CacheConfiguration();
		cacheConfiguration.setConsistentHashing(true);
		cacheFactory.setConfiguration(cacheConfiguration);

		return cacheFactory;
	}
	
	@Bean
	public CacheFactory notTemplateCacheFactory() {
		CacheFactory cacheFactory = new CacheFactory();
		cacheFactory.setCacheName(notificationCacheName);

		MemcacheClientFactoryImpl cacheClientFactory = new MemcacheClientFactoryImpl();
		cacheFactory.setCacheClientFactory(cacheClientFactory);

		DefaultAddressProvider addressProvider = new DefaultAddressProvider();
		addressProvider.setAddress(cacheAddress);
		cacheFactory.setAddressProvider(addressProvider);

		CacheConfiguration cacheConfiguration = new CacheConfiguration();
		cacheConfiguration.setConsistentHashing(true);
		cacheFactory.setConfiguration(cacheConfiguration);

		return cacheFactory;
	}

	@Bean
	public ExtendedSSMCacheManager cacheManager(List<Cache> caches) {
		ExtendedSSMCacheManager cacheManager = new ExtendedSSMCacheManager();
		Collection<SSMCache> ssmCaches = new ArrayList<SSMCache>();
		
		for(Cache cache: caches){
			SSMCache ssmCache = new SSMCache(cache, cacheTTL, false);
			ssmCaches.add(ssmCache);
		}

		cacheManager.setCaches(ssmCaches);
		return cacheManager;
	}

}
