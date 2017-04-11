package com.ebay.park.service.group;

import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
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
import com.ebay.park.service.group.dto.SubscribeGroupRequest;
import com.ebay.park.service.group.dto.UnsubscribeGroupFollowersRequest;
import com.ebay.park.service.group.dto.UnsubscribeGroupRequest;
import com.ebay.park.service.group.dto.UpdateGroupRequest;
import com.ebay.park.service.group.dto.UploadGroupPhotoRequest;
import com.ebay.park.service.item.dto.SearchItemResponse;
import com.ebay.park.service.item.dto.SmallGroupDTO;
import com.ebay.park.service.social.dto.SearchUserResponse;

/**
 * 
 * @author marcos.lambolay
 */
public interface GroupService {
	
	public SmallGroupDTO create(CreateGroupRequest request) throws ServiceException;
	
	public ServiceResponse subscribe(SubscribeGroupRequest request) throws ServiceException;
	
	public ServiceResponse unsubscribe(UnsubscribeGroupRequest request) throws ServiceException;

	public SearchGroupResponse search(SearchGroupRequest request) throws ServiceException;

	public ServiceResponse uploadPhoto(UploadGroupPhotoRequest request);

	public GroupDTO get(GetGroupRequest request);
	
	public SearchGroupResponse getRecommendedGroups(ParkRequest request);
	
	public SearchItemResponse getItems(GetGroupItemsRequest request);
	
	public SearchUserResponse getSubscribers(GetGroupSubscribersRequest request);
	
	public ServiceResponse unsubscribeGroupFollowers(UnsubscribeGroupFollowersRequest request);
	
	public ServiceResponse removeGroupItems(RemoveGroupItemsRequest request);
	
	public ServiceResponse updateGroup(UpdateGroupRequest request);
	
	public ShareGroupOnSocialResponse shareGroupOnSocial(ShareGroupOnSocialRequest request);
	
	public ServiceResponse deleteGroup(DeleteGroupRequest request);

	public ServiceResponse resetCounter(ResetGroupCounterRequest request);

	public Long countUnreadGroupItems(String token);

	public GetNewItemsInfoResponse getNewItemsInfo(GetNewItemsInfoRequest request);

    public ListGroupsNamesResponse listGroupsNames(ParkRequest request);
}