package com.ebay.park.service.session.interceptor;

import com.ebay.park.db.dao.UserSessionDao;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.command.UserSessionUpdaterCmd;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.service.session.dto.UserSessionUpdaterRequest;
import com.ebay.park.util.ParkConstants;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.ebay.park.service.ServiceException.createServiceException;

public class SessionInterceptor extends HandlerInterceptorAdapter implements ParkConstants {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private UserSessionDao userSessionDao;
	
	@Autowired
	private UserSessionUpdaterCmd userSessionUpdaterCmd;


	/**
	 * The logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(SessionInterceptor.class);

	/**
	 * It intercepts the calls to Park services and obtains the authorization
	 * token sent in the header. It verifies if the session is alive. If it is
	 * not, it throws and exception.
	 */
	@Override
	public boolean preHandle(final HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// Get the Auth Token
		final String parkToken = request.getHeader(PARK_TOKEN_HEADER);

		// validates token
		if (StringUtils.isBlank(parkToken)) {
			LOG.info("trying to get a session without a token");
			throw createServiceException(ServiceExceptionCode.EMPTY_PARK_TOKEN);
		}

		// validates session
		try {
			UserSessionCache userSessionCache = sessionService.getUserSession(parkToken);
			userSessionUpdaterCmd.execute(new UserSessionUpdaterRequest(userSessionCache, parkToken));
		} catch (ServiceException se) {

			UserSession userSession = userSessionDao.findUserSessionByToken(parkToken);

			if (userSession != null) {
				sessionService.createUserSessionCache(userSession);
			} else {
                LOG.info("can not get a session with token={}", parkToken);
				throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
			}
		}

		return super.preHandle(request, response, handler);
	}

}
