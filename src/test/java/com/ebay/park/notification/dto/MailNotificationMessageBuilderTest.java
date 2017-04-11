package com.ebay.park.notification.dto;

import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.notification.dto.MailNotificationMessage.MailNotificationBuilder;
import com.ebay.park.service.device.dto.DeviceDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

public class MailNotificationMessageBuilderTest {

	private static final String EMAIL_ADDRESS = "recipient@gmail.com";
	private static final String EMAIL_TEMPLATE = "email-template.vm";
	private static final String EMAIL_SUBJECT = "email-subject";
	private static final String USERNAME = "username";
	
	private MailNotificationBuilder builder;
	
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

		builder = new  MailNotificationBuilder(action, 
												type, 
												EMAIL_ADDRESS,
												EMAIL_SUBJECT,
												EMAIL_TEMPLATE,
												USERNAME);
		
		MailNotificationMessage notification = builder.build();
		
		assertNotNull(notification);

		assertEquals(action, notification.getAction());
		assertEquals(type, notification.getType());
		assertEquals(EMAIL_SUBJECT, notification.getSubject());
		assertEquals(EMAIL_ADDRESS, notification.getTo());
		assertEquals(EMAIL_TEMPLATE, notification.getTemplate());
		assertEquals(USERNAME, notification.get("username"));
	}
	
}
