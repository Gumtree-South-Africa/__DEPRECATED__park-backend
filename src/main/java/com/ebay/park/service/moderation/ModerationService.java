package com.ebay.park.service.moderation;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderation.dto.AdminSignInRequest;
import com.ebay.park.service.moderation.dto.AdminSignInResponse;
import com.ebay.park.service.moderation.dto.ContactPublisherRequest;
import com.ebay.park.service.moderation.dto.ContactUserRequest;
import com.ebay.park.service.moderation.dto.RejectItemRequest;
import com.ebay.park.service.moderation.dto.FilterForModerationRequest;
import com.ebay.park.service.moderation.dto.GroupIdRequest;
import com.ebay.park.service.moderation.dto.ItemRequest;
import com.ebay.park.service.moderation.dto.SearchGroupForModerationRequest;
import com.ebay.park.service.moderation.dto.SearchGroupForModerationResponse;
import com.ebay.park.service.moderation.dto.SearchItemForModerationRequest;
import com.ebay.park.service.moderation.dto.SearchItemForModerationResponse;
import com.ebay.park.service.moderation.dto.SearchUserForModerationRequest;
import com.ebay.park.service.moderation.dto.SearchUserForModerationResponse;
import com.ebay.park.service.moderation.dto.SendNotificationsForModerationRequest;
import com.ebay.park.service.moderation.dto.SendNotificationsForModerationResponse;
import com.ebay.park.service.moderation.dto.UserIdRequest;
import com.ebay.park.service.zipcode.dto.SearchZipCodeRequest;
import com.ebay.park.service.zipcode.dto.SearchZipCodeResponse;

public interface ModerationService {

	public SearchItemForModerationResponse searchItem(
			SearchItemForModerationRequest request);

	public ServiceResponse activateItem(ItemRequest request)
			throws ServiceException;

	public ServiceResponse reject(RejectItemRequest request) throws ServiceException;

	public ServiceResponse contactPublisher(ContactPublisherRequest request)
			throws ServiceException;

	public AdminSignInResponse signIn(AdminSignInRequest request)
			throws ServiceException;

	public ServiceResponse signOut(String token) throws ServiceException;

	public SearchUserForModerationResponse searchUser(
			SearchUserForModerationRequest request);

	public ServiceResponse banUser(UserIdRequest request)
			throws ServiceException;

	public ServiceResponse activateUser(UserIdRequest request)
			throws ServiceException;

	public ServiceResponse contactUser(ContactUserRequest request)
			throws ServiceException;

	public ServiceResponse removeGroup(GroupIdRequest request)
			throws ServiceException;

	public SearchGroupForModerationResponse searchGroups(SearchGroupForModerationRequest request);
	
	public SendNotificationsForModerationResponse sendNotifications(
			SendNotificationsForModerationRequest request);

	public SendNotificationsForModerationResponse preFilter(
			FilterForModerationRequest request);

	public SearchZipCodeResponse searchZipCodesByState(SearchZipCodeRequest request);

}
