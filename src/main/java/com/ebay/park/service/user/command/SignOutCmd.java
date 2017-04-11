/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.user.command;


import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserSessionDao;
import com.ebay.park.db.entity.DeviceType;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.device.dto.DeviceDTO;
import com.ebay.park.service.device.dto.DeviceRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.service.user.dto.RemoveCurrentUserSessionRequest;

/**
 * Command class to perform a user log out.
 * 
 * @author lucia.masola
 * 
 */
@Component
public class SignOutCmd implements ServiceCommand<DeviceRequest, Void> {

	@Autowired
	private UserDao userDao;

	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private UserSessionDao userSessionDao;
	
	@Autowired
	private RemoveCurrentUserSessionCmd removeCurrentUserSessionCmd;

	@Override
	public Void execute(DeviceRequest deviceRequest) throws ServiceException {
		UserSessionCache cacheSession = sessionService.getUserSession(deviceRequest.getToken());
		
		if (cacheSession == null) {
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}
		
		User user = userDao.findById(cacheSession.getUserId());

		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}
		
		DeviceDTO device = cacheSession.getDevice();
		
		//web session or device session without device information
		if (device == null) {
			removeCurrentUserSessionCmd.execute(new RemoveCurrentUserSessionRequest(
				deviceRequest.getToken()));
		} else {
		//device session with device information
			List<UserSession> userSessions = userSessionDao.findUserSessionsByDeviceId(
					device.getDeviceId(),
					DeviceType.getDeviceByValue(device.getDeviceType()));
			for (UserSession userSession : userSessions) {
				userSession.setSessionActive(false);
				userSessionDao.save(userSession);
			}
		}
		
		return null;
	}
}
