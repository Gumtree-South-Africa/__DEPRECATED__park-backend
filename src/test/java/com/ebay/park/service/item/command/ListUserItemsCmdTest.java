package com.ebay.park.service.item.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Category;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.ItemSummary;
import com.ebay.park.service.item.dto.ListUserItemsRequest;
import com.ebay.park.service.item.dto.ListUserItemsResponse;
import com.ebay.park.util.InternationalizationUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.MockitoAnnotations.initMocks;

public class ListUserItemsCmdTest {
	private static final String LANG = "en";
	private static final String CATEGORY_WEB_COLOR = "category_web_color";
	private static final String CATEGORY_NAME = "category_name";
	private static final String REQUEST_TIME = "1456434548";
	private static final String USER_NAME = "userName";
	private static final String SECOND_USER_NAME = "USERNAME_01";
	private static final String INVALID_TOKEN = null;
	private static final String TOKEN = "token";
    private static final int PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int PAGE_SIZE = 3;
    private static final Long ITEM_EXCLUDED = 5l;

	@Spy
	@InjectMocks
	private final ListUserItemsCmd cmd = new ListUserItemsCmdImpl();

	@Mock
	private ItemDao itemDao;

	@Mock
    private UserDao userDao;

	@Mock
	private InternationalizationUtil i18nUtil;
	
	@Mock
	private Category category;
	
	@Mock
	ListUserItemsRequest request;
	
	@Mock
	private ItemUtils itemUtils;
	
	private Item itemActive;
	private Item itemPending;
	private Item itemSold;
	private Item itemDeleted;

	@Before
	public void setUp() {
		initMocks(this);
		itemActive = mock(Item.class);
		when(itemActive.getStatus()).thenReturn(StatusDescription.ACTIVE);
		when(itemActive.getPublished()).thenReturn(new Date());
		when(i18nUtil.internationalize(itemActive.getCategory(), LANG)).thenReturn(category);
		
		itemPending = mock(Item.class);
		when(itemPending.getStatus()).thenReturn(StatusDescription.PENDING);
		when(itemPending.getPublished()).thenReturn(new Date());
		when(i18nUtil.internationalize(itemPending.getCategory(), LANG)).thenReturn(category);
		
		itemSold = mock(Item.class);
		when(itemSold.getStatus()).thenReturn(StatusDescription.SOLD);
		when(itemSold.getPublished()).thenReturn(new Date());
		when(i18nUtil.internationalize(itemSold.getCategory(), LANG)).thenReturn(category);
		
		itemDeleted = mock(Item.class);
		when(itemDeleted.isDeleted()).thenReturn(true);
		when(itemDeleted.getPublished()).thenReturn(new Date());
		when(i18nUtil.internationalize(itemDeleted.getCategory(), LANG)).thenReturn(category);
		
		when(category.getCategoryId()).thenReturn(1L);
		when(category.getName()).thenReturn(CATEGORY_NAME);
		when(category.getWebColor()).thenReturn(CATEGORY_WEB_COLOR);
		
		when(itemActive.getCategory()).thenReturn(category);
		when(itemDeleted.getCategory()).thenReturn(category);
		when(itemPending.getCategory()).thenReturn(category);
		when(itemSold.getCategory()).thenReturn(category);

		ReflectionTestUtils.setField(cmd, "defaultPageSize", DEFAULT_PAGE_SIZE);
		}

	@Test
	public void givenOwnItemsRequestWhenExecutingThenReturnListOwnItems() {

		ListUserItemsRequest request = mock(ListUserItemsRequest.class);
		when(request.getUsername()).thenReturn(USER_NAME);
		when(request.isOwnItems()).thenReturn(true);
		when(request.getPageSize()).thenReturn(2);
		when(request.getPage()).thenReturn(0);
		when(request.getToken()).thenReturn(INVALID_TOKEN);
		when(request.getLanguage()).thenReturn(LANG);
		when(request.getItemIdExcluded()).thenReturn(null);
		when(request.getRequestTime()).thenReturn(null);

		@SuppressWarnings("unchecked")
		Page<Item> page = mock(Page.class);
		when(page.getTotalElements()).thenReturn(3l);
		when(page.getNumberOfElements()).thenReturn(2);
		when(page.getTotalPages()).thenReturn(2);
		List<Item> items = Arrays.asList(itemActive, itemPending, itemSold); 
		when(page.getContent()).thenReturn(items);

		when(
				itemDao.listItemsFromUser(any(String.class),
						(StatusDescription[]) any(), any(Pageable.class)))
				.thenReturn(page);

		when(i18nUtil.internationalizeItems(items, request)).thenReturn(items);
		
		List<ItemSummary> itemSummaryList = Arrays.asList(
				ItemSummary.fromItem(itemActive),
				ItemSummary.fromItem(itemPending),
				ItemSummary.fromItem(itemSold));
		
		when(itemUtils.convertToItemSummary(items, LANG)).thenReturn(itemSummaryList);

		
		ListUserItemsResponse response = cmd.execute(request);

		assertEquals(response.getItemsOnPage(), 2);
		assertEquals(response.getTotalPages(), 2);
		assertEquals(response.getTotalOfItems(), 3);
		validateItemSummaryAgainstItem(response.getItems().get(0), itemActive);
		validateItemSummaryAgainstItem(response.getItems().get(1), itemPending);
		validateItemSummaryAgainstItem(response.getItems().get(2), itemSold);

		verify(itemDao).listItemsFromUser(any(String.class),
				(StatusDescription[]) any(), any(Pageable.class));
	}
	
