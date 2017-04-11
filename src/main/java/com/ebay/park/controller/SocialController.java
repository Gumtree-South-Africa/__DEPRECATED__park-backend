package com.ebay.park.controller;

import javax.ws.rs.QueryParam;

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
import com.ebay.park.service.social.SocialService;
import com.ebay.park.service.social.dto.DiscoverUsersRequest;
import com.ebay.park.service.social.dto.FollowUserRequest;
import com.ebay.park.service.social.dto.GetFollowersRequest;
import com.ebay.park.service.social.dto.GetFollowingsRequest;
import com.ebay.park.service.social.dto.ListFacebookFriendsRequest;
import com.ebay.park.service.social.dto.SearchUserRequest;
import com.ebay.park.service.social.dto.ShareProfileRequest;
import com.ebay.park.service.social.dto.SocialNetworkConnectRequest;
import com.ebay.park.service.social.dto.SocialNetworkDisconnectRequest;
import com.ebay.park.service.social.dto.UnfollowUserRequest;
import com.ebay.park.service.social.dto.UnreadCountRequest;
import com.ebay.park.service.social.dto.UserRatesRequest;
import com.ebay.park.util.ParkConstants;

@RestController
public class SocialController implements ParkConstants {

	private static Logger logger = LoggerFactory.getLogger(SocialController.class);

	@Autowired
	private SocialService socialService;
	
