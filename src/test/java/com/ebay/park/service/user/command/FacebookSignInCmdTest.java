package com.ebay.park.service.user.command;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.ebay.park.db.dao.SocialDao;
import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Idiom;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.db.entity.UserStatusDescription;
import com.ebay.park.service.device.dto.DeviceRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.command.CreateSessionCmd;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.dto.SignInRequest;
import com.ebay.park.service.user.dto.SignInResponse;
import com.ebay.park.service.user.dto.SignRequest;
import com.ebay.park.util.EmailVerificationUtil;
import com.ebay.park.util.FacebookUtil;

public class FacebookSignInCmdTest {
	@InjectMocks
	@Spy
	private FacebookSignInCmdImpl signin;

	@Mock
	private UserServiceHelper signInHelper;

	@Mock
	private SessionService sessionService;

	@Mock
	private UserSocialDao userSocialDao;

	@Mock
	private SocialDao socialDao;

	@Mock
	private FacebookUtil facebookUtil;

	@Mock
	private CreateSessionCmd createUserSessionCmd;

	@Mock
	private DeviceRequest device;

	@Mock
	private EmailVerificationUtil emailVerificationUtil;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp(){
		initMocks(this);
	}
	
	@Test
	public void testExecuteWhenTokenIsSaved() throws Exception {
		// given
		String requestFbToken = "request-fb-token";
		String requestFbUserId = "123456";
		String savedFbToken = "saved-fb-token";
		String expectedLang = "en-US";
		long userId = 123456L;

		SignInRequest request = new SignInRequest();
		request.setFbToken(requestFbToken);
		request.setFbUserId(requestFbUserId);

		User user = new User();
		user.setUserId(userId);
		Idiom idiomMock = mockIdiom(expectedLang);
		user.setIdiom(idiomMock);
		user.setStatus(UserStatusDescription.ACTIVE);

		UserSocial us = new UserSocial();
		us.setUser(user);

		when(signInHelper.findUserById(userId)).thenReturn(user);
		when(signInHelper.findUserByEmail(anyString())).thenReturn(user);
		when(userSocialDao.findByToken(eq(requestFbToken))).thenReturn(us);
		when(userSocialDao.findFacebookUser(eq(userId))).thenReturn(us);
		when(userSocialDao.findByUserId(requestFbUserId)).thenReturn(us);
		when(createUserSessionCmd.execute(Mockito.any(SignRequest.class))).thenReturn(savedFbToken);

		// when
		SignInResponse response = signin.execute(request);

		// then
		assertEquals("response has not the right session token", savedFbToken, response.getToken());

		verify(userSocialDao).findByUserId(requestFbUserId);
	}

	@Test
	public void testExecuteWhenTokenIsUnsaved() throws Exception {
		String expectedFbToken = "short-lived-token";
		String expectedFbUserId = "123456";
		String expectedEmail = "email";
		String expectedParkToken = "sessionToken";
		String expectedLang = "en-US";
		long expectedUserId = 1L;

		SignInRequest request = new SignInRequest();
		request.setFbToken(expectedFbToken);
		request.setFbUserId(expectedFbUserId);
		request.setDevice(device);

		User user = new User();
		user.setUserId(expectedUserId);
		Idiom idiomMock = mockIdiom(expectedLang);
		user.setIdiom(idiomMock);
		user.setStatus(UserStatusDescription.ACTIVE);

		UserSocial us = new UserSocial();
		us.setUser(user);
		us.setToken(expectedFbToken);
		
		Social social = new Social();
		social.setSocialId(2l);

		when(userSocialDao.findByToken(anyString())).thenReturn(null);
		when(facebookUtil.getEmail(expectedFbToken)).thenReturn(expectedEmail);
		when(signInHelper.createSessionToken()).thenReturn(expectedParkToken);
		when(signInHelper.findUserByEmail(expectedEmail)).thenReturn(user);
		when(createUserSessionCmd.execute(Mockito.any(SignRequest.class))).thenReturn(expectedParkToken);
		when(socialDao.findByDescription(Social.FACEBOOK)).thenReturn(social);

		SignInResponse response = signin.execute(request);

		assertEquals("response has not the correct session token", expectedParkToken, response.getToken());
		assertEquals("userSocial object does not have correct token", expectedFbToken, us.getToken());

		verify(facebookUtil).getEmail(expectedFbToken);
		verify(signInHelper).findUserByEmail(expectedEmail);
	}

	private Idiom mockIdiom(String expectedLang) {
		Idiom idiomMock = mock(Idiom.class);
		when(idiomMock.getCode()).thenReturn(expectedLang);
		return idiomMock;
	}

}