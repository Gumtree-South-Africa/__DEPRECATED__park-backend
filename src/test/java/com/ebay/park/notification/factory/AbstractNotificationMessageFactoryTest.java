package com.ebay.park.notification.factory;

import com.ebay.park.db.entity.Device;
import com.ebay.park.db.entity.NotificationConfig;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.notification.dto.NotificationMessage;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AbstractNotificationMessageFactoryTest {

	private AbstractNotificationFactory factory;

	@Mock
	private NotificationContext context;
	
	@Mock
	private User user;

	private NotificationAction action = NotificationAction.DELETE_AN_ITEM;

	@Mock
	private NotificationConfig notConfig;

	@Mock
	private NotificationFactoryStrategy feed_strategy;

	@Mock
	private NotificationFactoryStrategy email_strategy;

	@Mock
	private NotificationFactoryStrategy push_strategy;

	@Mock
	private NotificationMessage notification;

	@Mock
	private UserSession session;

	@Mock
	private Device device;

	@Before
	public void setUp(){
		initMocks(this);
		factory = new AbstractNotificationFactory(context){

			@Override
			public List<User> getRecipients() {
				List<User> users = new ArrayList<User>();
				users.add(user);
				return  users;
			}

		};

		setUserNotificationConfig();
		setFactories();

	}

	private void setUserNotificationConfig() {
		when(context.getNotificationAction()).thenReturn(action);
		when(notConfig.getNotificationAction()).thenReturn(action);
		when(user.getNotificationConfigs()).thenReturn(Arrays.asList(new NotificationConfig[]{notConfig}));
	}

	private void setFactories() {
		factory.putIntoNotificationFactories(NotificationType.FEED, feed_strategy);
		factory.putIntoNotificationFactories(NotificationType.PUSH, push_strategy);
		factory.putIntoNotificationFactories(NotificationType.EMAIL, email_strategy);
	}

	@Test
	public void givenPendingFeedWhenCreatingNotificationsThenCreate(){
		when(notConfig.getNotificationType()).thenReturn(NotificationType.FEED);

		List<NotificationMessage> expNotifications = Arrays.asList(notification);
		when(feed_strategy.createNotifications()).thenReturn(expNotifications);

		//when
		List<NotificationMessage> notifications = factory.createNotifications();

		assertEquals(expNotifications, notifications);
		verify(notConfig).getNotificationAction();
		verify(notConfig).getNotificationType();
		verify(user).getNotificationConfigs();
	}

	@Test
	public void givenPendingEmailWhenCreatingNotificationsThenCreate(){
		when(notConfig.getNotificationType()).thenReturn(NotificationType.EMAIL);
		List<NotificationMessage> expNotifications = Arrays.asList(notification);
		when(email_strategy.createNotifications()).thenReturn(expNotifications);

		//when
		List<NotificationMessage> notifications = factory.createNotifications();

		assertThat(notifications, is(expNotifications));
		verify(notConfig).getNotificationAction();
		verify(notConfig).getNotificationType();
		verify(user).getNotificationConfigs();
	}

	@Test
	public void givenPendingPushWhenCreatingNotificationsThenCreate(){
		when(notConfig.getNotificationType()).thenReturn(NotificationType.PUSH);

		List<NotificationMessage> expNotifications = Arrays.asList(notification);

		when(push_strategy.createNotifications()).thenReturn(expNotifications);
		when(session.isSessionActive()).thenReturn(true);
		when(user.getUserSessions()).thenReturn(Collections.singletonList(session));
		when(session.getDevice()).thenReturn(device);
		when(user.isNotificationEnabled(action, NotificationType.PUSH)).thenReturn(true);

		//when
		List<NotificationMessage> notifications = factory.createNotifications();

		assertThat(notifications, is(expNotifications));
		verify(notConfig).getNotificationAction();
		verify(notConfig).getNotificationType();
		verify(user).getNotificationConfigs();
	}

	@Test
	public void givenPendingPushAndInactiveSessionWhenCreatingNotificationsThenDoNotCreate(){
		when(notConfig.getNotificationType()).thenReturn(NotificationType.PUSH);

		List<NotificationMessage> expNotifications = Arrays.asList(notification);

		when(session.isSessionActive()).thenReturn(false);
		when(user.getUserSessions()).thenReturn(Collections.singletonList(session));
		when(session.getDevice()).thenReturn(device);
		when(user.isNotificationEnabled(action, NotificationType.PUSH)).thenReturn(true);

		//when
		List<NotificationMessage> notifications = factory.createNotifications();

		assertThat(notifications.size(), is(0));
		verify(notConfig).getNotificationAction();
		verify(notConfig).getNotificationType();
		verify(user).getNotificationConfigs();
		verify(push_strategy, never()).createNotifications();
	}

	@Test
	public void givenPendingPushAndNullDeviceWhenCreatingNotificationsThenCreate(){
		when(notConfig.getNotificationType()).thenReturn(NotificationType.PUSH);
		List<NotificationMessage> expNotifications = Arrays.asList(notification);

		when(push_strategy.createNotifications()).thenReturn(expNotifications);
		when(session.isSessionActive()).thenReturn(true);
		when(user.getUserSessions()).thenReturn(Collections.singletonList(session));
		when(session.getDevice()).thenReturn(null);
		when(user.isNotificationEnabled(action, NotificationType.PUSH)).thenReturn(true);

		//when
		List<NotificationMessage> notifications = factory.createNotifications();

		assertThat(notifications.size(), is(0));
		verify(notConfig).getNotificationAction();
		verify(notConfig).getNotificationType();
		verify(user).getNotificationConfigs();
		verify(push_strategy, never()).createNotifications();
	}

	@Test
	public void givenPendingPushAndUserNotEnablesWhenCreatingNotificationsThenCreate(){
		when(notConfig.getNotificationType()).thenReturn(NotificationType.PUSH);

		List<NotificationMessage> expNotifications = Arrays.asList(notification);

		when(push_strategy.createNotifications()).thenReturn(expNotifications);
		when(session.isSessionActive()).thenReturn(true);
		when(user.getUserSessions()).thenReturn(Collections.singletonList(session));
		when(session.getDevice()).thenReturn(device);
		when(user.isNotificationEnabled(action, NotificationType.PUSH)).thenReturn(false);

		//when
		List<NotificationMessage> notifications = factory.createNotifications();

		assertThat(notifications.size(), is(0));
		verify(notConfig).getNotificationAction();
		verify(notConfig).getNotificationType();
		verify(user).getNotificationConfigs();
		verify(push_strategy, never()).createNotifications();
	}
}
