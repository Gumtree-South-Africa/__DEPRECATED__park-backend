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
import com.ebay.park.email.Email;
import com.ebay.park.email.MailSender;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderation.dto.ContactUserRequest;

public class ContactUserCmdImplTest {

	private static final String EMPTY_STRING = "";
	private static final String EMAIL = "abc@gmail.com";

	@InjectMocks
	private ContactUserCmdImpl contactUserCmdImpl = new ContactUserCmdImpl();

	@Mock
	private UserDao userDao;

	@Mock
	private MailSender mailSender;

	@Mock
	private User user;

	@Mock
	private ContactUserRequest request;

	@Before
	public void setUp() {
		initMocks(this);
	}

	@Test
	public void givenNonExistentUserThenException() {
		when(userDao.findOne(request.getUserId())).thenReturn(null);
		try {
			contactUserCmdImpl.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_NOT_FOUND.getCode());
		}
	}

	@Test
	public void givenExistentUserWithEmptyEmailThenException() {
		when(userDao.findOne(request.getUserId())).thenReturn(user);
		when(user.getEmail()).thenReturn(EMPTY_STRING);
		try {
			contactUserCmdImpl.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.EMAIL_USER_EMPTY_EMAIL.getCode());
		}
	}

	@Test
	public void givenExistentUserWithEmailThenSuccess() {
		when(userDao.findOne(request.getUserId())).thenReturn(user);
		when(user.getEmail()).thenReturn(EMAIL);
		ServiceResponse response = contactUserCmdImpl.execute(request);
		assertEquals(ServiceResponse.SUCCESS.getStatusCode(), response.getStatusCode());
		verify(mailSender).sendAsync(any(Email.class));
	}

	@Test
	public void givenMailSenderExecutionExceptionThenException() {
		when(userDao.findOne(request.getUserId())).thenReturn(user);
		when(user.getEmail()).thenReturn(EMAIL);
		doThrow(new RuntimeException()).when(mailSender).sendAsync(Mockito.any(Email.class));
		try {
			contactUserCmdImpl.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.EMAIL_SEND_ERROR.getCode());
		}
	}

}
