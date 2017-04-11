package com.ebay.park.notification;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ebay.park.db.dao.FeedDao;
import com.ebay.park.db.dao.NotificationConfigDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Feed;
import com.ebay.park.db.entity.NotificationConfig;
import com.ebay.park.db.entity.User;
import com.ebay.park.notification.dto.NotifiableServiceResult;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.notification.FeedServiceHelper;
import com.ebay.park.service.user.UserServiceHelper;

/**
 * Test class for {@link FeedServiceHelper}
 * @author scalderon
 *
 */
public class FeedServiceHelperTest {
	
	private static final Long FEED_ID_1 = 1L;
    private static final Long FEED_ID_2 = 2L;
	private static final Long NOTIFICATION_CONFIG_ID = 1L;
	private static final Long OWNER_ID = 1L;
	private static final Long USER_ID = 2L;
	private static final String VALID_TOKEN = "token";
	private static final Long UNREAD_FEEDS_COUNTER = 4L;
	private static final long INVALID_FEED_ID = 3L;

	@Mock
	private FeedDao feedDao;
	
	@Mock
	private UserServiceHelper userServiceHelper;
	
	@Mock
	private NotificationConfigDao notificationConfigDao;

	@InjectMocks
	private FeedServiceHelper feedServiceHelper;
	
	private User owner;

	@Mock
	private User user;
	private NotificationConfig notificationConfig;
	private Map<String, String> props;
	private Feed fbFriendUsingTheAppFeed;
    private Feed secondFeed;
	
	@Before
	public void setUp() {
		initMocks(this);

		when(user.getId()).thenReturn(USER_ID);

		//given
		owner = new User();
		owner.setId(OWNER_ID);

		notificationConfig = new NotificationConfig(NotificationAction.FB_FRIEND_USING_THE_APP, NotificationType.FEED);
		notificationConfig.setNotId(NOTIFICATION_CONFIG_ID);
		
		props = new HashMap<>();
		
		fbFriendUsingTheAppFeed = new Feed();
		fbFriendUsingTheAppFeed.setId(FEED_ID_1);
		fbFriendUsingTheAppFeed.setNotificationConfig(notificationConfig);
		fbFriendUsingTheAppFeed.setFeedProperties(props);
		fbFriendUsingTheAppFeed.setOwner(owner);
		fbFriendUsingTheAppFeed.setUser(user);

        secondFeed = new Feed();
        secondFeed.setId(FEED_ID_2);
        secondFeed.setNotificationConfig(notificationConfig);
        secondFeed.setFeedProperties(props);
        secondFeed.setOwner(owner);
        secondFeed.setUser(user);
	}
	
	@Test
	public void givenAFeedWhenUpdateFollowedByUserPropertyThenUpdateSuccessfully() {
		//given
		props.put(NotifiableServiceResult.FOLLOWED_BY_USER, Boolean.toString(true));
		
		//when
		when(notificationConfigDao.findNotification(NotificationType.FEED, NotificationAction.FB_FRIEND_USING_THE_APP))
		.thenReturn(notificationConfig);
		when(feedDao.findFeedToUpdate(NOTIFICATION_CONFIG_ID, OWNER_ID, USER_ID)).thenReturn(Collections.singletonList(fbFriendUsingTheAppFeed));
		
		feedServiceHelper.updateFollowByUserFeedProperty(owner, user, Boolean.toString(false));
		
		//then
		assertEquals(Boolean.FALSE.toString(), fbFriendUsingTheAppFeed.getFeedProperties()
				.get(NotifiableServiceResult.FOLLOWED_BY_USER));
		verify(notificationConfigDao, Mockito.times(1)).findNotification(NotificationType.FEED, NotificationAction.FB_FRIEND_USING_THE_APP);
		verify(feedDao, Mockito.times(1)).findFeedToUpdate(NOTIFICATION_CONFIG_ID, OWNER_ID, USER_ID);
	}
	
	@Test
	public void givenANullNotificationConfigWhenUpdateFollowedByUserPropertyThenNotUpdate() {
		//when
		when(notificationConfigDao.findNotification(NotificationType.FEED, NotificationAction.FB_FRIEND_USING_THE_APP))
		.thenReturn(null);
		
		feedServiceHelper.updateFollowByUserFeedProperty(owner, user, Boolean.toString(false));
		
		//then
		verify(notificationConfigDao, Mockito.times(1)).findNotification(NotificationType.FEED, NotificationAction.FB_FRIEND_USING_THE_APP);
		verify(feedDao, Mockito.times(0)).findFeedToUpdate(NOTIFICATION_CONFIG_ID, OWNER_ID, USER_ID);
	}
	
	@Test
	public void givenNullFeedListWhenUpdateFollowedByUserPropertyThenNotUpdate() {
		//when
		when(notificationConfigDao.findNotification(NotificationType.FEED, NotificationAction.FB_FRIEND_USING_THE_APP))
		.thenReturn(notificationConfig);
		when(feedDao.findFeedToUpdate(NOTIFICATION_CONFIG_ID, OWNER_ID, USER_ID)).thenReturn(null);
		
		feedServiceHelper.updateFollowByUserFeedProperty(owner, user, Boolean.toString(false));
		
		//then
		verify(feedDao, Mockito.times(1)).findFeedToUpdate(NOTIFICATION_CONFIG_ID, OWNER_ID, USER_ID);
	}

    @Test
    public void givenEmptyFeedListWhenUpdateFollowedByUserPropertyThenNotUpdate() {
        when(notificationConfigDao.findNotification(NotificationType.FEED, NotificationAction.FB_FRIEND_USING_THE_APP))
                .thenReturn(notificationConfig);
        when(feedDao.findFeedToUpdate(NOTIFICATION_CONFIG_ID, OWNER_ID, USER_ID)).thenReturn(new ArrayList<Feed>());

        feedServiceHelper.updateFollowByUserFeedProperty(owner, user, Boolean.toString(false));
        
        verify(feedDao, Mockito.times(1)).findFeedToUpdate(NOTIFICATION_CONFIG_ID, OWNER_ID, USER_ID);
    }
	
