package com.ebay.park.notification.factory.item;

import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserFollowsItem;
import com.ebay.park.event.item.ItemNotificationEvent;
import com.ebay.park.notification.factory.AbstractNotificationFactory;
import com.ebay.park.notification.factory.NotificationContext;

import java.util.ArrayList;
import java.util.List;

public class ItemOwnerAndFollowersNotificationFactory extends AbstractNotificationFactory {

	public ItemOwnerAndFollowersNotificationFactory(NotificationContext context) {
		super(context);
	}

	@Override
	public List<User> getRecipients() {
		List<User> recipients = new ArrayList<User>();
		ItemNotificationEvent event = (ItemNotificationEvent) context.getNotifiableResult();
		recipients.add(event.getItem().getPublishedBy());
		for ( UserFollowsItem follower : event.getItem().getFollowers()){
			recipients.add(follower.getUser());
		}
		return recipients;
	}
}