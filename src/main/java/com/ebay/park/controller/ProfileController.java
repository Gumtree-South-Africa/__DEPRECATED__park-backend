/*
 * Copyright eBay, 2014
 */
package com.ebay.park.controller;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.service.profile.ProfileService;
import com.ebay.park.service.profile.dto.GetUserProfileRequest;
import com.ebay.park.service.profile.dto.ProfilePictureRequest;
import com.ebay.park.service.profile.dto.UserInfoRequest;
import com.ebay.park.util.KeyAndValue;
import com.ebay.park.util.ParkConstants;

/**
 * @author juan.pizarro
 */
@RestController
public class ProfileController implements ParkConstants {

	@Autowired
	private ProfileService profileService;
	
	private static Logger logger = LoggerFactory.getLogger(ProfileController.class);

	@RequestMapping(value = {"/users/v3/{username}/info","/users/v3.0/{username}/info"}, method = RequestMethod.PUT, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse updateUser(@PathVariable String username,
			@RequestBody UserInfoRequest request,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String language) {
		request.setLanguage(language);
		try {
			request.setUsername(username);
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					profileService.updateUserInfo(request));
		} catch (ServiceException e) {
            logger.error("error trying to update user. username = {}", username);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/public/users/v3/{username}/info","/public/users/v3.0/{username}/info"}, method = RequestMethod.GET, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse getPublicUserProfile(@RequestHeader(value = PARK_TOKEN_HEADER, required = false) String parkToken,
			@PathVariable String username,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String language) {
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					profileService.getUserProfile(new GetUserProfileRequest(parkToken, username, language)));
		} catch (ServiceException e) {
            logger.error("error trying to get user profile. Token: {}", parkToken);
			e.setKeyValueListToContext(KeyAndValue.from("username", username),
					KeyAndValue.from(PARK_TOKEN_HEADER, parkToken));
			throw e;
		} 

	}
	
	@RequestMapping(value = {"/users/v3/{username}/info","/users/v3.0/{username}/info"}, method = RequestMethod.GET, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse getUserProfile(@RequestHeader(value = PARK_TOKEN_HEADER, required = false) String parkToken,
			@PathVariable String username,
			@RequestHeader(value = LANGUAGE_HEADER, required = true) String language) {
		
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					profileService.getUserProfile(new GetUserProfileRequest(parkToken, username, language)));
		} catch (ServiceException e) {
            logger.error("error trying to get user profile. Token: {}", parkToken);
			e.setKeyValueListToContext(KeyAndValue.from("username", username),
					KeyAndValue.from(PARK_TOKEN_HEADER, parkToken));
			throw e;
		} 

	}

	@RequestMapping(value = {"/users/v3/{username}/picture","/users/v3.0/{username}/picture"}, method = RequestMethod.PUT, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse addProfilePicture(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@PathVariable String username, @RequestBody ProfilePictureRequest request) throws ServiceException {
		try {
			request.setUsername(username);
			request.setToken(parkToken);
			return profileService.addProfilePicture(request);
		} catch (ServiceException e) {
            logger.error("error trying to add profile picture. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}
}