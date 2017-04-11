/*
 * Copyright eBay, 2014
 */
package com.ebay.park.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.service.support.SupportServiceImpl;
import com.ebay.park.service.support.dto.SendUserFeedbackRequest;
import com.ebay.park.util.ParkConstants;

/**
 * @author marcos.lambolay
 */
@RestController
@RequestMapping(value = "/support")
public class SupportController implements ParkConstants {

	private static Logger logger = LoggerFactory
			.getLogger(SupportController.class);

	@Autowired
	private SupportServiceImpl supportService;

	@RequestMapping(value="/sendFeedback", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse sendFeedback(@RequestHeader(PARK_TOKEN_HEADER) String token,
		@RequestBody SendUserFeedbackRequest request) {
		request.setToken(token);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY, supportService.sendUserFeedback(request));
		} catch (ServiceException e) {
            logger.error("error trying to send user feedback. Token: {}", token);
			e.setRequestToContext(request);
			throw e;
		} 
	}
}
