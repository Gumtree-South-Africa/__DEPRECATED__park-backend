package com.ebay.park.controller.exceptionhandling;

import static com.ebay.park.service.ServiceResponseStatus.FAIL;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import com.ebay.park.exception.Context;
import com.ebay.park.service.ServiceErrorResponse;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.LanguageUtil;
import com.ebay.park.util.ParkConstants;

/**
 * It handles all the park errors.
 * @author scalderon
 *
 */
@Component
public class ParkErrorHandler implements ParkConstants {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ParkErrorHandler.class);
	
	@Autowired
	private SessionService sessionService;

	@Autowired
	private MessageSource messageSource;

	private static Map<String, Locale> locales = new HashMap<String, Locale>();

	
	private static final Map<String, String> errorMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("success", "false");
		}
	};

	public ServiceResponse serviceExceptionHandler(ServiceException exception, HttpServletRequest request) {
		String token = getToken(request, exception.getContext());
		Locale locale = getLocale(request, token);
		String message = getLocalizedMessage(exception, locale);

		logException(message, exception);
		Object errorData = null;
		if (exception.getContext() != null) {
			errorData = exception.getContext().getData();
		} else {
			errorData = errorMap;
		}

		return new ServiceErrorResponse(FAIL, exception.getCode(), message, errorData);

	}
	
	private String getLocalizedMessage(ServiceException exception, Locale locale) {
		String message = null;
		if (exception.getLocalizableMsgArgs() != null) {
			String[] args = new String[exception.getLocalizableMsgArgs().length];
			int i = 0;
			for (String msg : exception.getLocalizableMsgArgs()) {
				args[i++] = messageSource.getMessage(msg, null, msg, locale);
			}
			message = messageSource.getMessage(exception.getMessage(), args, locale);
		} else {
			message = messageSource.getMessage(exception.getMessage(), exception.getMessageArgs(), locale);
		}
		return message;
	}

	private Locale getLocale(HttpServletRequest request, String token) {
		String lang = null;
		if (StringUtils.isNotEmpty(token)) {
			try {
				UserSessionCache userSession = sessionService.getUserSession(token);
				lang = userSession.getLang();
			} catch (ServiceException e) {
			}
		}
		if (lang == null) {
			lang = getLanguage(request);
		}

		Locale locale = getLocalFromCache(lang);
		return locale;
	}
	
	private Locale getLocalFromCache(String lang) {
		Locale locale = locales.get(lang);
		if (locale == null) {
			locale = new Locale(lang);
			locales.put(lang, locale);
		}
		return locale;
	}

	private String getLanguage(HttpServletRequest request) {
		String lang = StringUtils.EMPTY;
		if (request != null) {
			lang = request.getHeader(LANGUAGE_HEADER);
		}
		return LanguageUtil.getValidLanguage(lang);
	}

	private String getToken(HttpServletRequest request, Context context) {
		String token = StringUtils.EMPTY;

		if (context != null) {
			token = context.getToken();
		}

		if (StringUtils.EMPTY.equals(token)) {
			if (request != null) {
				token = request.getHeader(PARK_TOKEN_HEADER);
			}
		}

		return token;
	}

	private void logException(String message, ServiceException exception) {
		if (!exception.isError()) {
			LOGGER.info("message={}", message);
		} else {
			if (exception.getCause() != null) {
                LOGGER.error("message={}", message, exception.getCause());
			} else {
                LOGGER.error("message={}", message, exception);
			}
		}
	}
	
	
	public ServiceResponse httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException exception) {
		LOGGER.info("message= HttpMessageNotReadableException: {}", exception.getMessage());
		return new ServiceResponse(FAIL, exception.getMessage(), errorMap);
	}

}
