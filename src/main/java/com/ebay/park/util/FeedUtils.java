package com.ebay.park.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.joda.time.DateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.NotificationConfigDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Feed;
import com.ebay.park.db.entity.User;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.notification.dto.FeedNotificationMessage;
import com.ebay.park.notification.dto.NotifiableServiceResult;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A set of validations and utilities common to feed notifications.
 * @author Julieta Salvadó
 *
 */
@Component
public class FeedUtils {

	private static Logger logger = LoggerFactory.getLogger(FeedUtils.class);
	
    @Autowired
    private UserDao userDao;

    @Autowired
    private NotificationConfigDao notificationConfigDao;

    @Autowired
    private ItemDao itemDao;

    @SuppressWarnings("unchecked")
	public Map<String, String> getFeedProperties(String feedProperty) {
    	JSONObject json;
		try {
			json = new JSONObject(feedProperty);
			return new ObjectMapper().readValue(json.toString(), Map.class);
		} catch (Exception e) {
			logger.error("Error trying to get feed properties", e);
		}
		return null;
    }

    public Feed createFeed(FeedNotificationMessage notification) {
        Feed feed = new Feed();
        feed.setFeedProperties(getFeedProperties(notification.getFeedProperties()));
        feed.setCreationDate(DateTime.now().toDate());
        feed.setOwner(userDao.findById(notification.getUserToNotify()));
        feed.setNotificationConfig(notificationConfigDao.findNotification(
                notification.getType() /*TODO: does it work?*/,
                notification.getAction() /*TODO: does it work?*/));
        
        if (notification.getBasedUser() != null){
            feed.setUser(userDao.findById(notification.getBasedUser()));
        }
        
        if (notification.getItem() != null){
            //item is stored even when it was already deleted
            feed.setItem(itemDao.getOne(notification.getItem()));
        }
        return feed;
    }
    
    public List<Feed> createFeedListForBulk(List<Long> userIds, String message) {
        Map<String, String> props = new HashMap<String, String>();
        props.put(NotifiableServiceResult.MODERATION_MESSAGE, message);
        
        List<Feed> feedList = new ArrayList<Feed>();
        userIds.stream()
            .map(userDao::findById)
            .map(createFeedForBulk)
            .forEach(f -> 
            {
                f.setFeedProperties(props);
                feedList.add(f);
            });
        return feedList;
    }

    Function<User, Feed> createFeedForBulk = new Function<User, Feed>() {

    @Override
    public Feed apply(User user) {
        Feed feed = new Feed();
        feed.setOwner(user);
        feed.setCreationDate(DateTime.now().toDate());
        feed.setNotificationConfig(notificationConfigDao.findNotification(
                NotificationType.FEED,
                NotificationAction.FEED_FROM_MODERATION
                ));

        return feed;
        }
    };

}
