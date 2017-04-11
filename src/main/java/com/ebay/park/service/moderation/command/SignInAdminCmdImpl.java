/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service.moderation.command;

import com.ebay.park.db.dao.UserAdminDao;
import com.ebay.park.db.entity.UserAdmin;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.moderation.dto.AdminSignInRequest;
import com.ebay.park.service.moderation.dto.AdminSignInResponse;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.util.PasswdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class SignInAdminCmdImpl implements SignInAdminCmd {

    private static final Logger logger = LoggerFactory.getLogger(SignInAdminCmdImpl.class);

    @Autowired
	private SessionService sessionService;

	@Autowired
	private PasswdUtil passwdUtil;

	@Autowired
	private UserAdminDao userAdminDao;

	@Autowired
	private UserServiceHelper userServiceHelper;

	@Override
	public AdminSignInResponse execute(AdminSignInRequest request)
			throws ServiceException {
		logger.debug("Admin sign in is starting...");
		UserAdmin userAdmin = userAdminDao.findByUsername(request.getUsername());

		if (userAdmin == null) {
		    logger.warn("UserAdmin was not found [username={}]", request.getUsername());
			throw createServiceException(ServiceExceptionCode.WRONG_SIGNIN_DATA);
		}
		String oldParkToken = userAdmin.getToken();
		
		if (!passwdUtil.equalsToHashedPassword(request.getPassword(),
				userAdmin.getPassword())) {
			throw createServiceException(ServiceExceptionCode.WRONG_SIGNIN_DATA);
		}

		AdminSignInResponse response = new AdminSignInResponse();
		try {
		    response.setToken(userServiceHelper.updateApplicationToken(userAdmin));
			userAdmin.setToken(response.getToken());
		} catch (Exception e) {
			throw createServiceException(ServiceExceptionCode.IO_ERROR, e);
		}
		logger.debug("Sing in for admin is almost done. Now it remains create the user sesison in cache...");
        sessionService.createUserSessionCache(userAdmin, oldParkToken);
		return response;
	}
}
