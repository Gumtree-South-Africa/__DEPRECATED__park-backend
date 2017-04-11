package com.ebay.park.notification.factory.conversation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import com.ebay.park.db.entity.Device;
import com.ebay.park.db.entity.User;
import com.ebay.park.event.conversation.ChatSentEvent;
import com.ebay.park.notification.factory.NotificationContext;

public class ChatSentNotificationMessageFactoryTest {

	private static final Long USER_ID = new Long(1);

	private ChatSentNotificationFactory factory;

	@Mock
	private NotificationContext context;

	@Mock
	private ChatSentEvent event;

	@Mock
	private User user;
	
	@Mock
	private Device device;

	@Before
	public void setUp() {
		initMocks(this);
		factory = new ChatSentNotificationFactory(context);
	}

	@Test
	public void getRecipientsTest() {

		when(context.getNotifiableResult()).thenReturn(event);
		when(event.getReceiverId()).thenReturn(USER_ID);
		when(context.findUserById(USER_ID)).thenReturn(user);

		List<User> usersToNotify = factory.getRecipients();

		assertNotNull(usersToNotify);
		assertEquals(1, usersToNotify.size());
		assertEquals(user, usersToNotify.get(0));

		verify(event).getReceiverId();
		verify(context).findUserById(USER_ID);
		verify(context).getNotifiableResult();
	}

}
