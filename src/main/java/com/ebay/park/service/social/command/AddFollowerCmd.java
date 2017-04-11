package com.ebay.park.service.social.command;

import com.ebay.park.db.dao.FollowerDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Follower;
import com.ebay.park.db.entity.FollowerPK;
import com.ebay.park.db.entity.User;
import com.ebay.park.event.user.UsersEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.aop.Notifiable;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.social.dto.FollowUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;
@Component
public class AddFollowerCmd implements ServiceCommand<FollowUserRequest, UsersEvent>{

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private FollowerDao followerDao;
	
	@Override
	@Notifiable(action=NotificationAction.FOLLOW_USER)
	public UsersEvent execute(FollowUserRequest request) throws ServiceException {

		User userToFollow = userDao.findByUsername(request.getUserToFollow());
		if (userToFollow == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}
		
		User userFollower = userDao.findByUsername(request.getFollower());
		if (userFollower == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}
		
		Follower previousFollow = followerDao.findFollower(userFollower.getId(), userToFollow.getId());
		if (previousFollow != null){
			throw createServiceException(ServiceExceptionCode.FOLLOW_ALREADY_EXISTS);
		}		
		
		FollowerPK followerPK = new FollowerPK();
		followerPK.setFollowerId(userFollower.getId());
		followerPK.setUserId(userToFollow.getId());
		
		Follower follower = new Follower();
		follower.setUserFollower(userToFollow);
		follower.setId(followerPK);
		followerDao.save(follower);

		return new UsersEvent(userToFollow, userFollower);
		
	}

}
