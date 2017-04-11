package com.ebay.park.service.group.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserFollowsGroupDao;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserFollowsGroup;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.group.dto.GetNewItemsInfoRequest;
import com.ebay.park.service.group.dto.GetNewItemsInfoResponse;

@Component
public class GetNewItemsInfoCmd implements
ServiceCommand<GetNewItemsInfoRequest, GetNewItemsInfoResponse>{

	@Autowired
	private UserFollowsGroupDao userFollowsGroupDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private GroupDao groupDao;
	
	@Override
	public GetNewItemsInfoResponse execute(GetNewItemsInfoRequest request)
			throws ServiceException {
		User user = null;

		if (request.getToken() != null) {
			user = userDao.findByToken(request.getToken());
			if (user == null) {
				throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
			}
		}
		
		List<UserFollowsGroup> ownedGroupList = userFollowsGroupDao.findOwnedGroups(user.getUserId());
		
		Long ownedGroupsItems = 0l;
		for (UserFollowsGroup usergroup : ownedGroupList) {
			ownedGroupsItems += groupDao.getNewItemsCount(usergroup.getLastAccess(), usergroup.getGroup().getGroupId(),
					StatusDescription.ACTIVE);
		}
		
		List<UserFollowsGroup> subscribedGroupList = userFollowsGroupDao.findGroups(user.getUserId());
		
		Long subscribedGroupItems = 0l;
		for (UserFollowsGroup usergroup : subscribedGroupList) {
			subscribedGroupItems += groupDao.getNewItemsCount(usergroup.getLastAccess(), usergroup.getGroup().getGroupId(),
					StatusDescription.ACTIVE);
		}

		return new GetNewItemsInfoResponse(ownedGroupsItems, subscribedGroupItems);
	}

}
