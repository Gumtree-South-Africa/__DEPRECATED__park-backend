package com.ebay.park.service.group.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.group.dto.UnsubscribeGroupRequest;

public class UnsubscribeGroupCmdTest {
	@Spy
	@InjectMocks
	private final UnsubscribeGroupCmd cmd = new UnsubscribeGroupCmd();

	@Mock
	private UnsubscribeGroupRequest request;

	@Mock
	private GroupDao groupDao;

	@Mock
	private UserDao userDao;

	@Mock
	private User user;

	@Mock
	private User user2;

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
		when(groupDao.getGroupFollowedByUserAndId(1l, 1l)).thenReturn(group);
		when(group.getCreator()).thenReturn(user2);
		ServiceResponse response = cmd.execute(request);
		verify(groupDao).saveAndFlush(group);
		assertEquals(ServiceResponse.SUCCESS, response);
	}

	@Test
	public void testExecuteShouldFailOwnerCanNotBeUnsubscribed() {
		when(userDao.findByToken("token")).thenReturn(user);
		when(groupDao.findOne(1l)).thenReturn(group);
		when(groupDao.getGroupFollowedByUserAndId(1l, 1l)).thenReturn(group);
		when(group.getCreator()).thenReturn(user);
		try {
			cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.OWNER_CAN_NOT_BE_UNSUBSCRIBED.getCode(), e.getCode());
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
	public void testExecuteShouldFailWhenUserIsNotSuscribed() {
		when(userDao.findByToken("token")).thenReturn(user);
		when(groupDao.findOne(1l)).thenReturn(group);
		when(groupDao.getGroupFollowedByUserAndId(1l, 1l)).thenReturn(null);
		try {
			cmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.USER_NOT_SUBSCRIBED.getCode(), e.getCode());
		}
	}
}
