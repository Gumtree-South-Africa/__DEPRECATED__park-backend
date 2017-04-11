package com.ebay.park.service.social.command;

import com.ebay.park.db.dao.FollowerDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Follower;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.social.dto.UnfollowUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;
/**
 * Command that given a @UnfollowUserRequest, removes the follow relation between "userToUnfollow" and "follower" if exists
 * In case of wrong parameters it throws a ServiceException
 * @author cbirge
 *
 */
@Component
public class RemoveFollowerCmd implements ServiceCommand<UnfollowUserRequest, Void>{

	private static Logger logger = LoggerFactory.getLogger(RemoveFollowerCmd.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private FollowerDao followerDao;

	@Override
	public Void execute(UnfollowUserRequest request) throws ServiceException {

		User userToUnfollow = userDao.findByUsername(request.getUserToUnfollow());
		if (userToUnfollow == null){
			logger.error("Error trying to unfollow an nonexisting user!");
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

		User userFollower = userDao.findByUsername(request.getFollower());
		if (userFollower == null){
			logger.error("Error trying to unfollow an nonexisting user!");
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

		Follower userFollowed = followerDao.findFollower(userFollower.getId(), userToUnfollow.getUserId());
		if (userFollowed == null){
			logger.error("Error trying to unfollow a user, but the followig relation doesn't exist!");
			throw createServiceException(ServiceExceptionCode.FOLLOWING_NOT_FOUND);
		}

		followerDao.delete(userFollowed);

		return null;
	}

}
