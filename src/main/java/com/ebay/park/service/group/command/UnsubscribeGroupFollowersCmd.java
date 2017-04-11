package com.ebay.park.service.group.command;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.group.dto.UnsubscribeGroupFollowersRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * @author gabriel.sideri
 */
@Component
public class UnsubscribeGroupFollowersCmd implements
ServiceCommand<UnsubscribeGroupFollowersRequest, ServiceResponse> {

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private UserDao userDao;
	
	private static Logger logger = LoggerFactory.getLogger(UnsubscribeGroupFollowersCmd.class);
	
	@Override
	public ServiceResponse execute(UnsubscribeGroupFollowersRequest request)
			throws ServiceException {

		User user = userDao.findByToken(request.getToken());

		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}

		Group group = groupDao.findOne(request.getGroupId());

		if (group == null) {
			throw createServiceException(ServiceExceptionCode.INVALID_GROUP);
		}
		
		if (!group.getCreator().equals(user)) {
			throw createServiceException(ServiceExceptionCode.INVALID_GROUP_OWNER);
		}
		
		long millis = System.currentTimeMillis();
		
		// Remove Followers
		
		List<Long> followerdsIdsModified = group.removeFollowers(request
				.getFollowersIdsValidated());
		
		if (followerdsIdsModified.isEmpty()) {
			throw createServiceException(ServiceExceptionCode.USERS_NOT_SUBSCRIBED);
		}
		
		// Save & update indexes
		groupDao.saveAndFlush(group);
		userDao.save(user);
		//

        logger.info("REMOVE BATCH FOLLOWERS GROUP. Followers modified: {}. Execution took {} seconds.", followerdsIdsModified.size(), (System.currentTimeMillis() - millis) / 1000);

		return ServiceResponse.SUCCESS;

	}

}