/*
 * Copyright eBay, 2014
 */
package com.ebay.park.notification.consumer;


import com.ebay.park.notification.dto.PushNotificationMessage;
import com.ebay.park.push.PushNotification;
import com.ebay.park.push.SmartPusher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * This is basically a JMS consumer. It's listening the queue <code>pushQueueDestination</code>, in where all
 * {@link PushNotificationMessage} ready to be sent should be queued...
 *
 * @author gervasio.amy & Julieta Salvadó
 * @see JmsListener
 */
@Component
public class PushNotificationConsumer {

    private static final Logger logger = LogManager.getLogger(PushNotificationConsumer.class);

    @Autowired
	private SmartPusher notificationPusher;

    @JmsListener(destination = "pushQueueDestination")
	public void consume(PushNotificationMessage pushNotification) {
        try {
            PushNotification pushNot = createPushNotification(pushNotification);
            notificationPusher.push(pushNot);
            logger.debug("A PUSH notification for deviceId: {} and deviceType: {} were pushed.", pushNot.getDeviceId(), pushNot
                    .getDeviceType());
        } catch (Exception e) {
            logger.error("PushNotificationConsumer error", e);
        }
	}

    /**
     * Creates a {@link PushNotification} based on the {@link PushNotificationMessage} received
     *
     * @param pushNotification the message to be send
     * @return the resultant notification
     */
    protected PushNotification createPushNotification(PushNotificationMessage pushNotification) {
		PushNotification notification = new PushNotification();
		notification.setDeviceId(pushNotification.getDeviceId());
		notification.setDeviceType(pushNotification.getDeviceType());
		notification.setTemplateMessage(pushNotification.getTemplateMsg());
        notification.setBadge(Integer.parseInt(pushNotification.getBadge()));

        notification.setParams(pushNotification.getParams());
        logger.debug("A PUSH notification for deviceId: {} - deviceType: {} was created", notification.getDeviceId(), notification.getDeviceType());
		return notification;
	}

}
