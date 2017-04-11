package com.ebay.park.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.service.admin.AdminService;
import com.ebay.park.service.admin.dto.AddModeratorRequest;
import com.ebay.park.service.moderation.ModerationService;
import com.ebay.park.service.moderation.dto.AdminSignInRequest;
import com.ebay.park.service.moderation.dto.ContactPublisherRequest;
import com.ebay.park.service.moderation.dto.ContactUserRequest;
import com.ebay.park.service.moderation.dto.RejectItemRequest;
import com.ebay.park.service.moderation.dto.FilterForModerationRequest;
import com.ebay.park.service.moderation.dto.GroupIdRequest;
import com.ebay.park.service.moderation.dto.ItemRequest;
import com.ebay.park.service.moderation.dto.SearchGroupForModerationRequest;
import com.ebay.park.service.moderation.dto.SearchItemForModerationRequest;
import com.ebay.park.service.moderation.dto.SearchUserForModerationRequest;
import com.ebay.park.service.moderation.dto.SendNotificationsForModerationRequest;
import com.ebay.park.service.moderation.dto.UserIdRequest;
import com.ebay.park.util.ParkConstants;

@RestController
@RequestMapping(ModerationController.MODERATION_PATH)
public class ModerationController implements ParkConstants {

	public static final String MODERATION_PATH = "/moderation";
	private static Logger logger = LoggerFactory.getLogger(ModerationController.class);

	@Autowired
	ModerationService moderationService;
	
	@Autowired
	AdminService adminService;

