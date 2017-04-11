package com.ebay.park.notification.factory.user;

import java.util.Arrays;
import java.util.List;

import com.ebay.park.db.entity.User;
import com.ebay.park.event.user.UserFBFriendEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.factory.AbstractNotificationFactory;
import com.ebay.park.notification.factory.NotificationContext;

/**
 * Class in charge of creating the {@link NotificationAction} FB_FRIEND_USING_THE_APP notifications. 
 * @author scalderon
 * @since v2.0.4
 *
 */
public class UserFBFriendNotificationFactory extends AbstractNotificationFactory {
	
	public UserFBFriendNotificationFactory(NotificationContext context) {
		super(context);
	}

	/**
	 * Returns the list of recipients.
	 */
	@Override
    public List<User> getRecipients() {
		//UserFBFriendEvent is associated to UserFBFriendNotificationFactory. The casting is safe.
		return Arrays.asList(((UserFBFriendEvent) context.getNotifiableResult()).getFbFriend());
	}

}
