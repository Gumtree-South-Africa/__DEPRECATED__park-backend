/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.social.command;

import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocialPK;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.email.EmailVerificationHelper;
import com.ebay.park.service.social.UserSocialHelper;
import com.ebay.park.service.social.dto.SocialNetworkDisconnectRequest;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.util.EmailVerificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author federico.jaite
 *
 */
@Component
public class SocialDisconnectCmd implements ServiceCommand<SocialNetworkDisconnectRequest, Boolean> {

	@Autowired
	private UserSocialDao userSocialDao;

	@Autowired
	private UserSocialHelper userSocialHelper;

	@Autowired
	private UserServiceHelper userHelper;

	@Autowired
	private EmailVerificationUtil emailVerificationUtil;
	
	@Override
	public Boolean execute(SocialNetworkDisconnectRequest request) throws ServiceException {
		User user = userHelper.findUserByToken(request.getToken());
		UserSocialPK id = userSocialHelper.findUserSocialIdByUserAndSocialNetwork(
				user,
				request.getSocialNetwork());

		if (user.isMobileVerified()) {
			emailVerificationUtil.unverify(user);
		}

		userSocialDao.delete(id);
		return Boolean.TRUE;
	}

}
