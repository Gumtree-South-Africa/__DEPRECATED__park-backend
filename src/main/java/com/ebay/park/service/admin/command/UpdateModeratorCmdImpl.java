package com.ebay.park.service.admin.command;

import com.ebay.park.db.dao.UserAdminDao;
import com.ebay.park.db.entity.UserAdmin;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.admin.dto.SmallUserAdmin;
import com.ebay.park.service.admin.dto.UpdateModeratorRequest;
import com.ebay.park.util.PasswdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class UpdateModeratorCmdImpl implements UpdateModeratorCmd {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateModeratorCmdImpl.class);

	@Autowired
	private UserAdminDao userAdminDao;

	@Autowired
	private PasswdUtil passwdUtil;

	@Override
	public SmallUserAdmin execute(UpdateModeratorRequest request) throws ServiceException {

		UserAdmin moderator = userAdminDao.findById(request.getId());

		if (moderator == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

		if (!StringUtils.isEmpty(request.getUsername())) {
			if (userAdminDao.findByUsername(request.getUsername()) != null) {
				throw createServiceException(ServiceExceptionCode.USERNAME_DUPLICATED);
			}
			moderator.setUsername(request.getUsername());
		}

		if (!StringUtils.isEmpty(request.getEmail())) {
			if (userAdminDao.findByEmail(request.getEmail()) != null) {
				throw createServiceException(ServiceExceptionCode.EMAIL_DUPLICATED);
			}
			moderator.setEmail(request.getEmail());
		}

		if (!StringUtils.isEmpty(request.getPassword())) {
			moderator.setPassword(passwdUtil.hash(request.getPassword()));
		}

		if (!StringUtils.isEmpty(request.getName())) {
			moderator.setName(request.getName());
		}

		if (!StringUtils.isEmpty(request.getLastname())) {
			moderator.setLastname(request.getLastname());
		}

		try {
			moderator = userAdminDao.save(moderator);

			if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Update moderator {} success", request.getUsername());
			}
			return new SmallUserAdmin(moderator);
		} catch (Exception e) {
            LOGGER.error("Error updating moderator [{}]", request.getUsername(), e);
			throw createServiceException(ServiceExceptionCode.UPDATE_MODERATOR_ERROR);
		}
	}
}