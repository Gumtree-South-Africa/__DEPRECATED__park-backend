/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.social.validator;

import com.ebay.park.db.dao.SocialDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.db.entity.UserSocialPK;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.social.dto.SocialNetworkDisconnectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;
/**
 * @author federico.jaite
 * 
 */
@Component
public class SocialDisconnectValidator implements
		ServiceValidator<SocialNetworkDisconnectRequest> {

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserSocialDao userSocialDao;

	@Autowired
	private SocialDao socialDao;

	@Override
	public void validate(SocialNetworkDisconnectRequest toValidate) {

		if (!Social.FACEBOOK.equalsIgnoreCase(toValidate.getSocialNetwork())
				&& !Social.TWITTER.equalsIgnoreCase(toValidate
						.getSocialNetwork())) {
			throw createServiceException(ServiceExceptionCode.INVALID_SOCIAL_NETWORK);
		}

		User user = userDao.findByUsername(toValidate.getUsername());
		Social social = socialDao.findByDescription(toValidate
				.getSocialNetwork());
		UserSocialPK id = new UserSocialPK(user.getId(), social.getSocialId());
		UserSocial userSocial = userSocialDao.findOne(id);
		if (userSocial == null) {
			throw createServiceException(ServiceExceptionCode.INVALID_USER_SOCIAL);
		}
	}

}
