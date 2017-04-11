package com.ebay.park.service.group.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserFollowsGroup;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.group.dto.CreateGroupRequest;
import com.ebay.park.service.group.dto.CreateGroupResponse;

/**
 * 
 * @author federico.jaite
 */
@Component
public class CreateGroupCmd implements
ServiceCommand<CreateGroupRequest, CreateGroupResponse> {
	
	private static Logger LOGGER = LoggerFactory.getLogger(CreateGroupCmd.class);

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private UserDao userDao;

	@Override
	public CreateGroupResponse execute(CreateGroupRequest request)
			throws ServiceException {

		CreateGroupResponse response = new CreateGroupResponse();
		
		User user = userDao.findByToken(request.getToken());
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}

		Group group = new Group(request.getName(), user, request.getDescription());
		
		if(StringUtils.isNotBlank(request.getLocation())) {

			String[] locationArr = request.getLocation().split(",");

			Double latitude = Double.valueOf(locationArr[0]);
			Double longitude = Double.valueOf(locationArr[1]);

			group.setLocation(longitude, latitude, request.getLocationName(), request.getZipCode());
		}
		
		try {
			
			group = groupDao.save(group);
			group.addFollower(new UserFollowsGroup(group,user));		
			group = groupDao.save(group);
			
		} catch (Exception e) {

            LOGGER.error("Error creating group {}", request.getName(), e);
			throw createServiceException(ServiceExceptionCode.ERROR_CREATING_GROUP);
		}
		
		response.setGroup(group);
		return response;

	}
	
}