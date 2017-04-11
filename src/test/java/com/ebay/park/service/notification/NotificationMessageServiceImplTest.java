package com.ebay.park.service.notification;

import com.ebay.park.service.notification.command.UpdateNotificationConfigCmd;
import com.ebay.park.service.notification.dto.NotificationConfigRequest;
import com.ebay.park.service.notification.validator.NotificationRequestValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

public class NotificationMessageServiceImplTest {

	@InjectMocks
	private NotificationService notificationService;

	@Mock
	private NotificationRequestValidator notificationReqValidator;
	
	@Mock
	private UpdateNotificationConfigCmd updateNotConfigCmd;
		
	@Before
	public void setUp(){
		notificationService = new NotificationServiceImpl();
		initMocks(this);
	}
	
	@Test
	public void updatePushNotificationsConfigTest(){
		NotificationConfigRequest request = new NotificationConfigRequest();
		Mockito.doNothing().when(notificationReqValidator).validate(request);
		Mockito.when(updateNotConfigCmd.execute(request)).thenReturn(true);

		Boolean response = notificationService.updateNotificationsConfig(request);

		assertTrue(response);
		Mockito.verify(notificationReqValidator, Mockito.times(1)).validate(request);
		Mockito.verify(updateNotConfigCmd, Mockito.times(1)).execute(request);
	}
	
}
