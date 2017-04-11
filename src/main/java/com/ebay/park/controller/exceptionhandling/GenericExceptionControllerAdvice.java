package com.ebay.park.controller.exceptionhandling;

import static com.ebay.park.service.ServiceException.createServiceException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;

/**
 * This controller advice catches all the unexpected exception and 
 * creates a ServiceException with code {@link ServiceExceptionCode.GENERIC_ERROR}
 * @author scalderon
 *
 */
@ControllerAdvice
public class GenericExceptionControllerAdvice {
	
	@Autowired
	private ParkErrorHandler parkErrorHandler;
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody
	ServiceResponse exception(Exception exception, HttpServletRequest request) {
		return parkErrorHandler.serviceExceptionHandler(
				createServiceException(ServiceExceptionCode.GENERIC_ERROR, exception), request);
	}

}
