package com.ebay.park.service.profile.interceptor;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * Intercepter on services calls in order to obtain the authorization
 * token sent in the header and verifies if it belongs to the user being processed by the call. If it is
 * not, it throws and exception. *
 * @author cbirge
 *
 */
public class UserInterceptor extends HandlerInterceptorAdapter implements ParkConstants {

	@Autowired
	private SessionService sessionService;


	private String basePath;

	/**
	 * The logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(UserInterceptor.class);


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// Get the Auth Token
		final String parkToken = request.getHeader(PARK_TOKEN_HEADER);

		// validates token
		if (StringUtils.isBlank(parkToken)) {
			LOG.info("trying to get a session without a token");
			throw createServiceException(ServiceExceptionCode.EMPTY_PARK_TOKEN);
		}

		// Obtain username from request
		String username =  obtainUsernameFromRequest(request.getRequestURI());
		if (StringUtils.isBlank(username)) {
			LOG.info("The username isn't present in the requested url");
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

		// Obtains session from token
		UserSessionCache userSession = null;
		try {
			userSession = sessionService.getUserSession(parkToken);

		} catch (ServiceException se) {
            LOG.info("can not get a session with token={}", parkToken);
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}

		// validates session against username
		if (userSession == null || !username.equals(userSession.getUsername()))
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);

		return super.preHandle(request, response, handler);
	}


	/**
	 * Extract the username from the url
	 * @param requestedURL format: {basePath}/{username}/*
	 * @return
	 */
	protected String obtainUsernameFromRequest(String requestedURL){
		if (!StringUtils.isBlank(requestedURL)){
			Pattern p = Pattern.compile(basePath+"(v[\\d+].[\\d+]|v[\\d+])/([a-zA-Z0-9]*)/(.*)");
			Matcher m = p.matcher(requestedURL);
			if(m.matches()) {
				return m.group(2);
			}	  
		}
		return null;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
}
