package com.ebay.park.service.group.command;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.group.dto.GetGroupRequest;
import com.ebay.park.service.group.dto.GroupDTO;
import com.ebay.park.service.group.dto.SubscriberDTO;
import com.ebay.park.service.picture.ResetEPSPictureExpireDateService;
import com.ebay.park.util.TextUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;


@Component
public class GetGroupCmd  implements ServiceCommand<GetGroupRequest, GroupDTO>  {

	@Autowired
	GroupDao groupDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	private TextUtils textUtils;
	
	private int subscribersListSize = 3;
	
	@Autowired 
	private ResetEPSPictureExpireDateService resetEPSExpirateDate;

	private static Logger logger = LoggerFactory.getLogger(GetGroupCmd.class);
	
	@Override
	public GroupDTO execute(GetGroupRequest request) throws ServiceException {
		
		List<SubscriberDTO> subscribers = new ArrayList<SubscriberDTO> ();
		
		Group group = groupDao.findOne(Long.parseLong(request.getId()));
		if (group == null) {
			throw createServiceException(ServiceExceptionCode.INVALID_GROUP);
		}
		
		User viewer = null;
		if (!StringUtils.isBlank(request.getToken())) {
			viewer = userDao.findByToken(request.getToken());
			if (viewer == null) {
				throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
			}		
		}
		
		//search for users that are group subscribers and followed by the current user (if there is any)
		User groupAdmin = group.getCreator();
		List<User> groupSubscribers = viewer == null ?
				groupDao.getGroupFollowers(group.getGroupId()) : groupDao.getGroupFollowersAndUserFollowings(group.getGroupId(), viewer.getId());

    	for (User user : groupSubscribers) {
    		if (!user.equals(groupAdmin)) {
    			subscribers.add(new SubscriberDTO(user.getId(), user.getUsername(), user.getPicture()));
    		}
    	}

    	Boolean includeAdminUser = request.getIncludeAdminUser();
    	if (includeAdminUser != null && includeAdminUser.equals(Boolean.TRUE)) {
    	    subscribers.add(new SubscriberDTO(groupAdmin.getId(), groupAdmin.getUsername(), groupAdmin.getPicture()));
    	}

		// reset image expire date
		if (StringUtils.isNotBlank(group.getPicture())) {
            logger.info("Try to Reset Group's Picture Expire Date into EPS: {}GroupId:{}", group.getPicture(), group.getGroupId());
			resetEPSExpirateDate.resetEPSExpireDate(group.getPicture());
		}
		//
		
		GroupDTO groupDTO = GroupDTO.fromGroup(group, subscribers);
		
		if (viewer != null) {
			groupDTO.setSubscribed(viewer.isSubscribedToGroup(group));

			// Put subscribers with pictures first
			PageRequest pageReq = new PageRequest(0, subscribersListSize);
			for (User user : groupDao.getGroupFollowersAndUserFollowingsWithPicture(group.getGroupId(), viewer.getUserId(), pageReq)) {
				SubscriberDTO userWithPicture = new SubscriberDTO(user.getId(), user.getUsername(), user.getPicture());
				if (userWithPicture.getUserId() != groupAdmin.getId()) {
					subscribers.remove(userWithPicture);
					subscribers.add(0, userWithPicture);
				}
			}
		}
		
		groupDTO.setURL(textUtils.createGroupSEOURL(
				group.getName(), group.getId()));

		return groupDTO;

	}

}

