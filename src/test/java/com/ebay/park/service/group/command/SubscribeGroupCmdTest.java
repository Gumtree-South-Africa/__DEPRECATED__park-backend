package com.ebay.park.service.group.command;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserFollowsGroupDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserFollowsGroup;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.group.dto.SubscribeGroupRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SubscribeGroupCmdTest {
	@Spy
	@InjectMocks
	private final SubscribeGroupCmd cmd = new SubscribeGroupCmd();

	@Mock
	private SubscribeGroupRequest request;

	@Mock
	private GroupDao groupDao;

	@Mock
	private UserDao userDao;

	@Mock
	private UserFollowsGroupDao userFollowsGroupDao;

	@Mock
	private User user;

	@Mock
	private Group group;

	@Before
	public void setUp() {
		initMocks(this);
		when(request.getToken()).thenReturn("token");
		when(request.getGroupId()).thenReturn(1l);
		when(user.getId()).thenReturn(1l);
		when(group.getId()).thenReturn(1l);
	}

	@Test
	public void testExecuteShouldSucceed() {
		when(userDao.findByToken("token")).thenReturn(user);
		when(groupDao.findOne(1l)).thenReturn(group);
		ServiceResponse response = cmd.execute(request);

		verify(userFollowsGroupDao).save(any(UserFollowsGroup.class));

		assertEquals(ServiceResponse.SUCCESS, response);
	}

	@Test
	public void testExecuteShouldFailOnInvalidGroup() {

		when(userDao.findByToken("token")).thenReturn(user);
		when(groupDao.findOne(1l)).thenReturn(null);

		try {
			cmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.INVALID_GROUP.getCode(), e.getCode());
		}
	}

	@Test
	public void testExecuteShouldFailOnMissingUser() {

		when(userDao.findByToken("token")).thenReturn(null);
		when(groupDao.findOne(1l)).thenReturn(group);

		try {
			cmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.USER_UNAUTHORIZED.getCode(), e.getCode());
		}
	}

	@Test
	public void testExecuteShouldFailWhenUserIsSuscribed() {

		when(userDao.findByToken("token")).thenReturn(user);
		when(groupDao.findOne(1l)).thenReturn(group);

		when(user.isSubscribedToGroup(group)).thenReturn(true);

		try {
			cmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.USER_ALREADY_SUBSCRIBED.getCode(), e.getCode());
		}
	}

}
