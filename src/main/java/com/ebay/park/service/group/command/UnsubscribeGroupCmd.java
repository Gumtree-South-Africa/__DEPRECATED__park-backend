package com.ebay.park.service.group.command;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.group.dto.UnsubscribeGroupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * 
 * @author federico.jaite
 */
@Component
public class UnsubscribeGroupCmd implements
ServiceCommand<UnsubscribeGroupRequest, ServiceResponse> {

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private UserDao userDao;

	@Override
	public ServiceResponse execute(UnsubscribeGroupRequest request)
			throws ServiceException {

		User user = userDao.findByToken(request.getToken());

		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}

		Group group = groupDao.getGroupFollowedByUserAndId(user.getId(),
				request.getGroupId());
		
		if (group == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_SUBSCRIBED);
		}

		if (group.getCreator().equals(user)) {
			throw createServiceException(ServiceExceptionCode.OWNER_CAN_NOT_BE_UNSUBSCRIBED);
		}
		
		group.removeFollower(user);
		
		//Saving the user and group in order to update the indexes...
		groupDao.saveAndFlush(group);
		userDao.save(user);
		return ServiceResponse.SUCCESS;
	}

}