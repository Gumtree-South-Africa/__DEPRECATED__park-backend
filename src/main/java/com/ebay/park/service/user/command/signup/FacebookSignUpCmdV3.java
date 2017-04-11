package com.ebay.park.service.user.command.signup;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.session.UserSessionHelper;
import com.ebay.park.service.social.SocialNotificationHelper;
import com.ebay.park.service.social.UserSocialHelper;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.dto.SignUpResponse;
import com.ebay.park.service.user.dto.signup.FacebookSignUpRequest;
import com.ebay.park.util.EmailVerificationUtil;

/**
 * Command to perform a Facebook sign up. Version 3.
 * @author scalderon
 * @since 2.0.2
 *
 */
@Component
public class FacebookSignUpCmdV3 implements ServiceCommand<FacebookSignUpRequest, SignUpResponse> {

	@Autowired
	private SignUpCommand signUpCmd;

	@Autowired
	private UserServiceHelper userServiceHelper;

	@Autowired
	private UserSessionHelper userSessionHelper;

	@Autowired
	private EmailVerificationUtil emailVerificationUtil;
	
	@Autowired
	private SocialNotificationHelper socialNotificationHelper;

	@Autowired
	private UserSocialHelper userSocialHelper;


	@Override
	public SignUpResponse execute(FacebookSignUpRequest param) throws ServiceException {
		Assert.notNull(param, "The incoming request must be not null");
		User user = signUpCmd.execute(param);
		userSetup(param, user);

		return new SignUpResponse(
				user.getUsername(),
				UUID.fromString(userSessionHelper.createSession(user, param)));
	}

	private void userSetup(FacebookSignUpRequest param, User user) {
		userServiceHelper.setUserEmail(user, param.getEmail());
		emailVerificationUtil.verify(user);
		userServiceHelper.saveUser(user);

		userSocialHelper.addUserSocialInformation(user, param.getFacebookToken(), param.getFacebookUserId(), Social.FACEBOOK, null);
	}
}
