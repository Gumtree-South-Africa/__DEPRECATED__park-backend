package com.ebay.park.service.item.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.item.dto.SearchItemIdsResponse;
import com.ebay.park.service.item.dto.SearchItemRequest;
import com.ebay.park.util.InternationalizationUtil;

/**
 * Unit tests for {@link RecommendedItemIdsCmd}
 * @author scalderon
 *
 */
public class RecommendedItemIdsCmdTest {
	
	private static final String TOKEN = "token";
	private static final Double LATITUDE = 5.0;
	private static final Double LONGITUDE = 5.0;
	private static final int LIST_RECOMMENDED_ITEMS_MAX = 24;
	private static final Double LIST_RECOMMENDED_ITEMS_RADIUS_MILES = 5.0;
	private static final Long ITEM_ID = 1L;
	private static final Long USER_ID = 1L;
	
	@Mock
	private UserDao userDao;
	
	@Mock
	private ItemDao itemDao;
	
	@Mock
	private InternationalizationUtil i18nUtil;
	
	@Mock
	private SearchItemCmdHelper searchCmdHelper;
	
	@Mock
	private SearchItemRequest request;
	
	@Mock
	private User user;
	
	@Mock
	private PageRequest pageRequest;

	@InjectMocks
	private RecommendedItemIdsCmd cmd = new RecommendedItemIdsCmd();
	
	@Before
	public void setUp() {
		initMocks(this);
		ReflectionTestUtils.setField(cmd, "LIST_RECOMMENDED_ITEMS_MAX", LIST_RECOMMENDED_ITEMS_MAX);
		ReflectionTestUtils.setField(cmd, "LIST_RECOMMENDED_ITEMS_RADIUS_MILES", LIST_RECOMMENDED_ITEMS_RADIUS_MILES);
	}
	
	@Test(expected=ServiceException.class)
	public void givenANotValidTokenWhenExecuteThenServiceExceptionCode() {
		when(request.getToken()).thenReturn(TOKEN);
		when(userDao.findByToken(TOKEN)).thenReturn(null);
		cmd.execute(request);
	}
	
	@Test
	public void givenANullUserWhenExecuteThenGetPublicRecommendedItemIds() {
		//given
		List<Long> expectedItemIds = new ArrayList<>();
		expectedItemIds.add(ITEM_ID);
		
		when(request.getLatitude()).thenReturn(LATITUDE);
		when(request.getLongitude()).thenReturn(LONGITUDE);
		
		
		when(itemDao.getPublicRecommendedItemIds(StatusDescription.ACTIVE, request.getLatitude(),
				request.getLongitude(), LIST_RECOMMENDED_ITEMS_RADIUS_MILES,
				new PageRequest(0, LIST_RECOMMENDED_ITEMS_MAX))).thenReturn(expectedItemIds);
		
		//when
		SearchItemIdsResponse response = cmd.execute(request);
		
		//then
		assertNotNull(response);
		assertEquals(response.getItemIds().size(), expectedItemIds.size());
		assertEquals(ITEM_ID, expectedItemIds.get(0));
	}
	
	@Test
	public void givenAValidUserWhenExecuteThenGetRecommendedItemIds() {
		//given
		List<Long> expectedItemIds = new ArrayList<>();
		expectedItemIds.add(ITEM_ID);
		
		when(request.getToken()).thenReturn(TOKEN);
		when(userDao.findByToken(TOKEN)).thenReturn(user);
		when(user.getId()).thenReturn(USER_ID);
		when(request.getLatitude()).thenReturn(LATITUDE);
		when(request.getLongitude()).thenReturn(LONGITUDE);
		
		
		when(itemDao.getRecommendedItemIds(USER_ID, StatusDescription.ACTIVE, request.getLatitude(),
				request.getLongitude(), LIST_RECOMMENDED_ITEMS_RADIUS_MILES,
				new PageRequest(0, LIST_RECOMMENDED_ITEMS_MAX))).thenReturn(expectedItemIds);
		
		//when
		SearchItemIdsResponse response = cmd.execute(request);
		
		//then
		assertNotNull(response);
		assertEquals(response.getItemIds().size(), expectedItemIds.size());
		assertEquals(ITEM_ID, expectedItemIds.get(0));
	}
}
