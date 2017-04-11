package com.ebay.park.service.user.command;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.springframework.test.util.ReflectionTestUtils;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Access;
import com.ebay.park.db.entity.Idiom;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserStatusDescription;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.device.dto.DeviceRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.command.CreateSessionCmd;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.dto.SignInRequest;
import com.ebay.park.service.user.dto.SignInResponse;
import com.ebay.park.service.user.dto.SignRequest;
import com.ebay.park.util.PasswdUtil;

public class SignInCmdTest {

	private static final String TOKEN = "qwerty";
	private static final int signInMaxAttemptsMinutes = 30;
	private static final String USERNAME = "john";
	private static final String EXPECTED_LANG = "en-US";
	private static final String PASSWORD = "password";
	private static final Long USER_ID = 1L;
	private static final String EMAIL = "1@2.com";
	private static final String PASSWORD_CHANGED = PASSWORD + "-changed";
	
	@InjectMocks
	private SignInCmd signin = new SignInCmd();

	@Mock
	private UserServiceHelper userServiceHelper;

	@Mock
	private PasswdUtil passwdUtil;

	@Mock
	private SessionService sessionService;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Mock
	private DeviceRequest deviceReq;
	
	@Mock
	private UserDao userDao;

	@Mock
	private CreateSessionCmd createSessionCmd;
	
	@Before
	public void setUp(){
		initMocks(this);
		Mockito.when(createSessionCmd.execute(any(SignRequest.class))).thenReturn(TOKEN);
	}
	
	@Test
	public void testExecuteSucceeds() throws Exception {
		SignInRequest request = new SignInRequest();
		request.setUsername(USERNAME);
		request.setPassword(PASSWORD);
		request.setDevice(deviceReq);

		User user = new User();
		user.setUserId(USER_ID);
		user.setPassword(new byte[0]);
		Idiom idiomMock = mockIdiom(EXPECTED_LANG);
		user.setIdiom(idiomMock);
		user.setStatus(UserStatusDescription.ACTIVE);

		when(userServiceHelper.findUserByUsernameOrEmail(eq(USERNAME), any(String.class))).thenReturn(user);	
		when(passwdUtil.equalsToHashedPassword(PASSWORD, user.getPassword())).thenReturn(true);

		SignInResponse response = signin.execute(request);
		
		assertNotNull(response.getToken());
		verify(createSessionCmd).execute(any(SignInRequest.class));
		verify(passwdUtil).equalsToHashedPassword(PASSWORD, user.getPassword());
	}

	@Test
	public void testExecuteFailsWhenUsernameOrEmailNotFound() throws Exception {
		SignInRequest request = new SignInRequest();
		request.setUsername(USERNAME);
		User userMock = mockEmptyUser();

		when(userServiceHelper.findUserByUsernameOrEmail(eq(USERNAME), any(String.class))).thenReturn(userMock);
		Mockito.doThrow(ServiceException.createServiceException(ServiceExceptionCode.WRONG_SIGNIN_DATA)).when(userServiceHelper).assertUserNotNull(userMock);
		
		exception.expect(ServiceException.class);
		exception.expectMessage(ServiceExceptionCode.WRONG_SIGNIN_DATA.getMessage());

		try {
			signin.execute(request);

		} finally {
			verify(userServiceHelper).findUserByUsernameOrEmail(USERNAME, null);
		}
	}

	@Test
	public void testExecuteThrowsInvalidPwdWhenPasswordIsInvalid() {
		SignInRequest request = new SignInRequest();
		request.setUsername(USERNAME);
		request.setPassword(PASSWORD);

		User user = new User();
		user.setUserId(USER_ID);
		user.setPassword(new byte[0]);

		when(userServiceHelper.findUserByUsernameOrEmail(eq(USERNAME), any(String.class))).thenReturn(user);
		when(passwdUtil.equalsToHashedPassword(PASSWORD, user.getPassword())).thenReturn(false);

		exception.expect(ServiceException.class);
		exception.expectMessage(ServiceException.createServiceException(ServiceExceptionCode.WRONG_SIGNIN_DATA).getMessage());

		try {
			signin.execute(request);

		} finally {
			verify(userServiceHelper).findUserByUsernameOrEmail(USERNAME, null);
			verify(passwdUtil).equalsToHashedPassword(PASSWORD, user.getPassword());
		}
	}
	
