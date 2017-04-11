/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.session;

import static com.ebay.park.service.ServiceException.createServiceException;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.ebay.park.db.dao.UserSessionDao;
import com.ebay.park.db.entity.UserAdmin;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.session.dto.UserSessionCache;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author juan.pizarro
 * 
 */
@Service
public class SessionServiceImpl implements SessionService {

	private static Logger logger = LogManager.getLogger(SessionServiceImpl.class);

	private static final String SESSION_TTL = "2592000";

	private static final String SESSION_KEY = "#sessionToken";
	private static final String SESSION_CACHE_NAME = "parkCache";
	private static final String SESSION_CACHE = SESSION_CACHE_NAME + "#" + SESSION_TTL;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private UserSessionDao userSessionDao;

	private SessionService proxyOfMe;

	@PostConstruct
	public void initialize() {
		proxyOfMe = (SessionService) applicationContext.getBean(SessionService.class);
	}

	@Override
    @Cacheable(value = SESSION_CACHE, key = SESSION_KEY,  unless="#result == null")
	//@Transactional
	public UserSessionCache getUserSession(String sessionToken) throws ServiceException {
		if (logger.isDebugEnabled()) {
			logger.debug("Session for token {} was not stored in the cache. Will proceed to retrieve it from the DB", sessionToken);
		}
		// if the session is not on cache or cache is down, search on the DB.
		UserSession userSession = userSessionDao.findUserSessionByToken(sessionToken);
		if (userSession != null) {
			return createUserSessionCache(userSession);
		} else {
            logger.error("Can not retrieve a session with token = {} from de DB", sessionToken);
			throw createServiceException(ServiceExceptionCode.SESSION_NOT_STORED);
		}
	}

	@Override
    @CachePut(value = SESSION_CACHE, key = SESSION_KEY)
	public UserSessionCache saveUserSession(String sessionToken, UserSessionCache userSessionCache) {
		if (logger.isDebugEnabled()) {
			logger.debug("Saving user session in cache under token key: {} for username: {}", sessionToken, userSessionCache.getUsername());
		}
		return userSessionCache;

	}

	@Override
	@CacheEvict(value = SESSION_CACHE_NAME, key = SESSION_KEY)
	public void removeUserSession(String sessionToken) {
		if (logger.isDebugEnabled()) {
			logger.debug("Removing user session from the cache uner token key: {}", sessionToken);
		}
	}

	@Override
    public UserSessionCache createUserSessionCache(UserSession userSession) {
		logger.info("About to create UserSessionCache");
		try {
			// Create new session
			UserSessionCache userSessionCache = new UserSessionCache(userSession);
			return proxyOfMe.saveUserSession(userSession.getToken(), userSessionCache);
		} catch (Exception e) {
			logger.error("Error creating UserSessionCache for token {} ", userSession.getToken(), e);
			return null;
		}
	}

	@Override
    public UserSessionCache createUserSessionCache(UserAdmin user, String oldToken) {
		logger.debug("Create user session cache for admin is starting...");
		try {
			// Remove old session first
			if (oldToken != null) {
				proxyOfMe.removeUserSession(oldToken);
			}
			// Create new session
			UserSessionCache userSessionCache = new UserSessionCache(user);
			return proxyOfMe.saveUserSession(user.getToken(), userSessionCache);
		} catch (Exception e) {
			logger.error("Error creating user session Cache for Admin User ", e);
			return null;
		}
	}
}
