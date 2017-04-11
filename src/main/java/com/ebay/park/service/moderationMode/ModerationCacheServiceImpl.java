package com.ebay.park.service.moderationMode;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class ModerationCacheServiceImpl implements ModerationCacheService {
	
	private static Logger logger = LoggerFactory.getLogger(ModerationCacheServiceImpl.class);

	//Used by Memcached 
	private static final String MODERATION_MODE_CACHE_TTL = "900"; //5 min.
	private static final String MODERATION_MODE_CACHE_KEY = "#userId";
	private static final String MODERATION_MODE_ALL_LOCKED_USERS_KEY = "#lockedUsersKey";
	private static final String MODERATION_MODE_CACHE_NAME = "parkCache";
	private static final String MODERATION_MODE_CACHE = MODERATION_MODE_CACHE_NAME + "#" + MODERATION_MODE_CACHE_TTL;
	
	
	@Override
	@Cacheable(value = MODERATION_MODE_CACHE, key = MODERATION_MODE_CACHE_KEY)
	public String getLockOwner(Long userId) {
        logger.debug("User to moderate was not store in the cache.");
	    return null;
	}
	
	@Override
	@CachePut(value = MODERATION_MODE_CACHE , key = MODERATION_MODE_CACHE_KEY)
	public String lockUser(Long userId, String moderatorUserToken) {
        logger.debug("Saving user to lock for moderation in cache.");
		return moderatorUserToken;
	}
	
	@Override
	@CacheEvict(value = MODERATION_MODE_CACHE, key = MODERATION_MODE_CACHE_KEY)
	public void unlockUser(Long userId) {
        logger.debug("Removing user lock for moderation.");
	}

	@Override
	@Cacheable(value = MODERATION_MODE_CACHE,  key = MODERATION_MODE_ALL_LOCKED_USERS_KEY )
	public ArrayList<Long> getLockedUsers(String lockedUsersKey) {
		logger.debug("There is no locked users stored in cache");
		return null;
	}

	@Override
	@CachePut(value = MODERATION_MODE_CACHE , key = MODERATION_MODE_ALL_LOCKED_USERS_KEY)
	public ArrayList<Long> saveUsers(String lockedUsersKey, ArrayList<Long> users) {
		logger.debug("Saving locked users in cache.");
		return users;
	}
	
}
