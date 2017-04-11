package com.ebay.park.service.item.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.*;
import com.ebay.park.service.PaginatedRequest;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.ItemSummary;
import com.ebay.park.service.item.dto.ListFollowedItemsResponse;
import com.ebay.park.util.InternationalizationUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ListFollowedItemsCmdTest {

	private static final String CATEGORY_WEB_COLOR = "category_web_color";

	private static final String CATEGORY_NAME = "category_name";

	private static final String LANG = "en";

	private static final String ITEM1_NAME = "item1";
	private static final String ITEM2_NAME = "item2";
	private static final String ITEM3_NAME = "item3";
	

	@Spy
	@InjectMocks
	private final ListFollowedItemsCmd cmd = new ListFollowedItemsCmd();

	@Mock
	private ItemDao itemDao;

	@Mock
	private UserDao userDao;

	@Mock
	private PaginatedRequest request;

	@Mock
	private Item item1;

	@Mock
	private Item item2;

	@Mock
	private Item item3;
	
	@Mock
	private User user;
	
	@Mock
	private Category category;
	
	@Mock
	private InternationalizationUtil i18nUtil;
	
	@Mock
	private ItemUtils itemUtils;

	@Before
	public void setUp() {
		initMocks(this);

		when(user.getUsername()).thenReturn("USER");

		when(item1.getPublishedBy()).thenReturn(user);
		when(item1.getStatus()).thenReturn(StatusDescription.ACTIVE );
		when(item1.getPublished()).thenReturn(new Date());
		when(item1.getCategory()).thenReturn(category);
		when(item1.getName()).thenReturn(ITEM1_NAME);
		
		when(item2.getPublishedBy()).thenReturn(user);
		when(item2.getPublished()).thenReturn(new Date());
		when(item2.getStatus()).thenReturn(StatusDescription.ACTIVE );
		when(item2.getCategory()).thenReturn(category);
		when(item2.getName()).thenReturn(ITEM2_NAME);
		
		when(item3.getPublishedBy()).thenReturn(user);
		when(item3.getPublished()).thenReturn(new Date());
		when(item3.getStatus()).thenReturn(StatusDescription.ACTIVE );
		when(item3.getCategory()).thenReturn(category);
		when(item3.getName()).thenReturn(ITEM3_NAME);
		
		when(request.getPage()).thenReturn(0);
		when(request.getPageSize()).thenReturn(5);
		when(request.getToken()).thenReturn("token");
		when(userDao.findByToken("token")).thenReturn(user);
		
		Idiom idiom = mock(Idiom.class);
		when(idiom.getCode()).thenReturn("idiom");
		when(user.getIdiom()).thenReturn(idiom);
		
		when(category.getCategoryId()).thenReturn(1L);
		when(category.getName()).thenReturn(CATEGORY_NAME);
		when(category.getWebColor()).thenReturn(CATEGORY_WEB_COLOR);
		
		when(request.getLanguage()).thenReturn(LANG);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testGetFollowedItems() {

		Page<Item> page = mock(Page.class);
		when(page.getTotalPages()).thenReturn(1);
		when(page.getNumberOfElements()).thenReturn(3);
		when(page.getTotalElements()).thenReturn(3l);
		List<Item> items = Arrays.asList(item1, item2, item3);
		when(page.getContent()).thenReturn(items);
		when(
				itemDao.listItemsFollowed(any(Long.class),
						any(StatusDescription[].class), any(Pageable.class)))
				.thenReturn(page);

		when(i18nUtil.internationalizeItems(items, request)).thenReturn(items);
		when(i18nUtil.internationalize(item1.getCategory(), LANG)).thenReturn(category);
		when(i18nUtil.internationalize(item2.getCategory(), LANG)).thenReturn(category);
		when(i18nUtil.internationalize(item3.getCategory(), LANG)).thenReturn(category);
		
		List<ItemSummary> itemSummaryList = Arrays.asList(
			ItemSummary.fromItem(item1),
			ItemSummary.fromItem(item2),
			ItemSummary.fromItem(item3));
		
		when(itemUtils.convertToItemSummary(Mockito.eq(items), Mockito.eq(user), Mockito.eq(LANG))).thenReturn(itemSummaryList);
				
		ListFollowedItemsResponse response = cmd.execute(request);

		List<ItemSummary> summaries = response.getItems();

		assertEquals(3, summaries.size());
		
		validateItemSummary(summaries.get(0), item1);
		validateItemSummary(summaries.get(1), item2);
		validateItemSummary(summaries.get(2), item3);

	}

	private void validateItemSummary(ItemSummary summary, Item item) {
		assertEquals(summary.getName(), item.getName());
		assertEquals(summary.getPrice(), item.getPrice());
	}

}
