package com.ebay.park.service.user.command.signin;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserStatusDescription;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.dto.SignInResponse;

/**
 * Command to perform a sign in operation.
 * @author scalderon
 * @since 2.0.0
 *
 */
@Component
public class SignInCommand implements ServiceCommand<User, SignInResponse> {

    @Autowired
    private UserServiceHelper userServiceHelper;

	@Override
	public SignInResponse execute(User user) throws ServiceException {
		
		//If user does not exist, Front team redirects to sign up.
	    userServiceHelper.assertUserNotNull(user);

		if (user.getStatus().equals(UserStatusDescription.BANNED)) {
			throw createServiceException(ServiceExceptionCode.USER_BANNED_ERROR);
		}
		
		SignInResponse response = new SignInResponse();
		
		response.setUsername(user.getUsername());
		response.setProfilePicture(user.getPicture());

		return response;
	}
	
}