	@RequestMapping(value = "/items", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse searchItems(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "filter", required = false) String filter,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "exactMatch", required = false) Boolean exactMatch) {

		SearchItemForModerationRequest request = new SearchItemForModerationRequest(page, pageSize);
		request.setName(name);
		request.setDescription(description);
		/*TODO: Temporaly username to lowercase. In the future we will change the index avoid lowercase */
		request.setUsername( (username != null ? username.toLowerCase() : username ));
		request.setFilterType(filter);
		request.setOrder(order);
		//Set true, we only want to search by exactMatch through Moderation App.
		request.setExactMatch(true);

		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					moderationService.searchItem(request));
		} catch (ServiceException e) {
			logger.error("error trying to search items in moderation service");
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = "/items/{id}/activate", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse activateItem(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@PathVariable Long id) {

		try {
			return moderationService.activateItem(new ItemRequest(id));
		} catch (ServiceException e) {
			logger.error("error trying to activate item in moderation service");
			Map<String, String> eParams = new HashMap<String, String>();
			eParams.put("parkToken", parkToken);
			eParams.put("id", id.toString());
			e.setRequestToContext(eParams);
			throw e;
		} 

	}

	@RequestMapping(value = "/items/{itemId}/{reasonId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse reject(@RequestHeader(PARK_TOKEN_HEADER) String parkToken, @PathVariable Long itemId,
			@PathVariable Integer reasonId) {
		try {
			return moderationService.reject(new RejectItemRequest(itemId, reasonId));
		} catch (ServiceException e) {
			logger.error("error trying to reject an item in moderation service");
			Map<String, String> eParams = new HashMap<String, String>();
			eParams.put("parkToken", parkToken);
			eParams.put("item id", itemId.toString());
			e.setRequestToContext(eParams);
			throw e;
		} 
	}

	@RequestMapping(value = "/items/{id}/contactPublisher", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse contactPublisher(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@PathVariable Long id, @RequestBody ContactPublisherRequest request) {

		request.setItemId(id);
		try {
			return moderationService.contactPublisher(request);
		} catch (ServiceException e) {
			logger.error("error trying to contactPublisher in moderation service");
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = "/signin", method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse signIn(@RequestBody AdminSignInRequest request) {
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					moderationService.signIn(request));
		} catch (ServiceException e) {
			logger.error("error trying to signin admin user");
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = "/signout", method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse signOut(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@RequestBody ParkRequest request) throws ServiceException {
		try {
			return moderationService.signOut(parkToken);
		} catch (ServiceException e) {
			logger.error("error trying to signout from moderation service");
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse searchUsers(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "exactMatch", required = false) Boolean exactMatch,
			@RequestParam(value = "userVerified", required = false) Boolean userVerified){

		SearchUserForModerationRequest request = new SearchUserForModerationRequest(page, pageSize);
		request.setEmail(email);
		request.setStatus(status);
		/*TODO: Temporaly username to lowercase. In the future we will change the index avoid lowercase */
		request.setUsername((username != null ? username.toLowerCase() : username ));
		request.setOrder(order);
		request.setUserVerified(userVerified);
		//Set true, we only want to search by exactMatch trough Moderation App.
		request.setExactMatch(true);

		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					moderationService.searchUser(request));
		} catch (ServiceException e) {
			logger.error("error trying to search users in moderation service");
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = "/users/{userId}/activate", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse activateUser(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@PathVariable Long userId) {
		UserIdRequest request = new UserIdRequest(userId);
		try {
			return moderationService.activateUser(request);
		} catch (ServiceException e) {
			logger.error("error trying to activate user in moderation service. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = "/users/{userId}/ban", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse banUser(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@PathVariable Long userId) {
		UserIdRequest request = new UserIdRequest(userId);
		try {
			return moderationService.banUser(request);
		} catch (ServiceException e) {
			logger.error("error trying to ban user in moderation service. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = "/users/{userId}/contactUser", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse contactUser(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@PathVariable Long userId, @RequestBody ContactUserRequest request) {
		try {
			request.setUserId(userId);
			return moderationService.contactUser(request);
		} catch (ServiceException e) {
			logger.error("error trying to contact user in moderation service. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = "/groups/{groupId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse deleteGroup(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@PathVariable Long groupId) {
		GroupIdRequest request = new GroupIdRequest(groupId);
		try {
			return moderationService.removeGroup(request);
		} catch (ServiceException e) {
			logger.error("error trying to delete group in moderation service. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = "/groups", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse searchGroups(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "creatorName", required = false) String creatorName,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "userVerified", required = false) Boolean userVerified) {
		
		/*TODO: Temporaly username to lowercase. In the future we will change the index avoid lowercase */
		SearchGroupForModerationRequest request = new SearchGroupForModerationRequest(
				name,  (creatorName != null ? creatorName.toLowerCase() : creatorName ), page, pageSize);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					moderationService.searchGroups(request));
		} catch (ServiceException e) {
			logger.error("error trying to search group in moderation service. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	@RequestMapping(value = "/users/notify", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public  @ResponseBody WebAsyncTask<ServiceResponse> sendNotifications(
            @RequestHeader(PARK_TOKEN_HEADER) String parkToken,
            @RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
            @RequestBody SendNotificationsForModerationRequest request){

	    final Callable<ServiceResponse> callable = new Callable<ServiceResponse>() {

            @Override
            public ServiceResponse call() throws Exception {
                try {
                    return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
                            moderationService.sendNotifications(request));
                } catch (ServiceException e) {
                    logger.error("error trying to filter and send notifications in moderation service. Token: {}", parkToken);
                    e.setRequestToContext(request);
                    throw e;
                } 
            }

	    };

	    final Callable<ServiceResponse> callableTimeout = new Callable<ServiceResponse>() {
          @Override
          public ServiceResponse call() throws Exception {
              logger.error("Request has timed out in moderation service! Token: {}", parkToken);
              throw ServiceException.createServiceException(ServiceExceptionCode.TIMEOUT);
          }
      };

	    WebAsyncTask<ServiceResponse> task = new WebAsyncTask<>(300000 /*milliseconds*/,callable);
	    task.onTimeout(callableTimeout);
	    return task;
    }
	
	@RequestMapping(value = "/users/notify/preFilter", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse preFilter(
			@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestBody FilterForModerationRequest request) {	
		try { 
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					moderationService.preFilter(request));
		} catch (ServiceException e) {
			logger.error("error trying to filter in moderation service. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	@RequestMapping(value = "/admin", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public ServiceResponse addAdminUser(
			@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestBody AddModeratorRequest request){
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					adminService.addModerator(request));
		} catch (ServiceException e) {
			logger.error("error trying to filter and send notifications in moderation service. Token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}
}
