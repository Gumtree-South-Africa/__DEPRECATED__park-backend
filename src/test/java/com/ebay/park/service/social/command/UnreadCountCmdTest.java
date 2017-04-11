package com.ebay.park.service.social.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ebay.park.service.group.GroupService;
import com.ebay.park.service.notification.FeedServiceHelper;
import com.ebay.park.service.social.dto.UnreadCountRequest;
import com.ebay.park.service.social.dto.UnreadCountResponse;

public class UnreadCountCmdTest {

	private static final String TOKEN = "validToken";

	@InjectMocks
	private UnreadCountCmd unreadCountCmd;

	@Mock
	private FeedServiceHelper feedServiceHelper;

	@Mock
	private GroupService groupService;

	@Before
	public void setUp() {
		unreadCountCmd = new UnreadCountCmd();
		initMocks(this);
	}

	@Test
	public void testExecuteSuccess() {
		// given
		UnreadCountRequest request = Mockito.mock(UnreadCountRequest.class);
		Mockito.when(request.getToken()).thenReturn(TOKEN);
		Mockito.when(feedServiceHelper.countUnreadFeeds(TOKEN)).thenReturn(1l);
		Mockito.when(groupService.countUnreadGroupItems(TOKEN)).thenReturn(2l);
		// when
		UnreadCountResponse response = unreadCountCmd.execute(request);
		// then
		assertNotNull(response);
		assertEquals(Long.valueOf(1l), response.getUnreadFeeds());
		assertEquals(Long.valueOf(2l), response.getUnreadGroupItems());
		Mockito.verify(feedServiceHelper, Mockito.times(1)).countUnreadFeeds(TOKEN);
		Mockito.verify(groupService, Mockito.times(1)).countUnreadGroupItems(TOKEN);
	}

}
