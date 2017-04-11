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

/**
 * 
 * @author Julieta Salvadó
 *
 */
@Component
public class CountUnreadGroupItemsCmd implements ServiceCommand<String, Long>{

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserFollowsGroupDao userFollowsGroupDao;
	
	@Autowired
	private GroupDao groupDao;
	
	@Override
	public Long execute(String token) throws ServiceException {
		User user = userDao.findByToken(token);
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}
		
		Long count = 0l;
		List<UserFollowsGroup> groups = userFollowsGroupDao.findGroups(user.getUserId());
		for (UserFollowsGroup group : groups) {
			count += groupDao.getNewItemsCount(group.getLastAccess(), group.getGroup().getGroupId(), StatusDescription.ACTIVE);
		}
		return count;
	}

}