	@Test
	public void givenOwnItemsWithTimeRequestWhenExecutingThenReturnListOwnItems() {

		ListUserItemsRequest request = mock(ListUserItemsRequest.class);
		when(request.getUsername()).thenReturn(USER_NAME);
		when(request.isOwnItems()).thenReturn(true);
		when(request.getPageSize()).thenReturn(2);
		when(request.getPage()).thenReturn(0);
		when(request.getToken()).thenReturn(INVALID_TOKEN);
		when(request.getLanguage()).thenReturn(LANG);
		when(request.getRequestTime()).thenReturn(REQUEST_TIME);
		when(request.getItemIdExcluded()).thenReturn(null);

		@SuppressWarnings("unchecked")
		Page<Item> page = mock(Page.class);
		when(page.getTotalElements()).thenReturn(3l);
		when(page.getNumberOfElements()).thenReturn(2);
		when(page.getTotalPages()).thenReturn(2);
		List<Item> items = Arrays.asList(itemActive, itemPending, itemSold); 
		when(page.getContent()).thenReturn(items);

		when(
				itemDao.listItemsFromUser(any(String.class),any(Date.class),
						(StatusDescription[]) any(), any(Pageable.class)))
				.thenReturn(page);

		when(i18nUtil.internationalizeItems(items, request)).thenReturn(items);
		
		List<ItemSummary> itemSummaryList = Arrays.asList(
				ItemSummary.fromItem(itemActive),
				ItemSummary.fromItem(itemPending),
				ItemSummary.fromItem(itemSold));
		
		when(itemUtils.convertToItemSummary(items, LANG)).thenReturn(itemSummaryList);

		
		ListUserItemsResponse response = cmd.execute(request);

		assertEquals(response.getItemsOnPage(), 2);
		assertEquals(response.getTotalPages(), 2);
		assertEquals(response.getTotalOfItems(), 3);
		validateItemSummaryAgainstItem(response.getItems().get(0), itemActive);
		validateItemSummaryAgainstItem(response.getItems().get(1), itemPending);
		validateItemSummaryAgainstItem(response.getItems().get(2), itemSold);

		verify(itemDao).listItemsFromUser(any(String.class),any(Date.class),
				(StatusDescription[]) any(), any(Pageable.class));
	}

	@Test
	public void givenOthersItemsRequestWhenExecutingThenReturnListOthersItems() {

		ListUserItemsRequest request = mock(ListUserItemsRequest.class);
		when(request.getUsername()).thenReturn(SECOND_USER_NAME);
		when(request.isOwnItems()).thenReturn(false);
		when(request.getPageSize()).thenReturn(2);
		when(request.getPage()).thenReturn(0);
		when(request.getToken()).thenReturn(INVALID_TOKEN);
		when(request.getLanguage()).thenReturn(LANG);
		when(request.getItemIdExcluded()).thenReturn(null);
		when(request.getRequestTime()).thenReturn(null);

		@SuppressWarnings("unchecked")
		Page<Item> page = mock(Page.class);
		when(page.getTotalElements()).thenReturn(2l);
		when(page.getNumberOfElements()).thenReturn(2);
		when(page.getTotalPages()).thenReturn(1);
		List<Item> items = Arrays.asList(itemActive, itemSold);
		when(page.getContent()).thenReturn(items);

		when(
				itemDao.listItemsFromUser(any(String.class),
						(StatusDescription[]) any(), any(Pageable.class)))
				.thenReturn(page);

		when(i18nUtil.internationalizeItems(items, request)).thenReturn(items);
		when(i18nUtil.internationalize(category, LANG)).thenReturn(category);
		
		List<ItemSummary> itemSummaryList = Arrays.asList(
				ItemSummary.fromItem(itemActive),
				ItemSummary.fromItem(itemPending),
				ItemSummary.fromItem(itemSold));
		
		when(itemUtils.convertToItemSummary(items, LANG)).thenReturn(itemSummaryList);

		ListUserItemsResponse response = cmd.execute(request);

		assertEquals(response.getItemsOnPage(), 2);
		assertEquals(response.getTotalPages(), 1);
		assertEquals(response.getTotalOfItems(), 2);
		validateItemSummaryAgainstItem(response.getItems().get(0), itemActive);

		validateItemSummaryAgainstItem(response.getItems().get(1), itemSold);

		verify(itemDao).listItemsFromUser(any(String.class),
				(StatusDescription[]) any(), any(Pageable.class));
	}

