package com.ebay.park.service.moderation.command;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderation.dto.GroupIdRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.ebay.park.service.ServiceException.createServiceException;
@Component
public class RemoveGroupCmd implements ServiceCommand<GroupIdRequest, ServiceResponse> {

	@Autowired
	private GroupDao groupDao;
		
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse execute(GroupIdRequest request) throws ServiceException {
		
		Group group = groupDao.findOne(request.getGroupId());
		
		if (group == null) {
			throw createServiceException(ServiceExceptionCode.INVALID_GROUP);
		}

		groupDao.delete(group);
		
		return ServiceResponse.SUCCESS;
	}

}
