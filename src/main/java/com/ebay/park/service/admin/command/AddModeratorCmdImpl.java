package com.ebay.park.service.admin.command;

import com.ebay.park.db.dao.UserAdminDao;
import com.ebay.park.db.entity.UserAdmin;
import com.ebay.park.db.entity.UserRole;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.admin.dto.AddModeratorRequest;
import com.ebay.park.service.admin.dto.SmallUserAdmin;
import com.ebay.park.util.PasswdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class AddModeratorCmdImpl implements AddModeratorCmd {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddModeratorCmdImpl.class);

	@Autowired
	private UserAdminDao userAdminDao;

	@Autowired
	private PasswdUtil passwdUtil;

	@Override
	public SmallUserAdmin execute(AddModeratorRequest request) throws ServiceException {

		if (userAdminDao.findByUsername(request.getUsername()) != null) {
			throw createServiceException(ServiceExceptionCode.USERNAME_DUPLICATED);
		}

		if (userAdminDao.findByEmail(request.getEmail()) != null) {
			throw createServiceException(ServiceExceptionCode.EMAIL_DUPLICATED);
		}

		UserAdmin moderator = new UserAdmin();
		moderator.setUsername(request.getUsername());
		moderator.setEmail(request.getEmail());
		moderator.setName(request.getName());
		moderator.setLastname(request.getLastname());
		moderator.setPassword(passwdUtil.hash(request.getPassword()));

		Set<UserRole> roles = new HashSet<>();
		roles.add(UserRole.MODERATOR);
		moderator.setRoles(roles);

		try {
			moderator = userAdminDao.save(moderator);

			if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Add new moderator {} success", request.getUsername());
			}
			return new SmallUserAdmin(moderator);
		} catch (Exception e) {
            LOGGER.error("Error adding moderator [{}]", request.getUsername(), e);
			throw createServiceException(ServiceExceptionCode.ADD_MODERATOR_ERROR);
		}
	}
}