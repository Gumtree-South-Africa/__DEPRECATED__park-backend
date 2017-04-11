package com.ebay.park.notification.factory.strategies;

import com.ebay.park.db.entity.User;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.notification.dto.FeedNotificationMessage.FeedNotificationBuilder;
import com.ebay.park.notification.dto.NotificationMessage;
import com.ebay.park.notification.factory.NotificationContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class NotificationMessageFactoryFeedStrategyTest {

	private static final Long USER_ID = 1L;

	private NotificationFactoryFeedStrategy feedStrategy;
	
	@Mock
	private NotificationContext context;
	
	private NotificationAction action = NotificationAction.DELETE_AN_ITEM;
	
	private NotificationType type = NotificationType.EMAIL;
	
	@Mock
	private User user;

	@Before
	public void setUp(){
		initMocks(this);
		feedStrategy = new NotificationFactoryFeedStrategy(context){

			@Override
			protected String getFeedProperties() {
				return "username:luciamasola";
			}

			@Override
			public List<NotificationMessage> createNotifications() {
				return null;
			}
			
		};
		
	}
	
	@Test
	public void test(){ //TODO change test name
		when(context.getNotificationAction()).thenReturn(action);
		when(context.getNotificationType()).thenReturn(type);
		when(context.getUserRecipient()).thenReturn(user);
		when(user.getId()).thenReturn(USER_ID);

		FeedNotificationBuilder builder = feedStrategy.getFeedNotBuilder();
		
		assertNotNull(builder);
		
		verify(context).getNotificationAction();
		verify(context).getNotificationType();
		verify(context).getUserRecipient();
		verify(user).getId();
		
	}
	
}
