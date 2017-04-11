package com.ebay.park.service.social.command;

import com.ebay.park.db.dao.SocialDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.*;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.service.social.SocialNotificationHelper;
import com.ebay.park.service.social.UserSocialHelper;
import com.ebay.park.service.social.dto.SocialNetworkConnectRequest;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.util.EmailVerificationUtil;
import com.ebay.park.util.FacebookUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class SocialConnectCmdTest {

	private static final String TOKEN = "validToken";
	private static final String USERNAME = "timMartins";
	private static final String SOCIAL_NETWORK_TWITTER = "twitter";
	private static final String SOCIAL_NETWORK_FACEBOOK = "facebook";
	private static final String SOCIALUSERID = "socialUserId";
	private static final String EMAIL = "UserFollowed@mail.com";
	private static final long FACEBOOK_ID = 1L;
	private static final long TWITTER_ID = 2L;
	private static final String SOCIAL_TOKEN = "social token";

	@Mock
	private User user;
	private Device device;
	private UserSession userSession;
	private UserSessionCache sessionCache;

	@InjectMocks
	private SocialConnectCmd socialConnectCmd;

	@Mock
	private UserDao userDao;

	@Mock
	private UserSocialDao userSocialDao;

	@Mock
	private FacebookUtil facebookUtil;

	@Mock
	private SocialDao socialDao;

	@Mock
	private SessionService sessionService;

	@Mock
	private UserServiceHelper signInHelper;

	@Mock
	private EmailVerificationUtil emailVerificationUtil;
	
	@Mock
	private SocialNotificationHelper socialNotificationHelper;

    @Mock
    private SocialNetworkConnectRequest request;

	@Mock
	private UserServiceHelper userServiceHelper;
	
	@Mock
	private UserSocialHelper userSocialHelper;

	@Before
	public void setUp() {
		initMocks(this);
		when(facebookUtil.getEmail(SOCIAL_TOKEN)).thenReturn(EMAIL);
		setUserAndSessionFields();
		setEmptyUserSocial();
		setDeviceFields();
		setRequestFields();
	}

	private void setUserAndSessionFields() {
		when(user.getEmail()).thenReturn(EMAIL);
		userSession = new UserSession();
		userSession.setUser(user);
		userSession.setDevice(device);
		sessionCache = new UserSessionCache(userSession);
		when(userDao.findByUsername(USERNAME)).thenReturn(user);
		when(sessionService.getUserSession(TOKEN)).thenReturn(sessionCache);
		sessionCache.setUsername(USERNAME);
	}

	private void setDeviceFields() {
		device = new Device();
		device.setDeviceId("id");
		device.setId(1l);
		device.setUserSession(userSession);
		device.setPlatform(DeviceType.ANDROID);
	}

	private void setRequestFields() {
		when(request.getToken()).thenReturn(TOKEN);
		when(request.getUserName()).thenReturn(USERNAME);
		when(request.getSocialNetwork()).thenReturn(SOCIAL_NETWORK_TWITTER);
		when(request.getSocialTokenSecret()).thenReturn(TOKEN);
		when(request.getSocialUserId()).thenReturn(SOCIALUSERID);
		when(request.getSocialToken()).thenReturn(SOCIAL_TOKEN);
	}

	private void setEmptyUserSocial() {
		UserSocial userSocial = new UserSocial();
		List<UserSocial> userSocials = new ArrayList<>();
		userSocials.add(userSocial);
		user.setUserSocials(userSocials);
	}

	@Test
	public void givenANewTwitterAccountWhenConnectingThenConnectAccount() {
		// given
		when(request.getToken()).thenReturn(TOKEN);
		when(request.getUserName()).thenReturn(USERNAME);
		when(request.getSocialUserId()).thenReturn(SOCIALUSERID);
		setTWRequest();

		// when
		Boolean result = socialConnectCmd.execute(request);

		// then
		assertTrue(result);
		verify(userServiceHelper).findUserByUsername(USERNAME);
		verify(sessionService).getUserSession(TOKEN);
	}

	@Test
	public void givenANewFBAccountWhenConnectingThenConnectAccount() {
		// given
		SocialNetworkConnectRequest request = Mockito.mock(SocialNetworkConnectRequest.class);

		user.setEmailVerified(Boolean.FALSE);

		when(request.getToken()).thenReturn(TOKEN);
		when(request.getUserName()).thenReturn(USERNAME);
		when(request.getSocialNetwork()).thenReturn(SOCIAL_NETWORK_FACEBOOK);
		when(request.getSocialUserId()).thenReturn(SOCIALUSERID);

		when(userServiceHelper.findUserByUsername(USERNAME)).thenReturn(user);

		// when
		Boolean result = socialConnectCmd.execute(request);

		// then
		assertTrue(result);
		verify(userServiceHelper).findUserByUsername(USERNAME);
		verify(sessionService).getUserSession(TOKEN);
	}

	@Test (expected = IllegalArgumentException.class)
	public void givenNullRequestWhenExecutingThenException() {
		socialConnectCmd.execute(null);
	}

	@Test
	public void givenFBRequestWhenExecutingThenVerifyEmail() {
		setFBRequest();
		when(userServiceHelper.findUserByUsername(USERNAME)).thenReturn(user);
		socialConnectCmd.execute(request);

		verify(emailVerificationUtil).verifyForFacebook(user, SOCIAL_TOKEN);
	}

	@Test
	public void givenTWRequestWhenExecutingThenDoNotVerifyEmail() {
		setTWRequest();
		socialConnectCmd.execute(request);

		verify(emailVerificationUtil, never()).verifyForFacebook(user, SOCIAL_TOKEN);
	}

	@Test
	public void givenUserWithPhoneNumberAndFBRequestAndNotNullEmailWhenExecutingThenDoNotSetFacebookEmailAddress() {
		setFBRequest();
		when(user.isMobileVerified()).thenReturn(true);
		when(userServiceHelper.findUserByUsername(USERNAME)).thenReturn(user);
		socialConnectCmd.execute(request);

		verify(userServiceHelper, never()).setUserEmail(user, EMAIL);
	}

	@Test
	public void givenUserWithPhoneNumberAndFBRequestAndNullEmailWhenExecutingThenSetFacebookEmailAddress() {
		setFBRequest();
		when(user.getEmail()).thenReturn(null);
		when(user.isMobileVerified()).thenReturn(true);
		when(userServiceHelper.findUserByUsername(USERNAME)).thenReturn(user);
		socialConnectCmd.execute(request);

		verify(userServiceHelper).setUserEmail(user, EMAIL);
		verify(userServiceHelper).saveUser(user);
	}

	@Test
	public void givenUserWithoutPhoneNumberAndFBRequestWhenExecutingThenDoNotSetFacebookEmailAddress() {
		setFBRequest();
		when(user.isMobileVerified()).thenReturn(false);
		when(userServiceHelper.findUserByUsername(USERNAME)).thenReturn(user);
		socialConnectCmd.execute(request);

		verify(userServiceHelper, never()).setUserEmail(user, EMAIL);
	}

	@Test
	public void givenUserWithPhoneNumberAndTWRequestWhenExecutingThenDoNotSetFacebookEmailAddress() {
		setTWRequest();
		when(user.isMobileVerified()).thenReturn(true);
		socialConnectCmd.execute(request);

		verify(userServiceHelper, never()).setUserEmail(user, EMAIL);
	}

	private void setFBRequest() {
		Social social = Mockito.mock(Social.class);
		when(socialDao.findByDescription(SOCIAL_NETWORK_FACEBOOK)).thenReturn(social);
		when(social.getSocialId()).thenReturn(FACEBOOK_ID);
		when(request.getSocialNetwork()).thenReturn(SOCIAL_NETWORK_FACEBOOK);
	}

	private void setTWRequest() {
		Social social = Mockito.mock(Social.class);
		when(socialDao.findByDescription(SOCIAL_NETWORK_TWITTER)).thenReturn(social);
		when(social.getSocialId()).thenReturn(TWITTER_ID);
		when(request.getSocialNetwork()).thenReturn(SOCIAL_NETWORK_TWITTER);
	}
}
