package com.ebay.park.notification.factory.strategies;

import com.ebay.park.db.entity.User;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.notification.dto.MailNotificationMessage;
import com.ebay.park.notification.dto.MailNotificationMessage.MailNotificationBuilder;
import com.ebay.park.notification.dto.NotificationMessage;
import com.ebay.park.notification.factory.NotificationContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


public class NotificationMessageFactoryEmailStrategyTest {
	
	private static final String EMAIL_ADDRESS = "recipient@gmail.com";
	private static final String EMAIL_TEMPLATE = "email-template.vm";
	private static final String EMAIL_SUBJECT = "email-subject";

	private NotificationFactoryEmailStrategy emailStrategy;
	
	@Mock
	private NotificationContext context;
	
	private NotificationAction action = NotificationAction.DELETE_AN_ITEM;
	
	private NotificationType type = NotificationType.EMAIL;
	
	@Mock
	private User user;

	@Before
	public void setUp(){
		initMocks(this);
		emailStrategy = new NotificationFactoryEmailStrategy(context){

			@Override
			public List<NotificationMessage> createNotifications() {
				// do nothing
				return null;
			}
			
		};
		
	}
	
	@Test
	public void whenAskingForBuilderThenReturnValidBuilder(){
		
		when(context.getNotificationAction()).thenReturn(action);
		when(context.getNotificationType()).thenReturn(type);
		when(context.getUserRecipient()).thenReturn(user);
		when(user.getEmail()).thenReturn(EMAIL_ADDRESS);
		when(context.getEmailSubject()).thenReturn(EMAIL_SUBJECT);
		when(context.getEmailTemplate()).thenReturn(EMAIL_TEMPLATE);
		
		MailNotificationBuilder builder = emailStrategy.getEmailNotBuilder();
		
		assertNotNull(builder);
		
		MailNotificationMessage mailNot = builder.build();
		assertThat(action, is(mailNot.getAction()));
		assertThat(type, is(mailNot.getType()));
		assertThat(EMAIL_SUBJECT, is(mailNot.getSubject()));
		assertThat(EMAIL_ADDRESS, is(mailNot.getTo()));
		assertThat(EMAIL_TEMPLATE, is(mailNot.getTemplate()));
	}
}
