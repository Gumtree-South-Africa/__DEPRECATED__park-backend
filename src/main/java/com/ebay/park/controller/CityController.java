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
import com.ebay.park.service.city.CityService;
import com.ebay.park.service.city.dto.SearchCityRequest;

/**
 * 
 * @author scalderon
 *
 */
@RestController
@RequestMapping(value={"/moderation/cities"})
public class CityController {
	
	private static Logger logger = LoggerFactory.getLogger(ModerationController.class);
	
	@Autowired
	private CityService cityService;
	
	@RequestMapping(value = "/{state}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse findCitiesByState(
			@PathVariable(value = "state") String state) {
		
		SearchCityRequest request = new SearchCityRequest(state);
		
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					cityService.searchCitiesByState(request));
		} catch (ServiceException e) {
            logger.error("error trying to search cities. state: {}", state);
			e.setRequestToContext(request);
			throw e;
		}
	}

}
