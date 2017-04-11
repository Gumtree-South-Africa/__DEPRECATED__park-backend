package com.ebay.park.service.item.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserReportItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserReportItem;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.item.dto.UserItemRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserUnreportItemCmdTest {

	@InjectMocks
	private final UserUnreportItemCmd cmd = new UserUnreportItemCmd();

	@Mock
	private UserItemRequest request;

	@Mock
	private ItemDao itemDao;

	@Mock
	private UserDao userDao;

	@Mock
	private UserReportItemDao userReportItemDao;

	@Before
	public void setUp(){
		initMocks(this);
	}
	
	@Test
	public void unreportItemSuccessTest() {
		when(request.getItemId()).thenReturn(1l);
		when(request.getToken()).thenReturn("TOKEN");

		Item item = mock(Item.class);
		when(item.getId()).thenReturn(1l);
		when(itemDao.findOne(1l)).thenReturn(item);

		User user = mock(User.class);
		when(user.getId()).thenReturn(100l);
		when(userDao.findByToken("TOKEN")).thenReturn(user);

		UserReportItem report = mock(UserReportItem.class);
		when(userReportItemDao.findUserReportForItem(100l, 1l)).thenReturn(
				report);

		ServiceResponse response = cmd.execute(request);
		assertEquals(ServiceResponse.SUCCESS, response);
	}

	@Test
	public void unreportItemItemNotFoundTest() {
		when(request.getItemId()).thenReturn(1l);
		when(request.getToken()).thenReturn("TOKEN");

		Item item = mock(Item.class);
		when(item.getId()).thenReturn(null);

		User user = mock(User.class);
		when(user.getId()).thenReturn(100l);
		when(userDao.findByToken("TOKEN")).thenReturn(user);

		ServiceResponse response = null;
		try {
			response = cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.ITEM_NOT_FOUND.getCode(), e.getCode());
		}
        assertNull(response);
	}

	@Test
	public void unreportItemUserNotFoundTest() {
		when(request.getItemId()).thenReturn(1l);
		when(request.getToken()).thenReturn("TOKEN");

		when(userDao.findByToken("TOKEN")).thenReturn(null);

		ServiceResponse response = null;
		try {
			response = cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.USER_UNAUTHORIZED.getCode(), e.getCode());
		}
        assertNull(response);
	}

	@Test
	public void unreportItemReportDoesntExistTest() {
		when(request.getItemId()).thenReturn(1l);
		when(request.getToken()).thenReturn("TOKEN");

		Item item = mock(Item.class);
		when(item.getId()).thenReturn(1l);
		User publisher = mock(User.class);
		when(publisher.getId()).thenReturn(999l);

		when(item.getPublishedBy()).thenReturn(publisher);
		when(itemDao.findOne(1l)).thenReturn(item);

		User user = mock(User.class);
		when(user.getId()).thenReturn(100l);
		when(userDao.findByToken("TOKEN")).thenReturn(user);

		when(userReportItemDao.findUserReportForItem(100l, 1l))
				.thenReturn(null);

		ServiceResponse response = null;
		try {
			response = cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.ITEM_REPORT_NOT_FOUND.getCode(), e.getCode());
		}
        assertNull(response);
	}
}
