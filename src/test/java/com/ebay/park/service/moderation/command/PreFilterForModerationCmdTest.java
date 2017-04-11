package com.ebay.park.service.moderation.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;

import com.ebay.park.service.moderation.dto.SendNotificationsForModerationRequest;
import com.ebay.park.service.moderation.dto.SendNotificationsForModerationResponse;

public class PreFilterForModerationCmdTest extends AbstractFiltererCmdTest {

	private static final int USERS_RECEIVERS = 2;

	@Spy
	@InjectMocks
	private final PreFilterForModerationCmd cmd = new PreFilterForModerationCmd();

	@Override
    @Before
	public void setUp() {
		initMocks(this);
		super.setUp();
	}

	@Test
	public void givenAllPlatformRequestWhenExecutingThenGetUsersReceiversResponse() {
		SendNotificationsForModerationResponse response = cmd.execute(request1);
		assertNotNull(response);
		assertNull(response.getPushConfirmed());
		assertEquals(USERS_RECEIVERS, response.getReceivers().intValue());
	}

	@Test
	public void givenAndroidPlatformRequestWhenExecutingThenGetUsersReceiversResponse() {
		SendNotificationsForModerationResponse response = cmd.execute(request2);
		assertNotNull(response);
		assertNull(response.getPushConfirmed());
		assertEquals(USERS_RECEIVERS, response.getReceivers().intValue());
	}

	@Test
	public void givenIosPlatformRequestWhenExecutingThenGetUsersReceiversResponse() {
		SendNotificationsForModerationResponse response = cmd.execute(request3);
		assertNotNull(response);
		assertNull(response.getPushConfirmed());
		assertEquals(USERS_RECEIVERS, response.getReceivers().intValue());
	}

	@Test
	public void givenGroupIdRequestWhenExecutingThenGetUsersReceiversResponse() {
		SendNotificationsForModerationResponse response = cmd.execute(request4);
		assertNotNull(response);
		assertNull(response.getPushConfirmed());
		assertEquals(USERS_RECEIVERS, response.getReceivers().intValue());
	}
	
	@Test
	public void givenGroupIdRequestNoSessionWhenExecutingThenGetUsersReceiversResponse() {
		SendNotificationsForModerationResponse response = cmd.execute(request5);
		assertNotNull(response);
		assertNull(response.getPushConfirmed());
		assertEquals(USERS_RECEIVERS, response.getReceivers().intValue());
	}
	
	@Test
    public void givenNullSessionStatusWhenFilteringThenFilteringWithDefaultValue() {
        SendNotificationsForModerationRequest request = new SendNotificationsForModerationRequest();

        cmd.execute(request);
        assertNotNull(request.getSessionStatus());
    }

	@Test
    public void givenNullPlatformWhenFilteringThenFilteringWithDefaultValue() {
	    SendNotificationsForModerationRequest request = new SendNotificationsForModerationRequest();

        cmd.execute(request);
        assertNotNull(request.getPlatform());
    }
}
