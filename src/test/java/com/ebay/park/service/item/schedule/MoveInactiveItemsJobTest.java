package com.ebay.park.service.item.schedule;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.event.item.ItemNotificationEvent;

public class MoveInactiveItemsJobTest {

	private static final int DAYS_TO_ITEM_EXPIRE = 180;
	private static final int BATCH_SIZE = 3;
	private static final int DAY_TO_NOTIFY = 3;
	private static final String ITEM_NAME = "name";
	private static final Double ITEM_PRICE = 20.5;
	private static final String ITEM_VERSION_PUBLISH = "versionPublish";
	private Page<Item> itemsToExpire;
	private Item item1;
	private Item item2;

	@InjectMocks
	private MoveInactiveItemsJob moveInactiveItemsJob = new MoveInactiveItemsJob();

	@Mock
	private ItemDao itemDao;

	@Mock
	InactiveItemsHelper inactiveItemsHelper;

	@Before
	public void setUp() {
		initMocks(this);
		ReflectionTestUtils.setField(moveInactiveItemsJob, "daysToItemExpire", DAYS_TO_ITEM_EXPIRE);
		ReflectionTestUtils.setField(moveInactiveItemsJob, "batchSize", BATCH_SIZE);
		ReflectionTestUtils.setField(moveInactiveItemsJob, "dayToNotify", DAY_TO_NOTIFY);
		// Create the list of items.
		List<Item> items = new ArrayList<Item>();
		item1 = new Item(ITEM_NAME, ITEM_PRICE, ITEM_VERSION_PUBLISH, Boolean.FALSE, Boolean.FALSE);
		User user = Mockito.mock(User.class);
		List<Conversation> conversations = new ArrayList<Conversation>();
		item1.setPublishedBy(user);
		item1.setOpenConversations(conversations);
		item1.activate();
		items.add(item1);
		item2 = new Item(ITEM_NAME, ITEM_PRICE, ITEM_VERSION_PUBLISH, Boolean.FALSE, Boolean.FALSE);
		item2.setPublishedBy(user);
		item2.setOpenConversations(conversations);
		item2.activate();
		items.add(item2);
		itemsToExpire = new PageImpl<Item>(items);

		when(itemDao.listElementsToExpire(Mockito.any(StatusDescription.class), Mockito.any(Date.class),
				Mockito.any(PageRequest.class))).thenReturn(itemsToExpire)
						.thenReturn(new PageImpl<Item>(new ArrayList<Item>()));

		when(itemDao.listElementsCloseToExpire(Mockito.any(StatusDescription.class), Mockito.any(Date.class),
				Mockito.any(Date.class), Mockito.any(PageRequest.class))).thenReturn(itemsToExpire)
						.thenReturn(new PageImpl<Item>(new ArrayList<Item>()));

		when(inactiveItemsHelper.notifyExpired(Mockito.any(Item.class)))
				.thenReturn(Mockito.mock(ItemNotificationEvent.class));
	}

	@Test
	public void givenListElementsToExpireThenInactiveItemsSuccess() {
		// verify items status before the job execution
		assertEquals(StatusDescription.ACTIVE, item1.getStatus());
		assertEquals(StatusDescription.ACTIVE, item2.getStatus());
		moveInactiveItemsJob.execute();
		verify(itemDao, times(2)).listElementsToExpire(Mockito.any(StatusDescription.class), Mockito.any(Date.class),
				Mockito.any(PageRequest.class));
		verify(itemDao, times(2)).listElementsCloseToExpire(Mockito.any(StatusDescription.class),
				Mockito.any(Date.class), Mockito.any(Date.class), Mockito.any(PageRequest.class));
		// after the job execution the status should be EXPIRED.
		assertEquals(StatusDescription.EXPIRED, item1.getStatus());
		assertEquals(StatusDescription.EXPIRED, item2.getStatus());
	}

}
