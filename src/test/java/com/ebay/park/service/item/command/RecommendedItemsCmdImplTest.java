package com.ebay.park.service.item.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Idiom;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.ItemSummary;
import com.ebay.park.service.item.dto.SearchItemRequest;
import com.ebay.park.service.item.dto.SearchItemResponse;
import com.ebay.park.service.session.TestServiceUtil;
import com.ebay.park.util.InternationalizationUtil;

public class RecommendedItemsCmdImplTest {

	private static final String TOKEN = "validToken";
	private static final String USERNAME = "timMartins";
	private static final String FAIL_MSG = "An exception was expected";
	private static final Double LIST_RECOMMENDED_ITEMS_RADIUS_MILES = 40.0;
	private static final int LIST_RECOMMENDED_ITEMS_MAX = 10;
	private static final Double LATITUD = 50.0;
	private static final Double LONGITUDE = 50.0;
	private static final String LANGUAGE = "es";
	private User user;

	@InjectMocks
	private RecommendedItemsCmdImpl recommendedItemsCmdImpl;

	@Mock
	private UserDao userDao;

	@Mock
	private ItemDao itemDao;

	@Mock
	private ItemUtils itemUtils;

	@Mock
	private InternationalizationUtil i18nUtil;
	
	@Mock 
	private SearchItemCmdHelper searchCmdHelper;

	@Before
	public void setUp() {
		recommendedItemsCmdImpl = new RecommendedItemsCmdImpl();
		initMocks(this);
		user = TestServiceUtil.createUserMock(888l, USERNAME, "UserFollowed@mail.com", null, null, null, null);
		ReflectionTestUtils.setField(recommendedItemsCmdImpl, "LIST_RECOMMENDED_ITEMS_MAX", 10);
		ReflectionTestUtils.setField(recommendedItemsCmdImpl, "LIST_RECOMMENDED_ITEMS_RADIUS_MILES", 40.0);
		ReflectionTestUtils.setField(searchCmdHelper, "defaultUnloggedLatitude", 40.0);
		ReflectionTestUtils.setField(searchCmdHelper, "defaultUnloggedLongitude", 40.0);
	}

	@Test
	public void testExecuteTokenAndUserNotNullSuccess() {
		// given
		SearchItemRequest request = Mockito.mock(SearchItemRequest.class);
		Item item = Mockito.mock(Item.class);
		List<Item> items = new ArrayList<>();
		items.add(item);
		ItemSummary itemS = Mockito.mock(ItemSummary.class);
		List<ItemSummary> itemsDTO = new ArrayList<>();
		itemsDTO.add(itemS);
		Mockito.when(request.getToken()).thenReturn(TOKEN);
		Mockito.when(request.getLatitude()).thenReturn(LATITUD);
		Mockito.when(request.getLongitude()).thenReturn(LONGITUDE);
		Mockito.when(request.getLanguage()).thenReturn(LANGUAGE);
		Mockito.when(userDao.findByToken(request.getToken())).thenReturn(user);
		Mockito.when(itemDao.getRecommendedItems(user.getId(), StatusDescription.ACTIVE, request.getLatitude(),
				request.getLongitude(), LIST_RECOMMENDED_ITEMS_RADIUS_MILES,
				new PageRequest(0, LIST_RECOMMENDED_ITEMS_MAX))).thenReturn(items);
		Mockito.when(itemUtils.convertToItemSummary(items, user, LANGUAGE)).thenReturn(itemsDTO);
		// when
		SearchItemResponse response = recommendedItemsCmdImpl.execute(request);
		// then
		assertNotNull(response);
		assertEquals(itemsDTO.size(), response.getItems().size());
		Mockito.verify(userDao, Mockito.times(1)).findByToken(TOKEN);
		Mockito.verify(itemDao, Mockito.times(1)).getRecommendedItems(user.getId(), StatusDescription.ACTIVE,
				request.getLatitude(), request.getLongitude(), LIST_RECOMMENDED_ITEMS_RADIUS_MILES,
				new PageRequest(0, LIST_RECOMMENDED_ITEMS_MAX));
		Mockito.verify(itemUtils, Mockito.times(1)).convertToItemSummary(items, user, LANGUAGE);
	}

