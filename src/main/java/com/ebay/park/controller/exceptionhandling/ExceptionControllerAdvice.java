/*
 * Copyright eBay, 2014
 */
package com.ebay.park.controller.exceptionhandling;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;

/**
 * This controller advice handles all the ServiceException 
 * and HttpMessageNotReadableException.
 *
 * @author juan.pizarro
 * 
 */
@ControllerAdvice
public class ExceptionControllerAdvice {
	
	@Autowired
	private ParkErrorHandler parkErrorHandler;

	@ExceptionHandler({ ServiceException.class})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody
	ServiceResponse serviceException(ServiceException exception, HttpServletRequest request) {
		return parkErrorHandler.serviceExceptionHandler(exception, request);
	}
	
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody
	ServiceResponse exception(HttpMessageNotReadableException exception) {
		return parkErrorHandler.httpMessageNotReadableExceptionHandler(exception);
	}
}