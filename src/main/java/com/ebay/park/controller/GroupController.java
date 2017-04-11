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
import org.springframework.web.multipart.MultipartFile;

import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.service.group.GroupService;
import com.ebay.park.service.group.dto.CreateGroupRequest;
import com.ebay.park.service.group.dto.DeleteGroupRequest;
import com.ebay.park.service.group.dto.GetGroupItemsRequest;
import com.ebay.park.service.group.dto.GetGroupRequest;
import com.ebay.park.service.group.dto.GetGroupSubscribersRequest;
import com.ebay.park.service.group.dto.GetNewItemsInfoRequest;
import com.ebay.park.service.group.dto.RemoveGroupItemsRequest;
import com.ebay.park.service.group.dto.ResetGroupCounterRequest;
import com.ebay.park.service.group.dto.SearchGroupRequest;
import com.ebay.park.service.group.dto.ShareGroupOnSocialRequest;
import com.ebay.park.service.group.dto.SubscribeGroupRequest;
import com.ebay.park.service.group.dto.UnsubscribeGroupFollowersRequest;
import com.ebay.park.service.group.dto.UnsubscribeGroupRequest;
import com.ebay.park.service.group.dto.UpdateGroupRequest;
import com.ebay.park.service.group.dto.UploadGroupPhotoRequest;
import com.ebay.park.util.ParkConstants;

/**
 * @author marcos.lambolay
 */
@RestController
public class GroupController implements ParkConstants {
	
	/**
	 * Page size to fix <a href=https://jira.globant.com/browse/EPA001-10562/>
	 */
	private static final Integer PAGE_SIZE_LAST_VERSION = 100;
	private static final Integer PAGE_SIZE_PREVIOUS_VERSION = 100000;

	private static Logger logger = LoggerFactory
			.getLogger(GroupController.class);

	@Autowired
	private GroupService groupService;

