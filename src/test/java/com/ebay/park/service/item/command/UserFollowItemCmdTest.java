package com.ebay.park.service.item.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserFollowsItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserFollowsItem;
import com.ebay.park.db.entity.UserFollowsItemPK;
import com.ebay.park.event.user.UserItemToFollowersEvent;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.dto.UserItemRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserFollowItemCmdTest {

	@InjectMocks
	private final UserFollowItemCmd cmd = new UserFollowItemCmd();

	@Mock
	private UserItemRequest request;

	@Mock
	private ItemDao itemDao;

	@Mock
	private UserDao userDao;

	@Mock
	private Item item;

	@Mock
	private User follower;

	@Mock
	private UserFollowsItem userFollowsItem;

	@Mock
	private UserFollowsItemDao userFollowsItemDao;

	@Before
	public void setUp() {
		initMocks(this);
		when(request.getItemId()).thenReturn(1l);
		when(request.getToken()).thenReturn("TOKEN");

		when(item.getId()).thenReturn(1l);

		when(follower.getId()).thenReturn(999l);

		when(itemDao.findOne(1l)).thenReturn(item);

		when(userDao.findByToken("TOKEN")).thenReturn(follower);
	}

	@Test
	public void followItemSuccessTest() {

		when(userFollowsItemDao.findOne(new UserFollowsItemPK(follower, item)))
		.thenReturn(null);

		cmd.execute(request);
	}

	@Test
	public void followItemItemNotFoundTest() {

		when(itemDao.findOne(1l)).thenReturn(null);

		UserItemToFollowersEvent response = null;
		try {
			cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.ITEM_NOT_FOUND.getCode(), e.getCode());
		}
        assertNull(response);
	}

	@Test
	public void followItemUserNotFoundTest() {

		when(userDao.findByToken("TOKEN")).thenReturn(null);

		try {
			cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.USER_UNAUTHORIZED.getCode(), e.getCode());
		}
	}

	@Test
	public void followItemAlreadyFollowedTest() {

		when(userFollowsItemDao.findOne(new UserFollowsItemPK(follower, item)))
		.thenReturn(userFollowsItem);


		try {
			cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.ITEM_ALREADY_FOLLOWED_BY_USER.getCode(), e.getCode());
		}
	}

}
