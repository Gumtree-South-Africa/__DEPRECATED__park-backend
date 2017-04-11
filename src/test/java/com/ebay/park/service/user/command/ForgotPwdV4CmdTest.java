package com.ebay.park.service.user.command;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.user.dto.EmailRequest;

/**
 * Test class for {@link ForgotPwdV4Cmd}
 * @author scalderon
 *
 */
public class ForgotPwdV4CmdTest {
	
	@Spy
	@InjectMocks
	private ForgotPwdV4Cmd forgetCmd = new ForgotPwdV4Cmd();
	
	@Mock
	private UserDao userDao;

	@Rule
	public ExpectedException exception = ExpectedException.none();


	@Before
	public void setUp(){
		initMocks(this);
	}
	
	@Test
	public void givenANullUserWhenExecuteThenUserNonExistEmailException() {
		EmailRequest request = new EmailRequest();
		request.setEmail("email@email.com");

		when(userDao.findByEmail(request.getEmail())).thenReturn(null);
		exception.expect(ServiceException.class);
		exception.expectMessage(ServiceExceptionCode.NON_EXISTENT_EMAIL.getMessage());

		forgetCmd.execute(request);
		verify(userDao).findByEmail(request.getEmail());
	}
}
