package com.ebay.park.service.social.command;

import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.event.user.UserEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.aop.Notifiable;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.util.FacebookUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FacebookTokenVerifierCmd implements ServiceCommand<User, UserEvent>{

	@Autowired
	private FacebookUtil facebookUtil;
	
	@Autowired
	private UserSocialDao userSocialDao;
	
	@Override
	@Notifiable(action=NotificationAction.FB_TOKEN_EXPIRED)
	public UserEvent execute(User user) throws ServiceException {
		UserSocial facebookUserSocial = null;
		try {
			for (UserSocial userSocial : user.getUserSocials()) {
				if(userSocial.getSocial().getDescription().equals(Social.FACEBOOK)) {
					facebookUserSocial = userSocial;
					facebookUtil.tokenIsValid(userSocial.getToken(), userSocial.getUserId());
				} 
			}
		} catch (ServiceException e) {
			if(facebookUserSocial != null) {
				userSocialDao.delete(facebookUserSocial);
			}
			return new UserEvent(user);
		}
		return null;
	}

	public void setFacebookUtil(FacebookUtil facebookUtil) {
		this.facebookUtil = facebookUtil;
	}

	public void setUserSocialDao(UserSocialDao userSocialDao) {
		this.userSocialDao = userSocialDao;
	}

}
