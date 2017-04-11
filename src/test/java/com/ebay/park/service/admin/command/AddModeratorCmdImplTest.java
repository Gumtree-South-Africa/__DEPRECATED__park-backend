package com.ebay.park.service.admin.command;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ebay.park.db.dao.UserAdminDao;
import com.ebay.park.db.entity.UserAdmin;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.admin.dto.AddModeratorRequest;
import com.ebay.park.service.admin.dto.SmallUserAdmin;
import com.ebay.park.util.PasswdUtil;

/**
 * Unit test for {@link AddModeratorCmdImpl}.
 */
public class AddModeratorCmdImplTest {

	private static final String EMAIL = "mail@mail.com";
	private static final String USERNAME = "username";
	private static final String PWD = "pwd";

	@InjectMocks
	private AddModeratorCmdImpl cmd;
	
	@Mock
	private UserAdminDao userAdminDao;

	@Mock
	private PasswdUtil passwdUtil;
	
	private AddModeratorRequest request;
	
	@Before
	public void setUp() {
		initMocks(this);
		request = new AddModeratorRequest();
		request.setEmail(EMAIL);
		request.setUsername(USERNAME);
		request.setPassword(PWD);
		
		UserAdmin moderator = new UserAdmin();
		when(userAdminDao.save(any(UserAdmin.class))).thenReturn(moderator);
		moderator.setEmail(EMAIL);
		moderator.setUsername(USERNAME);
	}

	@Test
	public void givenValidRequestWhenExecutingThenAddModerator() {
		SmallUserAdmin response = cmd.execute(request);
		assertEquals(response.getEmail(), EMAIL);
		assertEquals(response.getUsername(), USERNAME);
	}

	@Test
	public void givenDuplicatedUsernameWhenExecutingThenException() {
		when(userAdminDao.findByUsername(USERNAME)).thenReturn(new UserAdmin());

		try {
			cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.USERNAME_DUPLICATED.getCode(), e.getCode());
		}
	}
	
	@Test
	public void givenDuplicatedEmailWhenExecutingThenException() {
		when(userAdminDao.findByEmail(EMAIL)).thenReturn(new UserAdmin());

		try {
			cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.EMAIL_DUPLICATED.getCode(), e.getCode());
		}
	}

	@Test
	public void givenSavingErrorWhenExecutingThenException() {
		doThrow(Exception.class).when(userAdminDao).save(any(UserAdmin.class));
		try {
			cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.ADD_MODERATOR_ERROR.getCode(), e.getCode());
		}
	}
}