	@Test
	public void givenNullFeedPropertyMapWhenUpdateFollowedByUserPropertyThenAddProperty() {
		//given
		fbFriendUsingTheAppFeed.setFeedProperties(null);
		
		//when
		when(notificationConfigDao.findNotification(NotificationType.FEED, NotificationAction.FB_FRIEND_USING_THE_APP))
		.thenReturn(notificationConfig);
		when(feedDao.findFeedToUpdate(NOTIFICATION_CONFIG_ID, OWNER_ID, USER_ID)).thenReturn(Collections.singletonList(fbFriendUsingTheAppFeed));
		
		feedServiceHelper.updateFollowByUserFeedProperty(owner, user, Boolean.toString(false));
		
		//then
		assertEquals(Boolean.FALSE.toString(), fbFriendUsingTheAppFeed.getFeedProperties()
				.get(NotifiableServiceResult.FOLLOWED_BY_USER));
		verify(notificationConfigDao, Mockito.times(1)).findNotification(NotificationType.FEED, NotificationAction.FB_FRIEND_USING_THE_APP);
		verify(feedDao, Mockito.times(1)).findFeedToUpdate(NOTIFICATION_CONFIG_ID, OWNER_ID, USER_ID);
	}

	@Test
    public void givenTwoFeedWithTheSameUsersWhenUpdateFollowedByUserPropertyThenAddPropertyToBothFeeds() {
        props.put(NotifiableServiceResult.FOLLOWED_BY_USER, Boolean.toString(true));

        when(notificationConfigDao.findNotification(NotificationType.FEED, NotificationAction.FB_FRIEND_USING_THE_APP))
                .thenReturn(notificationConfig);
        when(feedDao.findFeedToUpdate(NOTIFICATION_CONFIG_ID, OWNER_ID, USER_ID)).thenReturn(Arrays.asList(fbFriendUsingTheAppFeed, secondFeed));

        feedServiceHelper.updateFollowByUserFeedProperty(owner, user, Boolean.toString(false));

        assertEquals(Boolean.FALSE.toString(), fbFriendUsingTheAppFeed.getFeedProperties()
                .get(NotifiableServiceResult.FOLLOWED_BY_USER));
        assertEquals(Boolean.FALSE.toString(), secondFeed.getFeedProperties()
                .get(NotifiableServiceResult.FOLLOWED_BY_USER));
    }
	
	@Test(expected=IllegalArgumentException.class)
	public void givenANullTokenWhenCountingUnreadFeedsThenIllegalException() {
		feedServiceHelper.countUnreadFeeds(null);
	}

	@Test
	public void givenAValidTokenWhenCountingUnreadFeedsThenSuccess() {
		when(userServiceHelper.findUserByToken(VALID_TOKEN)).thenReturn(user);
		when(user.getUnreadFeeds()).thenReturn(UNREAD_FEEDS_COUNTER);

		Long counter = feedServiceHelper.countUnreadFeeds(VALID_TOKEN);

		assertNotNull(counter);
		assertEquals(counter, UNREAD_FEEDS_COUNTER);
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenNullIdWhenGettingFeedThenException() {
		feedServiceHelper.getFeed(null);
	}

	@Test
	public void givenInvalidFeedIdWhenGettingFeedThenException() {
		when(feedDao.findById(INVALID_FEED_ID)).thenReturn(null);
		try {
			feedServiceHelper.getFeed(INVALID_FEED_ID);
			fail();
		} catch (ServiceException e) {
			assertThat(e.getCode(), is(ServiceExceptionCode.NOTIFICATION_NOT_FOUND.getCode()));
		}
	}

	@Test
	public void givenValidFeedIdWhenGettingFeedThenReturnFeed() {
		Feed expectedFeed = mock(Feed.class);
		when(feedDao.findById(FEED_ID_1)).thenReturn(expectedFeed);

		assertThat(feedServiceHelper.getFeed(FEED_ID_1), is(expectedFeed));
	}

	@Test
	public void givenNotNullFeedWhenAssertingNullThenDoNothing() {
		feedServiceHelper.assertNotNull(mock(Feed.class));
	}

	@Test
	public void givenNullFeedWhenAssertingNullThenException() {
		try {
			feedServiceHelper.assertNotNull(null);
			fail();
		} catch (ServiceException e) {
			assertThat(e.getCode(), is(ServiceExceptionCode.NOTIFICATION_NOT_FOUND.getCode()));
		}
	}

	@Test (expected = IllegalArgumentException.class)
	public void givenNullFeedWhenMarkingAsReadThenException() {
		feedServiceHelper.markAsRead(null);
	}

	@Test
	public void givenValidFeedWhenMarkingAsReadThenSave() {
		Feed feed = mock(Feed.class);
		feedServiceHelper.markAsRead(feed);

		verify(feedDao).save(feed);
	}

	@Test
	public void givenReadFeedWhenMarkingAsReadThenUpdateReadValue() {
		Feed feed = new Feed();
		feed.setRead(true);

		feedServiceHelper.markAsRead(feed);

		assertThat("The feed should be marked as read", feed.isRead(), is(true));
	}

	@Test
	public void givenUnreadFeedWhenMarkingAsReadThenUpdateReadValue() {
		Feed feed = new Feed();
		feed.setRead(false);

		feedServiceHelper.markAsRead(feed);

		assertThat("The feed should be marked as read", feed.isRead(), is(true));
	}
}
