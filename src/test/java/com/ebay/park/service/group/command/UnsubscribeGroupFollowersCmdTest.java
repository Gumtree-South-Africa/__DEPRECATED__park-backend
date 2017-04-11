package com.ebay.park.service.group.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.group.dto.UnsubscribeGroupFollowersRequest;

public class UnsubscribeGroupFollowersCmdTest {

	private static final Long FOLLOWER_ID = 1L;

	@InjectMocks
	private UnsubscribeGroupFollowersCmd unsubscribeGroupFollowersCmd = new UnsubscribeGroupFollowersCmd();

	@Mock
	private GroupDao groupDao;

	@Mock
	private UserDao userDao;

	@Mock
	private User user;

	@Mock
	private Group group;

	@Mock
	private UnsubscribeGroupFollowersRequest request;

	@Before
	public void setUp() {
		initMocks(this);
	}

	@Test
	public void givenNonExistentUserThenException() {
		when(userDao.findByToken(request.getToken())).thenReturn(null);
		try {
			unsubscribeGroupFollowersCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_UNAUTHORIZED.getCode());
		}
	}

	@Test
	public void givenExistentUserWithNoExistingGroupThenException() {
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getGroupId())).thenReturn(null);
		try {
			unsubscribeGroupFollowersCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.INVALID_GROUP.getCode());
		}
	}

	@Test
	public void givenExistentUserWithExistingGroupNotOwnerThenException() {
		User creator = Mockito.mock(User.class);
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getGroupId())).thenReturn(group);
		when(group.getCreator()).thenReturn(creator);
		try {
			unsubscribeGroupFollowersCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.INVALID_GROUP_OWNER.getCode());
		}
	}

	@Test
	public void givenExistingUserOwnerOfGroupThenUnsubscribeGroupFollowersSuccess() {
		List<Long> followerdsIdsModified = new ArrayList<Long>();
		followerdsIdsModified.add(FOLLOWER_ID);
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getGroupId())).thenReturn(group);
		when(group.getCreator()).thenReturn(user);
		when(group.removeFollowers(request.getFollowersIdsValidated())).thenReturn(followerdsIdsModified);
		ServiceResponse response = unsubscribeGroupFollowersCmd.execute(request);
		assertEquals(ServiceResponse.SUCCESS.getStatusCode(), response.getStatusCode());
		verify(groupDao).saveAndFlush(group);
		verify(userDao).save(user);
	}

	@Test
	public void givenExistingUserOwnerOfGroupNonSubscribedToGroupThenException() {
		List<Long> followerdsIdsModified = new ArrayList<Long>();
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getGroupId())).thenReturn(group);
		when(group.getCreator()).thenReturn(user);
		when(group.removeFollowers(request.getFollowersIdsValidated())).thenReturn(followerdsIdsModified);
		try {
			unsubscribeGroupFollowersCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USERS_NOT_SUBSCRIBED.getCode());
		}

	}

}
