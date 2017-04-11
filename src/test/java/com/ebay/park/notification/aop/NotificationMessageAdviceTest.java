package com.ebay.park.notification.aop;

import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationDispatcher;
import com.ebay.park.notification.dto.NotifiableServiceResult;
import com.ebay.park.notification.dto.NotificationMessage;
import com.ebay.park.notification.factory.NotificationContext;
import com.ebay.park.notification.factory.NotificationFactory;
import com.ebay.park.service.notification.NotificationHelper;
import com.ebay.park.service.social.SocialService;
import com.ebay.park.service.user.finder.UserInfoUtil;
import org.aspectj.lang.JoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class NotificationMessageAdviceTest {

	@InjectMocks
	private NotificationAdvice notificationAdvice; 
	
	// dependencies
	@Mock
	private NotificationDispatcher notificationDispatcher;
	@Mock
	private UserInfoUtil userInfoUtil;
	@Mock
	private NotificationHelper notificationHelper;
	@Mock
	private SocialService socialService;
	
	@Mock
	private JoinPoint joinPoint;
	@Mock
	private Notifiable notifiable;
	@Mock
	private NotificationFactory notificationFactory;
	@Mock
	private NotificationAction action;
	@Mock
	private List<NotificationMessage> notificationMessages;
	@Mock
	private NotifiableServiceResult result;
	
	@Before
	public void setUp(){
		notificationAdvice = new NotificationAdvice();
		initMocks(this);
	}
	
	@Test
	public void givenValidNotificationWhenExecutingAspectThenDispatchNotification() {
		NotificationAction[] actions = {action};
		when(notifiable.action()).thenReturn(actions);
		when(action.notificationFactory(Mockito.isA(NotificationContext.class))).thenReturn(notificationFactory);
		when(notificationFactory.createNotifications()).thenReturn(notificationMessages);
		when(notificationDispatcher.dispatch(notificationMessages)).thenReturn(notificationMessages);
		
		notificationAdvice.allServiceMethodsPointcut(joinPoint, notifiable, result);

		verify(notifiable, times(2)).action();
		verify(action).notificationFactory(Mockito.isA(NotificationContext.class));
		verify(notificationFactory).createNotifications();
		verify(notificationDispatcher).dispatch(notificationMessages);
	}

	@Test
	public void givenInvalidNotificationWhenExecutingAspectThenDoNotThrownException() {
		NotificationAction[] actions = {action};
		when(notifiable.action()).thenReturn(actions);
		when(action.notificationFactory(Mockito.isA(NotificationContext.class))).thenReturn(null);

		notificationAdvice.allServiceMethodsPointcut(joinPoint, notifiable, result);
	}
		
}
