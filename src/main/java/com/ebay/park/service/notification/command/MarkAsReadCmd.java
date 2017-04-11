package com.ebay.park.service.notification.command;

import com.ebay.park.db.entity.Feed;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.notification.FeedServiceHelper;
import com.ebay.park.service.notification.dto.MarkAsReadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 *
 */
@Component
public class MarkAsReadCmd implements ServiceCommand<MarkAsReadRequest,Void> {

    @Autowired
    private FeedServiceHelper helper;

    /**
     * It marks a feed as read.
     * @param request the incoming request
     * @throws ServiceException with code NOTIFICATION_NOT_FOUND when the feed cannot be found
     */
    @Override
    public Void execute(MarkAsReadRequest request) {
        Assert.notNull(request, "The request cannot be null");

        Feed feed = helper.getFeed(request.getFeedId());
        helper.markAsRead(feed);

        return null;
    }
}
