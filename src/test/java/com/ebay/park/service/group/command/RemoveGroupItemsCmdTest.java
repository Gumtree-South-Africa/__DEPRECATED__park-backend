package com.ebay.park.service.group.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.group.dto.RemoveGroupItemsRequest;

public class RemoveGroupItemsCmdTest {

	private static final int MAX_ITEMS_TO_PROCCESS = 1;

	@InjectMocks
	private RemoveGroupItemsCmd removeGroupItemsCmd = new RemoveGroupItemsCmd();

	@Mock
	private GroupDao groupDao;

	@Mock
	private UserDao userDao;

	@Mock
	private ItemDao itemDao;

	@Mock
	private RemoveGroupItemsRequest request;

	@Mock
	private User user;

	@Mock
	private Group group;

	@Before
	public void setUp() {
		initMocks(this);
		ReflectionTestUtils.setField(removeGroupItemsCmd, "MAX_ITEMS_TO_PROCCESS", MAX_ITEMS_TO_PROCCESS);
	}

	@Test
	public void givenNonExitingUserThenException() {
		when(userDao.findByToken(request.getToken())).thenReturn(null);
		try {
			removeGroupItemsCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_UNAUTHORIZED.getCode());
		}
	}

	@Test
	public void givenExitingUserWithNullGroupThenException() {
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getGroupId())).thenReturn(null);
		try {
			removeGroupItemsCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.INVALID_GROUP.getCode());
		}
	}

	@Test
	public void givenExitingUserWithGroupNotOwnerThenException() {
		User owner = Mockito.mock(User.class);
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getGroupId())).thenReturn(group);
		when(group.getCreator()).thenReturn(owner);
		try {
			removeGroupItemsCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.INVALID_GROUP_OWNER.getCode());
		}
	}

	@Test
	public void givenExistingUserGroupOwnerThenRemoveGroupItemsByUsersIdsSuccess() {
		List<Item> items = new ArrayList<Item>();
		Item item = Mockito.mock(Item.class);
		items.add(item);
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getGroupId())).thenReturn(group);
		when(group.getCreator()).thenReturn(user);
		when(request.isDeleteByUsersIds()).thenReturn(Boolean.TRUE);
		when(itemDao.getItemsByGroupIdAndUsersIds(request.getGroupId(), StatusDescription.ACTIVE,
				request.getIdsValidated())).thenReturn(items);
		ServiceResponse response = removeGroupItemsCmd.execute(request);
		assertNotNull(response);
		assertEquals(ServiceResponse.SUCCESS.getStatusCode(), response.getStatusCode());
		verify(itemDao).save(items);
	}

	@Test
	public void givenExistingUserGroupOwnerEmptyListThenException() {
		List<Item> items = new ArrayList<Item>();
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getGroupId())).thenReturn(group);
		when(group.getCreator()).thenReturn(user);
		when(request.isDeleteByUsersIds()).thenReturn(Boolean.TRUE);
		when(itemDao.getItemsByGroupIdAndUsersIds(request.getGroupId(), StatusDescription.ACTIVE,
				request.getIdsValidated())).thenReturn(items);
		try {
			removeGroupItemsCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.ITEMS_ARE_NOT_IN_THE_GROUP.getCode());
		}

	}

	@Test
	public void givenExistingUserGroupOwnerThenRemoveGroupItemsByIdsSuccess() {
		List<Item> items = new ArrayList<Item>();
		Item item = Mockito.mock(Item.class);
		items.add(item);
		Page<Item> page = new PageImpl<Item>(items);
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getGroupId())).thenReturn(group);
		when(group.getCreator()).thenReturn(user);
		when(request.isDeleteByUsersIds()).thenReturn(Boolean.FALSE);
		when(itemDao.getItemsById(request.getGroupId(), StatusDescription.ACTIVE, request.getIdsValidated(),
				new PageRequest(0, MAX_ITEMS_TO_PROCCESS))).thenReturn(page);
		ServiceResponse response = removeGroupItemsCmd.execute(request);
		assertNotNull(response);
		assertEquals(ServiceResponse.SUCCESS.getStatusCode(), response.getStatusCode());
		verify(itemDao).save(items);
	}

}
