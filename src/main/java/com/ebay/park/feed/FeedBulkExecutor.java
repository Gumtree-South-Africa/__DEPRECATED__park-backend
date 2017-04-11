package com.ebay.park.feed;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.FeedDao;
import com.ebay.park.db.entity.Feed;
import com.ebay.park.util.FeedUtils;

/**
 * Class to send massive Feed Messages.
 * @author Julieta Salvadó
 */
@Component
public class FeedBulkExecutor {

    @Value("${feed_notifications.max_devices_to_send_per_request}")
    private Integer max;

    private static final int FROM = 0;

    @Autowired
    private FeedUtils feedUtils;

    @Autowired
    private FeedDao feedDao;

    public void execute(List<Long> userIds, String message) {
        if (!CollectionUtils.isEmpty(userIds)) {

            //creates feed for the list of user
            List<Feed> feedList = feedUtils.createFeedListForBulk(userIds, message);

            //send the feed notifications
            while (!feedList.isEmpty()) {
                int maximum = feedList.size() >= max ? max : feedList.size();
                feedDao.save(feedList.subList(FROM, maximum));
                feedList.subList(FROM, maximum).clear();
            }
        }
    }

}
