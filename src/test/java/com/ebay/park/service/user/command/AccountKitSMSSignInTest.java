package com.ebay.park.service.user.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ebay.park.db.dao.UserSessionDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.command.signin.AccountKitSMSSignInCmd;
import com.ebay.park.service.user.command.signin.SignInCommand;
import com.ebay.park.service.user.dto.SignInResponse;
import com.ebay.park.service.user.dto.signin.AccountKitSMSSignInRequest;
import com.ebay.park.service.user.dto.signin.SignInRequest;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class AccountKitSMSSignInTest {

	@InjectMocks
	private AccountKitSMSSignInCmd accountKitSMSSignInCmd = new AccountKitSMSSignInCmd();

	@Mock
	private UserServiceHelper userServiceHelper;

	@Mock
	private SignInCommand signInReqCmd;

	@Mock
	private ServiceCommand<SignInRequest, UserSession> createUserSession;

	@Mock
	private SessionService sessionService;

	@Mock
	private AccountKitSMSSignInRequest request;

	@Mock
	private User user;

	@Mock
	private UserSession userSession;

	@Mock
	private UserSessionDao userSessionDao;

	@Mock
	private SignInResponse signInResponse;

	@Before
	public void setUp() {
		initMocks(this);
	}

	@Test
	public void givenAccountKitSMSRequestThenSingInSuccess() {
		when(userServiceHelper.findUserByPhoneNumber(request.getMobilePhone())).thenReturn(user);
		when(signInReqCmd.execute(user)).thenReturn(signInResponse);
		when(createUserSession.execute(request)).thenReturn(userSession);
		SignInResponse response = accountKitSMSSignInCmd.execute(request);
		assertNotNull(response);
	}

	@Test
	public void givenAccountKitSMSUserNullThenException() {
		when(userServiceHelper.findUserByPhoneNumber(request.getMobilePhone())).thenReturn(null);
		try {
			accountKitSMSSignInCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_NOT_FOUND.getCode());
		}

	}

}
