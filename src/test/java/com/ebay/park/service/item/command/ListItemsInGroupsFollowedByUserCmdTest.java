package com.ebay.park.service.item.command;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.PaginatedRequest;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.ItemSummary;
import com.ebay.park.service.item.dto.ListFollowedItemsResponse;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.TestServiceUtil;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.InternationalizationUtil;

public class ListItemsInGroupsFollowedByUserCmdTest {

	private static final String TOKEN = "validToken";
	private static final String USERNAME = "timMartins";
	private static final int PAGE = 0;
	private static final int DEFAULTPAGESIZE = 10;
	private static final String LANGUAGE = "es";
	private static final Integer NUMBER_OF_ELEMENTS = 1;
	private static final Integer TOTAL_PAGES = 10;
	private static final Long TOTAL_ELEMENTS = 10l;
	private User user;

	@InjectMocks
	private ListItemsInGroupsFollowedByUserCmd listItemsInGroupsFollowedByUserCmd;

	@Mock
	private ItemDao itemDao;

	@Mock
	private SessionService sessionService;

	@Mock
	private ItemUtils itemUtils;

	@Mock
	private InternationalizationUtil i18nUtil;

	@Before
	public void setUp() {
		listItemsInGroupsFollowedByUserCmd = new ListItemsInGroupsFollowedByUserCmd();
		initMocks(this);
		ReflectionTestUtils.setField(listItemsInGroupsFollowedByUserCmd, "defaultPageSize", DEFAULTPAGESIZE);
		user = TestServiceUtil.createUserMock(888l, USERNAME, "UserFollowed@mail.com", null, null, null, null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testExecuteSuccess() {
		// given
		PaginatedRequest request = Mockito.mock(PaginatedRequest.class);
		UserSession userSession = new UserSession();
		userSession.setId(1l);
		userSession.setUser(user);
		List<ItemSummary> itemSummary = new ArrayList<>();
		ItemSummary itemS = Mockito.mock(ItemSummary.class);
		itemSummary.add(itemS);
		Page<Item> page = mock(Page.class);
		List<Item> listItem = new ArrayList<>();
		Item item = Mockito.mock(Item.class);
		listItem.add(item);
		Mockito.when(page.getContent()).thenReturn(listItem);
		Mockito.when(page.getNumberOfElements()).thenReturn(NUMBER_OF_ELEMENTS);
		Mockito.when(page.getTotalElements()).thenReturn(TOTAL_ELEMENTS);
		Mockito.when(page.getTotalPages()).thenReturn(TOTAL_PAGES);
		UserSessionCache userSessionCache = new UserSessionCache(userSession);
		Pageable pageable = new PageRequest(PAGE, DEFAULTPAGESIZE);
		Mockito.when(request.getToken()).thenReturn(TOKEN);
		Mockito.when(request.getLanguage()).thenReturn(LANGUAGE);
		Mockito.when(sessionService.getUserSession(request.getToken())).thenReturn(userSessionCache);
		Mockito.when(itemDao.listItemsInGroupsFollowedByUser(userSessionCache.getUserId(),
				StatusDescription.visibleStatusForFollowedItems, pageable)).thenReturn(page);
		Mockito.when(itemUtils.convertToItemSummaryFromPublisher(page.getContent(), request.getLanguage()))
				.thenReturn(itemSummary);
		// when
		ListFollowedItemsResponse response = listItemsInGroupsFollowedByUserCmd.execute(request);
		// then
		assertNotNull(response);
		assertEquals(listItem.size(),response.getItems().size());
		Mockito.verify(sessionService, Mockito.times(1)).getUserSession(request.getToken());
		Mockito.verify(itemDao, Mockito.times(1)).listItemsInGroupsFollowedByUser(userSessionCache.getUserId(),
				StatusDescription.visibleStatusForFollowedItems, pageable);
		Mockito.verify(itemUtils, Mockito.times(1)).convertToItemSummaryFromPublisher(page.getContent(),
				request.getLanguage());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testExecuteRequestLanguageNullSuccess() {
		// given
		PaginatedRequest request = Mockito.mock(PaginatedRequest.class);
		UserSession userSession = new UserSession();
		userSession.setId(1l);
		userSession.setUser(user);
		List<ItemSummary> itemSummary = new ArrayList<>();
		ItemSummary itemS = Mockito.mock(ItemSummary.class);
		itemSummary.add(itemS);
		Page<Item> page = mock(Page.class);
		List<Item> listItem = new ArrayList<>();
		Item item = Mockito.mock(Item.class);
		listItem.add(item);
		Mockito.when(page.getContent()).thenReturn(listItem);
		Mockito.when(page.getNumberOfElements()).thenReturn(NUMBER_OF_ELEMENTS);
		Mockito.when(page.getTotalElements()).thenReturn(TOTAL_ELEMENTS);
		Mockito.when(page.getTotalPages()).thenReturn(TOTAL_PAGES);
		UserSessionCache userSessionCache = new UserSessionCache(userSession);
		userSessionCache.setLang(LANGUAGE);
		Pageable pageable = new PageRequest(PAGE, DEFAULTPAGESIZE);
		Mockito.when(request.getToken()).thenReturn(TOKEN);
		Mockito.when(request.getLanguage()).thenReturn(null);
		Mockito.when(sessionService.getUserSession(request.getToken())).thenReturn(userSessionCache);
		Mockito.when(itemDao.listItemsInGroupsFollowedByUser(userSessionCache.getUserId(),
				StatusDescription.visibleStatusForFollowedItems, pageable)).thenReturn(page);
		Mockito.when(itemUtils.convertToItemSummaryFromPublisher(page.getContent(), request.getLanguage()))
				.thenReturn(itemSummary);
		// when
		ListFollowedItemsResponse response = listItemsInGroupsFollowedByUserCmd.execute(request);
		// then
		assertNotNull(response);
		assertEquals(listItem.size(),response.getItems().size());
		Mockito.verify(sessionService, Mockito.times(1)).getUserSession(request.getToken());
		Mockito.verify(itemDao, Mockito.times(1)).listItemsInGroupsFollowedByUser(userSessionCache.getUserId(),
				StatusDescription.visibleStatusForFollowedItems, pageable);
		Mockito.verify(itemUtils, Mockito.times(1)).convertToItemSummaryFromPublisher(page.getContent(),
				request.getLanguage());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testExecuteRequestGetPageAndPageSizeNotNullSuccess() {
		// given
		PaginatedRequest request = Mockito.mock(PaginatedRequest.class);
		UserSession userSession = new UserSession();
		userSession.setId(1l);
		userSession.setUser(user);
		List<ItemSummary> itemSummary = new ArrayList<>();
		ItemSummary itemS = Mockito.mock(ItemSummary.class);
		itemSummary.add(itemS);
		Page<Item> page = mock(Page.class);
		List<Item> listItem = new ArrayList<>();
		Item item = Mockito.mock(Item.class);
		listItem.add(item);
		Mockito.when(page.getContent()).thenReturn(listItem);
		Mockito.when(page.getNumberOfElements()).thenReturn(NUMBER_OF_ELEMENTS);
		Mockito.when(page.getTotalElements()).thenReturn(TOTAL_ELEMENTS);
		Mockito.when(page.getTotalPages()).thenReturn(TOTAL_PAGES);
		UserSessionCache userSessionCache = new UserSessionCache(userSession);
		userSessionCache.setLang(LANGUAGE);
		Pageable pageable = new PageRequest(PAGE, DEFAULTPAGESIZE);
		Mockito.when(request.getToken()).thenReturn(TOKEN);
		Mockito.when(request.getLanguage()).thenReturn(null);
		Mockito.when(request.getPage()).thenReturn(PAGE);
		Mockito.when(request.getPageSize()).thenReturn(DEFAULTPAGESIZE);
		Mockito.when(sessionService.getUserSession(request.getToken())).thenReturn(userSessionCache);
		Mockito.when(itemDao.listItemsInGroupsFollowedByUser(userSessionCache.getUserId(),
				StatusDescription.visibleStatusForFollowedItems, pageable)).thenReturn(page);
		Mockito.when(itemUtils.convertToItemSummaryFromPublisher(page.getContent(), request.getLanguage()))
				.thenReturn(itemSummary);
		// when
		ListFollowedItemsResponse response = listItemsInGroupsFollowedByUserCmd.execute(request);
		// then
		assertNotNull(response);
		assertEquals(listItem.size(),response.getItems().size());
		Mockito.verify(sessionService, Mockito.times(1)).getUserSession(request.getToken());
		Mockito.verify(itemDao, Mockito.times(1)).listItemsInGroupsFollowedByUser(userSessionCache.getUserId(),
				StatusDescription.visibleStatusForFollowedItems, pageable);
		Mockito.verify(itemUtils, Mockito.times(1)).convertToItemSummaryFromPublisher(page.getContent(),
				request.getLanguage());
	}
}
