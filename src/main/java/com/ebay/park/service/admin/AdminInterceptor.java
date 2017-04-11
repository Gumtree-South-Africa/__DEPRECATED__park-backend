package com.ebay.park.service.admin;

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

import static com.ebay.park.service.ServiceException.createServiceException;

public class AdminInterceptor extends HandlerInterceptorAdapter implements ParkConstants {

	@Autowired
	private SessionService sessionService;

	/**
	 * The logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		// Get the Auth Token
		final String parkToken = request.getHeader(PARK_TOKEN_HEADER);

		// validates token
		if (StringUtils.isBlank(parkToken)) {
			LOGGER.info("trying to get a session without a token");
			throw createServiceException(ServiceExceptionCode.EMPTY_PARK_TOKEN);
		}

		// Obtains session from token
		UserSessionCache userSession = null;
		try {
			userSession = sessionService.getUserSession(parkToken);

		} catch (ServiceException se) {
            LOGGER.info("Can not get a session with token={}", parkToken);
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}

		// validates if is a Admin Session
		if (userSession == null || !userSession.is(UserRole.SUPER_ADMIN)) {
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED_SUPER_ADMIN);
		}

		return super.preHandle(request, response, handler);
	}
}
