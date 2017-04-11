package com.ebay.park.service.moderationMode.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.moderationMode.dto.GetUserItemsForModerationModeRequest;
import com.ebay.park.service.moderationMode.dto.GetUserItemsForModerationModeResponse;
import com.ebay.park.service.moderationMode.dto.ItemSummaryForModeration;

public class GetUserItemsForModerationModeCmdTest {
	
	private static final long USER_ID = 1l;
	private static final long ITEM_ID_EXCLUDED = 7l;
	private static final String ITEM_NAME1 = null;
	private static final Double PRICE = null;
	private static final String VERSION = null;
	private static final boolean FB = false;
	private static final boolean TW = false;
	private static final long ITEM_ID1 = 1L;
	private static final long ITEM_ID2 = 2L;
	private static final long ITEM_ID3 = 3L;
	private static final long ITEM_ID4 = 4L;
	private static final long ITEM_ID5 = 5L;
	private static final long ITEM_ID6 = 6L;
	private static final String MSG2 = "The user was not found";
	private static final String TOKEN = "123456";
	private static final String LANG = "es";
    private static final int VALID_PAGE = 0;
    private static final int VALID_PAGE_SIZE = 3;
    private static final Object DEFAULT_PAGE_SIZE = 2;

	@InjectMocks
	private GetUserItemsForModerationModeCmd cmd = new GetUserItemsForModerationModeCmdImpl();
	
	@Mock
	private ItemDao itemDao;
	
	@Mock
	private UserDao userDao;
	
	private List<Item> items;
	private Page<Item> itemsPage;
	
	private User user1;
	
	@Mock
	private ItemUtils itemUtils;
	
	List<ItemSummaryForModeration> itemSummaryList;
	
	@Before
	public void setUp(){
		initMocks(this);

		ReflectionTestUtils.setField(cmd, "defaultPageSize", DEFAULT_PAGE_SIZE);

		//list of users
		user1 = Mockito.mock(User.class);
		user1.setId(USER_ID);
		
		//list of items
		Item item1 = new Item(ITEM_NAME1, PRICE, VERSION, FB, TW);
		item1.setId(ITEM_ID1);
		item1.setPublishedBy(user1);
		
		Item item2 = new Item(ITEM_NAME1, PRICE, VERSION, FB, TW);
		item2.setId(ITEM_ID2);
		item2.setPublishedBy(user1);
		
		Item item3 = new Item(ITEM_NAME1, PRICE, VERSION, FB, TW);
		item3.setId(ITEM_ID3);
		item3.setPublishedBy(user1);
		
		Item item4 = new Item(ITEM_NAME1, PRICE, VERSION, FB, TW);
		item4.setId(ITEM_ID4);
		item4.setPublishedBy(user1);
		
		Item item5 = new Item(ITEM_NAME1, PRICE, VERSION, FB, TW);
		item5.setId(ITEM_ID5);
		item5.setPublishedBy(user1);
		
		Item item6 = new Item(ITEM_NAME1, PRICE, VERSION, FB, TW);
		item6.setId(ITEM_ID6);
		item6.setPublishedBy(user1);

		items = Arrays.asList(item1, item2, item3, item4, item5, item6);
		
		itemSummaryList = Arrays.asList(
				ItemSummaryForModeration.fromItem(item1),
				ItemSummaryForModeration.fromItem(item2),
				ItemSummaryForModeration.fromItem(item4),
				ItemSummaryForModeration.fromItem(item5),
				ItemSummaryForModeration.fromItem(item6));
		when(itemUtils.convertToItemSummaryForModeration(items, LANG)).thenReturn(itemSummaryList);
	}
	
	@Test
	public void givenValidDataWhenExecutingThenReturnUserItemList() {
		GetUserItemsForModerationModeRequest request = new GetUserItemsForModerationModeRequest(TOKEN, LANG,
				USER_ID, ITEM_ID_EXCLUDED, null, null);
		StatusDescription[] itemsInStatus = {StatusDescription.ACTIVE};	
		when(itemDao.listItemsFromUserId(USER_ID, ITEM_ID_EXCLUDED, itemsInStatus)).thenReturn(items);
		when(userDao.findOne(USER_ID)).thenReturn(user1);
		
		GetUserItemsForModerationModeResponse response = cmd.execute(request);

		assertEquals(items.size() - 1 , response.getItems().size());
	}
	
