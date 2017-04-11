package com.ebay.park.service.moderation;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ebay.park.event.item.ItemNotificationEvent;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.moderation.command.PreFilterForModerationCmd;
import com.ebay.park.service.moderation.command.SendNotificationsForModerationCmd;
import com.ebay.park.service.moderation.dto.AdminSignInRequest;
import com.ebay.park.service.moderation.dto.AdminSignInResponse;
import com.ebay.park.service.moderation.dto.ContactPublisherRequest;
import com.ebay.park.service.moderation.dto.ContactUserRequest;
import com.ebay.park.service.moderation.dto.FilterForModerationRequest;
import com.ebay.park.service.moderation.dto.GroupIdRequest;
import com.ebay.park.service.moderation.dto.ItemRequest;
import com.ebay.park.service.moderation.dto.RejectItemRequest;
import com.ebay.park.service.moderation.dto.SearchGroupForModerationRequest;
import com.ebay.park.service.moderation.dto.SearchGroupForModerationResponse;
import com.ebay.park.service.moderation.dto.SearchItemForModerationRequest;
import com.ebay.park.service.moderation.dto.SearchItemForModerationResponse;
import com.ebay.park.service.moderation.dto.SearchUserForModerationRequest;
import com.ebay.park.service.moderation.dto.SearchUserForModerationResponse;
import com.ebay.park.service.moderation.dto.SendNotificationsForModerationRequest;
import com.ebay.park.service.moderation.dto.SendNotificationsForModerationResponse;
import com.ebay.park.service.moderation.dto.UserIdRequest;
import com.ebay.park.service.moderation.validator.SendNotificationsForModerationRequestValidator;
import com.ebay.park.service.zipcode.dto.SearchZipCodeRequest;
import com.ebay.park.service.zipcode.dto.SearchZipCodeResponse;
@Component
public class ModerationServiceImpl implements ModerationService {

	@Autowired
	private ServiceCommand<SearchItemForModerationRequest, SearchItemForModerationResponse> searchItemForModerationCmd;
	
	@Autowired
	private ServiceCommand<SearchZipCodeRequest, SearchZipCodeResponse> searchZipCodeByStateForModerationCmd;

	@Autowired
	@Qualifier("activateItemCmdImpl")
	private ServiceCommand<ItemRequest, ItemNotificationEvent> activateItemCmd;

	@Autowired
	@Qualifier("rejectItemCmdImpl")
	private ServiceCommand<RejectItemRequest, ServiceResponse> rejectItemCmd;

	@Autowired
	private ServiceCommand<ContactPublisherRequest, ServiceResponse> contactPublisherCmd;

	@Autowired
	private ServiceCommand<AdminSignInRequest, AdminSignInResponse> signInAdminCmd;

	@Autowired
	private ServiceCommand<String, ServiceResponse> signOutAdminCmd;

	@Autowired
	@Qualifier("activateUserCmdImpl")
	private ServiceCommand<UserIdRequest, ServiceResponse> activateUserCmd;

	@Autowired
	@Qualifier("banUserCmdImpl")
	private ServiceCommand<UserIdRequest, ServiceResponse> banUserCmd;

	@Autowired
	private ServiceCommand<SearchUserForModerationRequest, SearchUserForModerationResponse> searchUserForModerationCmd;

	@Autowired
	private ServiceCommand<ContactUserRequest, ServiceResponse> contactUserCmd;

	@Autowired
	private ServiceCommand<GroupIdRequest, ServiceResponse> removeGroupCmd;

	@Autowired
	private ServiceCommand<SearchGroupForModerationRequest, SearchGroupForModerationResponse>  searchGroupCmd;
	
	@Autowired
	private SendNotificationsForModerationCmd sendNotificationsForModerationCmd;
	
	@Autowired
	private SendNotificationsForModerationRequestValidator sendNotificationsValidator;
	
	@Autowired
	private ServiceValidator<FilterForModerationRequest> preFilterValidator;

	@Autowired
	private PreFilterForModerationCmd preFilterForModerationCmd;
	
	@Autowired
	private ServiceValidator<RejectItemRequest> rejectItemValidator;
	
	@Override
	public SearchItemForModerationResponse searchItem(
			SearchItemForModerationRequest request) {
		return searchItemForModerationCmd.execute(request);
	}

	@Override
	public ServiceResponse activateItem(ItemRequest request)
			throws ServiceException {
		activateItemCmd.execute(request);
		return ServiceResponse.SUCCESS;
	}

	@Override
	public ServiceResponse reject(RejectItemRequest request) throws ServiceException {
		rejectItemValidator.validate(request);
		return rejectItemCmd.execute(request);
	}

	@Override
	public ServiceResponse contactPublisher(ContactPublisherRequest request)
			throws ServiceException {
		if (StringUtils.isEmpty(request.getSubject())) {
			throw createServiceException(ServiceExceptionCode.EMAIL_EMPTY_SUBJECT_ERROR);
		}

		if (StringUtils.isEmpty(request.getBody())) {
			throw createServiceException(ServiceExceptionCode.EMAIL_EMPTY_BODY_ERROR);
		}
		return contactPublisherCmd.execute(request);
	}

	@Override
	public AdminSignInResponse signIn(AdminSignInRequest request)
			throws ServiceException {
		return signInAdminCmd.execute(request);
	}

	@Override
	public ServiceResponse signOut(String token) throws ServiceException {
		return signOutAdminCmd.execute(token);
	}

	@Override
	public ServiceResponse banUser(UserIdRequest request)
			throws ServiceException {
		return banUserCmd.execute(request);
	}

	@Override
	public ServiceResponse activateUser(UserIdRequest request)
			throws ServiceException {
		return activateUserCmd.execute(request);
	}

	@Override
	public SearchUserForModerationResponse searchUser(
			SearchUserForModerationRequest request) {
		return searchUserForModerationCmd.execute(request);
	}

	@Override
	public ServiceResponse contactUser(ContactUserRequest request)
			throws ServiceException {
		return contactUserCmd.execute(request);
	}

	@Override
	public ServiceResponse removeGroup(GroupIdRequest request)
			throws ServiceException {
		return removeGroupCmd.execute(request);
	}

	@Override
	public SearchGroupForModerationResponse searchGroups(SearchGroupForModerationRequest request) {
		return searchGroupCmd.execute(request);
	}

	@Override
	public SendNotificationsForModerationResponse sendNotifications(
			SendNotificationsForModerationRequest request) {
		sendNotificationsValidator.validate(request);
		return sendNotificationsForModerationCmd.execute(request);
	}

	@Override
	public SendNotificationsForModerationResponse preFilter(
			FilterForModerationRequest request) {
		preFilterValidator.validate(request);
		return preFilterForModerationCmd.execute(request);
	}

	@Override
	public SearchZipCodeResponse searchZipCodesByState(
			SearchZipCodeRequest request) {
		return searchZipCodeByStateForModerationCmd.execute(request);
	}
}
