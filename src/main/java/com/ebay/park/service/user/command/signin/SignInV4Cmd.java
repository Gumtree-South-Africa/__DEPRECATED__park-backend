package com.ebay.park.service.user.command.signin;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.springframework.stereotype.Component;

import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.user.command.SignInCmd;

/**
 * Performs a sign in operation for Smart Lock login.
 * It checks if an email exists and returns different message
 * than Sign In V3
 * @author scalderon
 * @since v2.0.6 - Smart Lock login
 *
 */
@Component
public class SignInV4Cmd extends SignInCmd {
	
	/**
	 * Validates if a user is null,
	 * then return a ServiceExceptionCode.NON_EXISTENT_EMAIL
	 */
	@Override
	protected void validateUserNull(User user) {
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.NON_EXISTENT_EMAIL);
		}
	}	
}

