package com.ebay.park.controller;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.ebay.park.service.notification.NotificationService;
import com.ebay.park.service.notification.dto.GetUserNotificationRequest;
import com.ebay.park.service.notification.dto.NotificationConfigRequest;
import com.ebay.park.util.ParkConstants;

@RestController
@RequestMapping(value={"/notifications"})
public class NotificationController implements ParkConstants{

	private static Logger logger = LoggerFactory.getLogger(NotificationController.class);

	@Resource(name = "NotificationServiceOp")
	private NotificationService notificationService;


	@RequestMapping(value = {"/v3/users/{username}/config", "/v3.0/users/{username}/config"},
	        method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse updateEmailNotification(@PathVariable String username, @RequestBody NotificationConfigRequest request) {

		request.setUsername(username);
		try {
			boolean success = notificationService.updateNotificationsConfig(request);
			return success ? ServiceResponse.SUCCESS : ServiceResponse.FAIL;
		} catch (ServiceException e) {
			logger.error("error trying to configure the push notifications for user. Username={}", username);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	//TODO Refactor this when adding full integration with the retrocompatibility solution
	@RequestMapping(value = {"/v3/users/{username}/config", "/v3.0/users/{username}/config"},
	        method = RequestMethod.GET, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse getNotificationConfigurationV3(@PathVariable String username,
			@RequestHeader(value = PARK_TOKEN_HEADER, required = false) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang) {

		logger.info("getNotificationConfiguration, Version 3 - Lang: {}", lang);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					notificationService
					.getNotificationConfigV3(new GetUserNotificationRequest(parkToken, lang, username)));
		} catch (ServiceException e) {
			logger.error("error trying to get configuration notifications for user. Username={}. Endpoint version= 3", username);
			e.setRequestToContext(username);
			throw e;
		} 
	}

	//TODO Refactor this when adding full integration with the retrocompatibility solution
	@RequestMapping(value = {"/v4/users/{username}/config", "/v4.0/users/{username}/config"},
            method = RequestMethod.GET, consumes = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public ServiceResponse getNotificationConfigurationV4(@PathVariable String username,
            @RequestHeader(value = PARK_TOKEN_HEADER, required = false) String parkToken,
            @RequestHeader(value = LANGUAGE_HEADER, required = false) String lang) {

        logger.info("getNotificationConfiguration, Version 4 - Lang: {}", lang);
        try {
            return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
                    notificationService
                    .getNotificationConfigV4(new GetUserNotificationRequest(parkToken, lang, username)));
        } catch (ServiceException e) {
            logger.error("error trying to get configuration notifications for user. Username={}. Endpoint version= 4", username);
            e.setRequestToContext(username);
            throw e;
        }
    }
	
	//TODO Refactor this when adding full integration with the retrocompatibility solution
	@RequestMapping(value = {"/v5/users/{username}/config", "/v5.0/users/{username}/config"},
	       method = RequestMethod.GET, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse getNotificationConfigurationV5(@PathVariable String username,
	        @RequestHeader(value = PARK_TOKEN_HEADER, required = false) String parkToken,
	        @RequestHeader(value = LANGUAGE_HEADER, required = false) String lang) {

	    logger.info("getNotificationConfiguration, Version 5 - Lang: {}", lang);
	    try {
	        return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
	                notificationService
	                .getNotificationConfigV5(new GetUserNotificationRequest(parkToken, lang, username)));
	    } catch (ServiceException e) {
	        logger.error("error trying to get configuration notifications for user. Username={}. Endpoint version= 5", username);
	        e.setRequestToContext(username);
	        throw e;
	    }
	}

}
