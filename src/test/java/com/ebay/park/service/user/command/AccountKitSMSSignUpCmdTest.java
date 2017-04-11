package com.ebay.park.service.user.command;

import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.command.signup.AccountKitSMSSignUpCmd;
import com.ebay.park.service.user.command.signup.SignUpCommand;
import com.ebay.park.service.user.dto.SignUpResponse;
import com.ebay.park.service.user.dto.signin.SignInRequest;
import com.ebay.park.service.user.dto.signup.AccountKitSMSSignUpRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class AccountKitSMSSignUpCmdTest {
	private static final String AN_EXCEPTION_WAS_EXPECTED = "An exception was expected";
	private static final String MOBILE_PHONE = "12345678910";
	private static final String USER_NAME = "userName";
	private static final String EMPTY_STRING = "";
	private static final String TOKEN = "51fcc9cb-8e34-4799-ad19-566cc4c8b408";

	@InjectMocks
	private AccountKitSMSSignUpCmd accountKitSMSSignUpCmd = new AccountKitSMSSignUpCmd();

	@Mock
	private SignUpCommand signUpCmd;

	@Mock
	private UserServiceHelper userServiceHelper;

	@Mock
	private ServiceCommand<SignInRequest, UserSession> createUserSession;

	@Mock
	private SessionService sessionService;

	@Mock
	private UserDao userDao;

	@Mock
	private AccountKitSMSSignUpRequest request;

	@Mock
	private User user;

	@Mock
	private UserSession userSession;

	@Before
	public void setUp() {
		initMocks(this);
		when(request.getMobilePhone()).thenReturn(MOBILE_PHONE);
	}

	@Test
	public void givenAccountKitSMSSignUpRequestWithUserNameThenSignUpSuccess() {
		when(user.getUsername()).thenReturn(USER_NAME);
		when(createUserSession.execute(request)).thenReturn(userSession);
		when(userSession.getToken()).thenReturn(TOKEN);
		when(user.getMobile()).thenReturn(MOBILE_PHONE);
		when(signUpCmd.execute(request)).thenReturn(user);
		SignUpResponse response = accountKitSMSSignUpCmd.execute(request);
		assertNotNull(response);
		assertEquals(USER_NAME, response.getUsername());
		assertEquals(TOKEN, response.getToken().toString());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void givenAccountKitSMSSignUpRequestUserSessionExceptionThenException() {
		when(user.getUsername()).thenReturn(USER_NAME);
		when(userSession.getToken()).thenReturn(TOKEN);
		when(signUpCmd.execute(request)).thenReturn(user);
		try {
			when(createUserSession.execute(request)).thenThrow(NullPointerException.class);
			accountKitSMSSignUpCmd.execute(request);
			fail(AN_EXCEPTION_WAS_EXPECTED);
		} catch (ServiceException e) {
			assertEquals(e.getClass(), ServiceException.class);
		}
		
	}

	@Test
	public void givenAccountKitSMSSignUpRequestWithNoUserNameThenSignUpSuccess() {
		when(user.getUsername()).thenReturn(EMPTY_STRING);
		when(user.getMobile()).thenReturn(MOBILE_PHONE);
		when(userDao.findByUsername(Mockito.anyString())).thenReturn(null);
		when(createUserSession.execute(request)).thenReturn(userSession);
		when(userSession.getToken()).thenReturn(TOKEN);
		when(signUpCmd.execute(request)).thenReturn(user);
		SignUpResponse response = accountKitSMSSignUpCmd.execute(request);
		assertNotNull(response);
		assertEquals(TOKEN, response.getToken().toString());
	}

	@Test
	public void givenAccountKitSMSSignUpRequestUserNameAlreadyExistThenSignUpSuccess() {
		when(user.getUsername()).thenReturn(EMPTY_STRING);
		when(user.getMobile()).thenReturn(MOBILE_PHONE);
		when(userDao.findByUsername(Mockito.anyString())).thenReturn(user).thenReturn(null);
		when(createUserSession.execute(request)).thenReturn(userSession);
		when(userSession.getToken()).thenReturn(TOKEN);
		when(signUpCmd.execute(request)).thenReturn(user);
		SignUpResponse response = accountKitSMSSignUpCmd.execute(request);
		assertNotNull(response);
		assertEquals(TOKEN, response.getToken().toString());
	}

	@Test
	public void givenNoMobileWhenExecutingThenException() {
		when(request.getMobilePhone()).thenReturn(null);
		when(signUpCmd.execute(request)).thenReturn(mock(User.class));

		try {
			accountKitSMSSignUpCmd.execute(request);
			fail(AN_EXCEPTION_WAS_EXPECTED);
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.EMPTY_PHONE_NUMBER.getCode());
		}

	}

}
