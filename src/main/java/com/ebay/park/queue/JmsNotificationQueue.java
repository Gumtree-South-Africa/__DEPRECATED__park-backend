package com.ebay.park.queue;

import com.ebay.park.notification.dto.NotificationMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;

/**
 * This is the main  based on JMS
 *
 * @author gervasio.amy
 * @since 05/09/2016
 *
 * @see JmsTemplate#convertAndSend(Destination, Object)
 */
@Component
public class JmsNotificationQueue {

    private static final Logger logger = LogManager.getLogger(JmsNotificationQueue.class);

    @Autowired
    @Qualifier("emailQueueDestination")
    private Destination emailQueue;

    // TODO evaluate if it would be a better idea to have two different queues, one for ioS and other for Android
    @Autowired
    @Qualifier("pushQueueDestination")
    private Destination pushQueue;

    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    public NotificationMessage queuePushNotification(NotificationMessage notificationMessage) {
        logger.debug("Sending to pushQueue a push notification message: {}", notificationMessage);
        jmsTemplate.convertAndSend(pushQueue, notificationMessage);
        return notificationMessage;
    }

    public NotificationMessage queueMailNotification(NotificationMessage notificationMessage) {
        logger.debug("Sending to emailQueue a email notification message: {}", notificationMessage);
        jmsTemplate.convertAndSend(emailQueue, notificationMessage);
        return notificationMessage;
    }



}
