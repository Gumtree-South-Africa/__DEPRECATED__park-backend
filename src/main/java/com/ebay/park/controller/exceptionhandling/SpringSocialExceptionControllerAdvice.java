package com.ebay.park.controller.exceptionhandling;

import static com.ebay.park.service.ServiceException.createServiceException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.social.ApiException;
import org.springframework.social.DuplicateStatusException;
import org.springframework.social.ExpiredAuthorizationException;
import org.springframework.social.InsufficientPermissionException;
import org.springframework.social.InvalidAuthorizationException;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.RateLimitExceededException;
import org.springframework.social.RevokedAuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;

/**
 * This controller advice handles all the Spring Social exceptions 
 * and creates the corresponding ServiceException.
 * @author scalderon
 *
 */
@ControllerAdvice
public class SpringSocialExceptionControllerAdvice {
	
	@Autowired
	private ParkErrorHandler parkErrorHandler;
	
	@ExceptionHandler({ InvalidAuthorizationException.class})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody
	ServiceResponse invalidAuthorizationException(InvalidAuthorizationException exception, HttpServletRequest request) {
		return parkErrorHandler.serviceExceptionHandler(
				createServiceException(ServiceExceptionCode.INVALID_FB_TOKEN, exception), request);
	}
	
	@ExceptionHandler({ ExpiredAuthorizationException.class})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody
	ServiceResponse expiredAuthorizationException(ExpiredAuthorizationException exception, HttpServletRequest request) {
		return parkErrorHandler.serviceExceptionHandler(
				createServiceException(ServiceExceptionCode.INVALID_FB_TOKEN, exception), request);
	}

	@ExceptionHandler({ InsufficientPermissionException.class})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody
	ServiceResponse insufficientPermissionException(InsufficientPermissionException exception, HttpServletRequest request) {
		return parkErrorHandler.serviceExceptionHandler(
				createServiceException(ServiceExceptionCode.INSUFFICIENT_FB_PERMISSION, exception), request);
	}


	@ExceptionHandler({ RevokedAuthorizationException.class})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody
	ServiceResponse revokedAuthorizationException(RevokedAuthorizationException exception, HttpServletRequest request) {
		return parkErrorHandler.serviceExceptionHandler(
				createServiceException(ServiceExceptionCode.FB_REVOKED_AUTHORIZATION, exception), request);
	}
	
	@ExceptionHandler({ MissingAuthorizationException.class})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody
	ServiceResponse missingAuthorizationException(MissingAuthorizationException exception, HttpServletRequest request) {
		return parkErrorHandler.serviceExceptionHandler(
				createServiceException(ServiceExceptionCode.INVALID_FB_TOKEN, exception), request);
	}

	@ExceptionHandler({ DuplicateStatusException.class})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody
	ServiceResponse duplicateStatusException(DuplicateStatusException exception, HttpServletRequest request) {
		return parkErrorHandler.serviceExceptionHandler(
				createServiceException(ServiceExceptionCode.DUPLICATED_FB_POST, exception), request);
	}
	
	@ExceptionHandler({ RateLimitExceededException.class})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody
	ServiceResponse rateLimitExceededException(RateLimitExceededException exception, HttpServletRequest request) {
		return parkErrorHandler.serviceExceptionHandler(
				createServiceException(ServiceExceptionCode.RATE_LIMIT_FB_POST_EXCEED, exception), request);
	}
	
	@ExceptionHandler({ ApiException.class})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody
	ServiceResponse apiException(ApiException exception, HttpServletRequest request) {
		return parkErrorHandler.serviceExceptionHandler(
				createServiceException(ServiceExceptionCode.ERROR_FB_COMMUNICATION, exception), request);
	}

}
