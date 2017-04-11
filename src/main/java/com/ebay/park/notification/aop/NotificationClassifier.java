/*
 * Copyright eBay, 2014
 */
package com.ebay.park.notification.aop;

import com.ebay.park.db.entity.NotificationConfig;
import com.ebay.park.db.entity.User;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author jpizarro
 * 
 */
public class NotificationClassifier {

	/**
	 * It classifies what kind of {@link NotificationType} we need to send for
	 * the given {@code action} {@link NotificationAction} and user.
	 * 
	 * @param action
	 *            a NotificationAction
	 * @param user
	 *            the user involved
	 * @return a list of notification types
	 */
	public List<NotificationType> classify(NotificationAction action, User user) {

		//@formatter:off
		ArrayList<NotificationType> types = Lists.newArrayList(
				Iterables.transform(
						Iterables.filter(
								user.getNotificationConfigs(),
								actionPredicate(action)),
						typeFunction()
				)
		);
		//@formatter:on

		types.sort(Comparator.comparing(NotificationType::getOrder));
		return types;
	}

	private Function<NotificationConfig, NotificationType> typeFunction() {
		return new Function<NotificationConfig, NotificationType>() {

			@Override
			public NotificationType apply(NotificationConfig notification) {
				return notification.getNotificationType();
			}

		};
	}

	private Predicate<NotificationConfig> actionPredicate(final NotificationAction action) {
		return new Predicate<NotificationConfig>() {

			@Override
			public boolean apply(NotificationConfig notification) {
				return notification.getNotificationAction().equals(action);
			}
		};
	}

}