	@Test
	public void testExecuteThrowsInvalidPwdWhenPasswordIsInvalid_OneShotLeft() {
		SignInRequest request = new SignInRequest();
		request.setUsername(USERNAME);
		request.setPassword(PASSWORD);

		User user = new User();
		user.setUserId(USER_ID);
		user.setPassword(new byte[0]);

		when(userServiceHelper.findUserByUsernameOrEmail(eq(USERNAME), any(String.class))).thenReturn(user);
		when(passwdUtil.equalsToHashedPassword(PASSWORD, user.getPassword())).thenReturn(false);
		when(userServiceHelper.shouldBlockAccount(user.getAccess())).thenReturn(false);
		when(userServiceHelper.oneToBlockAccount(user.getAccess())).thenReturn(true);
		
		exception.expect(ServiceException.class);
		exception.expectMessage(ServiceException.createServiceException(ServiceExceptionCode.WRONG_SIGNIN_DATA_ONE_TO_BLOCK).getMessage());

		try {
			signin.execute(request);
		
		} finally {
			verify(userServiceHelper).findUserByUsernameOrEmail(USERNAME, null);
			verify(passwdUtil).equalsToHashedPassword(PASSWORD, user.getPassword());

			// No mail sent
			verify(userServiceHelper, never()).sendAccountBlockedEmail(any(User.class));
		}
	}
	
	@Test(expected = ServiceException.class)
	public void testMaxAttempsLockUser() {
		// given
		User userMock = mockEmptyUser();
		
		SignInRequest request = new SignInRequest();
		request.setPassword(PASSWORD);
		request.setUsername(USERNAME);
		request.setEmail(EMAIL);

		when(userMock.getPassword()).thenReturn(PASSWORD_CHANGED.getBytes());
		when(passwdUtil.equalsToHashedPassword(eq(PASSWORD), eq(PASSWORD_CHANGED.getBytes()))).thenReturn(Boolean.FALSE);
		when(userServiceHelper.findUserByUsernameOrEmail(eq(USERNAME), eq(EMAIL))).thenReturn(userMock);
		when(userServiceHelper.saveUser(eq(userMock))).thenReturn(userMock);
		
		when(userServiceHelper.shouldBlockAccount(eq(userMock.getAccess()))).thenReturn(true);
		when(userMock.getStatus()).thenReturn(UserStatusDescription.LOCKED);
		
		// when
		try {
			signin.execute(request);
		} catch (ServiceException e) {
			if (ServiceExceptionCode.ACCOUNT_LOCKED.getMessage().equals(e.getMessage())) {
				// then
				verify(userServiceHelper).saveUser(userMock);
				verify(userServiceHelper).findUserByUsernameOrEmail(eq(USERNAME), eq(EMAIL));
				verify(userMock).getPassword();
				verify(userMock.getAccess()).incFailedSignInAttempts();
				verify(userMock, new Times(3)).getAccess();
				verify(passwdUtil).equalsToHashedPassword(PASSWORD, PASSWORD_CHANGED.getBytes());
				verify(userServiceHelper).shouldBlockAccount(userMock.getAccess());
				throw e;	
			} else {
				throw new RuntimeException("Invalid exception caught, it should be ServiceException.ACCOUNT_LOCKED", e);
			}
		}

		throw new RuntimeException("ServiceException.ACCOUNT_LOCKED expected and no exception received");
	}
		
	
	@Test
	public void testUnLockUserAfterMaxMinutes() {
		User userMock = mock(User.class);

		SignInRequest request = new SignInRequest();
		request.setPassword(PASSWORD);
		request.setUsername(USERNAME);
		request.setEmail(EMAIL);

		when(userMock.getPassword()).thenReturn(PASSWORD_CHANGED.getBytes());
		when(passwdUtil.equalsToHashedPassword(eq(PASSWORD), eq(PASSWORD_CHANGED.getBytes()))).thenReturn(Boolean.TRUE);
		when(userMock.getStatus()).thenReturn(UserStatusDescription.LOCKED);
		
		when(userServiceHelper.canUserBeUnlocked(userMock)).thenCallRealMethod();
		ReflectionTestUtils.setField(userServiceHelper, "signInMaxAttemptsMinutes", signInMaxAttemptsMinutes);

		Access accessMock = mock(Access.class);
		accessMock.setTemporaryToken("1");
		when(userMock.getAccess()).thenReturn(accessMock);
		DateTime lastSignInAttempt = new DateTime();

		when(userMock.getAccess().getLastSignInAttempt()).thenReturn(
				lastSignInAttempt.minusMinutes(31).toDate());
		
		when(userDao.save(userMock)).thenReturn(userMock);
		ReflectionTestUtils.setField(userServiceHelper, "userDao", userDao);
		
		when(userServiceHelper.findUserByUsernameOrEmail(eq(USERNAME), eq(EMAIL))).thenReturn(userMock);
		when(userServiceHelper.saveUser(eq(userMock))).thenReturn(userMock);
		
		try {
			signin.execute(request);
		} catch (ServiceException e) {
				throw new RuntimeException("Invalid exception caught, it shouldn't receive any exception because the user should be unlocked", e);
		}

		verify(passwdUtil).equalsToHashedPassword(PASSWORD, PASSWORD_CHANGED.getBytes());		
		verify(userServiceHelper).saveUser(userMock);
		verify(userServiceHelper).findUserByUsernameOrEmail(eq(USERNAME), eq(EMAIL));
		verify(userMock).getPassword();
		verify(userMock).setStatus(UserStatusDescription.ACTIVE);
		verify(userMock.getAccess(), new Times(2)).resetFailedSignInAttempts();
		verify(userMock, new Times(4)).getAccess();
		verify(userDao).save(userMock);

	}