	@RequestMapping(value = {"/groups/v3","/groups/v3.0"}, method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public ServiceResponse createGroup(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestBody CreateGroupRequest request) throws ServiceException {
		request.setLanguage(lang);
		request.setToken(parkToken);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, groupService.create(request));
		} catch (ServiceException e) {
			logger.error("error trying to create group. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	@RequestMapping(value = {"/groups/v3/{id}","/groups/v3.0/{id}"}, method = RequestMethod.PUT, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse updateGroup(
			@PathVariable Long id,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@RequestBody UpdateGroupRequest request) {
		try {
			request.setId(id);
			request.setLanguage(lang);
			request.setToken(parkToken);
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, groupService.updateGroup(request));
		} catch (ServiceException e) {
			logger.error("error trying to update group. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	/**
	 * Given the id, it searches for the group.
	 * @param parkToken
	 * 			user token
	 * @param lang
	 * 			language
	 * @param id
	 * 			group id
	 * @return
	 * 		the group found
	 */
	@RequestMapping(value = {"groups/v3/{id}","groups/v3.0/{id}"}, method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse getGroup(
			@RequestHeader(value = PARK_TOKEN_HEADER, required = false) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@PathVariable String id,
			@RequestParam(value = "includeAdminUser", required = false) Boolean includeAdminUser) {
		GetGroupRequest request = new GetGroupRequest(id, includeAdminUser, parkToken, lang);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					groupService.get(request));
		} catch (ServiceException e) {
			logger.error("error trying to get group. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	@RequestMapping(value = {"groups/v3/{id}","groups/v3.0/{id}"}, method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse deleteGroup(
			@RequestHeader(value = PARK_TOKEN_HEADER, required = true) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@PathVariable Long id) {
		DeleteGroupRequest request = new DeleteGroupRequest(id, parkToken, lang);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					groupService.deleteGroup(request));
		} catch (ServiceException e) {
			logger.error("error trying to delete a group. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	
	@RequestMapping(value = {"/groups/v3/{id}/share", "/groups/v3.0/{id}/share"}, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse shareGroupOnSocial(
			@RequestHeader(value = PARK_TOKEN_HEADER, required = true) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestBody ShareGroupOnSocialRequest request,
			@PathVariable Long id) {
		request.setGroupId(id);
		request.setLanguage(lang);
		request.setToken(parkToken);

		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, groupService.shareGroupOnSocial(request));
		} catch (ServiceException e) {
			logger.error("error trying to share group on social. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	/**
	 * Subscribe the current user to a specific group.
	 * 
	 * @param parkToken
	 * @param groupId
	 * @return
	 * @throws ServiceException
	 */
	
	@RequestMapping(value = {"/groups/v3/{id}/subscribe", "/groups/v3.0/{id}/subscribe"}, method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse subscribeToGroup(
			@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@PathVariable("id") Long groupId) throws ServiceException {
		SubscribeGroupRequest request = new SubscribeGroupRequest();
		request.setToken(parkToken);
		request.setGroupId(groupId);
		try {
			return groupService.subscribe(request);
		} catch (ServiceException e) {
			logger.error("error trying to subscribe a user to a group. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	/**
	 * Unsubscribe the current user from a group.
	 * 
	 * @param parkToken
	 * @param groupId
	 * @return
	 * @throws ServiceException
	 */
	@RequestMapping(value = {"/groups/v3/{id}/unsubscribe","/groups/v3.0/{id}/unsubscribe"}, method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse unsubscribeToGroup(
			@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@PathVariable("id") Long groupId)
					throws ServiceException {
		UnsubscribeGroupRequest request = new UnsubscribeGroupRequest ();
		request.setToken(parkToken);
		request.setGroupId(groupId);
		try {
			return groupService.unsubscribe(request);
		} catch (ServiceException e) {
			logger.error("error trying to unsubscribe a user to a group. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	
	/**
	 * Unsubscribe users from a specific group. The user logged must be the
	 * group's creator.
	 * 
	 * @param parkToken
	 * @param lang
	 * @param id
	 * @return
	 */
	@RequestMapping(value = {"/groups/v3/{id}/unsubscribe/followers","/groups/v3.0/{id}/unsubscribe/followers"}, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse unsubscribeGroupFollowers(
			@RequestHeader(value = PARK_TOKEN_HEADER, required = true) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestBody UnsubscribeGroupFollowersRequest request,
			@PathVariable long id) {
		    
			request.setGroupId(id);
		    request.setToken(parkToken);
		    request.setLanguage(lang);

		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY,
					groupService.unsubscribeGroupFollowers(request));
		} catch (ServiceException e) {
            logger.error("error trying to unsuscribe followers for group id: {}", id);
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	/**
	 * Remove items from a specific group. The user logged must be the group's
	 * creator.
	 * 
	 * @param parkToken
	 * @param lang
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = {"/groups/v3/{id}/remove/items","/groups/v3.0/{id}/remove/items"}, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse removeGroupItems(
			@RequestHeader(value = PARK_TOKEN_HEADER, required = true) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestBody RemoveGroupItemsRequest request,
			@PathVariable long id) {
		  
			request.setGroupId(id);
		    request.setToken(parkToken);
		    request.setLanguage(lang);

		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY,
					groupService.removeGroupItems(request));
		} catch (ServiceException e) {
            logger.error("error trying to remove items for group id: {}", id);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	/**
	 * Searches for groups that agree to the filters.
	 * This is the public search.
	 * @param criteria
	 * 			search criteria
	 * @param page
	 * 			page number
	 * @param latitude
	 * 			coordinate defined for sorting purposes
	 * @param longitude
	 * 			coordinate defined for sorting purposes
	 * @param pageSize
	 * 			size of the page when paginating
	 * @param language
	 * 			language defined for translations purposes
	 * @param radius
	 * 			radius used to limit the search area
	 * @param order
	 * 			sorting order
	 * @return
	 * 		groups that agreed to the set filters
	 * @throws ServiceException
	 */
	@RequestMapping(value = {"/public/groups/v3","/public/groups/v3.0"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse searchPublicGroups(
			@RequestParam(value = "q", required = false) String criteria,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String language,
			@RequestParam(value = "radius", required = false) Double radius,
			@RequestParam(value = "order", required = false, defaultValue = "nearest") String order,
			@RequestParam(value = "requestTime", required = false) String requestTime)
					throws ServiceException {
		SearchGroupRequest request = new SearchGroupRequest(null, language, page, pageSize);
		request.setCriteria(criteria);
		request.setLatitude(latitude);
		request.setLongitude(longitude);
		request.setLanguage(language);
		request.setRadius(radius);
		request.setOrder(order);
		request.setRequestTime(requestTime);

		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, groupService.search(request));
		} catch (ServiceException e) {
			logger.error("error searching groups");
			e.setRequestToContext(request);
			throw e;
		} 
	}


	/**
	 * Search a group only by name using location.
	 * 
	 * @param parkToken
	 * @param criteria
	 * @param page
	 * @param latitude
	 * @param longitude
	 * @param pageSize
	 * @param language
	 * @param radius
	 * @param order
	 * @return
	 * @throws ServiceException
	 */
	@RequestMapping(value = {"/groups/v3","/groups/v3.0"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse searchGroups(
			@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@RequestParam(value = "q", required = false) String criteria,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String language,
			@RequestParam(value = "radius", required = false) Double radius,
			@RequestParam(value = "order", required = false, defaultValue = "nearest") String order,
			@RequestParam(value = "requestTime", required = false) String requestTime)
					throws ServiceException {
		SearchGroupRequest request = new SearchGroupRequest(parkToken, language, page, pageSize);
		request.setCriteria(criteria);
		request.setLatitude(latitude);
		request.setLongitude(longitude);
		request.setLanguage(language);
		request.setRadius(radius);
		request.setOrder(order);
		request.setRequestTime(requestTime);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, groupService.search(request));
		} catch (ServiceException e) {
            logger.error("error searching groups. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	/**
	 * Search groups followed or created for the current user. 
	 * 
	 * @param parkToken
	 * @param username
	 * @param criteria
	 * @param page
	 * @param latitude
	 * @param longitude
	 * @param pageSize
	 * @param language
	 * @param radius
	 * @param order
	 * @param onlyOwned
	 * @return
	 * @throws ServiceException
	 */
	@RequestMapping(value = {"/groups/v3/user/{username}","/groups/v3.0/user/{username}"}, method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse listGroup(
			@RequestHeader(value = PARK_TOKEN_HEADER, required = true) String parkToken,
			@PathVariable String username,
			@RequestParam(value = "q", required = false) String criteria,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String language,
			@RequestParam(value = "radius", required = false) Double radius,
			@RequestParam(value = "order", required = false, defaultValue = "nearest")  String order,
			@RequestParam(value = "onlyOwned", required = false) boolean onlyOwned,
			@RequestParam(value = "requestTime", required = false) String requestTime)
					throws ServiceException {
		/**
		 * Fix for <a href=https://jira.globant.com/browse/EPA001-10562/>
		 */
		if (pageSize != null && PAGE_SIZE_PREVIOUS_VERSION.equals(pageSize)) {
			pageSize = PAGE_SIZE_LAST_VERSION;
		}
		SearchGroupRequest request = new SearchGroupRequest(parkToken, language, page, pageSize);
		request.setCriteria(criteria);
		request.setLatitude(latitude);
		request.setLongitude(longitude);
		request.setLanguage(language);
		request.setRequestTime(requestTime);
		if (onlyOwned) {
			//Only groups created by the user
			request.setOnlyOwned(true);
		} else {
			//Only groups followed by the user
			request.setFindOnlyUserFollowsGroup(true);
		}

		request.setRadius(radius);
		request.setOrder(order);
		
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, groupService.search(request));
		} catch (ServiceException e) {
            logger.error("error searching groups. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		}  
	}


	@RequestMapping(value = {"/groups/v3/resetCounter","/groups/v3.0/resetCounter"}, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse resetCounter(
			@RequestHeader(value = PARK_TOKEN_HEADER, required = true) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestBody ResetGroupCounterRequest request) {
		request.setLanguage(lang);
		request.setToken(parkToken);
		try {
			return groupService.resetCounter(request);
		} catch (ServiceException e) {
            logger.error("error trying to reset group counter. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	@RequestMapping(value = {"/groups/v3/{id}/picture","/groups/v3.0/{id}/picture"}, method = RequestMethod.POST, consumes = { "multipart/form-data" })
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse uploadPictureToGroup(
			@RequestHeader(PARK_TOKEN_HEADER) String parkToken, @PathVariable Long id,
			@RequestParam(value = "photo") MultipartFile photo) {

		UploadGroupPhotoRequest request = new UploadGroupPhotoRequest();
		request.setToken(parkToken);
		request.setGroupId(id);
		request.setPhoto(photo);
		try {
			return groupService.uploadPhoto(request);
		} catch (ServiceException e) {
            logger.error("error trying to upload group photo. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	
	@RequestMapping(value = {"/public/groups/v3/recommended","/public/groups/v3.0/recommended"}, method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse getRecommendedGroups(
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang) {
		
		ParkRequest request = new ParkRequest();
		request.setToken(null);
		request.setLanguage(lang);
		
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					groupService.getRecommendedGroups(request));
		} catch (ServiceException e) {
			logger.error("error trying to get recommended groups");
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/groups/v3/recommended", "/groups/v3.0/recommended"}, method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse getRecommendedGroups(
			@RequestHeader(value = PARK_TOKEN_HEADER, required = true) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang) {
		
		ParkRequest request = new ParkRequest();
		request.setToken(parkToken);
		request.setLanguage(lang);
		
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					groupService.getRecommendedGroups(request));
		} catch (ServiceException e) {
            logger.error("error trying to get recommended groups. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	@RequestMapping(value = {"/groups/v3/{id}/items","/groups/v3.0/{id}/items"}, method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse getGroupItems(
//			@RequestHeader(value = PARK_TOKEN_HEADER, required = true) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@PathVariable long id) {
		//temporary: we made this endpoint public in order to make able to use it from web
		GetGroupItemsRequest request = new GetGroupItemsRequest(id, /*parkToken*/ null, lang, page, pageSize);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY, groupService.getItems(request));
		} catch (ServiceException e) {
            logger.error("error trying to get group items for id: {}", id);
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	
	/**
	 * Return a list of subscribers from a specific group.
	 * 
	 * @param parkToken
	 * @param lang
	 * @param page
	 * @param pageSize
	 * @param id
	 * @return
	 */
	@RequestMapping(value = {"/groups/v3/{id}/subscribers","/groups/v3.0/{id}/subscribers"}, method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse getGroupSubscribers(
			@RequestHeader(value = PARK_TOKEN_HEADER, required = false) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@PathVariable long id) {
		GetGroupSubscribersRequest request = new GetGroupSubscribersRequest(id, parkToken, lang, page, pageSize);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY, groupService.getSubscribers(request));
		} catch (ServiceException e) {
            logger.error("error trying group suscribers for id: {}", id);
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	@RequestMapping(value = {"/groups/v3/newItemsInfo","/groups/v3.0/newItemsInfo"}, method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse getNewItemsInfo(
			@RequestHeader(value = PARK_TOKEN_HEADER, required = false) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang) {
		GetNewItemsInfoRequest request = new GetNewItemsInfoRequest(parkToken, lang);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					groupService.getNewItemsInfo(request));
		} catch (ServiceException e) {
            logger.error("error trying to get new items info for token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	/**
	 * Return the list of groups.
	 * @param lang
	 * @return
	 */
    @RequestMapping(value = {"/public/groups/v3/list","/public/groups/v3.0/list"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public ServiceResponse listGroupsNames(
            @RequestHeader(value = LANGUAGE_HEADER, required = false) String lang) {
        ParkRequest request = new ParkRequest(null, lang);
        try {
            return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
                    groupService.listGroupsNames(request));
        } catch (ServiceException e) {
            logger.error("Error trying to get the group list.");
            throw e;
        } 
    }
}
