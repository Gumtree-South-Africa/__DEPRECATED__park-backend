package com.ebay.park.service.moderation.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ebay.park.db.dao.UserAdminDao;
import com.ebay.park.db.entity.UserAdmin;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.moderation.dto.AdminSignInRequest;
import com.ebay.park.service.moderation.dto.AdminSignInResponse;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.util.PasswdUtil;

public class SignInAdminCmdImplTest {

	private static final String TOKEN = UUID.randomUUID().toString();

	@InjectMocks
	private SignInAdminCmdImpl signInAdminCmdImpl = new SignInAdminCmdImpl();

	@Mock
	private SessionService sessionService;

	@Mock
	private PasswdUtil passwdUtil;

	@Mock
	private UserAdminDao userAdminDao;

	@Mock
	private UserServiceHelper userServiceHelper;

	@Mock
	private AdminSignInRequest request;

	@Mock
	private UserAdmin user;

	@Before
	public void setUp() {
		initMocks(this);
	}

	@Test
	public void givenNonExistentUserAdminThenException() {
		when(userAdminDao.findByUsername(request.getUsername())).thenReturn(null);
		try {
			signInAdminCmdImpl.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.WRONG_SIGNIN_DATA.getCode());
		}
	}

	@Test
	public void givenExistentUserAdminWrongPasswordThenException() {
		when(userAdminDao.findByUsername(request.getUsername())).thenReturn(user);
		when(passwdUtil.equalsToHashedPassword(request.getPassword(), user.getPassword())).thenReturn(Boolean.FALSE);
		try {
			signInAdminCmdImpl.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.WRONG_SIGNIN_DATA.getCode());
		}
	}

	@Test
	public void givenExistentUserThenSignInAdminSuccess() {
		when(userAdminDao.findByUsername(request.getUsername())).thenReturn(user);
		when(user.getToken()).thenReturn(TOKEN);
		when(passwdUtil.equalsToHashedPassword(request.getPassword(), user.getPassword())).thenReturn(Boolean.TRUE);
		when(userServiceHelper.updateApplicationToken(user)).thenReturn(TOKEN);
		AdminSignInResponse response = signInAdminCmdImpl.execute(request);
		assertNotNull(response);
		assertEquals(TOKEN, response.getToken());
		verify(sessionService).createUserSessionCache(user, TOKEN);
	}

	@Test
	public void givenUserServiceHelperExecutionExceptionThenException() {
		when(userAdminDao.findByUsername(request.getUsername())).thenReturn(user);
		when(passwdUtil.equalsToHashedPassword(request.getPassword(), user.getPassword())).thenReturn(Boolean.TRUE);
		doThrow(new RuntimeException()).when(userServiceHelper).updateApplicationToken(user);
		try {
			signInAdminCmdImpl.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.IO_ERROR.getCode());
		}
	}

}
