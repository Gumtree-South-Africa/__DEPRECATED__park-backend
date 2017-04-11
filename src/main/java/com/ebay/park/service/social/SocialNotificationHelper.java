package com.ebay.park.service.social;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ebay.park.db.dao.SocialDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.event.user.UserFBFriendEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.aop.Notifiable;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.util.FacebookUtil;

/**
 * 
 * @author scalderon
 * @since v2.0.4
 *
 */
@Component
public class SocialNotificationHelper {
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(SocialNotificationHelper.class);
	
	@Autowired
	private FacebookUtil facebookUtil;
	
	@Autowired
	private UserServiceHelper userServiceHelper;

	@Autowired
	private SocialDao socialDao;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private SocialNotificationHelper proxyOfMe;

	@PostConstruct
	public void initialize()  {
		proxyOfMe = applicationContext.getBean(SocialNotificationHelper.class);
	}

	/**
	 * Sends a notification to indicate a new Facebook friend is using the app
	 * @param user the user whose friends will be notified
	 * @param socialId the fb user social id
	 * @return the associated event
	 */
	public void notifyFBFriends(User user, String facebookToken, String socialId) {
		Assert.notNull(user, "The feed user cannot be null.");
		if (StringUtils.isEmpty(facebookToken)) {
			throw createServiceException(ServiceExceptionCode.EMPTY_TOKEN);
		}
		
		//get FB user friends
		Social facebook = socialDao.findByDescription(Social.FACEBOOK);
		List<String> facebookFriendIds = facebookUtil.getFriendIds(facebookToken);
		
		if (facebook != null && !facebookFriendIds.isEmpty()) {
			List<User> facebookFriends =  userServiceHelper.findBySocialIdAndUserSocialIds(facebook.getSocialId(),
					facebookUtil.getFriendIds(facebookToken));
            logger.debug("Facebook friends list size: {} - UserId: {}", facebookFriends.size(), user.getId());

			String fbPictureUrl = facebookUtil.getUserPictureEscaped(facebookToken, socialId);
			String fbUsername = facebookUtil.getFacebookUsername(facebookToken);
			for (User fbFriend : facebookFriends) {
				//create the feed notification event
                logger.debug("Creating UserFBFriendEvent for fbFriend: {}", fbFriend.getId());
				proxyOfMe.notifyFBFriend(user, fbFriend, fbUsername, fbPictureUrl);
			}
		}
	}
	
	@Notifiable(action = NotificationAction.FB_FRIEND_USING_THE_APP)
	public UserFBFriendEvent notifyFBFriend(User userActionGenerator, User fbFriend,
											String fbUsername, String fbPictureUrl) {
		return new UserFBFriendEvent(userActionGenerator, fbFriend, fbUsername, fbPictureUrl);
	}

}
