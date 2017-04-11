package com.ebay.park.service.user.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.springframework.stereotype.Component;

import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceExceptionCode;

@Component
public class ForgotPwdV4Cmd extends ForgotPwdCmd {
	
	/**
	 * Validates when a user is null.
	 * For v4 throws a ServiceExceptionCode.NON_EXISTENT_EMAIL
	 * @param user
	 */
	@Override
	protected void validateUserNull(User user) {
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.NON_EXISTENT_EMAIL);
		}
	}
}
