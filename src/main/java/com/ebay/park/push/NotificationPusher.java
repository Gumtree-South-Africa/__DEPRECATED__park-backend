package com.ebay.park.push;

import com.ebay.park.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Map;

public abstract class NotificationPusher {

	public static String IOS = "ios";
	public static String ANDROID = "android";
	
	@Autowired
	protected MessageUtil messageUtil;

	/**
	 * Sends a push notification to the proper server (apn, gcm, etc). Implementations must define the specific behaviour depending on
     * the platform.
	 * @param notification the {@link PushNotification} to be pushed. <b>It must not be null</b>
	 */
	public abstract void push(PushNotification notification);

    /**
     * Creates a message from a {@link PushNotification} received. It basically delegates in
     * {@link MessageUtil#formatMessage(String, Map, String)}
     *
     * @param notification
     * @return
     * @see MessageUtil#formatMessage(String, Map, String)
     */
    protected String createMessage(PushNotification notification) {
		String locale = null; // default value
		if (!CollectionUtils.isEmpty(notification.getParams())) {
			locale = notification.getParams().get("locale");
		}
		return messageUtil.formatMessage(notification.getTemplateMessage(), notification.getParams(), locale);
	} 
	
}
