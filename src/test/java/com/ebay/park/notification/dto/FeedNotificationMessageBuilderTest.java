package com.ebay.park.notification.dto;

import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.notification.dto.FeedNotificationMessage.FeedNotificationBuilder;
import com.ebay.park.service.device.dto.DeviceDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

public class FeedNotificationMessageBuilderTest {

	private static final String PROPERTIES = "username:luciam";
	private static final Long USER_TO_NOTIFY = 1l;
	
	
	private FeedNotificationBuilder builder;
	
	private NotificationType type = NotificationType.EMAIL;
	
	private NotificationAction action = NotificationAction.DELETE_AN_ITEM;
	@Mock
	private DeviceDTO device;
	
	@Before
	public void setUp(){
		initMocks(this);
	}
	
	@Test
	public void test(){

		builder = new  FeedNotificationBuilder(action, 
												type, 
												PROPERTIES,
												USER_TO_NOTIFY);
		
		FeedNotificationMessage notification = builder.build();
		
		assertNotNull(notification);

		assertEquals(action, notification.getAction());
		assertEquals(type, notification.getType());
		assertEquals(PROPERTIES, notification.getFeedProperties());
		assertEquals(USER_TO_NOTIFY, notification.getUserToNotify());
	}
	
}
