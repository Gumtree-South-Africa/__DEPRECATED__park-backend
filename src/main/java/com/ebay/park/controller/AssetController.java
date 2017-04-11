/*
 * Copyright eBay, 2014
 */
package com.ebay.park.controller;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.service.asset.AssetService;
import com.ebay.park.service.asset.dto.AssetUploadRequest;
import com.ebay.park.service.asset.dto.GetStaticTextRequest;
import com.ebay.park.service.asset.dto.GetTutorialRequest;
import com.ebay.park.util.ParkConstants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author juan.pizarro
 */
@RestController
@RequestMapping(value = {"/assets/v3","/assets/v3.0"})
public class AssetController implements ParkConstants {

	private static Logger logger = LoggerFactory
			.getLogger(AssetController.class);

	@Autowired
	private AssetService assetService;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public ServiceResponse upload(@RequestParam("name") String name, @RequestParam("file") MultipartFile file, @RequestHeader(PARK_TOKEN_HEADER) String token) {
		AssetUploadRequest request = new AssetUploadRequest();
		request.setName(name);
		request.setFile(file);
		request.setToken(token);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY, assetService.upload(request));
		} catch(ServiceException e) {
            logger.error("error trying to upload photo for user: {}. Token: {}", name, token);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = "/terms", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse listTerms(@RequestHeader(PARK_TOKEN_HEADER) String parkToken) {
		GetStaticTextRequest request = new GetStaticTextRequest();
		request.setToken(parkToken);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, assetService.listTerms(request));
		} catch (ServiceException e) {
            logger.error("error trying to list terms and conditions. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = "/communityRules", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse listCommunityRules(
			@RequestHeader(PARK_TOKEN_HEADER) String parkToken) {
		GetStaticTextRequest request = new GetStaticTextRequest();
		request.setToken(parkToken);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, assetService.listCommunityRules(request));
		} catch (ServiceException e) {
            logger.error("error trying to list community rules. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = "/legalDisclosures", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse listLegalDisclosures(
			@RequestHeader(PARK_TOKEN_HEADER) String parkToken) {
		GetStaticTextRequest request = new GetStaticTextRequest();
		request.setToken(parkToken);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY,
					assetService.listLegalDisclosures(request));
		} catch (ServiceException e) {
            logger.error("error trying to list legal disclosures. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = "/tutorial/{step}", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse getTutorial(
			@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@PathVariable Integer step) {
		GetTutorialRequest request = new GetTutorialRequest(parkToken);
		request.setStep(step);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, assetService.getTutorial(request));
		} catch (ServiceException e) {
            logger.error("error trying to get tutorial. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}
}
