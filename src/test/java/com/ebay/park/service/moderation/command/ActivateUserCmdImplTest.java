package com.ebay.park.service.moderation.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserStatusDescription;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderation.dto.UserIdRequest;

public class ActivateUserCmdImplTest {

	@InjectMocks
	private ActivateUserCmdImpl activateUserCmdImpl = new ActivateUserCmdImpl();

	@Mock
	private UserDao userDao;

	@Mock
	private UserIdRequest request;

	@Mock
	private User user;

	@Before
	public void setUp() {
		initMocks(this);
	}

	@Test
	public void givenNonExistentUserThenException() {
		when(userDao.findOne(request.getUserId())).thenReturn(null);
		try {
			activateUserCmdImpl.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_NOT_FOUND.getCode());
		}
	}

	@Test
	public void givenExistentUserStatusActiveThenException() {
		when(userDao.findOne(request.getUserId())).thenReturn(user);
		when(user.getStatus()).thenReturn(UserStatusDescription.ACTIVE);
		try {
			activateUserCmdImpl.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.MODERATION_USER_NOT_BANNED.getCode());
		}
	}

	@Test
	public void givenExistengUserThenActivateUserSuccess() {
		when(userDao.findOne(request.getUserId())).thenReturn(user);
		when(user.getStatus()).thenReturn(UserStatusDescription.BANNED);
		ServiceResponse response = activateUserCmdImpl.execute(request);
		assertEquals(ServiceResponse.SUCCESS.getStatusCode(), response.getStatusCode());
		verify(userDao).save(user);
	}
	
	@Test
	public void givenUserDaoSaveExecutionExceptionThenException() {
		when(userDao.findOne(request.getUserId())).thenReturn(user);
		when(user.getStatus()).thenReturn(UserStatusDescription.BANNED);
		doThrow(new RuntimeException()).when(userDao).save(user);
		try {
			activateUserCmdImpl.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_ACTIVATION_ERROR.getCode());
		}
	}
}