	@Test(expected = ServiceException.class)
	public void testHandleInvalidPasswordFailsWithInvalidPwd() {
		// given
		User userMock = mockEmptyUser();

		SignInRequest request = new SignInRequest();
		request.setPassword(PASSWORD);
		request.setUsername(USERNAME);
		request.setEmail(EMAIL);

		when(userServiceHelper.findUserByUsernameOrEmail(eq(USERNAME), eq(EMAIL))).thenReturn(userMock);
		when(userServiceHelper.saveUser(eq(userMock))).thenReturn(userMock);
		when(userMock.getPassword()).thenReturn(PASSWORD_CHANGED.getBytes());
		when(passwdUtil.equalsToHashedPassword(eq(PASSWORD), eq(PASSWORD_CHANGED.getBytes()))).thenReturn(Boolean.FALSE);

		// when
		try {
			signin.execute(request);
		} catch (ServiceException e) {
			if (ServiceExceptionCode.WRONG_SIGNIN_DATA.getMessage().equals(e.getMessage())) {
				// then
				verify(userServiceHelper).saveUser(userMock);
				verify(userServiceHelper).findUserByUsernameOrEmail(eq(USERNAME), eq(EMAIL));
				verify(userMock).getPassword();
				verify(userMock.getAccess()).incFailedSignInAttempts();
				verify(userMock, new Times(2)).getAccess();
				verify(passwdUtil).equalsToHashedPassword(PASSWORD, PASSWORD_CHANGED.getBytes());

				throw e;
			} else {
				throw new RuntimeException("Invalid exception caught, it should be ServiceException.INVALID_PWD", e);
			}
		}

		throw new RuntimeException("ServiceException.INVALID_PWD expected and no exception received");
	}
	
	
	@Test
	public void givenABannedUserWhenSignInThenUserBannedError() {
		//given
		User userMock = mock(User.class);

		SignInRequest request = new SignInRequest();
		request.setPassword(PASSWORD);
		request.setUsername(USERNAME);
		request.setEmail(EMAIL);

		when(userMock.getStatus()).thenReturn(UserStatusDescription.BANNED);
		when(userServiceHelper.findUserByUsernameOrEmail(eq(USERNAME), eq(EMAIL))).thenReturn(userMock);
		exception.expect(ServiceException.class);
		exception.expectMessage(ServiceException.createServiceException(ServiceExceptionCode.USER_BANNED_ERROR).getMessage());
		try {
			signin.execute(request);
		} finally {
			verify(userServiceHelper).findUserByUsernameOrEmail(eq(USERNAME), eq(EMAIL));
		}
	}
	

	private User mockEmptyUser() {
		User userMock = mock(User.class);
		when(userMock.getStatus()).thenReturn(UserStatusDescription.ACTIVE);
		Access accessMock = mock(Access.class);

		
		accessMock.setTemporaryToken("1");
		int NO_FAILED_TRIES = 0;
		when(accessMock.getFailedSignInAttempts()).thenReturn(NO_FAILED_TRIES);
		when(userMock.getAccess()).thenReturn(accessMock);

		return userMock;
	}

	private Idiom mockIdiom(String expectedLang) {
		Idiom idiomMock = mock(Idiom.class);
		when(idiomMock.getCode()).thenReturn(expectedLang);
		return idiomMock;
	}

}