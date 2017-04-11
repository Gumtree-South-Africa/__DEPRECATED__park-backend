/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.social.validator;

import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.social.dto.SocialNetworkConnectRequest;
import com.ebay.park.util.FacebookUtil;
import com.ebay.park.util.TwitterUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;
/**
 * @author jpizarro
 * 
 */
@Component
public class SocialConnectValidator implements ServiceValidator<SocialNetworkConnectRequest> {

	@Autowired
	private FacebookUtil facebookUtil;

	@Autowired
	private TwitterUtil twitterUtil;
	
	@Autowired
	private UserSocialDao userSocialDao;

	/**
	 * It validates a request for binding between the Park and FB account.
 	 * @param toValidate The request to be validated
	 * @throws ServiceException with code INVALID_SOCIAL_USERID when the user id was not defined
	 * @throws ServiceException with code USER_SOCIAL_ALREADY_REGISTERED_FB when an existing Park
	 * account is found with that FB data
	 * @throws ServiceException with code USER_SOCIAL_ALREADY_REGISTERED_TW when an existing Park
	 * account is found with that TW data
	 * @throws ServiceException with code INVALID_SOCIAL_NETWORK when the social network in the
	 * request is invalid
	 * @throws ServiceException with code INVALID_FB_TOKEN when user id cannot be validated by Facebook
     */
	@Override
	public void validate(SocialNetworkConnectRequest toValidate) {
		ServiceExceptionCode userAlreadyRegisteredEntry = null;
		
		if (StringUtils.isBlank(toValidate.getSocialUserId())) {
			throw createServiceException(ServiceExceptionCode.INVALID_SOCIAL_USERID);
		}

		if (Social.FACEBOOK.equalsIgnoreCase(toValidate.getSocialNetwork())) {
			facebookUtil.tokenIsValid(toValidate.getSocialToken(), toValidate.getSocialUserId());
			toValidate.setSocialNetwork(Social.FACEBOOK);
			userAlreadyRegisteredEntry = ServiceExceptionCode.USER_SOCIAL_ALREADY_REGISTERED_FB;
		} else if (Social.TWITTER.equalsIgnoreCase(toValidate.getSocialNetwork())) {
			twitterUtil.tokenIsValid(toValidate.getSocialToken(), toValidate.getSocialTokenSecret());
			toValidate.setSocialNetwork(Social.TWITTER);
			userAlreadyRegisteredEntry = ServiceExceptionCode.USER_SOCIAL_ALREADY_REGISTERED_TW;
		} else {
			throw createServiceException(ServiceExceptionCode.INVALID_SOCIAL_NETWORK);
		}
		
		if(!userSocialDao.findBySocialUserIdAndNetwork(toValidate.getSocialUserId(), toValidate.getSocialNetwork()).isEmpty()){
			throw createServiceException(userAlreadyRegisteredEntry);
		}
	}

	public void setFacebookUtil(FacebookUtil facebookUtil) {
		this.facebookUtil = facebookUtil;
	}

	public void setTwitterUtil(TwitterUtil twitterUtil) {
		this.twitterUtil = twitterUtil;
	}

	public void setUserSocialDao(UserSocialDao userSocialDao) {
		this.userSocialDao = userSocialDao;
	}

}
