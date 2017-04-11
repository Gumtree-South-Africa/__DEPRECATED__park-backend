/*
 * Copyright eBay, 2014
 */
package com.ebay.park.notification;

import com.ebay.park.notification.factory.NotificationContext;
import com.ebay.park.notification.factory.NotificationFactory;
import com.ebay.park.notification.factory.conversation.ChatSentNotificationFactory;
import com.ebay.park.notification.factory.conversation.ConversationAcceptedNotificationFactory;
import com.ebay.park.notification.factory.conversation.ConversationRejectedNotificationFactory;
import com.ebay.park.notification.factory.item.InterestedItemFollowersNotificationFactory;
import com.ebay.park.notification.factory.item.ItemFollowersNotificationFactory;
import com.ebay.park.notification.factory.item.ItemOwnerNotificationFactory;
import com.ebay.park.notification.factory.item.NotInterestedItemFollowersNotificationFactory;
import com.ebay.park.notification.factory.item.UserFollowersNotificationFactory;
import com.ebay.park.notification.factory.user.UserDirectNotificationFactory;
import com.ebay.park.notification.factory.user.UserFBFriendNotificationFactory;
import com.ebay.park.notification.factory.user.UsersNotificationFactory;
import com.ebay.park.service.moderation.FeedModerationFactory;

import java.io.Serializable;

/**
 * Enumeration that represents the action that needs to be notified to the user.
 * 
 * @author jpizarro
 * 
 */
public enum NotificationAction implements Serializable {

