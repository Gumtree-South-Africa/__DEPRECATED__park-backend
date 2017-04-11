package com.ebay.park.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.service.moderationMode.ModerationModeService;
import com.ebay.park.service.moderationMode.dto.ApplyFiltersForModerationModeRequest;
import com.ebay.park.service.moderationMode.dto.GetItemInformationRequest;
import com.ebay.park.service.moderationMode.dto.GetUserItemsForModerationModeRequest;
import com.ebay.park.service.moderationMode.dto.UnlockUserRequest;
import com.ebay.park.service.moderationMode.dto.UpdateItemForModerationModeRequest;
import com.ebay.park.util.ParkConstants;

@RestController
@RequestMapping(ModerationModeController.MODERATION_MODE_PATH)
public class ModerationModeController implements ParkConstants {

	public static final String MODERATION_MODE_PATH = "/moderationMode";
	private static Logger logger = LoggerFactory.getLogger(ModerationModeController.class);
	
	@Autowired
	private ModerationModeService moderationModeService;
	
	
	@RequestMapping(value = "/{user_id}/{item_id_excluded}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse getUserItemsForModerationMode(
			@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String language,
			@PathVariable Long user_id,
			@PathVariable Long item_id_excluded,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		try {
			GetUserItemsForModerationModeRequest request = new GetUserItemsForModerationModeRequest(
					parkToken, language, user_id, item_id_excluded, page, pageSize);
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					moderationModeService.getUserItemsForModerationMode(request));
		} catch (ServiceException e) {
			logger.error("Error trying to get user items for moderation mode.");
			Map<String, String> eParams = new HashMap<String, String>();
			eParams.put("parkToken", parkToken);
			eParams.put("user_id", user_id.toString());
			eParams.put("item_id_excluded",item_id_excluded.toString());
			e.setRequestToContext(eParams);
			throw e;
		} 
	}
	
	@RequestMapping(value = "/{item_id}", method = RequestMethod.PUT,consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse updateItemsForModerationMode(
			@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String language,
			@PathVariable Long item_id,
			@RequestBody UpdateItemForModerationModeRequest request) {
		try {
			request.setItemId(item_id);
			request.setLanguage(language);
			request.setToken(parkToken);
			return moderationModeService.updateItemForModerationMode(request);
		} catch (ServiceException e) {
			logger.error("Error trying to update an item for moderation mode.");
			Map<String, String> eParams = new HashMap<String, String>();
			eParams.put("parkToken", parkToken);
			eParams.put("item_id", item_id.toString());
			e.setRequestToContext(eParams);
			throw e;
		} 
	}

	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse applyForModerationMode(
			@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String language,
			@RequestBody ApplyFiltersForModerationModeRequest request) {
		try {
		    request.setToken(parkToken);
		    request.setLanguage(language);
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY, 
					moderationModeService.applyFiltersForModerationMode(request));
		} catch (ServiceException e) {
			logger.error("Error trying to filter items for moderation mode.");
			Map<String, String> eParams = new HashMap<String, String>();
			eParams.put("parkToken", parkToken);
			e.setRequestToContext(eParams);
			throw e;
		} 
	}

	@RequestMapping(value = "/item/{itemId}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse getItemInformation(
			@RequestHeader(value = PARK_TOKEN_HEADER, required = false) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@PathVariable Long itemId) {

		GetItemInformationRequest request = new GetItemInformationRequest(itemId, parkToken, lang);

		try { 
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY, 
					moderationModeService.getItemInformation(request));
			
		} catch (ServiceException e) {
            logger.error("Error trying to get item with id: {} for moderation", itemId);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = "/unlockItem/{itemId}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse unlockItem(
			@RequestHeader(value = PARK_TOKEN_HEADER, required = false) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@PathVariable Long itemId) {
		
		UnlockUserRequest request = new UnlockUserRequest(itemId, parkToken, lang);
		
		try { 
			return moderationModeService.unlockItem(request);
			
		} catch (ServiceException e) {
            logger.error("Error trying to unlock item with id: {} for moderation", itemId);
			e.setRequestToContext(request);
			throw e;
		}  
	}
	
}
