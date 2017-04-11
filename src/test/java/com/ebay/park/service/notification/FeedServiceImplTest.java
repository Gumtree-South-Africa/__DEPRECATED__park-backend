package com.ebay.park.service.notification;

import com.ebay.park.service.notification.command.GetFeedsCmd;
import com.ebay.park.service.notification.command.GetFeedsV3Cmd;
import com.ebay.park.service.notification.command.UpdateNotificationConfigCmd;
import com.ebay.park.service.notification.dto.GetFeedsRequest;
import com.ebay.park.service.notification.dto.GetFeedsResponse;
import com.ebay.park.service.notification.dto.GetUnreadFeedsCounterRequest;
import com.ebay.park.service.notification.dto.GetUnreadFeedsCounterResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FeedServiceImplTest {

	private static final String ES = "es";

    private static final String TOKEN = "token";

    private static final String USERNAME = "luciam";
    
    private static final Long UNREAD_FEEDS_COUNTER = 3L;

	@InjectMocks
	@Spy
	private FeedServiceImpl feedServiceImpl;

	@Mock
	private GetFeedsV3Cmd getFeedsV3Cmd;

	@Mock
    private GetFeedsCmd getFeedsV4Cmd;

	@Mock
	private NotificationHelper notificationHelper;

	@Mock
	private UpdateNotificationConfigCmd updateNotConfigCmd;
	
	@Mock
	private FeedServiceHelper feedServiceHelper;

	@Before
	public void setUp(){
		feedServiceImpl = new FeedServiceImpl();
		initMocks(this);
	}

	@Test
	public void getFeedsHappyPathTestV3() {

		GetFeedsResponse feeds = Mockito.mock(GetFeedsResponse.class);
		when(getFeedsV3Cmd.execute(new GetFeedsRequest(USERNAME, TOKEN, ES))).thenReturn(feeds);

		GetFeedsResponse response = feedServiceImpl.getFeedsV3(new GetFeedsRequest(USERNAME, TOKEN, ES));

		assertNotNull(response);
		assertEquals(feeds, response);

	}

	@Test
    public void getFeedsHappyPathTestV4() {

        GetFeedsResponse feeds = Mockito.mock(GetFeedsResponse.class);
        when(getFeedsV4Cmd.execute(new GetFeedsRequest(USERNAME, TOKEN, ES))).thenReturn(feeds);

        GetFeedsResponse response = feedServiceImpl.getFeedsV4(new GetFeedsRequest(USERNAME, TOKEN, ES));

        assertNotNull(response);
        assertEquals(feeds, response);

    }
	
	@Test(expected=IllegalArgumentException.class)
	public void givenANullRequestWhenCountUnreadFeedsThenIllegalAgumentException() {
		feedServiceImpl.countUnreadFeeds(null);
	}
	
	@Test
	public void givenAValidTokenWhenCountUnreadFeedsThenSuccess() {
		//given
		GetUnreadFeedsCounterRequest request = new GetUnreadFeedsCounterRequest(TOKEN, ES);
		when(feedServiceHelper.countUnreadFeeds(TOKEN)).thenReturn(UNREAD_FEEDS_COUNTER);
		
		//when
		GetUnreadFeedsCounterResponse response = feedServiceImpl.countUnreadFeeds(request);
		
		//then
		assertNotNull(response);
		assertNotNull(response.getUnreadFeedsCounter());
		assertEquals(UNREAD_FEEDS_COUNTER, response.getUnreadFeedsCounter());
	}
}