	@Test
	public void givenUserItemsRequestWhenExecutingThenReturnListUserItemsNonPaged() {

		ListUserItemsRequest request = mock(ListUserItemsRequest.class);
		when(request.getUsername()).thenReturn(SECOND_USER_NAME);
		when(request.isOwnItems()).thenReturn(false);
		when(request.getPageSize()).thenReturn(null);
		when(request.getPage()).thenReturn(null);
		when(request.getToken()).thenReturn(INVALID_TOKEN);
		when(request.getLanguage()).thenReturn(LANG);
		when(request.getItemIdExcluded()).thenReturn(null);
		when(request.getRequestTime()).thenReturn(null);
		List<Item> items = Arrays.asList(itemActive, itemPending, itemSold);
		when(
				itemDao.listItemsFromUser(any(String.class),
						(StatusDescription[]) any())).thenReturn(items);
		when(i18nUtil.internationalizeItems(items, request)).thenReturn(items);
		
		List<ItemSummary> itemSummaryList = Arrays.asList(
				ItemSummary.fromItem(itemActive),
				ItemSummary.fromItem(itemPending),
				ItemSummary.fromItem(itemSold));
		
		when(itemUtils.convertToItemSummary(items, LANG)).thenReturn(itemSummaryList);

		
		ListUserItemsResponse response = cmd.execute(request);

		assertEquals(response.getItemsOnPage(), 3);
		assertEquals(response.getTotalPages(), 0);
		assertEquals(response.getTotalOfItems(), 3);
		validateItemSummaryAgainstItem(response.getItems().get(0), itemActive);
		validateItemSummaryAgainstItem(response.getItems().get(1), itemPending);
		validateItemSummaryAgainstItem(response.getItems().get(2), itemSold);

		verify(itemDao).listItemsFromUser(any(String.class),
				(StatusDescription[]) any());
	}

	private void validateItemSummaryAgainstItem(ItemSummary summary, Item item) {
		assertEquals(summary.getName(), item.getName());
		assertEquals(summary.getPictureUrl(), item.getPicture1Url());
		assertEquals(summary.getPrice(), item.getPrice());
	}

	@Test
    public void givenValidPageAndNullPageSizeWhenExecutingThenReturnListUserItemsPaged() {
	    ListUserItemsRequest request = mock(ListUserItemsRequest.class);
        when(request.getUsername()).thenReturn(USER_NAME);
        when(request.isOwnItems()).thenReturn(true);
        when(request.getPageSize()).thenReturn(null);
        when(request.getPage()).thenReturn(PAGE);
        when(request.getToken()).thenReturn(INVALID_TOKEN);
        when(request.getLanguage()).thenReturn(LANG);
        when(request.getItemIdExcluded()).thenReturn(null);
        when(request.getRequestTime()).thenReturn(null);

        @SuppressWarnings("unchecked")
        Page<Item> page = mock(Page.class);
        when(page.getTotalElements()).thenReturn(3l);
        when(page.getNumberOfElements()).thenReturn(2);
        when(page.getTotalPages()).thenReturn(2);
        List<Item> items = Arrays.asList(itemActive, itemPending, itemSold); 
        when(page.getContent()).thenReturn(items);

        when(
                itemDao.listItemsFromUser(any(String.class),
                        (StatusDescription[]) any(), any(Pageable.class)))
                .thenReturn(page);

        when(i18nUtil.internationalizeItems(items, request)).thenReturn(items);
        
        List<ItemSummary> itemSummaryList = Arrays.asList(
                ItemSummary.fromItem(itemActive),
                ItemSummary.fromItem(itemPending),
                ItemSummary.fromItem(itemSold));
        
        when(itemUtils.convertToItemSummary(items, LANG)).thenReturn(itemSummaryList);

        
        ListUserItemsResponse response = cmd.execute(request);

        assertNotNull(response);
    }
	
