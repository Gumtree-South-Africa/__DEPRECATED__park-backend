package com.ebay.park.service.user.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ebay.park.db.entity.Access;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserStatusDescription;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.dto.SignInRequest;
import com.ebay.park.service.user.dto.SignInResponse;
import com.ebay.park.service.user.dto.SignRequest;
import com.ebay.park.util.PasswdUtil;


/**
 * Park application sign in.
 * 
 * @author marcos.lambolay
 */
@Component
public class SignInCmd implements ServiceCommand<SignInRequest, SignInResponse> {
	
	@Autowired
	private PasswdUtil passwdUtil;

	@Autowired
	protected UserServiceHelper userServiceHelper;

	@Autowired
	@Qualifier("createSessionCmd")
	private ServiceCommand<SignRequest, String>  createUserSession;
	
	
	@Override
	public SignInResponse execute(SignInRequest request) throws ServiceException {
		Assert.notNull(request, "The signInRequest cannot be null");
		User user = userServiceHelper.findUserByUsernameOrEmail(request.getUsername(), request.getEmail());
		
		validateUserNull(user);
		
		if (UserStatusDescription.BANNED.equals(user.getStatus())) {
			throw createServiceException(ServiceExceptionCode.USER_BANNED_ERROR);
		}

		if (!passwdUtil.equalsToHashedPassword(request.getPassword(), user.getPassword())) {
			handleInvalidPassword(user);
		}

		if (UserStatusDescription.LOCKED.equals(user.getStatus()) && 
				!userServiceHelper.canUserBeUnlocked(user)) {
			userServiceHelper.sendAccountBlockedEmail(user);
			throw createServiceException(ServiceExceptionCode.ACCOUNT_LOCKED);				
		}
		
		// Login Success !!!
		user.getAccess().resetFailedSignInAttempts();
		
		SignInResponse response = new SignInResponse();

		response.setToken(createUserSession.execute(request));

		userServiceHelper.resetAccessByUser(user);
		userServiceHelper.saveUser(user);

		response.setUsername(user.getUsername());
		response.setProfilePicture(user.getPicture());

		return response;
		
	}

	private void handleInvalidPassword(User user) {
		Access access = user.getAccess();
		access.incFailedSignInAttempts();
		userServiceHelper.saveUser(user);

		if (userServiceHelper.shouldBlockAccount(access)) {

			// Change the state of the user to LOCKED the first time
			if (!UserStatusDescription.LOCKED.equals(user.getStatus())) {
				user.setStatus(UserStatusDescription.LOCKED);
				userServiceHelper.saveUser(user);
			}

			userServiceHelper.sendAccountBlockedEmail(user);
			throw createServiceException(ServiceExceptionCode.ACCOUNT_LOCKED);

		} else if (userServiceHelper.oneToBlockAccount(access)) {

			throw createServiceException(ServiceExceptionCode.WRONG_SIGNIN_DATA_ONE_TO_BLOCK);

		} else {

			throw createServiceException(ServiceExceptionCode.WRONG_SIGNIN_DATA);
		}

	}
	
	protected void validateUserNull(User user) {
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.WRONG_SIGNIN_DATA);
		}
	}

}
