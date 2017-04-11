package com.ebay.park.service.moderation;

import com.ebay.park.db.dao.UserAdminDao;
import com.ebay.park.db.entity.UserAdmin;
import com.ebay.park.db.entity.UserRole;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.ParkConstants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * Intercepter on services calls for moderation in order to obtain the
 * authorization token sent in the header and verifies if it belongs to a Admin
 * user if it is not, it throws and exception.
 * 
 * @author cbirge
 * 
 */
public class ModerationInterceptor extends HandlerInterceptorAdapter implements ParkConstants {

	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private UserAdminDao userAdminDao;

	/**
	 * The logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ModerationInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// Get the Auth Token
		final String parkToken = request.getHeader(PARK_TOKEN_HEADER);
		LOGGER.debug("ModerationInterceptor is about to run");
		// validates token
		if (StringUtils.isBlank(parkToken)) {
			LOGGER.error("All moderation actions MUST receive a token in the header. In this case it's blank or null");
			throw createServiceException(ServiceExceptionCode.EMPTY_PARK_TOKEN);
		}

		// Obtains session from token
		UserSessionCache userSession = null;
		try {
            LOGGER.debug("Trying to get the user under token {} from the session...", parkToken);
			userSession = sessionService.getUserSession(parkToken);
            LOGGER.debug("User retreived: {}", userSession);
		} catch (ServiceException se) {
            if (ServiceExceptionCode.SESSION_NOT_STORED.getCode() == se.getCode()) {
                LOGGER.debug("The session is not persisted in the DB nor in cache. Let's try to find the user admin through userAdminDao");
            } else {
                LOGGER.error("Unexcepcted ServiceException!!", se);
            }
			String oldToken = null;
			UserAdmin moderation = userAdminDao.findByToken(parkToken);
			if (moderation != null) {
			    LOGGER.debug("UserAdmin found using userAdminDao: [username={}, email{}]", moderation.getUsername(), moderation.getEmail());
				userSession = sessionService.createUserSessionCache(moderation, oldToken);
			} else {
				// No moderator
                LOGGER.warn("Can not get a session with token={}", parkToken);
				throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
			}	
		}

		LOGGER.debug("Ok, now we need to validate if is a Admin Session");
		if (userSession == null || !userSession.is(UserRole.MODERATOR)) {
            LOGGER.debug("NO, he/she isn't! Get out of here!!!");
            throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED_ADMIN);
        }
        LOGGER.debug("Yes, he/she is!");
		return super.preHandle(request, response, handler);
	}
}
