package com.ebay.park.service.social;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.db.entity.UserSocialPK;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.social.validator.SocialHelper;
import com.ebay.park.service.user.UserServiceHelper;

/**
 * Helper for user social methods.
 * @author Julieta Salvadó
 */
@Component
public class UserSocialHelper {
	
    @Autowired
    private SocialHelper socialHelper;

    @Autowired
    private UserSocialDao userSocialDao;

    @Autowired
    private UserServiceHelper userHelper;
    
    @Autowired
	private SocialNotificationHelper socialNotificationHelper;


    public void addUserSocialInformation(User user, String token, String userId, 
    		String socialNetwork, String tokenSecret) {
        Assert.notNull(user, "User must be not null");
        Assert.notNull(token, "Social token must be not null");
    	Assert.notNull(userId, "Social UserId must be not null");
    	Assert.notNull(socialNetwork, "Social network must be not null");
    	if (Social.TWITTER.equalsIgnoreCase(socialNetwork)) {
    		Assert.notNull(tokenSecret, "tokenSecret must be not null");
    	}
        if (!StringUtils.isBlank(token)) {
            UserSocial userSocial = createUserSocialInformation(user, token, userId, socialNetwork, tokenSecret);
            userSocialDao.save(userSocial);
            user.addUserSocial(userSocial);
            
            if (Social.FACEBOOK.equalsIgnoreCase(socialNetwork)) {
				//New FB linking account --> Notify to FB friends
				socialNotificationHelper.notifyFBFriends(user, token, userId);
			}
        }
    }

    /**
     * Creates the user social information.
     *
     * @param user the user. It must be not null.
     * @param token the social token.
     * @param userId the user social id.
     * @param socialNetwork the social network (facebook or twitter)
     * @param tokenSecret the twitter token secret
     * @return the user social
     * @throws ServiceException with code INVALID_SOCIAL_NETWORK
     */
    private UserSocial createUserSocialInformation(User user, String token, String userId, 
    		String socialNetwork, String tokenSecret) {
    	Social social = socialHelper.findSocialByDescription(socialNetwork);

        // creates UserSocial key
        UserSocialPK pk = new UserSocialPK(user.getUserId(), social.getSocialId());

        UserSocial userSocial = new UserSocial(pk);
        userSocial.setToken(token);
        userSocial.setTokenSecret(tokenSecret);
        userSocial.setUserId(userId);
        userSocial.setSocial(social);

        return userSocial;
    }

    /**
     * Find the user social id by token and social network.
     * @param token user Park token
     * @param socialNetwork social network to search for
     * @return the user social id
     * @throws ServiceException with code USER_NOT_FOUND when the user cannot be found
     * @throws ServiceException with code INVALID_SOCIAL_NETWORK when the social network cannot be found
     * @throws IllegalArgumentException when the token is null
     * @throws IllegalArgumentException when the social network is null
     */
    public UserSocialPK findUserSocialIdByTokenAndSocialNetwork(String token, String socialNetwork) {
    	Assert.notNull(token, "Social token must be not null");
        Assert.notNull(socialNetwork, "Social network must be not null");
        User user = userHelper.findUserByToken(token);
        return findUserSocialIdByUserAndSocialNetwork(user, socialNetwork);
    }

    /**
     * Find the user social id by user and social network.
     * @param user the user
     * @param socialNetwork social network to search for
     * @return the user social id
     * @throws ServiceException with code INVALID_SOCIAL_NETWORK when the social network cannot be found
     * @throws IllegalArgumentException when the token is null
     * @throws IllegalArgumentException when the social network is null
     */
    public UserSocialPK findUserSocialIdByUserAndSocialNetwork(User user, String socialNetwork) {
        Assert.notNull(user, "User token must be not null");
        Assert.notNull(socialNetwork, "Social network must be not null");

        Social social = socialHelper.findSocialByDescription(socialNetwork);
        return new UserSocialPK(user.getId(), social.getSocialId());
    }
    
    /**
     * Updates the social information
     * @param userSocial
     * @param token
     * @param userId
     */
    public void updateSocialInformation(UserSocial userSocial, String token, String userId) {
    	Assert.notNull(userSocial, "UserSocial must be not null");
    	Assert.notNull(token, "Social token must be not null");
    	Assert.notNull(userId, "Social UserId must be not null");
    	userSocial.setToken(token);
		userSocial.setUserId(userId);
		userSocialDao.save(userSocial);
    }
    
    /**
     * Finds the user social by social network.
     * @param user the user
     * @param socialNetwork the social network
     * @return user social
     */
    public UserSocial findUserSocialBySocialNetwork(User user, String socialNetwork) {
    	Assert.notNull(user, "User token must be not null");
        Assert.notNull(socialNetwork, "Social network must be not null");

    	Social social = socialHelper.findSocialByDescription(socialNetwork);

		for (UserSocial us : user.getUserSocials()){
			if (us.getSocial().equals(social)){
				return us;
			}
		}
		return null;
    }
    
    /**
     * Finds the user social by user id
     * @param userId the user id
     * @return user social
     */
    public UserSocial findUserSocialByUserId(String userId) {
    	Assert.notNull(userId, "Social UserId must be not null");
    	return userSocialDao.findByUserId(userId);
    }
}
