package com.ebay.park.event.user;

import java.util.HashMap;
import java.util.Map;

import com.ebay.park.db.entity.User;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.dto.NotifiableServiceResult;


/**
 * The associated event for {@link NotificationAction} FB_FRIEND_USING_THE_APP 
 * @author scalderon
 * @since v2.0.4
 */
public class UserFBFriendEvent implements NotifiableServiceResult{
	
	/**  User that generate the action. */
	private User userActionGenerator;
	
	/** The recipient. */
	private User fbFriend;
	
	/** The user action generator facebook username. */
	private String fbUserName;
	
	private boolean followedByUser;
	private String fbPictureUrl;
	
	/**
	 * Instantiates a new user fb friend event.
	 *  @param userActionGenerator the user action generator
     * @param fbFriend another user that is friend of the action generator
	 * @param fbUserName username in Facebook
	 * @param fbPictureUrl the url for the profile picture
	 */
	public UserFBFriendEvent(User userActionGenerator, User fbFriend, String fbUserName, String fbPictureUrl) {
		this.userActionGenerator = userActionGenerator;
		this.fbFriend = fbFriend;
		this.fbUserName = fbUserName;
		this.followedByUser = userActionGenerator.isFollowedByUser(fbFriend);
		this.fbPictureUrl = fbPictureUrl;
	}

	public User getUserActionGenerator() {
		return userActionGenerator;
	}

	public void setUserActionGenerator(User userActionGenerator) {
		this.userActionGenerator = userActionGenerator;
	}

	public User getFbFriend() {
		return fbFriend;
	}

	public void setFbFriend(User fbFriend) {
		this.fbFriend = fbFriend;
	}
	
	public boolean isFollowedByUser() {
		return followedByUser;
	}

	public void setFollowedByUser(boolean followedByUser) {
		this.followedByUser = followedByUser;
	}

	@Override
	public Long getBasedUserId() {
		return getUserActionGenerator().getId();
	}

	@Override
	public Long getItemId() {
		return null;
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> props = new HashMap<String, String>();
		props.put(USER_NAME, userActionGenerator.getUsername());
		props.put(FB_USERNAME, fbUserName);
		props.put(FOLLOWED_BY_USER, Boolean.toString(followedByUser));
		props.put(FRIEND_USERNAME, fbFriend.getUsername());
		props.put(FRIEND_PICTURE, fbPictureUrl);
		return props;
	}

}
