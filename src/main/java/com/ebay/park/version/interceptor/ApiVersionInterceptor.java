package com.ebay.park.version.interceptor;

import static com.ebay.park.service.ServiceException.createServiceException;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.util.ParkConstants;

/**
 * Verifies current Back End Version compatibility. If it is not, it throws and
 * exception.
 * 
 * @author gabriel.sideri
 *
 */
public class ApiVersionInterceptor extends HandlerInterceptorAdapter implements
		ParkConstants {

	@Autowired
	private SessionService sessionService;
	
	private static final String ORIGINAL_REQUESTED_URL_ATT = "javax.servlet.error.request_uri";
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		// Get the API VERSION & PLARFORM Headers
		final String apiVersion = request.getHeader(API_VERSION_HEADER);
		final String platform = request.getHeader(PLATFORM_HEADER);
		final String token = request.getHeader(PARK_TOKEN_HEADER);
	
		String url = request.getDispatcherType().equals(DispatcherType.ERROR) ? request
				.getAttribute(ORIGINAL_REQUESTED_URL_ATT).toString() : request
				.getRequestURI();
			
		try {
			validateCompatibility(platform, apiVersion, url);
		} catch (ServiceException e) {
			if (token != null) {
				sessionService.removeUserSession(token);
			}
			throw e;
		}

		return super.preHandle(request, response, handler);
	}

	private void validateCompatibility(String platform, String apiVersion, String url) {
		
		// Block Version < 0.9.8 - Check Headers
		if (StringUtils.isNotBlank(platform)
				|| StringUtils.isNotBlank(apiVersion)) {
			throw createServiceException(ServiceExceptionCode.INCOMPATIBLE_VERSION_MOBILE);
		}
		
		// Block Version 1.0.0 - Checks Url
		if (url.contains("/v2/") || url.contains("/v2.0/")) {
			throw createServiceException(ServiceExceptionCode.INCOMPATIBLE_VERSION_MOBILE);
		}

	}
}