	@RequestMapping(value = {"/public/users/v3/{username}/followers","/public/users/v3.0/{username}/followers"}, method = RequestMethod.GET, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse getFollowers(
			@PathVariable String username,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String language) {
		GetFollowersRequest request = new GetFollowersRequest( username, null, language);

		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					socialService.getFollowers(request));
		} catch (ServiceException e) {
			logger.error("error trying to get followers for: {}", username);
			e.setRequestToContext(username);
			throw e;
		} 
	}

	@RequestMapping(value = {"/users/v3/{username}/followers", "/users/v3.0/{username}/followers"}, method = RequestMethod.GET, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse getFollowers(
			@RequestHeader(value = PARK_TOKEN_HEADER, required = false) String parkToken,
			@PathVariable String username,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String language) {
		GetFollowersRequest request = new GetFollowersRequest( username, parkToken, language);

		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					socialService.getFollowers(request));
		} catch (ServiceException e) {
			logger.error("error trying to get followers for: {}", username);
			e.setRequestToContext(username);
			throw e;
		} 
	}
	
	@RequestMapping(value = {"/public/users/v3/{username}/follow","/public/users/v3.0/{username}/follow"}, method = RequestMethod.GET, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse getFollowings(
			@PathVariable String username,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String language) {
		try {
			GetFollowingsRequest request = new GetFollowingsRequest();
			request.setToken(null);
			request.setUsername(username);
			request.setLanguage(language);
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					socialService.getFollowings(request));
		} catch (ServiceException e) {
			logger.error("Error trying to get followings");
			e.setRequestToContext(username);
			throw e;
		} 
	}

	@RequestMapping(value = {"/users/v3/{username}/follow","/users/v3.0/{username}/follow"}, method = RequestMethod.GET, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse getFollowings(
			@RequestHeader(value = PARK_TOKEN_HEADER, required = false) String parkToken,
			@PathVariable String username,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String language) {
		try {
			GetFollowingsRequest request = new GetFollowingsRequest();
			request.setToken(parkToken);
			request.setUsername(username);
			request.setLanguage(language);
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					socialService.getFollowings(request));
		} catch (ServiceException e) {
			logger.error("Error trying to get followings. Token: {}", parkToken);
			e.setRequestToContext(username);
			throw e;
		} 
	}

	@RequestMapping(value = {"/users/v3/{username}/follow","/users/v3.0/{username}/follow"}, method = RequestMethod.PUT, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse addFollowing(@RequestHeader(PARK_TOKEN_HEADER) String token,
			@PathVariable String username, @RequestBody FollowUserRequest request) {
		request.setFollower(username);
		request.setToken(token);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					socialService.addFollowerToUser(request));
		} catch (ServiceException e) {
			logger.error("Error trying to follow a user. Token: {}", token);
			e.setRequestToContext(username);
			throw e;
		} 
	}

	@RequestMapping(value = {"/users/v3/{username}/unfollow","/users/v3.0/{username}/unfollow"}, method = RequestMethod.PUT, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse removeFollow(@RequestHeader(PARK_TOKEN_HEADER) String token,
			@PathVariable String username, @RequestBody UnfollowUserRequest request) {
		request.setFollower(username);
		request.setToken(token);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					socialService.removeFollowToUser(request));
		} catch (ServiceException e) {
			logger.error("Error trying to unfollow a user. Token: {}", token);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/public/users/v3/{username}/rates","/public/users/v3.0/{username}/rates"}, method = RequestMethod.GET, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse getRates(@PathVariable String username,
			@QueryParam(value = "rate") String rate) {
		UserRatesRequest request = new UserRatesRequest(username, rate);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					socialService.getUserRates(request));
		} catch (ServiceException e) {
			e.setRequestToContext(request);
			logger.error("error trying to get rates for username: {}", username);
			throw e;
		} 
	}

	@RequestMapping(value = {"/social/v3/{username}/facebook/share","/social/v3.0/{username}/facebook/share"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse shareProfileOnFacebook(@RequestHeader(PARK_TOKEN_HEADER) String token,
			@PathVariable String username, @RequestBody ShareProfileRequest request) {
		request.setSharerUsername(username);
		request.setToken(token);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					socialService.shareProfileOnFacebook(request));
		} catch (ServiceException e) {
			logger.error("Error trying to share a profile on facebook. Token: {}", token);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/social/v3/{username}/twitter/share","/social/v3.0/{username}/twitter/share"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse shareProfileOnTwitter(@RequestHeader(PARK_TOKEN_HEADER) String token,
			@PathVariable String username, @RequestBody ShareProfileRequest request) {
		request.setSharerUsername(username);
		request.setToken(token);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					socialService.shareProfileOnTwitter(request));
		} catch (ServiceException e) {
			logger.error("Error trying to share a profile on twitter. Token: {}", token);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/social/v3/{username}/connect","/social/v3.0/{username}/connect"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse bindUserToSocialNetwork(@RequestHeader(PARK_TOKEN_HEADER) String token,
			@PathVariable String username, @RequestBody SocialNetworkConnectRequest request) {
		request.setToken(token);
		request.setUserName(username);
		try {
			socialService.connectToSocialNetwork(request);
			return ServiceResponse.SUCCESS;
		} catch (ServiceException e) {
			logger.error("Error trying to bind Park account to social network account. Token: {}", token);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/social/v3/{username}/disconnect","/social/v3.0/{username}/disconnect"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse unbindUserToSocialNetwork(@RequestHeader(PARK_TOKEN_HEADER) String token,
			@PathVariable String username, @RequestBody SocialNetworkDisconnectRequest request) {
		request.setToken(token);
		request.setUsername(username);
		try {
			socialService.disconnectSocialNetwork(request);
			return ServiceResponse.SUCCESS;
		} catch (ServiceException e) {
			logger.error("Error trying to unbind Park account to social network account. Token: {}", token);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/social/v3/{username}/facebook/friends","/social/v3.0/{username}/facebook/friends"}, method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse facebookFriends(@RequestHeader(PARK_TOKEN_HEADER) String token,
			@PathVariable String username) {
		ListFacebookFriendsRequest request = new ListFacebookFriendsRequest();
		request.setToken(token);
		request.setUsername(username);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					socialService.listFacebookFriends(request));
		} catch (ServiceException e) {
			logger.error("Error trying to get the list of Facebook friends. Token: {} ", token);
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	@RequestMapping(value = {"/public/social/v3/discover","/public/social/v3.0/discover"}, method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse discoverPublicUsers(
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,
			@RequestParam(value = "radius", required = false) Double radius,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String language) {

		DiscoverUsersRequest request = new DiscoverUsersRequest(null, latitude, longitude, radius, null, language);

		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					socialService.discoverPublicUsers(request));
		} catch (ServiceException e) {
			logger.error("Error trying to discover users");
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/social/v3/{username}/discover","/social/v3.0/{username}/discover"}, method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse discoverUsers(@RequestHeader(PARK_TOKEN_HEADER) String token,
			@PathVariable("username") String username,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,
			@RequestParam(value = "radius", required = false) Double radius,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String language) {

		DiscoverUsersRequest request = new DiscoverUsersRequest( username,  latitude,  longitude,  radius,  token,  language);

		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					socialService.discoverUsers(request));
		} catch (ServiceException e) {
			logger.error("Error trying to discover users. Token: {}", token);
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	@RequestMapping(value = {"/public/users/v3/search","/public/users/v3.0/search"}, method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse searchUsers(
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@RequestParam(value = "q", required = false) String criteria,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,
			@RequestParam(value = "radius", required = false) Double radius,
			@RequestParam(value = "groupId", required = false) Long groupId,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "requestTime", required = false) String requestTime) {
		SearchUserRequest request = new SearchUserRequest(null, lang, page,
				pageSize, null, latitude, longitude, radius, criteria, order,
				false, false, groupId);	
		request.setRequestTime(requestTime);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					socialService.searchPublicUsers(request));
		} catch (ServiceException e) {
			logger.error("Error trying to discover users");
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"/social/v3/{username}/search","/social/v3.0/{username}/search"}, method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse searchUsers(@RequestHeader(PARK_TOKEN_HEADER) String token,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang,
			@PathVariable("username") String username,
			@RequestParam(value = "q", required = false) String criteria,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,
			@RequestParam(value = "radius", required = false) Double radius,
			@RequestParam(value = "followed", required = false) boolean followed,
			@RequestParam(value = "followingMe", required = false) boolean followingMe,
			@RequestParam(value = "groupId", required = false) Long groupId,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "requestTime", required = false) String requestTime) {
		SearchUserRequest request = new SearchUserRequest(token, lang, page,
				pageSize, username, latitude, longitude, radius, criteria, order,
				followed, followingMe, groupId);
		request.setRequestTime(requestTime);
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					socialService.searchUsers(request));
		} catch (ServiceException e) {
			logger.error("Error trying to discover users. Token: {}", token);
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	@RequestMapping(value = {"/social/v3/unreadCount","/social/v3.0/unreadCount", "/social/unreadCount"}, method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ServiceResponse countUnread(@RequestHeader(PARK_TOKEN_HEADER) String token,
			@RequestHeader(value = LANGUAGE_HEADER, required = false) String lang) {
		UnreadCountRequest request = new UnreadCountRequest(token, lang);
		
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY,
					socialService.unreadCount(request));
		} catch (ServiceException e) {
			logger.error("Error trying to get unread elements. Token: {}", token);
			e.setRequestToContext(request);
			throw e;
		} 
	}
}
