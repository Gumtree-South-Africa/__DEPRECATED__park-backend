package com.ebay.park.service.group.command;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserFollowsGroupDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserFollowsGroup;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.group.dto.SubscribeGroupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.ebay.park.service.ServiceException.createServiceException;
/**
 * 
 * @author federico.jaite
 */
@Component
public class SubscribeGroupCmd implements ServiceCommand<SubscribeGroupRequest, ServiceResponse> {

	protected SubscribeGroupCmd() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserFollowsGroupDao userFollowsGroupDao;

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse execute(SubscribeGroupRequest request)
			throws ServiceException {

		User user = userDao.findByToken(request.getToken());

		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}

		Group group = groupDao.findOne(request.getGroupId());

		if (group == null) {
			throw createServiceException(ServiceExceptionCode.INVALID_GROUP);
		}

		if (user.isSubscribedToGroup(group)) {
			throw createServiceException(ServiceExceptionCode.USER_ALREADY_SUBSCRIBED);
		}

		UserFollowsGroup userFollowsGroup  = new UserFollowsGroup(group, user);
		userFollowsGroup = userFollowsGroupDao.save(userFollowsGroup);

		group.addFollower(userFollowsGroup);
		user.getGroups().add(userFollowsGroup);

		//Saving the user and group in order to update the indexes...
		userDao.save(user);
		groupDao.saveAndFlush(group);
		return ServiceResponse.SUCCESS;
	}

	public GroupDao getGroupDao() {
		return groupDao;
	}

	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}