/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.social.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.service.social.UserSocialHelper;
import com.ebay.park.service.social.dto.SocialNetworkConnectRequest;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.util.EmailVerificationUtil;
import com.ebay.park.util.FacebookUtil;

/**
 * @author jpizarro
 *
 */
@Component
public class SocialConnectCmd implements ServiceCommand<SocialNetworkConnectRequest, Boolean> {

	@Autowired
	private UserSocialHelper userSocialHelper;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private EmailVerificationUtil emailVerificationUtil;
	
	@Autowired
	private UserServiceHelper userServiceHelper;

	@Autowired
	private FacebookUtil facebookUtil;

	/** This creates (or updates if already exists) the social information */
	@Override
	public Boolean execute(SocialNetworkConnectRequest request) throws ServiceException {
		Assert.notNull(request, "The incoming request must be not null");
		UserSessionCache session = sessionService.getUserSession(request.getToken());
		User user = userServiceHelper.findUserByUsername(session.getUsername());

		UserSocial userSocial = userSocialHelper.findUserSocialBySocialNetwork(user, request.getSocialNetwork());

		if (userSocial == null) {
			//Create social information
			userSocialHelper.addUserSocialInformation(user, request.getSocialToken(), request.getSocialUserId(), 
					request.getSocialNetwork(), request.getSocialTokenSecret());
		} else {
			userSocialHelper.updateSocialInformation(userSocial, request.getToken(), request.getSocialUserId());
		}

		// Facebook's sign in verifies the email
		if (Social.FACEBOOK.equalsIgnoreCase(request.getSocialNetwork())) {
			//when it's an mobile account, set email from FB
			if (user.isMobileVerified() && user.getEmail() == null) {
				userServiceHelper.setUserEmail(user, facebookUtil.getEmail(request.getSocialToken()));
				userServiceHelper.saveUser(user);
			}
		    emailVerificationUtil.verifyForFacebook(user, request.getSocialToken());
		}

		return Boolean.TRUE;
	}

}
