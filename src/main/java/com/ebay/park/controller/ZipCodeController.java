/**
 * 
 */
package com.ebay.park.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.service.zipcode.ZipCodeService;
import com.ebay.park.service.zipcode.dto.SearchZipCodeByStateAndCityRequest;
import com.ebay.park.service.zipcode.dto.SearchZipCodeRequest;

/**
 * @author jppizarro
 *
 */
@RestController
@RequestMapping(value={"/moderation/zipcode"})
public class ZipCodeController {

	private static Logger logger = LoggerFactory.getLogger(ModerationController.class);

	@Autowired
	private ZipCodeService zipcodeService;

	
	@RequestMapping(value = "/{state}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse findByState(
			@PathVariable(value = "state") String state) {
		
		SearchZipCodeRequest request = new SearchZipCodeRequest(state);
		
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					zipcodeService.searchZipCodesByState(request));
		} catch (ServiceException e) {
            logger.error("error trying to search zipcodes. state: {}", state);
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	@RequestMapping(value = "/{state}/{city}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse findByStateAndCity(
			@PathVariable(value = "state") String state,
			@PathVariable(value = "city") String city) {
		
		SearchZipCodeByStateAndCityRequest request = new SearchZipCodeByStateAndCityRequest(state, city);
		
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					zipcodeService.searchZipCodesByStateAndCity(request));
		} catch (ServiceException e) {
            logger.error("error trying to search zipcodes. state: {}and city: {}", state, city);
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
}
