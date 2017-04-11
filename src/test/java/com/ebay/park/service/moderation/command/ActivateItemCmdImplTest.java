package com.ebay.park.service.moderation.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.event.item.ItemNotificationEvent;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.moderation.dto.ItemRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author giriarte
 *
 */
public class ActivateItemCmdImplTest {

	private static final Long ITEM_ID = 1L;

	@InjectMocks
	private ActivateItemCmdImpl activateItemCmdImpl = new ActivateItemCmdImpl();
	
	@Mock
	private ItemDao itemDao;
	
	@Mock
	private ItemUtils itemUtils;
	
	Item item;
	
	ItemRequest request;
	
	@Before
	public void setUp(){
		initMocks(this);
		request = new ItemRequest(ITEM_ID);
		
		item = new Item("itName", 345D, "v1.0", false,false);
		item.setPublishedBy(new User());
		item.setOpenConversations(new ArrayList<Conversation>());
		
		when(itemDao.findOne(ITEM_ID)).thenReturn(item);
		when(itemUtils.activateItem(item)).thenReturn(new ItemNotificationEvent(item));
	}
	
	@Test
	public void activatePendingItem(){
		item.ban();
		activateItemCmdImpl.execute(request);
	}
	
	@Test
	public void itemToActivateNotFound(){
		request.setItemId(111L);
		try{
			activateItemCmdImpl.execute(request);
		} catch(ServiceException e) {
			assertEquals(ServiceExceptionCode.ITEM_NOT_FOUND.getCode(), e.getCode());
			return;
		}
		fail();
	}
	
	@Test
	public void itemToActivateExpired(){
		item.expired();
		try{
			activateItemCmdImpl.execute(request);
		} catch(ServiceException e) {
			assertEquals(ServiceExceptionCode.ITEM_EXPIRED.getCode(), e.getCode());
			return;
		}
		fail();
	}
	
	@Test
	public void itemToActivateNotPending(){
		item.sold();
		try{
			activateItemCmdImpl.execute(request);
		} catch(ServiceException e) {
			assertEquals(ServiceExceptionCode.MODERATION_ITEM_NOT_PENDING.getCode(), e.getCode());
			return;
		}
		fail();
	}
	
}
