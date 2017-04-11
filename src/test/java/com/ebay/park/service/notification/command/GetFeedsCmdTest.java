package com.ebay.park.service.notification.command;

import com.ebay.park.db.dao.FeedDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Feed;
import com.ebay.park.db.entity.Idiom;
import com.ebay.park.db.entity.NotificationConfig;
import com.ebay.park.db.entity.User;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.service.notification.dto.GetFeedsRequest;
import com.ebay.park.service.notification.dto.GetFeedsResponse;
import com.ebay.park.util.InternationalizationUtil;
import com.ebay.park.util.MessageUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class GetFeedsCmdTest {

	private static final String USERNAME = "luciam";
	private static final Long USER_ID = 1l;
	private static final String FORMATED_MESSAGE = "formattedMsg";
	private static final Date DATE = new Date();

	@InjectMocks
	private GetFeedsCmd getFeedsCmd;

	@Mock
	private UserDao userDao;

	@Mock
	private FeedDao feedDao;

	@Mock
	private InternationalizationUtil i18nUtil;

	@Mock
	private User user;

	@Mock
	private Feed feed;

	@Mock
	protected MessageUtil messageUtil;

	@Before
	public void setUp(){
		getFeedsCmd = new GetFeedsCmd();
		initMocks(this);
	}

	//TODO rewrite this test
	@Test
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void test(){

		when(userDao.findByUsername(USERNAME)).thenReturn(user);
		when(user.getId()).thenReturn(USER_ID);

		List<Feed> feeds = new ArrayList<Feed>();
		feeds.add(feed);

		when(feed.getCreationDate()).thenReturn(DATE);
		NotificationConfig notTempMock = Mockito.mock(NotificationConfig.class);
		when(feed.getNotificationConfig()).thenReturn(notTempMock);
		when(notTempMock.getTemplateName()).thenReturn("notTemplate");

		NotificationAction notAction = Mockito
				.mock(NotificationAction.class);
		when(notTempMock.getNotificationAction()).thenReturn(notAction);

		Idiom idiom = Mockito.mock(Idiom.class);
		when(user.getIdiom()).thenReturn(idiom);
		when(idiom.getCode()).thenReturn("en");
		Map props = Mockito.mock(Map.class);
		when(feed.getFeedProperties()).thenReturn(props);
		when(messageUtil.formatMessage("notTemplate", props, "es")).thenReturn(FORMATED_MESSAGE);

		when(feedDao.findOrderedFeeds(USER_ID)).thenReturn(feeds);

		when(feedDao.save(feeds)).thenReturn(feeds);

		doNothing().when(i18nUtil)
		.internationalizeListedResponse(Mockito.isA(GetFeedsResponse.class), Mockito.isA(String.class), Mockito.isA(String.class));

		GetFeedsResponse response = getFeedsCmd.execute(new GetFeedsRequest(USERNAME, "token", "es"));

		assertNotNull(response);
		assertNotNull(response.getFeeds());
		assertEquals(1, response.getFeeds().size());
		assertEquals(DATE, response.getFeeds().get(0).getCreationDate());
		assertEquals(FORMATED_MESSAGE, response.getFeeds().get(0).getMessage());

		verify(userDao).findByUsername(USERNAME);
		verify(user).getId();
		verify(feed).getCreationDate();
		verify(feed, times(2)).getNotificationConfig();

		verify(messageUtil).formatMessage("notTemplate", props, "es");
		verify(feedDao).findOrderedFeeds(USER_ID);
		verify(feedDao).save(feeds);
		verify(i18nUtil).internationalizeListedResponse(Mockito.isA(GetFeedsResponse.class), Mockito.isA(String.class), Mockito.isA(String.class));
	}
}
