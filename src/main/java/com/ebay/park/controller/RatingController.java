/*
 * Copyright eBay, 2014
 */
package com.ebay.park.controller;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.service.rating.RatingServiceImpl;
import com.ebay.park.service.rating.dto.ListPendingRatingsRequest;
import com.ebay.park.service.rating.dto.ListRatingsRequest;
import com.ebay.park.service.rating.dto.RateUserRequest;
import com.ebay.park.service.rating.dto.RatingRequest;
import com.ebay.park.util.ParkConstants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author marcos.lambolay
 */
@RestController
public class RatingController implements ParkConstants {

	private static Logger logger = LoggerFactory
			.getLogger(RatingController.class);

	@Autowired
	private RatingServiceImpl ratingService;

	@RequestMapping(value = {"/ratings/v3/rate","/ratings/v3.0/rate"}, method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse rateUser(@RequestHeader(PARK_TOKEN_HEADER) String token,
			@RequestBody RateUserRequest request) {
		request.setToken(token);
		try {
			ratingService.rateUser(request);
			return ServiceResponse.SUCCESS;
		} catch (ServiceException e) {
            logger.error("error trying to rate user. Token: {}", token);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/ratings/v3/rankings","/ratings/v3.0/rankings"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse list() {
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, ratingService.listRatingStatus());
		} catch (ServiceException e) {
			logger.error("error trying to list rating statuses");
			e.setRequestToContext(Void.TYPE);
			throw e;
		} 
	}

	@RequestMapping(value = {"/ratings/v3/pending","/ratings/v3.0/pending"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse listPendingRatings(
			@RequestHeader(PARK_TOKEN_HEADER) String token,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "requestTime", required = false) String requestTime) {
		ListPendingRatingsRequest request = new ListPendingRatingsRequest(page, pageSize, token, lang);
		request.setUsername(null);
		request.setRequestTime(requestTime);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY,
					ratingService.listPendingRatings(request));
		} catch (ServiceException e) {
            logger.error("error trying to list pending ratings. Token: {}", token);
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	@RequestMapping(value = {"/public/ratings/v3/pending","/public/ratings/v3.0/pending"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse listPublicPendingRatings(
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize, 
			@RequestParam(value = "username", required = true) String username) {
		ListPendingRatingsRequest request = new ListPendingRatingsRequest(page, pageSize, null, lang);
		request.setUsername(username);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY,
					ratingService.listPublicPendingRatings(request));
		} catch (ServiceException e) {
			logger.error("error trying to list pending ratings");
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	@RequestMapping(value = {"/ratings/v3/pending/{id}","/ratings/v3.0/pending/{id}"}, method = RequestMethod.DELETE, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse deletePendingRating(@PathVariable Long id,
			@RequestHeader(PARK_TOKEN_HEADER) String parkToken) {
		RatingRequest request = new RatingRequest(parkToken, id);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY,
					ratingService.deletePendingRating(request));
		} catch (ServiceException e) {
            logger.error("error trying to delete a pending rate. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/public/ratings/v3", "/public/ratings/v3.0"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse listRatings(
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "role", required = false) String role,
			@RequestParam(value = "username") String username,
			@RequestParam(value = "requestTime", required = false) String requestTime) {
		ListRatingsRequest request = new ListRatingsRequest(page, pageSize,
				null, lang);
		request.setRole(role);
		request.setUsername(username);
		request.setRequestTime(requestTime);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, ratingService.listRatings(request));
		} catch (ServiceException e) {
			logger.error("error trying to list user ratings");
			e.setRequestToContext(request);
			throw e;
		} 
	}
}