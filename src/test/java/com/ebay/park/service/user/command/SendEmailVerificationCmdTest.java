package com.ebay.park.service.user.command;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Access;
import com.ebay.park.db.entity.User;
import com.ebay.park.email.MailSender;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.email.EmailVerificationHelper;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import com.ebay.park.email.Email;

public class SendEmailVerificationCmdTest {

	private static final String TOKEN = "token";
	private static final String INVALID_TOKEN = "invalidToken";
	private static final String EMAIL = "abc@gmail.com";
	private static final String EMAIL_WELCOME_SUBJECT = "[Vivanuncios] Welcome!";
	private static final String EMAIL_WELCOME_TEMPLATE = "templates/registration-confirm.vm";
	private static final String PARK_URL = "https://devrest.newapptest.com";

	@InjectMocks
	private SendEmailVerificationCmd sendEmailVerificationCmd = new SendEmailVerificationCmd();

	@Mock
	private MailSender mailSender;

	@Mock
	private UserDao userDao;

	@Mock
	private EmailVerificationHelper emailVerificationHelper;

	@Mock
	private User user;

	@Before
	public void setUp() {
		initMocks(this);
		ReflectionTestUtils.setField(sendEmailVerificationCmd, "emailWelcomeSubject", EMAIL_WELCOME_SUBJECT);
		ReflectionTestUtils.setField(sendEmailVerificationCmd, "emailWelcomeTemplate", EMAIL_WELCOME_TEMPLATE);
		ReflectionTestUtils.setField(sendEmailVerificationCmd, "parkUrl", PARK_URL);
	}

	@Test
	public void givenValidTokenThenSendEmailVerificationSuccess() {
		when(userDao.findByToken(TOKEN)).thenReturn(user);
		when(user.isEmailVerified()).thenReturn(Boolean.FALSE);
		when(user.getEmail()).thenReturn(EMAIL);
		when(user.getAccess()).thenReturn(Mockito.mock(Access.class));
		when(user.getAccess().getTemporaryToken()).thenReturn(TOKEN);
		sendEmailVerificationCmd.execute(TOKEN);
		verify(mailSender).sendAsync(Mockito.any(Email.class));

	}

	@Test
	public void givenInvalidTokenUserNullThenException() {
		when(userDao.findByToken(INVALID_TOKEN)).thenReturn(null);
		try {
			sendEmailVerificationCmd.execute(INVALID_TOKEN);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_UNAUTHORIZED.getCode());
		}

	}

	@Test
	public void givenUserWithEmailVerifiedThenException() {
		when(userDao.findByToken(TOKEN)).thenReturn(user);
		when(user.isEmailVerified()).thenReturn(Boolean.TRUE);
		when(user.hasFacebookAccountLinked()).thenReturn(Boolean.FALSE);
		try {
			sendEmailVerificationCmd.execute(TOKEN);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.EMAIL_ALREADY_VERIFIED.getCode());
		}

	}

}
