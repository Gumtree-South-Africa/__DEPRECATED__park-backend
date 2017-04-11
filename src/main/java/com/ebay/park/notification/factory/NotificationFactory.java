/*
 * Copyright eBay, 2014
 */
package com.ebay.park.notification.factory;

import com.ebay.park.notification.dto.NotificationMessage;

import java.util.List;

/**
 * @author jpizarro
 *
 */
public interface NotificationFactory {
	public List<NotificationMessage> createNotifications();
}
