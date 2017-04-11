package com.ebay.park.service.social.command;

import java.util.List;

import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.event.user.UserEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.aop.Notifiable;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.util.TwitterUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("twitterTokenVerifierCmd")
public class TwitterTokenVerifierCmd implements ServiceCommand<User, UserEvent>{
	@Autowired
	private TwitterUtil twitterUtil;
	
	@Autowired
	private UserSocialDao userSocialDao;
	
	@Override
	@Notifiable(action=NotificationAction.TW_TOKEN_EXPIRED)
	public UserEvent execute(User user) throws ServiceException {
		UserSocial twitterUserSocial = null;
		try {
		    List<UserSocial> userSocials = user.getUserSocials();
		    if (userSocials != null && !userSocials.isEmpty()) {
    			for (UserSocial userSocial : user.getUserSocials()) {
    				if(userSocial.getSocial().getDescription().equals(Social.TWITTER)) {
    					twitterUserSocial = userSocial;
    					twitterUtil.tokenIsValid(userSocial.getToken(), userSocial.getTokenSecret());
    				}
    			}
		    }
		} catch (ServiceException e) {
			if(twitterUserSocial != null) {
				userSocialDao.delete(twitterUserSocial);
			}
			return new UserEvent(user);
		}
		return null;
	}

	public void setTwitterUtil(TwitterUtil twitterUtil) {
		this.twitterUtil = twitterUtil;
	}

	public void setUserSocialDao(UserSocialDao userSocialDao) {
		this.userSocialDao = userSocialDao;
	}
}
