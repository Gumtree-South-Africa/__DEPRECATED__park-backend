/*
 * Copyright eBay, 2014
 */
package com.ebay.park.notification;

import com.ebay.park.notification.dto.FeedNotificationMessage;
import com.ebay.park.notification.dto.MailNotificationMessage;
import com.ebay.park.notification.dto.NotificationMessage;
import com.ebay.park.notification.dto.PushNotificationMessage;
import com.ebay.park.queue.FeedNotificationHandler;
import com.ebay.park.queue.JmsNotificationQueue;
import com.ebay.park.service.notification.FeedServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Class in charge of dispatching a set of {@link NotificationMessage} . It uses "double dispatching" against
 * {@link NotificationMessage} so they can delegate back to this class with the proper method (dispatchPushNotification,
 * dispatchFeedNotification, dispatchMailNotification)
 *
 * @author jpizarro
 * @author gervasio.amy
 *
 * @see JmsNotificationQueue
 * @see FeedNotificationHandler
 *
 */
@Component
public class NotificationDispatcher {

	private static Logger logger = LoggerFactory.getLogger(NotificationDispatcher.class);
	
	@Autowired
    private FeedNotificationHandler feedNotificationHandler;

    @Autowired
    private JmsNotificationQueue jmsNotificationQueue;

	@Autowired
	private FeedServiceHelper feedServiceHelper;



    /**
     * Dispatched a set of {@link NotificationMessage}s to where they should go
     *
     * @param notificationMessages
     * @return the received set
     */
    public List<NotificationMessage> dispatch(List<NotificationMessage> notificationMessages) {
		for (NotificationMessage notificationMessage : notificationMessages) {
			try {
				this.dispatch(notificationMessage);
			} catch (Exception e) {
                logger.error("error trying to dispatch a notificationMessage: {}", notificationMessage, e);
			}
		}
		return notificationMessages;
	}

	private NotificationMessage dispatch(NotificationMessage notificationMessage) {
        logger.info("A {} notificationMessage is ready to be queued", notificationMessage.getType());
	    // double dispatcthing -->
        return notificationMessage.doDispatch(this);
	}

	public NotificationMessage dispatchPushNotification(PushNotificationMessage notificationMessage) {
		updateBadgeValue(notificationMessage);
	    return jmsNotificationQueue.queuePushNotification(notificationMessage);
	}

    public NotificationMessage dispatchMailNotification(MailNotificationMessage notificationMessage) {
        return jmsNotificationQueue.queueMailNotification(notificationMessage);
    }

    public NotificationMessage dispatchFeedNotification(FeedNotificationMessage notificationMessage) {
        return feedNotificationHandler.put(notificationMessage);
    }

    private void updateBadgeValue(PushNotificationMessage notification) {
        if (notification.getKeys() != null && notification.getDeviceId() != null) {
            Long currentValue = feedServiceHelper.countUnreadFeedsByDeviceId(notification.getDeviceId());
            notification.setProperty(PushNotificationMessage.BADGE, currentValue.toString());
        }
    }
}
