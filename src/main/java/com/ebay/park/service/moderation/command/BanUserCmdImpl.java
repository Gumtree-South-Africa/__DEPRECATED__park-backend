package com.ebay.park.service.moderation.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserStatusDescription;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.device.command.RemoveUserSessionsByUserCmd;
import com.ebay.park.service.device.dto.RemoveUserSessionsByUserRequest;
import com.ebay.park.service.moderation.dto.UserIdRequest;

@Component
public class BanUserCmdImpl implements BanUserCmd {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BanUserCmdImpl.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private RemoveUserSessionsByUserCmd removeUserSessionsByUserCmd;
	
	@Override
	public ServiceResponse execute(UserIdRequest request)
			throws ServiceException {

		User user = userDao.findOne(request.getUserId());

		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

		if (user.getStatus() != null
				&& user.getStatus().equals(UserStatusDescription.BANNED)) {
			throw createServiceException(ServiceExceptionCode.MODERATION_USER_ALREADY_BANNED);
		}

		try {
			user.setStatus(UserStatusDescription.BANNED);
			userDao.save(user);
			removeUserSessionsByUserCmd.execute(new RemoveUserSessionsByUserRequest(user.getId()));

			if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Ban user id: {}successfull", request.getUserId());
			}
			return ServiceResponse.SUCCESS;
		} catch (Exception e) {
            LOGGER.error("Error banning user [{}]", request.getUserId(), e);
			throw createServiceException(ServiceExceptionCode.USER_BAN_ERROR);
		}
	}
}
