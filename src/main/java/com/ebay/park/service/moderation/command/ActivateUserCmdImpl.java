package com.ebay.park.service.moderation.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserStatusDescription;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderation.dto.UserIdRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class ActivateUserCmdImpl implements ActivateUserCmd {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ActivateUserCmdImpl.class);

	@Autowired
	private UserDao userDao;

	@Override
	public ServiceResponse execute(UserIdRequest request)
			throws ServiceException {

		User user = userDao.findOne(request.getUserId());

		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

		if (!user.getStatus().equals(UserStatusDescription.BANNED)) {
			throw createServiceException(ServiceExceptionCode.MODERATION_USER_NOT_BANNED);
		}

		try {
			user.setStatus(UserStatusDescription.ACTIVE);

			userDao.save(user);

			if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Activate user id: {}successfull", request.getUserId());
			}
			return ServiceResponse.SUCCESS;
		} catch (Exception e) {
            LOGGER.error("Error activating user [{}]", request.getUserId(), e);
			throw createServiceException(ServiceExceptionCode.USER_ACTIVATION_ERROR);
		}
	}

}
