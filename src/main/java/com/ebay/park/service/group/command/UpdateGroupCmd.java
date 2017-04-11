package com.ebay.park.service.group.command;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.group.dto.UpdateGroupRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * @author gabriel.sideri
 */
@Component
public class UpdateGroupCmd implements
ServiceCommand<UpdateGroupRequest, ServiceResponse> {
	
	private static Logger LOGGER = LoggerFactory.getLogger(UpdateGroupCmd.class);

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private UserDao userDao;
	
	@Override
	public ServiceResponse execute(UpdateGroupRequest request)
			throws ServiceException {
		
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
			
		if (StringUtils.isNotBlank(request.getName())) {
			// Verify if there is another group with the same name
			Group existedGroup = groupDao.findByName(request.getName());
			if (existedGroup != null && group.getId() != existedGroup.getId()) {
				throw createServiceException(ServiceExceptionCode.GROUP_ALREADY_EXISTS);
			}
			group.setName(request.getName());
		}
		
		if(StringUtils.isNotBlank(request.getDescription())) {
			group.setDescription(request.getDescription());
		}
		
		if(StringUtils.isNotBlank(request.getLocation())) {

			String[] locationArr = request.getLocation().split(",");

			Double latitude = Double.valueOf(locationArr[0]);
			Double longitude = Double.valueOf(locationArr[1]);

			group.setLocation(longitude, latitude, request.getLocationName(), request.getZipCode());
		}
		
		try {
			group = groupDao.save(group);
		} catch (Exception e) {
            LOGGER.error("Error updating group {}", request.getId(), e);
			throw createServiceException(ServiceExceptionCode.ERROR_UPDATING_GROUP);
		}
		
		return ServiceResponse.SUCCESS;

	}

}