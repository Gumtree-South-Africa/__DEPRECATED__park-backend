package com.ebay.park.service.user.command.signin;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ebay.park.db.dao.UserSessionDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.db.entity.UserStatusDescription;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.social.UserSocialHelper;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.dto.SignInResponse;
import com.ebay.park.service.user.dto.signin.FacebookSignInRequest;
import com.ebay.park.service.user.dto.signin.SignInRequest;
import com.ebay.park.util.EmailVerificationUtil;
import com.ebay.park.util.FacebookUtil;

/**
 * Command to perform a facebook sign in.
 * @author scalderon
 * @since 2.0.2
 *
 */
@Component
public class FacebookSignInCommand implements ServiceCommand<FacebookSignInRequest, SignInResponse> {
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(FacebookSignInCommand.class);

	@Autowired
	@Qualifier("createUserSessionCmd")
	private ServiceCommand<SignInRequest, UserSession>  createUserSession;
	
	@Autowired
	private SessionService sessionService;

	@Autowired
	private FacebookUtil facebookUtil;

	@Autowired
	private UserServiceHelper signInHelper;
	
	@Autowired
	private UserSessionDao userSessionDao;
	
	@Autowired
	private UserSocialHelper userSocialHelper;

	@Autowired
	private EmailVerificationUtil emailVerificationUtil;

	@Override
	public SignInResponse execute(FacebookSignInRequest request) throws ServiceException {
		Assert.notNull(request, "The request cannot be null");
		String fbToken = request.getFbToken();
		String fbUserId = request.getFbUserId();

		//Validates facebook token
		facebookUtil.tokenIsValid(fbToken, fbUserId);

		UserSocial userSocial = userSocialHelper.findUserSocialByUserId(fbUserId);
		if (userSocial != null) {
			return buildResponseAccountLinkedToFB(userSocial, request);
		} else {
			return buildResponseByFacebookEmail(request);
		}
	}


	/**
	 * Builds the response using a given a valid {@link UserSocial}.
	 *
	 * @param userSocial the user social
	 * @param request the FacebookSignInRequest
	 * @return the sign in response
	 */
	private SignInResponse buildResponseAccountLinkedToFB(UserSocial userSocial, FacebookSignInRequest request) {
		SignInResponse response = new SignInResponse();
		User user = signInHelper.findUserById(userSocial.getUser().getUserId());
		signInHelper.assertUserNotNull(user);

		if (user.getStatus().equals(UserStatusDescription.BANNED)) {
			throw createServiceException(ServiceExceptionCode.USER_BANNED_ERROR);
		}

		if (user.getStatus().equals(UserStatusDescription.LOCKED)) {
			if (!signInHelper.canUserBeUnlocked(user)) {
				signInHelper.sendAccountBlockedEmail(user);
				throw createServiceException(ServiceExceptionCode.ACCOUNT_LOCKED);
			}
		} else {
			UserSession session = createUserSession(request, user);
			response.setToken(session.getToken());
			response.setUsername(user.getUsername());
			response.setProfilePicture(user.getPicture());
		}

		//Update social information
		userSocialHelper.updateSocialInformation(userSocial, request.getFbToken(), request.getFbUserId());

		return response;
	}
	
	/**
	 * Builds the response by facebook email.
	 * Facebook email should be part of a Park account as email, but the Park account should not be linked to
	 * the Facebook account yet.
	 *
	 * @param request the FacebookSignInRequest
	 * @return the sign in response
	 */
	private SignInResponse buildResponseByFacebookEmail(FacebookSignInRequest request) {
		SignInResponse response = new SignInResponse();
		String email = facebookUtil.getEmail(request.getFbToken());
		
		User user = signInHelper.findUserByEmail(email);;
		signInHelper.assertUserNotNull(user);
		
		if (user.getStatus().equals(UserStatusDescription.BANNED)) {
			throw createServiceException(ServiceExceptionCode.USER_BANNED_ERROR);
		}

		// Only persists the FB token if it's a Mobile signin
		if (request.getDevice() != null) {
			userSocialHelper.addUserSocialInformation(user, request.getFbToken(), request.getFbUserId(), Social.FACEBOOK, null);
		}
		// Facebook's sign in verifies the email 
		if (!user.isEmailVerified()) {
			emailVerificationUtil.verify(user);
		}

		UserSession session = createUserSession(request, user);
		response.setUsername(user.getUsername());
		response.setToken(session.getToken());
		response.setProfilePicture(user.getPicture());

		return response;
	}
	
	
	/**
	 * Creates a UserSession {@link UserSession}
	 * @param request the FacebookSignInRequest
	 * @param user the user
	 * @return userSession
	 */
	private UserSession createUserSession(FacebookSignInRequest request, User user) {
		UserSession userSession;
		try {
		    //TODO refactor this: session setting to user goes ALWAYS after user session creation.
		    //session cache creation goes ALWAYS after user session setting.

			userSession = createUserSession.execute(request);
			if (userSession != null) {
				user.addUserSession(userSession);

				//Create Session Cache
				sessionService.createUserSessionCache(userSession);
		        userSessionDao.save(userSession);
			}
		} catch (Exception e) {
            logger.error("Error trying to create a user session for userID: {}", user.getId(), e);
			throw createServiceException(ServiceExceptionCode.IO_ERROR);
		}

		return userSession;
	}
}
