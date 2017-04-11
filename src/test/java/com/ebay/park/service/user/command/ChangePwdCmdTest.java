package com.ebay.park.service.user.command;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.device.command.RemoveUserSessionsByUserCmd;
import com.ebay.park.service.user.dto.ChangePwdRequest;
import com.ebay.park.service.user.dto.ChangePwdResponse;
import com.ebay.park.util.PasswdUtil;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ChangePwdCmdTest {

	private static final String TOKEN = "token";
	private static final String NEW_PWD = "newPassword";

	@InjectMocks
	private ChangePwdCmd changePwdCmd = new ChangePwdCmd();

	@Mock
	private UserDao userDao;

	@Mock
	private PasswdUtil passwdUtil;

	@Mock
	private RemoveUserSessionsByUserCmd removeUserSessionsByUserCmd;

	@Mock
	private ChangePwdRequest request;

	@Mock
	private User user;

	@Before
	public void setUp() {
		initMocks(this);
	}

	@Test
	public void givenUserNewPasswordThenChangePasswordSuccess() {
		when(request.getToken()).thenReturn(TOKEN);
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(request.getNewPassword()).thenReturn(NEW_PWD);
		ChangePwdResponse response = changePwdCmd.execute(request);
		assertEquals(Boolean.TRUE, response.isSuccess());
	}

}
