/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.user.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.IdiomDao;
import com.ebay.park.db.dao.SocialDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Access;
import com.ebay.park.db.entity.Idiom;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.db.entity.UserSocialPK;
import com.ebay.park.db.entity.UserStatusDescription;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.notification.NotificationService;
import com.ebay.park.service.social.SocialNotificationHelper;
import com.ebay.park.service.user.dto.SignRequest;
import com.ebay.park.service.user.dto.SignUpRequest;
import com.ebay.park.service.user.dto.SignUpResponse;
import com.ebay.park.util.EmailVerificationUtil;
import com.ebay.park.util.ParkConstants;
import com.ebay.park.util.ParkTokenUtil;
import com.ebay.park.util.PasswdUtil;

/**
 * Command class to perform a user sign up.
 * 
 * @author juan.pizarro
 * 
 */
@Component
public class SignUpCmd implements ServiceCommand<SignUpRequest, SignUpResponse> {

	@Autowired
	private UserDao userDao;

	@Autowired
	private IdiomDao idiomDao;

	@Autowired
	private PasswdUtil passwdUtil;

	@Autowired
	private ParkTokenUtil tokenUtil;

	@Autowired
	private SocialDao socialDao;

	@Autowired
	private UserSocialDao userSocialDao;

	@Autowired
	private SendEmailVerificationCmd sendEmailVerificationCmd;

	@Autowired
	private EmailVerificationUtil emailVerificationUtil;

	@Autowired
	@Qualifier("createSessionCmd")
	private ServiceCommand<SignRequest, String> createSessionCmd;

	@Resource(name = "NotificationServiceOp")
	private NotificationService notificationService;
	
	@Autowired
	private SocialNotificationHelper socialNotificationHelper;

	@Override
	public SignUpResponse execute(SignUpRequest request)
			throws ServiceException {

		User user = createUser(request);
		userDao.save(user);

		String sessionToken = createSessionCmd.execute(request);

    	if (!StringUtils.isBlank(request.getFacebookToken())) {
    	    emailVerificationUtil.verifyForFacebook(user, request.getFacebookToken());
    	} else {
    	    sendEmailVerificationCmd.execute(sessionToken);
    	}
    	userDao.save(user);

		return new SignUpResponse(user.getUsername(),
				UUID.fromString(sessionToken));
	}

	protected User createUser(SignUpRequest request) {

		User user = createUserBasicInfo(request);
		addUserSocialInfo(user, request);
		addDefaultNotificationConfig(user);

		return user;
	}

	protected User createUserBasicInfo(SignUpRequest request) {
		User user = new User();

		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwdUtil.hash(request.getPassword()));
		String[] locationArr = request.getLocation().split(",");
		user.setLatitude(Double.valueOf(locationArr[0]));
		user.setLongitude(Double.valueOf(locationArr[1]));
		user.setZipCode(request.getZipCode());
		user.setCreation(DateTime.now().toDate());

		Access access = user.getAccess();
		access.setTemporaryToken(tokenUtil.createSessionToken());

		//TODO: Uncomment later. PROD only needs 'ES' default language for now.
		Idiom idiom = idiomDao.findByCode(ParkConstants.LANG_SPA);
		/*
		Idiom idiom = idiomDao.findByCode(request.getLang());
		if (idiom == null) {
			throw createServiceException(ServiceExceptionCode.INVALID_LANGUAGE);
		}
		 */
		user.setIdiom(idiom);

		user.setLocationName(request.getLocationName());
		user.setStatus(UserStatusDescription.ACTIVE);
		user.setMobile(request.getMobile());

		try {
			user = userDao.save(user);
		} catch (Exception e){
			//FIXME if ES convertion fails, the users receives a DUPLICATED_SIGNUP_DATA error
			throw createServiceException(ServiceExceptionCode.DUPLICATED_SIGNUP_DATA);
		}

		return user;
	}

	protected UserSocial addUserSocialInfo(User user, SignUpRequest request) {
		UserSocial userSocial = null;
		if (!StringUtils.isBlank(request.getFacebookToken())) {
			userSocial = createUserSocial(user, request);
			userSocialDao.save(userSocial);
			user.addUserSocial(userSocial);
			//Notify to FB friends
			socialNotificationHelper.notifyFBFriends(user, request.getFacebookToken(), request.getFacebookUserId());
		}
		return userSocial;
	}

	protected UserSocial createUserSocial(User user, SignUpRequest request) {

		Social social = socialDao.findByDescription(Social.FACEBOOK);

		// creates UserSocial key
		UserSocialPK pk = new UserSocialPK(user.getUserId(), social.getSocialId());

		UserSocial userSocial = new UserSocial(pk);
		userSocial.setToken(request.getFacebookToken());
		userSocial.setUserId(request.getFacebookUserId());
		userSocial.setSocial(social);

		return userSocial;
	}

	protected User addDefaultNotificationConfig(User user) {

		user.setNotificationConfigs(notificationService
				.getAllApprovedNotificationConfig());
		return user;

	}

}
