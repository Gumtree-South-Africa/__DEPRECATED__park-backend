/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.user.command;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.reflect.Whitebox;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Access;
import com.ebay.park.db.entity.Idiom;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.email.EmailVerificationHelper;
import com.ebay.park.service.notification.NotificationService;
import com.ebay.park.service.user.dto.VerifyEmailRequest;
import com.ebay.park.util.EmailVerificationUtil;

/**
 * @author federico.jaite
 * 
 */
public class VerifyEmailCmdTest {

	@InjectMocks
	private VerifyEmailCmd verifyEmailCmd = new VerifyEmailCmd();

	@Mock
	private UserDao userDao;

	@Mock
	private User user;

	@Mock
	private VerifyEmailRequest request;

	@Mock
	private Access access;

	@Mock
	private EmailVerificationHelper emailVerificationHelper;

	@Mock
	private NotificationService notificationService;

	@Mock
    private EmailVerificationUtil emailVerificationUtil;

	@Before
	public void setUp() {
		initMocks(this);
		Whitebox.setInternalState(verifyEmailCmd, "emailVerificationSuccessUrl", "success");
		
		when(request.getTemporaryToken()).thenReturn("encrypted-token");

		when(user.getAccess()).thenReturn(access);
		when(request.getEmail()).thenReturn("encrypted-email");

		Idiom idiom = mock(Idiom.class);
		when(idiom.getCode()).thenReturn("en");
		when(user.getIdiom()).thenReturn(idiom);

		when(access.getTemporaryToken()).thenReturn("token");

		when(userDao.findByEmail(any(String.class))).thenReturn(user);

		when(emailVerificationHelper.decrypt("encrypted-email")).thenReturn(
				"email@junit.org");
		when(emailVerificationHelper.decrypt("encrypted-token")).thenReturn(
				"token");
	}

	@Test
	public void testVerifyEmailSuccessfully() {
		String result = verifyEmailCmd.execute(request);
		assertEquals("success", result);
	}

}
