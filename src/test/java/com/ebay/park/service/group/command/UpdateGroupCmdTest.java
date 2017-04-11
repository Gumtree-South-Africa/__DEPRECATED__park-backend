package com.ebay.park.service.group.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

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
import com.ebay.park.service.group.dto.UpdateGroupRequest;

public class UpdateGroupCmdTest {

	private static final String NAME = "name";
	private static final Long GROUP_ID = 1L;
	private static final Long SECOND_GROUP_ID = 2L;
	private static final String DESCRIPTION = "description";
	private static final String LOCATION = "34.99,46.57";
	private static final String EMPTY_STRING = "";


	@InjectMocks
	private UpdateGroupCmd updateGroupCmd = new UpdateGroupCmd();

	@Mock
	private GroupDao groupDao;

	@Mock
	private UserDao userDao;

	@Mock
	private UpdateGroupRequest request;

	@Mock
	private User user;

	@Mock
	private Group group;

	@Before
	public void setUp() {
		initMocks(this);
	}

	@Test
	public void givenNonExitingUserThenException() {
		when(userDao.findByToken(request.getToken())).thenReturn(null);
		try {
			updateGroupCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_UNAUTHORIZED.getCode());
		}
	}

	@Test
	public void givenExitingUserWithNullGroupThenException() {
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getId())).thenReturn(null);
		try {
			updateGroupCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.INVALID_GROUP.getCode());
		}
	}

	@Test
	public void givenExitingUserWithGroupNotOwnerThenException() {
		User owner = Mockito.mock(User.class);
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getId())).thenReturn(group);
		when(group.getCreator()).thenReturn(owner);
		try {
			updateGroupCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.INVALID_GROUP_OWNER.getCode());
		}
	}

	@Test
	public void givenExistingUserWithAlreadyExistingGroupThenException() {
		Group existedGroup = Mockito.mock(Group.class);
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getId())).thenReturn(group);
		when(group.getCreator()).thenReturn(user);
		when(request.getName()).thenReturn(NAME);
		when(groupDao.findByName(request.getName())).thenReturn(existedGroup);
		when(group.getId()).thenReturn(GROUP_ID);
		when(existedGroup.getId()).thenReturn(SECOND_GROUP_ID);
		try {
			updateGroupCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.GROUP_ALREADY_EXISTS.getCode());
		}
	}

	@Test
	public void givenExistingUserWithNonExistingGroupThenUpdateGroupSuccess() {
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getId())).thenReturn(group);
		when(group.getCreator()).thenReturn(user);
		when(request.getName()).thenReturn(NAME);
		when(groupDao.findByName(request.getName())).thenReturn(null);
		when(request.getDescription()).thenReturn(DESCRIPTION);
		when(request.getLocation()).thenReturn(LOCATION);
		ServiceResponse response = updateGroupCmd.execute(request);
		assertNotNull(response);
		assertEquals(ServiceResponse.SUCCESS.getStatusCode(), response.getStatusCode());
		verify(groupDao).save(group);
	}
	
	@Test
	public void givenExistingUserWithNonExistingGroupAndEmptyDataThenUpdateGroupSuccess() {
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getId())).thenReturn(group);
		when(group.getCreator()).thenReturn(user);
		when(request.getName()).thenReturn(EMPTY_STRING);
		when(groupDao.findByName(request.getName())).thenReturn(null);
		when(request.getDescription()).thenReturn(EMPTY_STRING);
		when(request.getLocation()).thenReturn(EMPTY_STRING);
		ServiceResponse response = updateGroupCmd.execute(request);
		assertNotNull(response);
		assertEquals(ServiceResponse.SUCCESS.getStatusCode(), response.getStatusCode());
		verify(groupDao).save(group);
	}


	@Test
	public void givenExistingUserWithNonExistingGroupSaveGroupExceptionThenException() {
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getId())).thenReturn(group);
		when(group.getCreator()).thenReturn(user);
		when(request.getName()).thenReturn(NAME);
		when(groupDao.findByName(request.getName())).thenReturn(null);
		when(request.getDescription()).thenReturn(DESCRIPTION);
		when(request.getLocation()).thenReturn(LOCATION);
		doThrow(new RuntimeException()).when(groupDao).save(group);
		try {
			updateGroupCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.ERROR_UPDATING_GROUP.getCode());
		}
	}

}
