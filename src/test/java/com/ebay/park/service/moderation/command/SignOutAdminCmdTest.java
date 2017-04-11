package com.ebay.park.service.moderation.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.session.SessionService;

public class SignOutAdminCmdTest {

	private static final String TOKEN = UUID.randomUUID().toString();

	@InjectMocks
	private SignOutAdminCmd signOutAdminCmd = new SignOutAdminCmd();

	@Mock
	private UserAdminDao userAdminDao;

	@Mock
	private SessionService sessionService;

	@Mock
	private UserAdmin user;

	@Before
	public void setUp() {
		initMocks(this);
	}

	@Test
	public void givenNonExistentUserAdminThenException() {
		when(userAdminDao.findByToken(TOKEN)).thenReturn(null);
		try {
			signOutAdminCmd.execute(TOKEN);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_NOT_FOUND.getCode());
		}
	}

	@Test
	public void givenExistentUserThenSignOutAdminSuccess() {
		when(userAdminDao.findByToken(TOKEN)).thenReturn(user);
		ServiceResponse response = signOutAdminCmd.execute(TOKEN);
		assertEquals(ServiceResponse.SUCCESS.getStatusCode(), response.getStatusCode());
		verify(userAdminDao).save(user);
		verify(sessionService).removeUserSession(TOKEN);
	}
}
