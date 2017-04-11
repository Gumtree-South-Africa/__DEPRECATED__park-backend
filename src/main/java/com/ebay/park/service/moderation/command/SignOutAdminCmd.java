/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.moderation.command;

import com.ebay.park.db.dao.UserAdminDao;
import com.ebay.park.db.entity.UserAdmin;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.session.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;
@Component
public class SignOutAdminCmd implements ServiceCommand<String, ServiceResponse> {

	@Autowired
	private UserAdminDao userAdminDao;

	@Autowired
	private SessionService sessionService;

	@Override
	public ServiceResponse execute(String token) throws ServiceException {
		UserAdmin user = userAdminDao.findByToken(token);

		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

		user.setToken(null);
		userAdminDao.save(user);

		sessionService.removeUserSession(token);

		return ServiceResponse.SUCCESS;
	}
}
