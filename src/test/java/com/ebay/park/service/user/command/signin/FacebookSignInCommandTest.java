package com.ebay.park.service.user.command.signin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ebay.park.db.dao.UserSessionDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.db.entity.UserStatusDescription;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.device.dto.DeviceRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.social.UserSocialHelper;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.command.signin.FacebookSignInCommand;
import com.ebay.park.service.user.dto.SignInResponse;
import com.ebay.park.service.user.dto.signin.FacebookSignInRequest;
import com.ebay.park.service.user.dto.signin.SignInRequest;
import com.ebay.park.util.EmailVerificationUtil;
import com.ebay.park.util.FacebookUtil;
import org.mockito.Spy;

public class FacebookSignInCommandTest {
	private static final String FB_USER_ID = "123";
	private static final String FB_TOKEN = "token";
	private static final String USER_TOKEN = "user_token";
	private static final String EMAIL = "abc@gmail.com";
	private static final Long USER_ID = 1L;
	private static final Long SOCIAL_ID = 2L;

	@InjectMocks
	@Spy
	private FacebookSignInCommand facebookSignInCommand;

	@Mock
	private ServiceCommand<SignInRequest, UserSession> createUserSession;

	@Mock
	private SessionService sessionService;
	
	@Mock
	private FacebookUtil facebookUtil;

	@Mock
	private UserServiceHelper signInHelper;

	@Mock
	private FacebookSignInRequest request;

	@Mock
	private UserSocial userSocial;

	@Mock
	private UserSession userSession;

	@Mock
	private User user;
	
	@Mock
    private UserSessionDao userSessionDao;

	@Mock
	private UserSocialHelper userSocialHelper;

	@Mock
	private EmailVerificationUtil emailVerificationUtil;

	@Before
	public void setUp() {
		initMocks(this);
		when(request.getFbToken()).thenReturn(FB_TOKEN);
		when(request.getFbUserId()).thenReturn(FB_USER_ID);
		when(createUserSession.execute(request)).thenReturn(userSession);
		when(userSession.getToken()).thenReturn(USER_TOKEN);
	}

	@Test
	public void givenFacebookSignInUserActiveRequestThenSignInSuccess() {
		when(userSocialHelper.findUserSocialByUserId(FB_USER_ID)).thenReturn(userSocial);
		when(userSocial.getUser()).thenReturn(user);
		when(signInHelper.findUserById(userSocial.getUser().getUserId())).thenReturn(user);
		when(user.getStatus()).thenReturn(UserStatusDescription.ACTIVE);
		SignInResponse response = facebookSignInCommand.execute(request);
		assertNotNull(response);
		assertEquals(USER_TOKEN, response.getToken());
	}

	@Test
	public void givenFacebookSignInUserSocialAndDeviceNullThenSignInSuccess() {
		when(userSocialHelper.findUserSocialByUserId(FB_USER_ID)).thenReturn(null);
		when(facebookUtil.getEmail(request.getFbToken())).thenReturn(EMAIL);
		when(signInHelper.findUserByEmail(EMAIL)).thenReturn(user);
		when(user.getStatus()).thenReturn(UserStatusDescription.ACTIVE);
		when(request.getDevice()).thenReturn(null);
		when(user.isEmailVerified()).thenReturn(Boolean.FALSE);
		SignInResponse response = facebookSignInCommand.execute(request);
		assertNotNull(response);
		assertEquals(USER_TOKEN, response.getToken());
	}

	@Test
	public void givenFacebookSignInUserBannedSocialAndDeviceNullThenSignInSuccess() {
		when(userSocialHelper.findUserSocialByUserId(FB_USER_ID)).thenReturn(null);
		when(facebookUtil.getEmail(request.getFbToken())).thenReturn(EMAIL);
		when(signInHelper.findUserByEmail(EMAIL)).thenReturn(user);
		when(user.getStatus()).thenReturn(UserStatusDescription.BANNED);
		try {
			facebookSignInCommand.execute(request);
			fail();

		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_BANNED_ERROR.getCode());
		}
	}

	@Test
	public void givenFacebookSignInUserSocialNullAndDeviceThenSignInSuccess() {
		DeviceRequest deviceRequest = Mockito.mock(DeviceRequest.class);
		Social social = Mockito.mock(Social.class);
		when(userSocialHelper.findUserSocialByUserId(FB_USER_ID)).thenReturn(null);
		when(facebookUtil.getEmail(request.getFbToken())).thenReturn(EMAIL);
		when(signInHelper.findUserByEmail(EMAIL)).thenReturn(user);
		when(user.getStatus()).thenReturn(UserStatusDescription.ACTIVE);
		when(request.getDevice()).thenReturn(deviceRequest);
		when(user.getUserId()).thenReturn(USER_ID);
		when(social.getSocialId()).thenReturn(SOCIAL_ID);
		when(user.isEmailVerified()).thenReturn(Boolean.FALSE);
		SignInResponse response = facebookSignInCommand.execute(request);
		assertNotNull(response);
		assertEquals(USER_TOKEN, response.getToken());
	}

	@Test
	public void givenFacebookSignInUserBannedRequestThenException() {
		when(userSocialHelper.findUserSocialByUserId(FB_USER_ID)).thenReturn(userSocial);
		when(userSocial.getUser()).thenReturn(user);
		when(signInHelper.findUserById(userSocial.getUser().getUserId())).thenReturn(user);
		when(user.getStatus()).thenReturn(UserStatusDescription.BANNED);
		try {
			facebookSignInCommand.execute(request);
			fail();

		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_BANNED_ERROR.getCode());
		}

	}

	@Test
	public void givenFacebookSignInUserLockedRequestThenException() {
		when(userSocialHelper.findUserSocialByUserId(FB_USER_ID)).thenReturn(userSocial);
		when(userSocial.getUser()).thenReturn(user);
		when(signInHelper.findUserById(userSocial.getUser().getUserId())).thenReturn(user);
		when(user.getStatus()).thenReturn(UserStatusDescription.LOCKED);
		when(signInHelper.canUserBeUnlocked(user)).thenReturn(Boolean.FALSE); //TODO what happens when true?
		try {
			facebookSignInCommand.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.ACCOUNT_LOCKED.getCode());
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenNullRequestWhenExecutingThenException() {
		facebookSignInCommand.execute(null);
	}
}
