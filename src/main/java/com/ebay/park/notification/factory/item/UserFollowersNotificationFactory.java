package com.ebay.park.notification.factory.item;

import com.ebay.park.db.entity.User;
import com.ebay.park.event.item.ItemNotificationEvent;
import com.ebay.park.notification.factory.AbstractNotificationFactory;
import com.ebay.park.notification.factory.NotificationContext;
import com.ebay.park.service.social.SocialService;

import java.util.List;

public class UserFollowersNotificationFactory extends AbstractNotificationFactory {

	public UserFollowersNotificationFactory(NotificationContext context) {
		super(context);

	}

	@Override
	public List<User> getRecipients() {
		ItemNotificationEvent event = (ItemNotificationEvent) context.getNotifiableResult();
		User user = event.getItem().getPublishedBy();
		SocialService socialService = context.getSocialService();
		return socialService.getFollowers(user);
	}
}