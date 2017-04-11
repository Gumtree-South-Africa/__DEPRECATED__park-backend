package com.ebay.park.queue;

import com.ebay.park.db.dao.FeedDao;
import com.ebay.park.db.entity.Feed;
import com.ebay.park.notification.dto.FeedNotificationMessage;
import com.ebay.park.notification.dto.NotificationMessage;
import com.ebay.park.util.FeedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This handler knows how to "dispatch" a feed. It basically knows how to create a {@link Feed} and how to save it through {@link FeedDao}
 *
 * @author lucia.masola
 * @author gervasio.amy
 */
@Component
public class FeedNotificationHandler {

    @Autowired
    private FeedUtils feedUtils;

    @Autowired
    private FeedDao feedDao;

    public NotificationMessage put(FeedNotificationMessage notificationMessage) {
        return this.save(notificationMessage);
    }

    protected NotificationMessage save(FeedNotificationMessage notificationMessage) {
        Feed feed = feedUtils.createFeed(notificationMessage);
        feedDao.save(feed);
        return notificationMessage;
    }

}