	@Test
	public void testExecuteUserNullFail() {
		// given
		SearchItemRequest request = Mockito.mock(SearchItemRequest.class);
		Mockito.when(request.getToken()).thenReturn(TOKEN);
		Mockito.when(userDao.findByToken(request.getToken())).thenReturn(null);
		try {
			recommendedItemsCmdImpl.execute(request);
			fail(FAIL_MSG);
		} catch (ServiceException se) {
			assertEquals(ServiceExceptionCode.USER_UNAUTHORIZED.getCode(), se.getCode());
		}
	}

	@Test
	public void testExecuteNullTokenSuccess() {
		// given
		SearchItemRequest request = Mockito.mock(SearchItemRequest.class);
		Item item = Mockito.mock(Item.class);
		List<Item> items = new ArrayList<>();
		items.add(item);
		ItemSummary itemS = Mockito.mock(ItemSummary.class);
		List<ItemSummary> itemsDTO = new ArrayList<>();
		itemsDTO.add(itemS);
		Mockito.when(request.getToken()).thenReturn(null);
		Mockito.when(request.getLatitude()).thenReturn(LATITUD);
		Mockito.when(request.getLongitude()).thenReturn(LONGITUDE);
		Mockito.when(request.getLanguage()).thenReturn(LANGUAGE);
		Mockito.when(userDao.findByToken(request.getToken())).thenReturn(user);
		Mockito.when(itemDao.getPublicRecommendedItems(StatusDescription.ACTIVE, request.getLatitude(),
				request.getLongitude(), LIST_RECOMMENDED_ITEMS_RADIUS_MILES,
				new PageRequest(0, LIST_RECOMMENDED_ITEMS_MAX))).thenReturn(items);
		Mockito.when(itemUtils.convertToItemSummary(items, user, LANGUAGE)).thenReturn(itemsDTO);
		// when
		SearchItemResponse response = recommendedItemsCmdImpl.execute(request);
		// then
		assertNotNull(response);
		assertEquals(0, response.getItems().size());
		Mockito.verify(itemDao, Mockito.times(1)).getPublicRecommendedItems(StatusDescription.ACTIVE,
				request.getLatitude(), request.getLongitude(), LIST_RECOMMENDED_ITEMS_RADIUS_MILES,
				new PageRequest(0, LIST_RECOMMENDED_ITEMS_MAX));
		Mockito.verify(itemUtils, Mockito.times(1)).convertToItemSummary(items, null, LANGUAGE);
	}

	@Test
	public void testExecuteTokenAndUserNotNullLanguageNullSuccess() {
		// given
		SearchItemRequest request = Mockito.mock(SearchItemRequest.class);
		Idiom mockIdiom = Mockito.mock(Idiom.class);
		Mockito.when(user.getIdiom()).thenReturn(mockIdiom);
		Item item = Mockito.mock(Item.class);
		List<Item> items = new ArrayList<>();
		items.add(item);
		ItemSummary itemS = Mockito.mock(ItemSummary.class);
		List<ItemSummary> itemsDTO = new ArrayList<>();
		itemsDTO.add(itemS);
		Mockito.when(request.getToken()).thenReturn(TOKEN);
		Mockito.when(request.getLatitude()).thenReturn(LATITUD);
		Mockito.when(request.getLongitude()).thenReturn(LONGITUDE);
		Mockito.when(request.getLanguage()).thenReturn(null);
		Mockito.when(userDao.findByToken(request.getToken())).thenReturn(user);
		Mockito.when(itemDao.getRecommendedItems(user.getId(), StatusDescription.ACTIVE, request.getLatitude(),
				request.getLongitude(), LIST_RECOMMENDED_ITEMS_RADIUS_MILES,
				new PageRequest(0, LIST_RECOMMENDED_ITEMS_MAX))).thenReturn(items);
		Mockito.when(itemUtils.convertToItemSummary(items, user, LANGUAGE)).thenReturn(itemsDTO);
		// when
		SearchItemResponse response = recommendedItemsCmdImpl.execute(request);
		// then
		assertNotNull(response);
		assertEquals(0, response.getItems().size());
		Mockito.verify(userDao, Mockito.times(1)).findByToken(TOKEN);
		Mockito.verify(itemDao, Mockito.times(1)).getRecommendedItems(user.getId(), StatusDescription.ACTIVE,
				request.getLatitude(), request.getLongitude(), LIST_RECOMMENDED_ITEMS_RADIUS_MILES,
				new PageRequest(0, LIST_RECOMMENDED_ITEMS_MAX));
		Mockito.verify(itemUtils, Mockito.times(1)).convertToItemSummary(items, user, null);
	}
}
