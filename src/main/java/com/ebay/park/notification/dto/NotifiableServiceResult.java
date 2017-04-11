/*
 * Copyright eBay, 2014
 */
package com.ebay.park.notification.dto;

import com.ebay.park.util.Mappable;

import java.util.Map;

/**
 * Classes implementing this interface are eligible to be handle by {@link com.ebay.park.notification.aop.NotificationAdvice}.
 * The notifiable method (mark with @Notifiable), must return an instance child classes.
 * @author jpizarro
 * 
 */
public interface NotifiableServiceResult extends Mappable<String, String> {
	
	static final String ITEM_NAME = "itemName";
	static final String USER_NAME = "username";
	static final String GROUP_NAME = "groupName";
	static final String CONVERSATION_ID = "conversationId";
	static final String SENDER_USERNAME = "senderUsername";
	static final String RATER = "rater";
	static final String RECIPIENT = "recipient";
	static final String URL = "url";
	static final String ITEM_ID = "itemId";
	static final String USER_ID_TO_NOTIFY = "userIdToNotify";
	static final String USERNAME_THAT_ACCEPTED = "usernameThatAccepted";
	static final String USER_ID_THAT_ACCEPTED = "userIdThatAccepted";
	
    static final String USERNAME_THAT_REJECTED = "usernameThatRejected";
    
    static final String MODERATION_MESSAGE = "message";
    static final String FB_USERNAME = "fbUsername";
    static final String FOLLOWED_BY_USER = "followedByUser";
    static final String FRIEND_USERNAME = "friendUsername";
    static final String FRIEND_PICTURE = "friendPicture";

	Long getBasedUserId();
	Long getItemId();
	@Override
    Map<String, String> toMap();

}
