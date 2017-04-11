package com.ebay.park.service.notification;

import com.ebay.park.db.dao.DeviceDao;
import com.ebay.park.db.dao.FeedDao;
import com.ebay.park.db.dao.NotificationConfigDao;
import com.ebay.park.db.entity.Device;
import com.ebay.park.db.entity.Feed;
import com.ebay.park.db.entity.NotificationConfig;
import com.ebay.park.db.entity.User;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.notification.dto.NotifiableServiceResult;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.notification.command.CountUnreadFeedsCmd;
import com.ebay.park.service.user.UserServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * The FeedService {@link FeedService} feedServiceHelper.
 * @author scalderon & Julieta Salvadó
 * @since v2.0.4
 *
 */
@Component
public class FeedServiceHelper {
	
	@Autowired
	private FeedDao feedDao;
	
	@Autowired
	private UserServiceHelper userServiceHelper;
	
	@Autowired
	private NotificationConfigDao notificationConfigDao;

	@Autowired
	private DeviceDao deviceDao;

	@Autowired
	private CountUnreadFeedsCmd countUnreadFeedsCmd;

	/**
	 * By FB_FRIEND_USING_THE_APP implementation, it is necessary to update 
	 * "followByUser" feed property every time the recipient follows a FB Friend user.
     * @param owner
     *      the user starting the action
     * @param user
     *      the other user in the relation
     * @param value
     *      true if the owner user is following the other user; otherwise, false.
     */
	public void updateFollowByUserFeedProperty(User owner, User user, String value) {
		Assert.notNull(owner, "The feed owner cannot be null.");
		Assert.notNull(user, "The feed user cannot be null.");
		NotificationConfig notificationConfig = notificationConfigDao.
				findNotification(NotificationType.FEED, NotificationAction.FB_FRIEND_USING_THE_APP);
		
		if (notificationConfig != null) {
            List<Feed> feeds = feedDao.findFeedToUpdate(notificationConfig.getNotId(), owner.getId(), user.getId());
            if (!CollectionUtils.isEmpty(feeds)) {
                for (Feed feed : feeds) {
                    if (feed.getFeedProperties() == null) {
                        feed.setFeedProperties(new HashMap<String, String>());
                    }
                    feed.getFeedProperties().put(NotifiableServiceResult.FOLLOWED_BY_USER, value);
                    feedDao.saveAndFlush(feed);
                }
            }
		}
	}
	
	public Long countUnreadFeeds(String token) {
		Assert.notNull(token, "The token cannot be null");
		User user = userServiceHelper.findUserByToken(token);
		userServiceHelper.assertUserNotNull(user);

		return user.getUnreadFeeds();
	}

	/**
	 * It returns the feed with the given id.
	 * @param feedId the id from the feed
	 * @return the feed with the given id
	 * @throws ServiceException with code NOTIFICATION_NOT_FOUND when the feed cannot be found
	 */
	public Feed getFeed(Long feedId) {
		Assert.notNull(feedId, "feedId' must be not null");
		Feed feed = feedDao.findById(feedId);
		assertNotNull(feed);
		return feed;
	}

	public Long countUnreadFeedsByDeviceId(String deviceId) {
		Assert.notNull(deviceId, "'deviceId' cannot be null");
		List<Device> devices = deviceDao.findByDeviceId(deviceId);
		if (!CollectionUtils.isEmpty(devices)) {
			for (Device device:devices) {
				return countUnreadFeedsCmd.execute(device.getUserSession().getToken());
			}
		}
		return 0L;
	}

	/**
	 * It verifies whether a feed is null or not.
	 * @param feed the feed under analysis
	 * @throws ServiceException with code NOTIFICATION_NOT_FOUND
	 */
	public void assertNotNull(Feed feed) {
		if (feed == null) {
			throw createServiceException(ServiceExceptionCode.NOTIFICATION_NOT_FOUND);
		}
	}

	public void markAsRead(Feed feed) {
		Assert.notNull(feed, "feed' must be not null");
		feed.setRead(true);
		feedDao.save(feed);
	}
}
