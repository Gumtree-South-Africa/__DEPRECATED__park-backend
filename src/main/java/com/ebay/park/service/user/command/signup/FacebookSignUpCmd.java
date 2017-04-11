package com.ebay.park.service.user.command.signup;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.SocialDao;
import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.db.entity.UserSocialPK;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.social.SocialNotificationHelper;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.dto.SignUpResponse;
import com.ebay.park.service.user.dto.signin.SignInRequest;
import com.ebay.park.service.user.dto.signup.FacebookSignUpRequest;
import com.ebay.park.util.EmailVerificationUtil;

/**
 * Command to perform a Facebook sign up.
 * @author scalderon
 * @since 2.0.2
 *
 */
@Component
public class FacebookSignUpCmd implements ServiceCommand<FacebookSignUpRequest, SignUpResponse> {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(FacebookSignUpCmd.class);

	@Autowired
	private SignUpCommand signUpCmd;

	@Autowired
	private SocialDao socialDao;

	@Autowired
	private UserSocialDao userSocialDao;

	@Autowired
	private UserServiceHelper userServiceHelper;

	@Autowired
	@Qualifier("createUserSessionCmd")
	private ServiceCommand<SignInRequest, UserSession>  createUserSession;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private EmailVerificationUtil emailVerificationUtil;
	
	@Autowired
	private SocialNotificationHelper socialNotificationHelper;


	@Override
	public SignUpResponse execute(FacebookSignUpRequest param) throws ServiceException {
		//Creates a new user
		User user = signUpCmd.execute(param);

	    //Set email address
		user.setEmail(param.getEmail());

		//Set verification value
		emailVerificationUtil.verify(user);

		//persisting user
        userServiceHelper.saveUser(user);

		addUserSocialInfo(user, param);

		SignUpResponse response = new SignUpResponse();
		response.setUsername(user.getUsername());

		try {
			UserSession userSession = createUserSession.execute(param);
			if (userSession != null) {
				user.addUserSession(userSession);

				//Create Session Cache
				sessionService.createUserSessionCache(userSession);
				response.setToken(UUID.fromString(userSession.getToken()));
			}
		} catch (Exception e) {
            logger.error("Error trying to create a user session for username: {}", user.getUsername(), e);
			throw createServiceException(ServiceExceptionCode.IO_ERROR);
		}
		//persisting user sessions
        userServiceHelper.saveUser(user);
        
        //notify to user FB friends
        socialNotificationHelper.notifyFBFriends(user, param.getFacebookToken(), param.getFacebookUserId());

		return response;
	}

	/**
	 * Adds the user social info.
	 *
	 * @param user the user
	 * @param request the request
	 * @return the user social
	 */
	protected UserSocial addUserSocialInfo(User user, FacebookSignUpRequest request) {
		UserSocial userSocial = null;
		if (!StringUtils.isBlank(request.getFacebookToken())) {
			userSocial = createUserSocial(user, request);
			userSocialDao.save(userSocial);
			user.addUserSocial(userSocial);
		}
		return userSocial;
	}

	/**
	 * Creates the user social.
	 *
	 * @param user the user
	 * @param request the request
	 * @return the user social
	 */
	protected UserSocial createUserSocial(User user, FacebookSignUpRequest request) {
		Social social = socialDao.findByDescription(Social.FACEBOOK);
	
		// creates UserSocial key
		UserSocialPK pk = new UserSocialPK(user.getUserId(), social.getSocialId());
	
		UserSocial userSocial = new UserSocial(pk);
		userSocial.setToken(request.getFacebookToken());
		userSocial.setUserId(request.getFacebookUserId());
		userSocial.setSocial(social);
	
		return userSocial;
	}
	
}
