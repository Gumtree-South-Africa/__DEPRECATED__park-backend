package com.ebay.park.service.group.command;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.group.dto.DeleteGroupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * @author gabriel.sideri
 */
@Component
public class DeleteGroupCmd implements ServiceCommand<DeleteGroupRequest, ServiceResponse> {
	
	private static Logger LOGGER = LoggerFactory.getLogger(DeleteGroupCmd.class);
	
	@Autowired
	private GroupDao groupDao;
	
	@Autowired
	private UserDao userDao;

	@Override
	public ServiceResponse execute(DeleteGroupRequest request) throws ServiceException {

		User user = userDao.findByToken(request.getToken());

		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}

		Group group = groupDao.findOne(request.getId());
		
		if (group == null) {
			throw createServiceException(ServiceExceptionCode.INVALID_GROUP);
		}
		
		if (!group.getCreator().equals(user)) {
			throw createServiceException(ServiceExceptionCode.INVALID_GROUP_OWNER);
		}

		try {
			groupDao.delete(group);
		} catch (Exception e) {
            LOGGER.error("Error deleting group {}", request.getId(), e);
			throw createServiceException(ServiceExceptionCode.ERROR_DELETING_GROUP);
		}
		
		return ServiceResponse.SUCCESS;

	}
}