	@Test
    public void givenNullPageAndInvalidPageSizeWhenExecutingThenReturnListUserItemsPaged() {
        ListUserItemsRequest request = mock(ListUserItemsRequest.class);
        when(request.getUsername()).thenReturn(USER_NAME);
        when(request.isOwnItems()).thenReturn(true);
        when(request.getPageSize()).thenReturn(PAGE_SIZE);
        when(request.getPage()).thenReturn(null);
        when(request.getToken()).thenReturn(INVALID_TOKEN);
        when(request.getLanguage()).thenReturn(LANG);
        when(request.getItemIdExcluded()).thenReturn(null);
        when(request.getRequestTime()).thenReturn(null);

        @SuppressWarnings("unchecked")
        Page<Item> page = mock(Page.class);
        when(page.getTotalElements()).thenReturn(3l);
        when(page.getNumberOfElements()).thenReturn(2);
        when(page.getTotalPages()).thenReturn(2);
        List<Item> items = Arrays.asList(itemActive, itemPending, itemSold); 
        when(page.getContent()).thenReturn(items);

        when(
                itemDao.listItemsFromUser(any(String.class),
                        (StatusDescription[]) any(), any(Pageable.class)))
                .thenReturn(page);

        when(i18nUtil.internationalizeItems(items, request)).thenReturn(items);
        
        List<ItemSummary> itemSummaryList = Arrays.asList(
                ItemSummary.fromItem(itemActive),
                ItemSummary.fromItem(itemPending),
                ItemSummary.fromItem(itemSold));
        
        when(itemUtils.convertToItemSummary(items, LANG)).thenReturn(itemSummaryList);

        
        ListUserItemsResponse response = cmd.execute(request);

        assertNotNull(response);
    }

	@Test
    public void givenTimeRequestAndItemExcludedWhenExecutingThenReturnList() {
        ListUserItemsRequest request = mock(ListUserItemsRequest.class);
        when(request.getUsername()).thenReturn(USER_NAME);
        when(request.isOwnItems()).thenReturn(false);
        when(request.getPageSize()).thenReturn(2);
        when(request.getPage()).thenReturn(0);
        when(request.getToken()).thenReturn(INVALID_TOKEN);
        when(request.getLanguage()).thenReturn(LANG);
        when(request.getRequestTime()).thenReturn(REQUEST_TIME);
        when(request.getItemIdExcluded()).thenReturn(ITEM_EXCLUDED);

        @SuppressWarnings("unchecked")
        Page<Item> page = mock(Page.class);
        when(page.getTotalElements()).thenReturn(3l);
        when(page.getNumberOfElements()).thenReturn(2);
        when(page.getTotalPages()).thenReturn(2);
        List<Item> items = Arrays.asList(itemActive, itemPending, itemSold);
        when(page.getContent()).thenReturn(items);

        when(
                itemDao.listItemsFromUser(any(String.class),any(Date.class),
                        (StatusDescription[]) any(), any(Pageable.class), anyLong()))
                .thenReturn(page);

        when(i18nUtil.internationalizeItems(items, request)).thenReturn(items);

        List<ItemSummary> itemSummaryList = Arrays.asList(
                ItemSummary.fromItem(itemActive),
                ItemSummary.fromItem(itemPending),
                ItemSummary.fromItem(itemSold));

        when(itemUtils.convertToItemSummary(items, LANG)).thenReturn(itemSummaryList);

        ListUserItemsResponse response = cmd.execute(request);

        assertNotNull(response);
    }

