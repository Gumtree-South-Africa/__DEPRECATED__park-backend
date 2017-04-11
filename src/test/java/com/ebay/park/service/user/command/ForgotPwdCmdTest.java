package com.ebay.park.service.user.command;

import static org.junit.Assert.*;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Access;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserStatusDescription;
import com.ebay.park.email.PasswordEmailSender;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.device.command.RemoveUserSessionsByUserCmd;
import com.ebay.park.service.device.dto.RemoveUserSessionsByUserRequest;
import com.ebay.park.service.user.dto.EmailRequest;
import com.ebay.park.util.ParkTokenUtil;
import com.ebay.park.util.PasswdUtil;
import com.ebay.park.util.Password;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ForgotPwdCmdTest {

	private static final String PASS = "pass";
	private static final byte[] HASHED = new byte[]{ 108, -95, 61, 82, -54, 112, -56, -125, -32, -16, -69, 16, 30, 66, 90, -119,
			-24, 98, 77, -27, 29, -78, -46, 57, 37, -109, -81, 106, -124, 17, -128, -112 };
	@Spy
	@InjectMocks
	private ForgotPwdCmd forgetCmd = new ForgotPwdCmd();

	@Mock
	private UserDao userDao;

	@Mock
	private PasswdUtil passwdUtil;

	@Mock
	private ParkTokenUtil tokenUtil;

	@Mock
	private RemoveUserSessionsByUserCmd removeUserSessionsByUserCmd;

	@Mock
	private User user;

	@Mock
	private Access access;

	@Mock
	private Password password;

    @Mock
    @Qualifier("welcomePasswordEmailSender")
    private PasswordEmailSender sender;

    @Mock
    ApplicationContext context;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp(){
		initMocks(this);
		when(password.getHashedPassword()).thenReturn(HASHED);
		when(password.getSimplePassword()).thenReturn(PASS);
	}

	@Test (expected = IllegalArgumentException.class)
	public void givenNullEmailWhenExecutingThenException() {
		EmailRequest request = new EmailRequest();
		request.setEmail(null);
		forgetCmd.execute(request);
	}

	@Test
	public void testExecuteShouldSucceed() {
		EmailRequest request = new EmailRequest();
		request.setEmail("email@email.com");
		when(user.getAccess()).thenReturn(access);

		when(userDao.findByEmail(request.getEmail())).thenReturn(user);
		when(userDao.save(user)).thenReturn(user);
		when(user.getStatus()).thenReturn(UserStatusDescription.ACTIVE);
		when(removeUserSessionsByUserCmd.execute(Mockito.any(
				RemoveUserSessionsByUserRequest.class))).thenReturn(true);
        when(context.getBean(anyString())).thenReturn(sender);

		forgetCmd.execute(request);


		verify(userDao).findByEmail(request.getEmail());
		verify(userDao).save(user);
		verify(user).setPassword((byte[]) Mockito.any());
		verify(access).resetFailedSignInAttempts();
	}

	@Test
	public void givenANullUserWhenExecuteThenException() {
		EmailRequest request = new EmailRequest();
		request.setEmail("email@email.com");

		when(userDao.findByEmail(request.getEmail())).thenReturn(null);
		exception.expect(ServiceException.class);
		exception.expectMessage(ServiceExceptionCode.USER_NOT_FOUND.getMessage());

		forgetCmd.execute(request);

		verify(userDao).findByEmail(request.getEmail());
	}

	@Test (expected = IllegalArgumentException.class)
	public void givenANullRequestWhenExecuteThenException() {
		forgetCmd.execute(null);
	}

	@Test
	public void givenABannedUserWhenExecuteThenException() {
		//given
		EmailRequest request = new EmailRequest();
		request.setEmail("email@email.com");

		when(userDao.findByEmail(request.getEmail())).thenReturn(user);
		when(userDao.save(user)).thenReturn(user);
		when(user.getStatus()).thenReturn(UserStatusDescription.BANNED);
		exception.expect(ServiceException.class);
		exception.expectMessage(ServiceExceptionCode.ERROR_FORGOT_PASSWORD_WHEN_BANNED.getMessage());


		//when
		forgetCmd.execute(request);

		//then
		verify(userDao).findByEmail(request.getEmail());
	}

}