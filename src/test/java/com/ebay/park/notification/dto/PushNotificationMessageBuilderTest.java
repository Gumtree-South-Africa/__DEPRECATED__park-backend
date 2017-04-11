package com.ebay.park.notification.dto;

import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.notification.dto.PushNotificationMessage.PushNotificationBuilder;
import com.ebay.park.service.device.dto.DeviceDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class PushNotificationMessageBuilderTest {

	private static final String TEMPLATE_MSG = "templateMsg";
	private static final String DEVICE_ID = "123456";
	private static final String DEVICE_TYPE = "ios";
	private static final String BADGE = "1";
	
	private PushNotificationBuilder builder;
	
	private NotificationType type = NotificationType.EMAIL;
	
	private NotificationAction action = NotificationAction.DELETE_AN_ITEM;
	@Mock
	private DeviceDTO device;
	
	@Before
	public void setUp() {
		initMocks(this);
	}
	
	@Test
	public void test(){
		when(device.getDeviceId()).thenReturn(DEVICE_ID);
		when(device.getDeviceType()).thenReturn(DEVICE_TYPE);
		builder = new  PushNotificationBuilder(action, type, device, TEMPLATE_MSG, BADGE);
		
		PushNotificationMessage notification = builder.build();
		
		assertNotNull(notification);
		assertEquals(action, notification.getAction());
		assertEquals(type, notification.getType());
		assertEquals(TEMPLATE_MSG, notification.getTemplateMsg());
		assertEquals(DEVICE_ID, notification.getDeviceId());
		assertEquals(DEVICE_TYPE, notification.getDeviceType());
		
		verify(device, times(1)).getDeviceId();
		verify(device, times(1)).getDeviceType();
	}
	
}