	@Test
	public void givenInvalidUserWhenExecutingThenException() {
		GetUserItemsForModerationModeRequest request = new GetUserItemsForModerationModeRequest(
				TOKEN, LANG, USER_ID, ITEM_ID_EXCLUDED, null, null); 
		when(userDao.findOne(USER_ID)).thenReturn(null);
		
		try {
			cmd.execute(request);
			fail(MSG2);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.USER_NOT_FOUND.getCode(), e.getCode());
		}
	}

	@Test
    public void givenPageAndPageSizeWhenExecutingThenReturnUserItemListPage() {
	    GetUserItemsForModerationModeRequest request = new GetUserItemsForModerationModeRequest(TOKEN, LANG,
                USER_ID, ITEM_ID_EXCLUDED, VALID_PAGE, VALID_PAGE_SIZE);

        PageRequest pageRequest = new PageRequest(VALID_PAGE, VALID_PAGE_SIZE);
        itemsPage = new PageImpl<Item>(items, pageRequest, items.size());
        when(itemDao.listItemsFromUserId(anyLong(), anyLong(), any(),
                any())).thenReturn(itemsPage);
        when(userDao.findOne(USER_ID)).thenReturn(user1);
        List<ItemSummaryForModeration> itemSummaryPage = new ArrayList<ItemSummaryForModeration>();
        
        for (Item item : itemsPage.getContent()) {
            itemSummaryPage.add(ItemSummaryForModeration.fromItem(item));
        }
        when(itemUtils.convertToItemSummaryForModeration(itemsPage.getContent(), LANG)).thenReturn(itemSummaryPage);
        
        GetUserItemsForModerationModeResponse response = cmd.execute(request);
        assertNotNull(response.getItemsOnPage());
	}
	
	@Test
    public void givenValidPageAndNullPageSizeWhenExecutingThenReturnUserItemListPage(){
        GetUserItemsForModerationModeRequest request = new GetUserItemsForModerationModeRequest(TOKEN, LANG,
                USER_ID, ITEM_ID_EXCLUDED, VALID_PAGE, null);

        PageRequest pageRequest = new PageRequest(VALID_PAGE, VALID_PAGE_SIZE);
        itemsPage = new PageImpl<Item>(items, pageRequest, items.size());
        when(itemDao.listItemsFromUserId(anyLong(), anyLong(), any(),
                any())).thenReturn(itemsPage);
        when(userDao.findOne(USER_ID)).thenReturn(user1);
        List<ItemSummaryForModeration> itemSummaryPage = new ArrayList<ItemSummaryForModeration>();
        
        for (Item item : itemsPage.getContent()) {
            itemSummaryPage.add(ItemSummaryForModeration.fromItem(item));
        }
        when(itemUtils.convertToItemSummaryForModeration(itemsPage.getContent(), LANG)).thenReturn(itemSummaryPage);
        
        GetUserItemsForModerationModeResponse response = cmd.execute(request);
        assertNotNull(response.getItemsOnPage());
    }
	
	@Test
    public void givenNullPageAndValidPageSizeWhenExecutingThenReturnUserItemListPage(){
        GetUserItemsForModerationModeRequest request = new GetUserItemsForModerationModeRequest(TOKEN, LANG,
                USER_ID, ITEM_ID_EXCLUDED, null, VALID_PAGE_SIZE);

        PageRequest pageRequest = new PageRequest(VALID_PAGE, VALID_PAGE_SIZE);
        itemsPage = new PageImpl<Item>(items, pageRequest, items.size());
        when(itemDao.listItemsFromUserId(anyLong(), anyLong(), any(),
                any())).thenReturn(itemsPage);
        when(userDao.findOne(USER_ID)).thenReturn(user1);
        List<ItemSummaryForModeration> itemSummaryPage = new ArrayList<ItemSummaryForModeration>();
        
        for (Item item : itemsPage.getContent()) {
            itemSummaryPage.add(ItemSummaryForModeration.fromItem(item));
        }
        when(itemUtils.convertToItemSummaryForModeration(itemsPage.getContent(), LANG)).thenReturn(itemSummaryPage);
        
        GetUserItemsForModerationModeResponse response = cmd.execute(request);
        assertNotNull(response.getItemsOnPage());
    }
}
