package com.ebay.park.notification.factory.strategies;

import com.ebay.park.db.entity.User;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.notification.dto.NotificationMessage;
import com.ebay.park.notification.dto.PushNotificationMessage.PushNotificationBuilder;
import com.ebay.park.notification.factory.NotificationContext;
import com.ebay.park.service.device.dto.DeviceDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class NotificationMessageFactoryPushStrategyTest {

	private static final String TEMPLATE_MSG = "templateMsg";

	private NotificationFactoryPushStrategy pushStrategy;
	
	@Mock
	private NotificationContext context;
	
	private NotificationAction action = NotificationAction.DELETE_AN_ITEM;
	
	private NotificationType type = NotificationType.EMAIL;
	
	@Mock
	private User user;
	
	@Mock
	private DeviceDTO device;
	
	@Before
	public void setUp(){
		initMocks(this);
		pushStrategy = new NotificationFactoryPushStrategy(context){

			@Override
			public List<NotificationMessage> createNotifications() {
				// do nothing
				return null;
			}
			
		};
		
	}
	
	@Test
	public void test(){ //TODO change test name
		
		when(context.getNotificationAction()).thenReturn(action);
		when(context.getNotificationType()).thenReturn(type);
//		when(context.getRecipient()).thenReturn(user);
//		when(context.getDevice(user)).thenReturn(device);
		when(context.getTemplateMsg()).thenReturn(TEMPLATE_MSG);
		
		PushNotificationBuilder builder = pushStrategy.getPushNotBuilder();
		
		assertNotNull(builder);
		
		verify(context).getNotificationAction();
		verify(context).getNotificationType();
//		verify(context, times(1)).getRecipient();
//		verify(context, times(1)).getDevice(user);
		
	}
	
}
