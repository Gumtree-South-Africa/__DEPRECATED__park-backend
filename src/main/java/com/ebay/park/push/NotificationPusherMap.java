/*
 * Copyright eBay, 2014
 */
package com.ebay.park.push;

import com.ebay.park.db.entity.DeviceType;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * It's a simple wrapper of a {@link Map} containing both Android and iOS {@link NotificationPusher}s, so, it's possible to push a
 * {@link PushNotification} without taking care if it is Android or iOS
 *
 * @author lucia.masola
 * @author gervasio.amy
 */
@Component
public class NotificationPusherMap implements SmartPusher {
	
	private Map<String, NotificationPusher> pushers = new HashMap<>();

	@Autowired
	public NotificationPusherMap(AndroidNotificationPusher androidPusher, IOSNotificationPusher iOSPusher) {
		this.pushers.put(DeviceType.IOS.getValue(), iOSPusher);
		this.pushers.put(DeviceType.ANDROID.getValue(), androidPusher);
	}

	public void push(PushNotification notification) {
        Assert.notNull(notification, "notification must not be null");
        NotificationPusher pusher = pushers.get(notification.getDeviceType());
		pusher.push(notification);
	}

}
