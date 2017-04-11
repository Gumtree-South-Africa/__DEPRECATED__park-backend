package com.ebay.park.notification.factory.item;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.ebay.park.db.entity.Device;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserFollowsItem;
import com.ebay.park.event.item.ItemNotificationEvent;
import com.ebay.park.notification.factory.NotificationContext;

public class ItemOwnerAndFollowersNotificationMessageFactoryTest {

	private ItemOwnerAndFollowersNotificationFactory factory;

	@Mock
	private NotificationContext context;

	@Mock
	private ItemNotificationEvent event;

	@Mock
	private Item item;

	@Mock
	private User userFollower;
	
	@Mock
	private User owner;
	
	@Mock
	private UserFollowsItem follower;
	
	@Mock
	private Device device1, device2;

	@Before
	public void setUp() {
		initMocks(this);
		factory = new ItemOwnerAndFollowersNotificationFactory(context);
	}

	@Test
	public void getRecipientsTest() {

		when(context.getNotifiableResult()).thenReturn(event);
		
		when(event.getItem()).thenReturn(item);
		
		when(item.getPublishedBy()).thenReturn(owner);
		
		when(item.getFollowers()).thenReturn(Arrays.asList(follower));
		when(follower.getUser()).thenReturn(userFollower);

		List<User> usersToNotify = factory.getRecipients();

		assertNotNull(usersToNotify);
		assertEquals(2, usersToNotify.size());

		verify(event, times(2)).getItem();
		verify(item).getPublishedBy();
		verify(item).getFollowers();
		verify(follower).getUser();
		verify(context).getNotifiableResult();
	}

}
