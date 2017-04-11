/*
 * Copyright eBay, 2014
 */
package com.ebay.park.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.service.category.CategoryServiceImpl;
import com.ebay.park.util.ParkConstants;

/**
 * @author marcos.lambolay
 */
@RestController
@RequestMapping(value = {"/public/categories/v3","/public/categories/v3.0"})
public class CategoryController implements ParkConstants {

	private static Logger logger = LoggerFactory.getLogger(CategoryController.class);

	@Autowired
	private CategoryServiceImpl categoryService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse list(
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String language) {
		ParkRequest request = new ParkRequest(null, language);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY, categoryService.list(request));
		} catch (ServiceException e) {
			logger.error("error trying to list categories");
			e.setRequestToContext(request);
			throw e;
		} 
	}
}
