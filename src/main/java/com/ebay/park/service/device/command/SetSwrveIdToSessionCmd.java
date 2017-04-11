package com.ebay.park.service.device.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.UserSessionDao;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.device.dto.DeviceSwrveIdRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;

@Component
public class SetSwrveIdToSessionCmd implements ServiceCommand<DeviceSwrveIdRequest, ServiceResponse>{

	@Autowired
	private UserSessionDao userSessionDao;


	@Autowired
	private SessionService sessionService;


	@Override
	public ServiceResponse execute(DeviceSwrveIdRequest request) throws ServiceException {
	    // it attempts to get the sesison from cache
		UserSessionCache userSessionCache =  sessionService.getUserSession(request.getToken());
		if (!request.getSwrveId().equals(userSessionCache.getSwrveId())) {
		    UserSession userSessionDB = userSessionDao.findUserSessionByToken(request.getToken());
            if (!request.getSwrveId().equals(userSessionCache.getSwrveId())) {
                // let's update the DB with the new (and different) swrveId
            	userSessionDB.setSwrveId(request.getSwrveId());
                userSessionDao.saveAndFlush(userSessionDB);
                // now let's update the cache...
                userSessionCache.setSwrveId(request.getSwrveId());
                sessionService.saveUserSession(request.getToken(), userSessionCache);
            }
        }
        return ServiceResponse.SUCCESS;
	}

}