	//@formatter:off
	/*
	NONE("NONE") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context) {
			throw new NotImplementedException();
		}
	},*/
	USER_BLOCKED("USER_BLOCKED", NotificationGroup.GENERAL, "notification.action.user_blocked", true){
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new UserDirectNotificationFactory(context);
		}

	},
	ITEM_BANNED("ITEM_BANNED", NotificationGroup.MY_PUBLICATION, "notification.action.item_banned"){
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new ItemOwnerNotificationFactory(context);
		}

	},
	FOLLOW_USER("FOLLOW_USER", NotificationGroup.GENERAL, "notification.action.follow_user") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context) {
			return new UsersNotificationFactory(context);
		}
	},
	SOLD_AN_ITEM("SOLD_AN_ITEM", NotificationGroup.GENERAL, "notification.action.sold_item") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context) {
			return new NotInterestedItemFollowersNotificationFactory(context);
		}
	},
	SOLD_AN_ITEM_FOR_INTERESTED_FOLLOWERS("SOLD_AN_ITEM_FOR_INTERESTED_FOLLOWERS", NotificationGroup.GENERAL,
	        "notification.action.sold_item_for_interested_follower") {

        @Override
        public NotificationFactory notificationFactory(
                NotificationContext context) {
            return new InterestedItemFollowersNotificationFactory(context);
        }
	},
	USER_RATED("USER_RATED", NotificationGroup.GENERAL, "notification.action.user_rated") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new UsersNotificationFactory(context);
		}
	},
	PENDING_RATE("PENDING_RATE", NotificationGroup.NEGOTIATION_CHAT, "notification.action.pending_rate") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new UsersNotificationFactory(context);
		}
	},
	DELETE_AN_ITEM("DELETE_AN_ITEM", NotificationGroup.GENERAL, "notification.action.delete_item") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new ItemFollowersNotificationFactory(context);
		}
	},
	UPDATE_AN_ITEM("UPDATE_AN_ITEM", NotificationGroup.GENERAL, "notification.action.update_item") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new ItemFollowersNotificationFactory(context);
		}
	},
	NEW_ITEM("NEW_ITEM", NotificationGroup.GENERAL, "notification.action.new_item") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new UserFollowersNotificationFactory(context);
		}
	},
	ITEM_APROVED("ITEM_APROVED", NotificationGroup.MY_PUBLICATION, "notification.action.item_approved") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new ItemOwnerNotificationFactory(context);
		}
	},
	ITEM_EXPIRE("ITEM_EXPIRE", NotificationGroup.MY_PUBLICATION, "notification.action.item_expire") { 
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new ItemOwnerNotificationFactory(context);
		}
	},
	ITEM_ABOUT_TO_EXPIRE("ITEM_ABOUT_TO_EXPIRE", NotificationGroup.MY_PUBLICATION, "notification.action.item_about_to_expire") { 
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new ItemOwnerNotificationFactory(context);
		}
	},
	
	ITEM_REJECTED("ITEM_REJECTED", NotificationGroup.MY_PUBLICATION, "notification.action.item_rejected") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new ItemOwnerNotificationFactory(context);
		}
	},

	ITEM_DELETED_FROM_MODERATION_DUPLICATED("ITEM_DELETED_FROM_MODERATION_DUPLICATED", NotificationGroup.MY_PUBLICATION, "notification.action.item_rejected_duplicated") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new ItemOwnerNotificationFactory(context);
		}
	},
	
	ITEM_DELETED_FROM_MODERATION_PICTURES("ITEM_DELETED_FROM_MODERATION_PICTURES", NotificationGroup.MY_PUBLICATION, "notification.action.item_rejected_pictures") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new ItemOwnerNotificationFactory(context);
		}
	},
	
	ITEM_DELETED_FROM_MODERATION_SERVICES("ITEM_DELETED_FROM_MODERATION_SERVICES", NotificationGroup.MY_PUBLICATION, "notification.action.item_rejected_services") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new ItemOwnerNotificationFactory(context);
		}
	},
	
	ITEM_DELETED_FROM_MODERATION_MAKEUP("ITEM_DELETED_FROM_MODERATION_MAKEUP", NotificationGroup.MY_PUBLICATION, "notification.action.item_rejected_makeup") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new ItemOwnerNotificationFactory(context);
		}
	},
	
	ITEM_DELETED_FROM_MODERATION_ANIMALS("ITEM_DELETED_FROM_MODERATION_ANIMALS", NotificationGroup.MY_PUBLICATION, "notification.action.item_rejected_animals") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new ItemOwnerNotificationFactory(context);
		}
	},
	
	ITEM_DELETED_FROM_MODERATION_COMMISSION("ITEM_DELETED_FROM_MODERATION_COMMISSION", NotificationGroup.MY_PUBLICATION, "notification.action.item_rejected_commission") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new ItemOwnerNotificationFactory(context);
		}
	},
	
	ITEM_DELETED_FROM_MODERATION_STYLE("ITEM_DELETED_FROM_MODERATION_STYLE", NotificationGroup.MY_PUBLICATION, "notification.action.item_rejected_style") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new ItemOwnerNotificationFactory(context);
		}
	},
	
	ITEM_DELETED_FROM_MODERATION_PRICE("ITEM_DELETED_FROM_MODERATION_PRICE", NotificationGroup.MY_PUBLICATION, "notification.action.item_rejected_price") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new ItemOwnerNotificationFactory(context);
		}
	},
	
	ITEM_DELETED_FROM_MODERATION_FORBIDDEN("ITEM_DELETED_FROM_MODERATION_FORBIDDEN", NotificationGroup.MY_PUBLICATION, "notification.action.item_rejected_forbidden") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new ItemOwnerNotificationFactory(context);
		}
	},
	
	CONVERSATION_ACCEPTED("CONVERSATION_ACCEPTED", NotificationGroup.NEGOTIATION_CHAT, "notification.action.conversation_accepted"){
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new ConversationAcceptedNotificationFactory(context);
		}

	},
	CONVERSATION_REJECTED("CONVERSATION_REJECTED", NotificationGroup.NEGOTIATION_CHAT, "notification.action.conversation_rejected") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new ConversationRejectedNotificationFactory(context);
		}
	},
	CHAT_SENT("CHAT_SENT", NotificationGroup.NEGOTIATION_CHAT, "notification.action.chat_sent") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new ChatSentNotificationFactory(context);
		}
	},
	TW_TOKEN_EXPIRED("TW_TOKEN_EXPIRED", NotificationGroup.GENERAL, "notification.action.twitter_token_expired") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new UserDirectNotificationFactory(context);
		}
	},
	FB_TOKEN_EXPIRED("FB_TOKEN_EXPIRED", NotificationGroup.GENERAL, "notification.action.facebook_token_expired") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new UserDirectNotificationFactory(context);
		}
	},
	FEED_FROM_MODERATION("FEED_FROM_MODERATION", NotificationGroup.GENERAL, "notification.action.from_moderation") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new FeedModerationFactory(context);
		}
	},
	FB_FRIEND_USING_THE_APP("FB_FRIEND_USING_THE_APP", NotificationGroup.GENERAL, "notification.action.facebook_friend_using_the_app") {
		@Override
		public NotificationFactory notificationFactory(NotificationContext context){
			return new UserFBFriendNotificationFactory(context);
		}
	}
	;
	//@formatter:on

	private final String value;
	private final NotificationGroup group;
	private final String messageKey;
	private boolean hideInConfiguration;

	public abstract NotificationFactory notificationFactory(NotificationContext context);

	NotificationAction(String value, NotificationGroup group, String messageKey, boolean hideInConfiguration) {
		this(value, group, messageKey);
		this.hideInConfiguration = hideInConfiguration;
	}

	NotificationAction(String value, NotificationGroup group, String messageKey) {
		this.value = value;
		this.group = group;
		this.messageKey = messageKey;
		this.hideInConfiguration = false;
	}

	public static NotificationAction fromValue(String value) {
		if (value != null) {
			for (NotificationAction action : values()) {
				if (action.value.equals(value)) {
					return action;
				}
			}
		}

		return null;
	}

	public String toValue() {
		return value;
	}

	/*
	 * public static NotificationAction getDefault() { return NONE; }
	 */
	/**
	 * @return the group
	 */
	public NotificationGroup getGroup() {
		return group;
	}

	/**
	 * @return the messageKey
	 */
	public String getMessageKey() {
		return messageKey;
	}

	/**
	 * @return the hideInConfiguration
	 */
	public boolean isHideInConfiguration() {
		return hideInConfiguration;
	}
}