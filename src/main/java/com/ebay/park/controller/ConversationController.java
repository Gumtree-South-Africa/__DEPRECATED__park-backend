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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.service.conversation.ConversationServiceImpl;
import com.ebay.park.service.conversation.dto.AcceptConversationRequest;
import com.ebay.park.service.conversation.dto.GetConversationRequest;
import com.ebay.park.service.conversation.dto.ListConversationsByItemRequest;
import com.ebay.park.service.conversation.dto.ListConversationsRequest;
import com.ebay.park.service.conversation.dto.RejectConversationRequest;
import com.ebay.park.service.conversation.dto.SendChatRequest;
import com.ebay.park.service.conversation.dto.SendOfferRequest;
import com.ebay.park.util.ParkConstants;

/**
 * @author marcos.lambolay
 */
@RestController
@RequestMapping(value = "/conversations")
public class ConversationController implements ParkConstants {

	private static Logger logger = LoggerFactory
			.getLogger(ConversationController.class);

	@Autowired
	private ConversationServiceImpl conversationService;

	@RequestMapping(value = {"/v3", "/v3.0"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse listConversation(@RequestHeader(PARK_TOKEN_HEADER) String token,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestParam String lastRequest, @RequestParam String role,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {

		ListConversationsRequest request = new ListConversationsRequest(token, lang);
		request.setPage(page);
		request.setPageSize(pageSize);
		request.setLastRequest(lastRequest);
		request.setRole(role);

		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, conversationService.list(request));
		} catch (ServiceException e) {
			logger.error("error trying to list conversations. Token: {}", token);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = {"/v3/item/{id}", "/v3.0/item/{id}"}, method = RequestMethod.GET)
	public ServiceResponse listConversationByItemIdV3(
			@RequestHeader(PARK_TOKEN_HEADER) String token, @PathVariable Long id,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestParam String lastRequest, @RequestParam String role,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {

		ListConversationsByItemRequest request = new ListConversationsByItemRequest(id, token, lang);
		setRequestParameters(request, page, pageSize, lastRequest, role);

		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY,
					conversationService.listByItemIdV3(request));
		} catch (ServiceException e) {
			logger.error("error trying to list conversations. Token: {}", token);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	private void setRequestParameters(ListConversationsByItemRequest request, Integer page, Integer pageSize, String lastRequest, String role) {
	    request.setPage(page);
        request.setPageSize(pageSize);
        request.setLastRequest(lastRequest);
        request.setRole(role);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = {"/v4/item/{id}", "/v4.0/item/{id}"}, method = RequestMethod.GET)
    public ServiceResponse listConversationByItemIdV4(
            @RequestHeader(PARK_TOKEN_HEADER) String token, @PathVariable Long id,
            @RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
            @RequestParam String lastRequest, @RequestParam String role,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        ListConversationsByItemRequest request = new ListConversationsByItemRequest(id, token, lang);
        setRequestParameters(request, page, pageSize, lastRequest, role);


        try {
            return new ServiceResponse(ServiceResponseStatus.SUCCESS,
                    StringUtils.EMPTY,
                    conversationService.listByItemIdV4(request));
        } catch (ServiceException e) {
            logger.error("error trying to list conversations. Token: {}", token);
            e.setRequestToContext(request);
            throw e;
        } 
    }

	@RequestMapping(value = {"/v3/accept", "/v3.0/accept"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse accept(@RequestHeader(PARK_TOKEN_HEADER) String token,
			@RequestBody AcceptConversationRequest request) {
		request.setToken(token);
		try {
			return conversationService.accept(request);
		} catch (ServiceException e) {
			logger.error("error trying to accept conversation. Token: {}", token);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/v3/reject", "/v3.0/reject"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse rejectConversation(@RequestHeader(PARK_TOKEN_HEADER) String token,
			@RequestBody RejectConversationRequest request) {
		request.setToken(token);
		try {
			return conversationService.reject(request);
		} catch (ServiceException e) {
			logger.error("error trying to reject conversation. Token: {}", token );
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/v3/{conversationId}", "/v3.0/{conversationId}"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse getConversationV3(@RequestHeader(PARK_TOKEN_HEADER) String token,
			@PathVariable String conversationId,
			@RequestParam String lastRequest,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String language) {
		GetConversationRequest request = new GetConversationRequest(token, language, conversationId, lastRequest);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, conversationService.getV3(request));
		} catch (ServiceException e) {
			logger.error("error trying to get conversation, version 3. Token: {}", token);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/v4/{conversationId}", "/v4.0/{conversationId}"}, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ServiceResponse getConversationV4(@RequestHeader(PARK_TOKEN_HEADER) String token,
            @PathVariable String conversationId,
            @RequestParam String lastRequest,
            @RequestHeader(value = LANGUAGE_HEADER, required = false) String language) {
        GetConversationRequest request = new GetConversationRequest(token, language, conversationId, lastRequest);
        try {
            return new ServiceResponse(ServiceResponseStatus.SUCCESS,
                    StringUtils.EMPTY, conversationService.getV4(request));
        } catch (ServiceException e) {
            logger.error("error trying to get conversation, version 4. Token: {}", token);
            e.setRequestToContext(request);
            throw e;
        }
    }

	@RequestMapping(value = {"/v3/sendChat", "/v3.0/sendChat"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse sendChat(@RequestHeader(PARK_TOKEN_HEADER) String token,
			@RequestBody SendChatRequest request) {
		request.setToken(token);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, conversationService.sendChat(request));
		} catch (ServiceException e) {
			logger.error("error trying to send chat. Token: {}", token);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/v3/sendOffer", "/v3.0/sendOffer"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse sendOffer(@RequestHeader(PARK_TOKEN_HEADER) String token,
			@RequestBody SendOfferRequest request) {
		request.setToken(token);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, conversationService.sendOffer(request));
		} catch (ServiceException e) {
			logger.error("error trying to send an offer. Token: {}", token);
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
}
