package com.ebay.park.service.item.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.ItemGroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserReportItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.ItemGroup;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.event.item.ItemNotificationEvent;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.dto.UserItemRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RemoveItemCmdTest {
	@Spy
	@InjectMocks
	private final RemoveItemCmd cmd = new RemoveItemCmd();

	private static final String USERNAME = "my-username";
	private static final String TOKEN = "token";
	private static final Long ITEM_ID = 1l;
	private static final Long USER_ID = 100l;

	@Mock
	private ItemDao itemDao;
	
	@Mock
	private UserDao userdao;

	@Mock
	private UserItemRequest request;

	@Mock
	private ItemGroupDao itemGroupDao;

	@Mock
	private SessionService sessionService;

	@Mock
	private User user;

	@Mock
	private UserReportItemDao userReport;

	@Before
	public void setUp() {
		initMocks(this);
		when(request.getItemId()).thenReturn(1l);
		when(user.getUserId()).thenReturn(USER_ID);
		when(user.getUsername()).thenReturn(USERNAME);
		UserItemRequest request = mock(UserItemRequest.class);
		when(request.getToken()).thenReturn(TOKEN);
		when(request.getItemId()).thenReturn(ITEM_ID);

		UserSessionCache userSession = mock(UserSessionCache.class);
		when(userSession.getUserId()).thenReturn(100l);
		when(sessionService.getUserSession(any(String.class))).thenReturn(
				userSession);
		when(user.getUserId()).thenReturn(100l);
	}

	@Test
	public void testExecuteShouldSucceed() {

		Item item = mock(Item.class);
		when(item.getPublishedBy()).thenReturn(user);
		when(item.getStatus()).thenReturn(StatusDescription.ACTIVE);
		when(itemDao.findOne(any(Long.class))).thenReturn(item);
		when(userdao.findByToken(any(String.class))).thenReturn(user);

		when(item.getItemGroups()).thenReturn(anyListOf(ItemGroup.class));

		cmd.execute(request);
	}

	@Test
	public void testInvalidItem() {
		when(itemDao.findOne(any(Long.class))).thenThrow(
				ServiceException.createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND));

		ItemNotificationEvent response = null;
		try {
			response = cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.ITEM_NOT_FOUND.getCode(), e.getCode());
		}
		assertNull(response);
	}

	@Test
	public void testAlreadyDeletedItem() {
		Item item = mock(Item.class);
		when(item.getPublishedBy()).thenReturn(user);
		when(item.isDeleted()).thenReturn(true);
		when(itemDao.findOne(any(Long.class))).thenReturn(item);

		ItemNotificationEvent response = null;
		try {
			response = cmd.execute(request);
		} catch (ServiceException e) {
			// Because deleted items aren't even returned
			assertEquals(ServiceExceptionCode.ITEM_NOT_FOUND.getCode(), e.getCode());
		}
		assertNull(response);
	}
}
