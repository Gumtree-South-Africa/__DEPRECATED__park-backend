/*
 * Copyright eBay, 2014
 */
package com.ebay.park.push;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>This class is the concrete implementation of a {@link NotificationPusher} for Android.</p>
 * <p>It knows how to deal with GCM, it means, it has the ability to receive a {@link PushNotification} and deliver
 * it properly</p>
 * <p>Please note it should be used from {@link NotificationPusherMap}</p>
 *
 * @see Sender
 * @see Message
 *
 * @author lucia.masola
 */
@Component
//@Scope("prototype")
public class AndroidNotificationPusher extends NotificationPusher {

	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(AndroidNotificationPusher.class);

    /** how many attempts to retry sending a message */
    public static final int RETRIES = 1;

	private static final String MESSAGE_KEY = "message";
	private static final String BADGE_KEY = "badge";
	private static final int TEN_MINS = 600;
	private static final boolean NO = false;

    @Autowired
	Sender sender;

	@Override
	public void push(PushNotification notification) {
        String deviceId = notification.getDeviceId();
		String messageToSend = createMessage(notification);
		int badge = notification.getBadge();
		
		logger.info("android notification push deviceId:{} template:{}  messageToSend:{}", deviceId, notification.getTemplateMessage(),
                messageToSend);
		
		//@formatter:off
		Message message = new Message.Builder()
			.timeToLive(TEN_MINS)
	        .delayWhileIdle(NO)
	        .addData(MESSAGE_KEY, messageToSend)
			.addData(BADGE_KEY, String.valueOf(badge))
	        .build();
		//@formatter:on
        logger.debug("Message to send info: Time to live: {} -- Is delay while Idle: {}  -- Message: {}", message
				.getTimeToLive(), message.isDelayWhileIdle(), messageToSend);
		
		try {
			Result result = sender.send(message, deviceId, RETRIES);
			if (result != null) {
				if (result.getErrorCodeName() != null) {
					logger.info("Push Notification sent with error= {}, message= {} and new deviceId= {}", result.getErrorCodeName(), messageToSend, result.getCanonicalRegistrationId());
				} else {
					logger.info("Push Notification sent with id= {}, message= {} and new deviceId= {}", result.getMessageId(), messageToSend);
				}
			}
		} catch (Exception e){
            logger.error("An exception occurred when trying to push a notification to an android device: {}", deviceId, e);
		}
	}

}
