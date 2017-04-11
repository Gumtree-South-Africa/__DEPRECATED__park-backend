package com.ebay.park.push;

import com.ebay.park.util.MessageUtil;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class NotificationMessagePusherTest {

	private static final String TEMPLATE_MSG = null;

	private static final String MSG_TO_SEND = null;
	public static final String LOCALE = "locale";
	public static final String EN = "en";

	@InjectMocks
	private NotificationPusher notificationPusher;
	
	@Mock
	private PushNotification notification;
	
	@Mock
	private MessageUtil messageUtil;
	
	@Before
	public void setUp(){
		notificationPusher = new NotificationPusher(){

			@Override
			public void push(PushNotification notification) {
				// do nothing
				
			}
			
		};
		initMocks(this);
	}
	
	@Test
	@Ignore
	public void whenCreatingMessageThenPush(){

		when(notification.getTemplateMessage()).thenReturn(TEMPLATE_MSG);
		@SuppressWarnings("unchecked")
		Map<String, String> params = mock(Map.class);
		when(notification.getParams()).thenReturn(params);
		when(params.get(LOCALE)).thenReturn(EN);
		when(messageUtil.formatMessage(TEMPLATE_MSG, params, EN)).thenReturn(MSG_TO_SEND);
		
		String message = notificationPusher.createMessage(notification);
		
		assertThat(MSG_TO_SEND, is(message));
		
		verify(notification).getTemplateMessage();
		verify(notification, Mockito.times(2)).getParams();
		verify(params).get(LOCALE);
		verify(messageUtil).formatMessage(TEMPLATE_MSG, params, EN);
	}
	
}
