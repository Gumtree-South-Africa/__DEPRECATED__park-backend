package com.ebay.park.service.group;


import static com.ebay.park.service.ServiceException.createServiceException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ebay.park.db.entity.Social;
import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.group.command.CountUnreadGroupItemsCmd;
import com.ebay.park.service.group.command.CreateGroupCmd;
import com.ebay.park.service.group.command.GetNewItemsInfoCmd;
import com.ebay.park.service.group.command.RecommendedGroupsCmd;
import com.ebay.park.service.group.command.ResetGroupCounterCmd;
import com.ebay.park.service.group.command.SearchGroupCmd;
import com.ebay.park.service.group.command.UnsubscribeGroupCmd;
import com.ebay.park.service.group.command.UploadGroupPhotoCmd;
import com.ebay.park.service.group.dto.CreateGroupRequest;
import com.ebay.park.service.group.dto.DeleteGroupRequest;
import com.ebay.park.service.group.dto.GetGroupItemsRequest;
import com.ebay.park.service.group.dto.GetGroupRequest;
import com.ebay.park.service.group.dto.GetGroupSubscribersRequest;
import com.ebay.park.service.group.dto.GetNewItemsInfoRequest;
import com.ebay.park.service.group.dto.GetNewItemsInfoResponse;
import com.ebay.park.service.group.dto.GroupDTO;
import com.ebay.park.service.group.dto.ListGroupsNamesResponse;
import com.ebay.park.service.group.dto.RemoveGroupItemsRequest;
import com.ebay.park.service.group.dto.ResetGroupCounterRequest;
import com.ebay.park.service.group.dto.SearchGroupRequest;
import com.ebay.park.service.group.dto.SearchGroupResponse;
import com.ebay.park.service.group.dto.ShareGroupOnSocialRequest;
import com.ebay.park.service.group.dto.ShareGroupOnSocialResponse;
import com.ebay.park.service.group.dto.ShareGroupToSocialNetworkRequest;
import com.ebay.park.service.group.dto.SubscribeGroupRequest;
import com.ebay.park.service.group.dto.UnsubscribeGroupFollowersRequest;
import com.ebay.park.service.group.dto.UnsubscribeGroupRequest;
import com.ebay.park.service.group.dto.UpdateGroupRequest;
import com.ebay.park.service.group.dto.UploadGroupPhotoRequest;
import com.ebay.park.service.group.validator.CreateGroupValidator;
import com.ebay.park.service.group.validator.RemoveGroupItemsValidator;
import com.ebay.park.service.group.validator.SearchGroupRequestValidator;
import com.ebay.park.service.group.validator.ShareGroupValidator;
import com.ebay.park.service.group.validator.UnsubscribeGroupFollowersValidator;
import com.ebay.park.service.group.validator.UpdateGroupValidator;
import com.ebay.park.service.group.validator.UploadGroupPhotoValidator;
import com.ebay.park.service.item.dto.SearchItemResponse;
import com.ebay.park.service.item.dto.SmallGroupDTO;
import com.ebay.park.service.social.dto.SearchUserResponse;
import com.ebay.park.util.InternationalizationUtil;
import com.ebay.park.util.TextUtils;

/**
 * 
 * @author marcos.lambolay
 */
