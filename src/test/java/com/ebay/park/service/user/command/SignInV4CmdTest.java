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

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.command.signin.SignInV4Cmd;
import com.ebay.park.service.user.dto.SignInRequest;

/**
 * Test class for {@link SignInV4Cmd}
 * @author scalderon
 *
 */
public class SignInV4CmdTest {
	
	private final static String EMAIL = "email@gmail.com";
	private final static String USERNAME = "USERNAME";
	
	@Spy
	@InjectMocks
	private SignInV4Cmd signInCmd = new SignInV4Cmd();
	
	@Mock
	private UserServiceHelper userServiceHelper;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	
	@Before
	public void setUp(){
		initMocks(this);
	}
	
	@Test
	public void givenANullUserWhenSignInThenUserNonExistEmailException() {
		//given
		SignInRequest request = new SignInRequest();
		request.setUsername(USERNAME);
		request.setEmail(EMAIL);
		
		when(userServiceHelper.findUserByUsernameOrEmail(USERNAME, EMAIL)).thenReturn(null);
		exception.expect(ServiceException.class);
		exception.expectMessage(ServiceExceptionCode.NON_EXISTENT_EMAIL.getMessage());

		signInCmd.execute(request);
		verify(userServiceHelper).findUserByUsernameOrEmail(USERNAME, EMAIL);
	}

}
