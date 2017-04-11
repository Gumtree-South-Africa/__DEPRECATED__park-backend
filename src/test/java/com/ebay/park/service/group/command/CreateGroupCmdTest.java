package com.ebay.park.service.group.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
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
import com.ebay.park.service.group.dto.CreateGroupRequest;
import com.ebay.park.service.group.dto.CreateGroupResponse;


public class CreateGroupCmdTest {

	@Spy
	@InjectMocks
	private final CreateGroupCmd cmd = new CreateGroupCmd();

	@Mock
	private CreateGroupRequest request;

	@Mock
	private GroupDao groupDao;

	@Mock
	private UserDao userDao;

	@Mock
	private User user;

	@Mock
	private Group group;

	@Before
	public void setUp() {
		initMocks(this);
		when(request.getLocation()).thenReturn("33.2,12.3");
		when(request.getName()).thenReturn("groupName");
		when(request.getToken()).thenReturn("123456");
		when(user.getId()).thenReturn(1l);
		when(group.getId()).thenReturn(1l);

	}

	@Test
	public void testExecuteShouldSucceed() {
		when(userDao.findByToken("123456")).thenReturn(user);
		when(groupDao.save(any(Group.class))).thenReturn(group);

		CreateGroupResponse groupResponse = cmd.execute(request);

		verify(groupDao, times(2) ).save(any(Group.class));
		assertNotNull(groupResponse);

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

}
