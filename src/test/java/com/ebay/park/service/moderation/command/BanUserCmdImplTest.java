package com.ebay.park.service.moderation.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserStatusDescription;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.device.command.RemoveUserSessionsByUserCmd;
import com.ebay.park.service.device.dto.RemoveUserSessionsByUserRequest;
import com.ebay.park.service.moderation.dto.UserIdRequest;

public class BanUserCmdImplTest {

	@InjectMocks
	private BanUserCmdImpl banUserCmdImpl = new BanUserCmdImpl();

	@Mock
	private UserDao userDao;

	@Mock
	private RemoveUserSessionsByUserCmd removeUserSessionsByUserCmd;

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
			banUserCmdImpl.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_NOT_FOUND.getCode());
		}
	}

	@Test
	public void givenExistentUserBannedThenException() {
		when(userDao.findOne(request.getUserId())).thenReturn(user);
		when(user.getStatus()).thenReturn(UserStatusDescription.BANNED);
		try {
			banUserCmdImpl.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.MODERATION_USER_ALREADY_BANNED.getCode());
		}
	}

	@Test
	public void givenExistentUserThenBanUserSuccess() {
		when(userDao.findOne(request.getUserId())).thenReturn(user);
		when(user.getStatus()).thenReturn(UserStatusDescription.ACTIVE);
		ServiceResponse response = banUserCmdImpl.execute(request);
		assertEquals(ServiceResponse.SUCCESS.getStatusCode(), response.getStatusCode());
		verify(removeUserSessionsByUserCmd).execute(any(RemoveUserSessionsByUserRequest.class));
		verify(userDao).save(user);
	}

	@Test
	public void givenRemoveUserSessionByUserExecutionExceptionThenException() {
		when(userDao.findOne(request.getUserId())).thenReturn(user);
		when(user.getStatus()).thenReturn(UserStatusDescription.ACTIVE);
		doThrow(new RuntimeException()).when(removeUserSessionsByUserCmd)
				.execute(Mockito.any(RemoveUserSessionsByUserRequest.class));
		try {
			banUserCmdImpl.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_BAN_ERROR.getCode());
		}
	}
}
