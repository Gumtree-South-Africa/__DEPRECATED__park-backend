package com.ebay.park.notification.factory.item;

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
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.event.item.ItemNotificationEvent;
import com.ebay.park.notification.factory.NotificationContext;

public class ItemOwnerNotificationMessageFactoryTest {


	private ItemOwnerNotificationFactory factory;

	@Mock
	private NotificationContext context;

	@Mock
	private ItemNotificationEvent event;

	@Mock
	private Item item;

	@Mock
	private User user;

	@Mock
	private Device device;

	@Before
	public void setUp() {
		initMocks(this);
		factory = new ItemOwnerNotificationFactory(context);
	}

	@Test
	public void getRecipientsTest() {

		when(context.getNotifiableResult()).thenReturn(event);

		when(event.getItem()).thenReturn(item);
		when(item.getPublishedBy()).thenReturn(user);

		List<User> usersToNotify = factory.getRecipients();

		assertNotNull(usersToNotify);
		assertEquals(1, usersToNotify.size());
		assertEquals(user, usersToNotify.get(0));

		verify(event).getItem();
		verify(item).getPublishedBy();
		verify(context).getNotifiableResult();
	}

}
