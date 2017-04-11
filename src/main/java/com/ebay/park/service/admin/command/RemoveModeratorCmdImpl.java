package com.ebay.park.service.admin.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.UserAdminDao;
import com.ebay.park.db.entity.UserAdmin;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderation.dto.UserIdRequest;

@Component
public class RemoveModeratorCmdImpl implements RemoveModeratorCmd {

	private static final Logger LOGGER = LoggerFactory.getLogger(RemoveModeratorCmdImpl.class);

	@Autowired
	private UserAdminDao userAdminDao;

	@Override
	public ServiceResponse execute(UserIdRequest request) throws ServiceException {

		UserAdmin moderator = userAdminDao.findById(request.getUserId());

		if (moderator == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

		try {
			userAdminDao.delete(moderator);

			if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Delete moderator {} success", request.getUserId());
			}
			return ServiceResponse.SUCCESS;
		} catch (Exception e) {
            LOGGER.error("Error removing moderator [{}]", request.getUserId(), e);
			throw createServiceException(ServiceExceptionCode.REMOVE_MODERATOR_ERROR);
		}
	}
}