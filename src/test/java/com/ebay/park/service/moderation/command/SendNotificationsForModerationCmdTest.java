package com.ebay.park.service.moderation.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.ebay.park.feed.FeedBulkExecutor;
import com.ebay.park.push.AndroidPushBatchExecutor;
import com.ebay.park.push.IOSPushBatchExecutor;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.moderation.dto.SendNotificationsForModerationResponse;
import com.ebay.park.util.UserUtils;

public class SendNotificationsForModerationCmdTest extends AbstractFiltererCmdTest {

	private static final int USERS_RECEIVERS = 2; 
	private static final int PUSHCONFIRMED = 0;

	@Spy
	@InjectMocks
	private final SendNotificationsForModerationCmd cmd = new SendNotificationsForModerationCmd();

	@Mock
	private UserUtils userUtils;

	@Mock
	private IOSPushBatchExecutor iOSPushExecutor;

	@Mock
	private AndroidPushBatchExecutor androidPushExecutor;

	@Mock
    private FeedBulkExecutor feedExecutor;

	@Override
    @Before
	public void setUp() {
		initMocks(this);
		super.setUp();
	}

	@Test
	public void givenValidRequestWhenExecutingThenGetSuccessfulResponse(){
		SendNotificationsForModerationResponse response = cmd.execute(request1);
		assertNotNull(response);
		assertEquals(USERS_RECEIVERS, response.getReceivers().intValue());
		assertEquals(PUSHCONFIRMED, response.getPushConfirmed().intValue());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void givenUnsuccessfulIOSPushedWhenExecutingThenGetError(){
		Mockito.when(iOSPushExecutor.getSuccessfulPushes()).thenThrow(ServiceException.class);
		try {
			cmd.execute(request1);
			fail(MSG2);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.ERROR_SENDING_PUSH_NOTIFICATIONS.getCode(), e.getCode());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void givenUnsuccessfulAndroidPushedWhenExecutingThenGetError(){
		Mockito.when(androidPushExecutor.getSuccessfulPushes()).thenThrow(ServiceException.class);
		try {
			cmd.execute(request1);
			fail(MSG2);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.ERROR_SENDING_PUSH_NOTIFICATIONS.getCode(), e.getCode());
		}
	}

	@Test
	public void givenRequestFilterByGroupIdWhenExecutingThenGetSuccessfulResponse(){
		SendNotificationsForModerationResponse response = cmd.execute(request4);
		assertNotNull(response);
		assertEquals(USERS_RECEIVERS, response.getReceivers().intValue());
		assertEquals(PUSHCONFIRMED, response.getPushConfirmed().intValue());
	}

	@Test
	public void givenRequestFilterByGroupIdNoSessionWhenExecutingThenGetSuccessfulResponse(){
		SendNotificationsForModerationResponse response = cmd.execute(request5);
		assertNotNull(response);
		assertEquals(USERS_RECEIVERS, response.getReceivers().intValue());
		assertEquals(PUSHCONFIRMED, response.getPushConfirmed().intValue());
	}

}
