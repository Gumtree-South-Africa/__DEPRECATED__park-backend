package com.ebay.park.service.item.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserFollowsItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserFollowsItem;
import com.ebay.park.db.entity.UserFollowsItemPK;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.dto.UserItemRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserUnfollowItemCmdTest {

	@InjectMocks
	private final UserUnfollowItemCmd cmd = new UserUnfollowItemCmd();

	@Mock
	private UserItemRequest request;

	@Mock
	private ItemDao itemDao;

	@Mock
	private UserDao userDao;

	@Mock
	private UserFollowsItemDao userFollowsItemDao;

	@Mock
	private Item item;

	@Mock
	private User follower;

	@Mock
	private UserFollowsItem userFollowsItem;

	@Before
	public void setUp() {
		initMocks(this);
		when(request.getItemId()).thenReturn(1l);
		when(request.getToken()).thenReturn("TOKEN");

		when(item.getId()).thenReturn(1l);

		when(follower.getId()).thenReturn(999l);

		when(itemDao.findOne(1l)).thenReturn(item);

		when(userDao.findByToken("TOKEN")).thenReturn(follower);

		when(userFollowsItemDao.findOne(new UserFollowsItemPK(follower, item)))
		.thenReturn(userFollowsItem);
	}

	@Test
	public void unfollowItemSuccessTest() {
		cmd.execute(request);
	}

	@Test
	public void unfollowItemItemNotFoundTest() {

		when(itemDao.findOne(1l)).thenReturn(null);

		try {
			cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.ITEM_NOT_FOUND.getCode(), e.getCode());
		}
	}

	@Test
	public void unfollowItemUserNotFoundTest() {

		when(userDao.findByToken("TOKEN")).thenReturn(null);

		try {
			cmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.USER_UNAUTHORIZED.getCode(), e.getCode());
		}
	}

	@Test
	public void unfollowItemNotFollowedTest() {

		when(userFollowsItemDao.findOne(new UserFollowsItemPK(follower, item)))
		.thenReturn(null);
		try {
			cmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.ITEM_NOT_FOLLOWED_BY_USER.getCode(), e.getCode());
		}
	}

}
