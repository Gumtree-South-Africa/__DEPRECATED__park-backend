/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service.user.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.SocialDao;
import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.db.entity.UserSocialPK;
import com.ebay.park.db.entity.UserStatusDescription;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.device.dto.DeviceRequest;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.dto.SignInRequest;
import com.ebay.park.service.user.dto.SignInResponse;
import com.ebay.park.service.user.dto.SignRequest;
import com.ebay.park.util.EmailVerificationUtil;
import com.ebay.park.util.FacebookUtil;

/**
 * @author jppizarro
 */
@Component
public class FacebookSignInCmdImpl implements FacebookSignInCmd {

	@Autowired
	private UserSocialDao userSocialDao;

	@Autowired
	@Qualifier("createSessionCmd")
	private ServiceCommand<SignRequest, String> createUserSessionCmd;

	@Autowired
	private FacebookUtil facebookUtil;

	@Autowired
	private UserServiceHelper signInHelper;
	
	@Autowired
	private SocialDao socialDao;

	@Autowired
	private EmailVerificationUtil emailVerificationUtil;
	
	@Override
	public SignInResponse execute(SignInRequest request) throws ServiceException {

		String fbToken = request.getFbToken();
		String fbUserId = request.getFbUserId();
		verifyFacebookToken(fbToken, fbUserId);

		SignInResponse response = new SignInResponse();
		User user = null;

		DeviceRequest deviceRequest = request.getDevice();
		UserSocial userSocial = userSocialDao.findByUserId(fbUserId);
		if (userSocial != null) {
			user = signInHelper.findUserById(userSocial.getUser().getUserId());
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
				response.setToken(createUserSession(user.getUsername(),request.getDevice()));
				response.setUsername(user.getUsername());
				response.setProfilePicture(user.getPicture());
			}

		} else {
			String email = facebookUtil.getEmail(request.getFbToken());
			user = signInHelper.findUserByEmail(email);
			signInHelper.assertUserNotNull(user);
			
			if (user.getStatus().equals(UserStatusDescription.BANNED)) {
				throw createServiceException(ServiceExceptionCode.USER_BANNED_ERROR);
			}
		
			// Only persists the FB token if it's a Mobile signin
			if (deviceRequest != null) {
				Social social = socialDao.findByDescription(Social.FACEBOOK);
				UserSocialPK pk = new UserSocialPK(user.getUserId(), social.getSocialId());
				userSocial = new UserSocial(pk);
				userSocial.setToken(request.getFbToken());
				userSocial.setUserId(fbUserId);
				userSocialDao.save(userSocial);
			}			
			// Facebook's sign in verifies the email 
			if (!user.isEmailVerified()) {
			    emailVerificationUtil.verify(user);
			}
			response.setToken(createUserSession(user.getUsername(),request.getDevice()));
			response.setUsername(user.getUsername());
			response.setProfilePicture(user.getPicture());
		}


		return response;
	}


	private String createUserSession(String username, DeviceRequest deviceRequest){
		SignInRequest request = new SignInRequest();
		request.setUsername(username);
		request.setDevice(deviceRequest);
		return createUserSessionCmd.execute(request);
	}

	private void verifyFacebookToken(String token, String userId) {
		facebookUtil.tokenIsValid(token, userId);
	}

}
