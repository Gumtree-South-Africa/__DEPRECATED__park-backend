package com.ebay.park.controller;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.service.blacklist.BlacklistService;
import com.ebay.park.service.blacklist.dto.SearchBlacklistedWordsRequest;
import com.ebay.park.service.moderation.dto.BlacklistedWordRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ModerationController.MODERATION_PATH + "/blacklist")
public class BlacklistController {

	private static Logger logger = LoggerFactory.getLogger(BlacklistController.class);
	
	@Autowired
	BlacklistService blacklistService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse search(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "description", required = false) String description) {

		SearchBlacklistedWordsRequest request = new SearchBlacklistedWordsRequest(page, pageSize, order, description);

		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, blacklistService.search(request));
		} catch(ServiceException e) {
            logger.error("error trying to search in blacklist. Description: {}", description);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ServiceResponse addWord(@RequestBody BlacklistedWordRequest request) {
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY,
					blacklistService.addBlacklistedWord(request));
		} catch (ServiceException e) {
            logger.error("error trying to add word in blacklist. Word: {}", request.getWord());
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse updateWord(@PathVariable Long id,
			@RequestBody BlacklistedWordRequest request) {
		try {
			request.setId(id);
			return blacklistService.updateBlacklistedWord(request);
		} catch (ServiceException e) {
            logger.error("error trying to update blacklist word. Id:{}", id);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse removeWord(@PathVariable Long id) {
		BlacklistedWordRequest request = new BlacklistedWordRequest();
		try {
			request.setId(id);
			return blacklistService.removeBlacklistedWord(request);
		} catch (ServiceException e) {
            logger.error("error trying to remove blacklist word. Id:{}", id);
			e.setRequestToContext(request);
			throw e;
		} 
	}
}