@Service
public class GroupServiceImpl implements GroupService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(GroupServiceImpl.class);
	
	@Autowired
	private CreateGroupValidator createGroupValidator;
	
	@Autowired
	private UpdateGroupValidator updateGroupValidator;
	
	@Autowired
	private UnsubscribeGroupFollowersValidator unsubscribeGroupFollowersValidator;

	@Autowired
	@Qualifier("createGroupCmd")
	private CreateGroupCmd createGroupCmd;

	@Autowired
	private  ServiceCommand<SubscribeGroupRequest, ServiceResponse>  subscribeGroupCmd;

	@Autowired
	private UnsubscribeGroupCmd unsubscribeGroupCmd;

	@Autowired
	private SearchGroupRequestValidator searchGroupRequestValidator;

	@Autowired
	private SearchGroupCmd searchGroupCmd;

	@Autowired
	private UploadGroupPhotoCmd uploadGroupPhotoCmd;

	@Autowired
	private UploadGroupPhotoValidator uploadGroupPhotoValidator;

	@Autowired
	private RecommendedGroupsCmd recommendGroupCmd;
	
	@Autowired
	@Qualifier("getGroupCmd")
	private  ServiceCommand<GetGroupRequest, GroupDTO>  getGroupCmd;

	@Autowired
	private ServiceCommand<GetGroupItemsRequest, SearchItemResponse>  getGroupItemsCmd;
	
	@Autowired
	private ServiceCommand<GetGroupSubscribersRequest, SearchUserResponse>  getGroupSubscribersCmd;
	
	@Autowired
	private  ServiceCommand<UnsubscribeGroupFollowersRequest, ServiceResponse>  unsubscribeGroupFollowersCmd;
	
	@Autowired
	private ServiceCommand<UpdateGroupRequest, ServiceResponse> updateGroupCmd;
	
	@Autowired
	private ServiceCommand<ShareGroupToSocialNetworkRequest, ServiceResponse> shareGroupCmd;
	
	@Autowired
	private ServiceCommand<DeleteGroupRequest, ServiceResponse> deleteGroupCmd;
	
	@Autowired
	private ServiceCommand<RemoveGroupItemsRequest, ServiceResponse> removeGroupItemsCmd;
	
	@Autowired
	private InternationalizationUtil i18nUtil;
	
	@Autowired
	private ShareGroupValidator shareGroupValidator;
	
	@Autowired
	private RemoveGroupItemsValidator removeGroupItemsValidator;
	
	@Autowired
	private TextUtils textUtils;

	@Autowired
	private ResetGroupCounterCmd resetGroupCounterCmd;

	@Autowired
	private CountUnreadGroupItemsCmd countUnreadGroupItemsCmd;

	@Autowired
	private GetNewItemsInfoCmd getNewItemsInfoCmd;

	@Autowired
    private ServiceCommand<ParkRequest, ListGroupsNamesResponse> listGroupsNamesCmd;
	
	@Override
	public SmallGroupDTO create(CreateGroupRequest request)
			throws ServiceException {
		createGroupValidator.validate(request);
		SmallGroupDTO reply = SmallGroupDTO.fromCreateGroupResponse(createGroupCmd
				.execute(request)); 
		reply.setURL(textUtils.createGroupSEOURL(reply.getName(), reply.getId()));
		return reply;
	}
	
	@Override
	public ServiceResponse updateGroup(UpdateGroupRequest request)
			throws ServiceException {		
		updateGroupValidator.validate(request);
		return updateGroupCmd.execute(request);
	}

	@Override
	public ServiceResponse subscribe(SubscribeGroupRequest request)
			throws ServiceException {
		return subscribeGroupCmd.execute(request);
	}

	@Override
	public ServiceResponse unsubscribe(UnsubscribeGroupRequest request)
			throws ServiceException {
		return unsubscribeGroupCmd.execute(request);
	}

	@Override
	public SearchGroupResponse search(SearchGroupRequest request) throws ServiceException {
		searchGroupRequestValidator.validate(request);

		return searchGroupCmd.execute(request);
	}

	@Override
	public ServiceResponse uploadPhoto(UploadGroupPhotoRequest request) {
		uploadGroupPhotoValidator.validate(request);
		return uploadGroupPhotoCmd.execute(request);
	}

	@Override
	public GroupDTO get(GetGroupRequest request) {
		return getGroupCmd.execute(request);
	}

	@Override
	public SearchGroupResponse getRecommendedGroups(ParkRequest request) {
		return recommendGroupCmd.execute(request);
	}

	@Override
	public SearchItemResponse getItems(GetGroupItemsRequest request) {
		return getGroupItemsCmd.execute(request);
	}

	@Override
	public SearchUserResponse getSubscribers(GetGroupSubscribersRequest request) {
		return getGroupSubscribersCmd.execute(request);
	}
	
	@Override
	public ServiceResponse unsubscribeGroupFollowers(
			UnsubscribeGroupFollowersRequest request) {
		unsubscribeGroupFollowersValidator.validate(request);
		return unsubscribeGroupFollowersCmd.execute(request);
	}
	
	@Override
	public ServiceResponse removeGroupItems(
			RemoveGroupItemsRequest request) {
		removeGroupItemsValidator.validate(request);
		return removeGroupItemsCmd.execute(request);
	}
	
	@Override
	public ServiceResponse deleteGroup(DeleteGroupRequest request){
		return deleteGroupCmd.execute(request);
	}

	@Override
	public ShareGroupOnSocialResponse shareGroupOnSocial(ShareGroupOnSocialRequest request){
		
		shareGroupValidator.validate(request);
		
		ShareGroupOnSocialResponse response = new ShareGroupOnSocialResponse();
		boolean overallShareStatus = false;

		if (request.isShareOnFacebook()) {
			try {		
				shareGroupCmd.execute(new ShareGroupToSocialNetworkRequest(request.getGroupId(), request.getToken(),
						Social.FACEBOOK));
				overallShareStatus = true;
				
			} catch (ServiceException e) {
				response.setFacebookPublishError(i18nUtil
						.internationalizeMessage(e.getMessage(),
								request.getLanguage()));
				LOGGER.error("Group share on Facebook failed", e);
			}
		}
		
		if (request.isShareOnTwitter()) {
			try {
				
				shareGroupCmd.execute(new ShareGroupToSocialNetworkRequest(request.getGroupId(), request.getToken(),
						Social.TWITTER));
				overallShareStatus = true;
				
			} catch (ServiceException e) {
				response.setTwitterPublishError(i18nUtil
						.internationalizeMessage(e.getMessage(),
								request.getLanguage(),
								e.getLocalizableMsgArgs()));
				LOGGER.error("Group share on Twitter failed", e);
			}
		}
		
		response.setSuccess(overallShareStatus);
		
		return response;

	}

	@Override
	public ServiceResponse resetCounter(ResetGroupCounterRequest request) {
		return resetGroupCounterCmd.execute(request);
	}

	@Override
	public Long countUnreadGroupItems(String token) {
		if (StringUtils.isBlank(token)) {
			throw createServiceException(ServiceExceptionCode.EMPTY_PARK_TOKEN);
		}
		return countUnreadGroupItemsCmd.execute(token);
	}

	@Override
	public GetNewItemsInfoResponse getNewItemsInfo(
			GetNewItemsInfoRequest request) {
		return getNewItemsInfoCmd.execute(request);
	}

    @Override
    public ListGroupsNamesResponse listGroupsNames(ParkRequest request) {
       return listGroupsNamesCmd.execute(request);
    }

	
}
