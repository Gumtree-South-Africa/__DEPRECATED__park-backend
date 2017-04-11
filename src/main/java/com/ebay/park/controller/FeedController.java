package com.ebay.park.controller;

import com.ebay.park.service.notification.dto.MarkAsReadRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.service.notification.FeedService;
import com.ebay.park.service.notification.dto.GetFeedsRequest;
import com.ebay.park.service.notification.dto.GetUnreadFeedsCounterRequest;
import com.ebay.park.util.ParkConstants;

@RestController
@RequestMapping(value="/feeds")
public class FeedController  implements ParkConstants{

	private static Logger logger = LoggerFactory.getLogger(FeedController.class);

	@Autowired
	private FeedService feedService;

	@RequestMapping(value = { "/v3/users/{username}", "/v3.0/users/{username}"}, method = RequestMethod.GET, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse getFeedsV3(@PathVariable String username,
			@RequestHeader(value = PARK_TOKEN_HEADER, required = false) String parkToken,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang) {
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					feedService.getFeedsV3(new GetFeedsRequest(username, parkToken, lang)));
		} catch (ServiceException e) {
			logger.error("error trying to get the feeds for user. Username={}", username);
			e.setRequestToContext(username);
			throw e;
		} 
	}

	@RequestMapping(value = { "/v4/users/{username}", "/v4.0/users/{username}"}, method = RequestMethod.GET, consumes = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public ServiceResponse getFeedsV4(@PathVariable String username,
            @RequestHeader(value = PARK_TOKEN_HEADER, required = false) String parkToken,
            @RequestHeader(value = LANGUAGE_HEADER, required = false) String lang) {
        try {
            return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
                    feedService.getFeedsV4(new GetFeedsRequest(username, parkToken, lang)));
        } catch (ServiceException e) {
            logger.error("error trying to get the feeds for user. Username={}", username);
            e.setRequestToContext(username);
            throw e;
        }
    }
	
	@RequestMapping(value = { "/v5/users/{username}", "/v5.0/users/{username}"}, method = RequestMethod.GET, consumes = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public ServiceResponse getFeedsV5(@PathVariable String username,
            @RequestHeader(value = PARK_TOKEN_HEADER, required = false) String parkToken,
            @RequestHeader(value = LANGUAGE_HEADER, required = false) String lang) {
        try {
            return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
                    feedService.getFeedsV5(new GetFeedsRequest(username, parkToken, lang)));
        } catch (ServiceException e) {
            logger.error("error trying to get the feeds for user. Username={}", username);
            e.setRequestToContext(username);
            throw e;
        }
    }
	
	@RequestMapping(value = { "/v3/unreadFeedsCount", "/v3.0/unreadFeedsCount"}, method = RequestMethod.GET, consumes = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public ServiceResponse getUnreadFeedsCounter(
            @RequestHeader(value = PARK_TOKEN_HEADER) String parkToken,
            @RequestHeader(value = LANGUAGE_HEADER, required = false) String lang) {
        try {
            return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
                    feedService.countUnreadFeeds(new GetUnreadFeedsCounterRequest(parkToken, lang)));
        } catch (ServiceException e) {
            logger.error("error trying to get the unread feeds counter for user. Token={}", parkToken);
            throw e;
        }
    }

    @RequestMapping(value = { "/v3/{feedId}/read", "/v3.0/{feedId}/read"}, method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public ServiceResponse markAsRead(@PathVariable Long feedId,
            @RequestHeader(value = PARK_TOKEN_HEADER) String parkToken,
            @RequestHeader(value = LANGUAGE_HEADER, required = false) String lang) {
        try {
            MarkAsReadRequest request = new MarkAsReadRequest(parkToken, lang, feedId);
            feedService.readFeed(request);

            return ServiceResponse.SUCCESS;
        } catch (ServiceException e) {
            logger.error("error trying to mark a feed as read. Token={}", parkToken);
            throw e;
        }
    }

}