	   @Test
	    public void givenItemExcludedWhenExecutingThenReturnList() {
	        ListUserItemsRequest request = mock(ListUserItemsRequest.class);
	        when(request.getUsername()).thenReturn(USER_NAME);
	        when(request.isOwnItems()).thenReturn(false);
	        when(request.getPageSize()).thenReturn(2);
	        when(request.getPage()).thenReturn(0);
	        when(request.getToken()).thenReturn(INVALID_TOKEN);
	        when(request.getLanguage()).thenReturn(LANG);
	        when(request.getRequestTime()).thenReturn(null);
	        when(request.getItemIdExcluded()).thenReturn(ITEM_EXCLUDED);

	        @SuppressWarnings("unchecked")
	        Page<Item> page = mock(Page.class);
	        when(page.getTotalElements()).thenReturn(3l);
	        when(page.getNumberOfElements()).thenReturn(2);
	        when(page.getTotalPages()).thenReturn(2);
	        List<Item> items = Arrays.asList(itemActive, itemPending, itemSold);
	        when(page.getContent()).thenReturn(items);

	        when(
	                itemDao.listItemsFromUser(any(String.class),
	                        (StatusDescription[]) any(), any(Pageable.class), anyLong()))
	                .thenReturn(page);

	        when(i18nUtil.internationalizeItems(items, request)).thenReturn(items);

	        List<ItemSummary> itemSummaryList = Arrays.asList(
	                ItemSummary.fromItem(itemActive),
	                ItemSummary.fromItem(itemPending),
	                ItemSummary.fromItem(itemSold));

	        when(itemUtils.convertToItemSummary(items, LANG)).thenReturn(itemSummaryList);

	        ListUserItemsResponse response = cmd.execute(request);

	        assertNotNull(response);
	    }

	   @Test
	    public void givenItemExcludedAndNotPagedRequestWhenExecutingThenReturnListUserItemsNonPaged() {
	        ListUserItemsRequest request = mock(ListUserItemsRequest.class);
	        when(request.getUsername()).thenReturn(SECOND_USER_NAME);
	        when(request.isOwnItems()).thenReturn(false);
	        when(request.getPageSize()).thenReturn(null);
	        when(request.getPage()).thenReturn(null);
	        when(request.getToken()).thenReturn(INVALID_TOKEN);
	        when(request.getLanguage()).thenReturn(LANG);
	        when(request.getItemIdExcluded()).thenReturn(ITEM_EXCLUDED);
	        when(request.getRequestTime()).thenReturn(null);
	        List<Item> items = Arrays.asList(itemActive, itemPending, itemSold);
	        when(itemDao.listItemsFromUser(any(String.class),
	                        (StatusDescription[]) any(), anyLong())).thenReturn(items);
	        when(i18nUtil.internationalizeItems(items, request)).thenReturn(items);

	        List<ItemSummary> itemSummaryList = Arrays.asList(
	                ItemSummary.fromItem(itemActive),
	                ItemSummary.fromItem(itemPending),
	                ItemSummary.fromItem(itemSold));

	        when(itemUtils.convertToItemSummary(items, LANG)).thenReturn(itemSummaryList);

	        ListUserItemsResponse response = cmd.execute(request);

	        assertNotNull(response);
	    }

	   @Test
	    public void givenLoggedUserWhenExecutingThenReturnListItems() {
	        User user = mock(User.class);
	        ListUserItemsRequest request = mock(ListUserItemsRequest.class);
	        when(request.getUsername()).thenReturn(USER_NAME);
	        when(request.isOwnItems()).thenReturn(true);
	        when(request.getPageSize()).thenReturn(2);
	        when(request.getPage()).thenReturn(0);
	        when(request.getToken()).thenReturn(TOKEN);
	        when(request.getLanguage()).thenReturn(LANG);
	        when(request.getItemIdExcluded()).thenReturn(null);
	        when(request.getRequestTime()).thenReturn(null);
	        when(userDao.findByToken(TOKEN)).thenReturn(user);

	        @SuppressWarnings("unchecked")
	        Page<Item> page = mock(Page.class);
	        when(page.getTotalElements()).thenReturn(3l);
	        when(page.getNumberOfElements()).thenReturn(2);
	        when(page.getTotalPages()).thenReturn(2);
	        List<Item> items = Arrays.asList(itemActive, itemPending, itemSold);
	        when(page.getContent()).thenReturn(items);

	        when(
	                itemDao.listItemsFromUser(any(String.class),
	                        (StatusDescription[]) any(), any(Pageable.class)))
	                .thenReturn(page);

	        when(i18nUtil.internationalizeItems(items, request)).thenReturn(items);

	        List<ItemSummary> itemSummaryList = Arrays.asList(
	                ItemSummary.fromItem(itemActive),
	                ItemSummary.fromItem(itemPending),
	                ItemSummary.fromItem(itemSold));

	        when(itemUtils.convertToItemSummary(items, user, LANG)).thenReturn(itemSummaryList);

	        ListUserItemsResponse response = cmd.execute(request);

	        assertNotNull(response);
	    }
}
