/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.user.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ebay.park.db.dao.IdiomDao;
import com.ebay.park.db.dao.SocialDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Idiom;
import com.ebay.park.db.entity.NotificationConfig;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.db.entity.UserSocialPK;
import com.ebay.park.email.MailSender;
import com.ebay.park.service.device.dto.DeviceRequest;
import com.ebay.park.service.notification.NotificationService;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.command.CreateSessionCmd;
import com.ebay.park.service.social.SocialNotificationHelper;
import com.ebay.park.service.user.dto.SignRequest;
import com.ebay.park.service.user.dto.SignUpRequest;
import com.ebay.park.service.user.dto.SignUpResponse;
import com.ebay.park.util.EmailVerificationUtil;
import com.ebay.park.util.FacebookUtil;
import com.ebay.park.util.ParkTokenUtil;
import com.ebay.park.util.PasswdUtil;

/**
 * @author juan.pizarro
 * 
 */
public class SignUpCmdTest {

	private static final String VALID_USERNAME = "validUserName";
	private static final String VALID_PASSWORD = "validPass";
	private static final byte[] VALID_ENCRYPTED_PASSWORD = { 10, 10,
			20 };
	private static final String VALID_EMAIL = "blabla@google.com";
	private static final String VALID_LAT = "33.234232";
	private static final String VALID_LONG = "-23.342342";
	private static final String VALID_LOCATION_1 = VALID_LAT + "," + VALID_LONG;
	private static final String VALID_FACEBOOK_ID = "validFaceId";
	private static final String VALID_FACEBOOK_TOKEN = "validFaceToken";
	private static final UUID VALID_TOKEN = UUID.randomUUID();
	private static final String VALID_ST_TOKEN = VALID_TOKEN.toString();
	private static final String VALID_LANG = "es";
	private static final String VALID_BIRTHDAY = "2001-11-11";
	private static final String VALID_GENDER = "M";
	private static final String VALID_MOBILE = "555-5555";

	@InjectMocks
	private final SignUpCmd signUpCmd = new SignUpCmd();

	@Mock
	private UserDao userDaoMock;

	@Mock
	private PasswdUtil passwdUtilMock;

	@Mock
	private ParkTokenUtil tokenUtilMock;

	@Mock
	private SocialDao socialDao;

	@Mock
	private IdiomDao idiomDaoMock;

	@Mock
	private SessionService sessionServiceMock;

	@Mock
	private UserSocialDao userSocialDao;

	@Mock
	private FacebookUtil facebookUtil;

	@Mock
	private MailSender emailSender;

	@Mock
	private SendEmailVerificationCmd sendEmailVerificationCmd;

	@Mock
	DeviceRequest deviceReq;

	@Mock
	private NotificationService notificationServiceMock;
	
	@Mock
	private CreateSessionCmd createSessionCmd;

	@Mock
	private EmailVerificationUtil emailVerificationUtil;
	
	@Mock
	private SocialNotificationHelper socialNotificationHelper;

	@Before
	public void setUp(){
		initMocks(this);
	}
	
	@Test
	public void testExecuteSignUpSuccessfully() throws Exception {
		// given
		String expectedUsername = VALID_USERNAME;
		String expectedPassword = VALID_PASSWORD;
		String expectedEmail = VALID_EMAIL;
		String expectedLocation = VALID_LOCATION_1;
		String expectedLang = VALID_LANG;
		String expectedStringToken = VALID_ST_TOKEN;
		UUID expectedToken = VALID_TOKEN;
		byte[] expectedEncryptedPassword = VALID_ENCRYPTED_PASSWORD;
		String expectedGender = VALID_GENDER;
		String expectedBirthday = VALID_BIRTHDAY;
		String expectedMobile = VALID_MOBILE;

		Long expectedUserId = 10L;

		SignUpRequest request = mock(SignUpRequest.class);
		mockSignUpRequest(expectedUsername, expectedPassword, expectedEmail,
				expectedLocation, expectedLang, request, deviceReq,
				expectedGender, expectedBirthday, expectedMobile);
		mockDeviceRequest(deviceReq);

		Idiom idiomMock = mockIdiom(expectedLang);
		User userMock = mockUser(expectedUsername, expectedEmail,
				expectedUserId, expectedEncryptedPassword, expectedToken,
				idiomMock);

		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		
		when(createSessionCmd.execute(any(SignRequest.class))).thenReturn(expectedStringToken);

		when(idiomDaoMock.findByCode(expectedLang)).thenReturn(idiomMock);
		when(passwdUtilMock.hash(request.getPassword())).thenReturn(
				expectedEncryptedPassword);
		when(tokenUtilMock.createSessionToken())
				.thenReturn(expectedStringToken);
		when(userDaoMock.save(userCaptor.capture())).thenReturn(userMock);

		@SuppressWarnings("unchecked")
		List<NotificationConfig> notificationConfigsMock = mock(ArrayList.class);
		when(notificationServiceMock.getAllNotificationConfig()).thenReturn(
				notificationConfigsMock);

		// when
		SignUpResponse actualResponse = signUpCmd.execute(request);

		// then
		assertNotNull("The response was not expected to be null.",
				actualResponse);
		assertEquals(actualResponse.getToken(), expectedToken);
		assertEquals(actualResponse.getUsername(), expectedUsername);

		//@formatter:off
		assertSignUpSuccessUserCaptor(expectedUsername
				, expectedEmail
				, expectedLang
				, expectedStringToken
				, expectedEncryptedPassword
				, userCaptor);
		//@formatter:on

		verify(userDaoMock, times(2)).save(userCaptor.getValue());
		verify(createSessionCmd).execute(request);
		verify(idiomDaoMock).findByCode(expectedLang);
		verify(passwdUtilMock).hash(expectedPassword);
		verify(tokenUtilMock).createSessionToken();
		verify(notificationServiceMock, times(1)).getAllApprovedNotificationConfig();
	}

	private void mockDeviceRequest(DeviceRequest deviceReq) {
		when(deviceReq.getDeviceId()).thenReturn("12345");
		when(deviceReq.getDeviceType()).thenReturn(
				com.ebay.park.db.entity.DeviceType.IOS.getValue());
	}

	private User mockUser(String expectedUsername, String expectedEmail,
			Long expectedUserId, byte[] expectedEncryptedPassword,
			UUID expectedToken, Idiom idiomMock) {
		User userMock = mock(User.class);
		when(userMock.getIdiom()).thenReturn(idiomMock);
		when(userMock.getId()).thenReturn(expectedUserId);
		when(userMock.getUsername()).thenReturn(expectedUsername);
		when(userMock.getEmail()).thenReturn(expectedEmail);
		when(userMock.getPassword()).thenReturn(expectedEncryptedPassword);
		when(userMock.getToken()).thenReturn(expectedToken.toString());
		return userMock;
	}

	private Idiom mockIdiom(String expectedLang) {
		Idiom idiomMock = mock(Idiom.class);
		when(idiomMock.getCode()).thenReturn(expectedLang);
		return idiomMock;
	}

	private void mockSignUpRequest(String expectedUsername,
			String expectedPassword, String expectedEmail,
			String expectedLocation, String expectedLang,
			SignUpRequest request, DeviceRequest device, String expectedGender,
			String expectedBirthday, String expectedMobile) {
		when(request.getUsername()).thenReturn(expectedUsername);
		when(request.getPassword()).thenReturn(expectedPassword);
		when(request.getEmail()).thenReturn(expectedEmail);
		when(request.getLocation()).thenReturn(expectedLocation);
		when(request.getLang()).thenReturn(expectedLang);
		when(request.getDevice()).thenReturn(device);

		when(request.getMobile()).thenReturn(expectedMobile);
	}

	@Test
	public void testExecuteFacebookSignUpSuccessfully() throws Exception {
		// given
		String expectedUsername = VALID_USERNAME;
		String expectedPassword = VALID_PASSWORD;
		String expectedEmail = VALID_EMAIL;
		String expectedLocation = VALID_LOCATION_1;
		String expectedLang = VALID_LANG;
		String expectedStringToken = VALID_ST_TOKEN;
		UUID expectedToken = VALID_TOKEN;
		byte[] expectedEncryptedPassword = VALID_ENCRYPTED_PASSWORD;
		Long expectedUserId = 10L;
		String expectedFacebookId = VALID_FACEBOOK_ID;
		String expectedFacebookToken = VALID_FACEBOOK_TOKEN;
		String expectedGender = VALID_GENDER;
		String expectedBirthday = VALID_BIRTHDAY;
		String expectedMobile = VALID_MOBILE;
		Long expectedSocialId = 100L;

		SignUpRequest request = mock(SignUpRequest.class);
		mockSignUpRequest(expectedUsername, expectedPassword, expectedEmail,
				expectedLocation, expectedLang, request, deviceReq,
				 expectedGender,
				expectedBirthday, expectedMobile);

		when(request.getFacebookToken()).thenReturn(expectedFacebookToken);
		when(request.getFacebookUserId()).thenReturn(expectedFacebookId);

		Idiom idiomMock = mockIdiom(expectedLang);

		Social socialMock = mock(Social.class);
		when(socialMock.getSocialId()).thenReturn(expectedSocialId);
		when(socialMock.getDescription()).thenReturn(Social.FACEBOOK);

		when(createSessionCmd.execute(any(SignRequest.class))).thenReturn(expectedStringToken);
		when(tokenUtilMock.createSessionToken())
				.thenReturn(expectedStringToken);
		when(passwdUtilMock.hash(request.getPassword())).thenReturn(
				expectedEncryptedPassword);
		when(socialDao.findByDescription(Social.FACEBOOK)).thenReturn(
				socialMock);

		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		ArgumentCaptor<UserSocial> userSocialCaptor = ArgumentCaptor
				.forClass(UserSocial.class);

		User userMock = mockUser(expectedUsername, expectedEmail,
				expectedUserId, expectedEncryptedPassword, expectedToken,
				idiomMock);
		UserSocial userSocialMock = mock(UserSocial.class);
		UserSocialPK userSocialIdMock = mock(UserSocialPK.class);

		mockUserSocialId(expectedSocialId, expectedUserId, userSocialIdMock);

		when(userDaoMock.save(userCaptor.capture())).thenReturn(userMock);
		when(userSocialDao.save(userSocialCaptor.capture())).thenReturn(
				userSocialMock);

		when(idiomDaoMock.findByCode(expectedLang)).thenReturn(idiomMock);

		// when
		SignUpResponse actualResponse = signUpCmd.execute(request);

		// then
		assertNotNull("The response was not expected to be null. ",
				actualResponse);
		assertEquals(actualResponse.getToken(), expectedToken);
		assertEquals(actualResponse.getUsername(), expectedUsername);

		//@formatter:off
		assertSignUpSuccessUserCaptor(expectedUsername
				, expectedEmail
				, expectedLang
				, expectedStringToken
				, expectedEncryptedPassword
				, userCaptor);
		//@formatter:on
		assertEquals(userSocialCaptor.getValue().getId().getSocialId(),
				expectedSocialId);
		assertNotEquals(userSocialCaptor.getValue().getId().getUserId(),
				expectedUserId);

		//verify(sessionServiceMock).createUserSession(any(User.class), (String) isNull());
//		verify(updateDeviceCmd).execute(deviceReq);
		verify(passwdUtilMock).hash(request.getPassword());
		verify(userDaoMock, times(2)).save(userCaptor.getValue());
		verify(socialDao).findByDescription(Social.FACEBOOK);
		verify(userSocialDao).save(userSocialCaptor.getValue());

	}

	private void mockUserSocialId(Long expectedSocialId, Long expectedUserId,
			UserSocialPK userSocialIdMock) {
		when(userSocialIdMock.getSocialId()).thenReturn(expectedSocialId);
		when(userSocialIdMock.getUserId()).thenReturn(expectedUserId);
	}


	private void assertSignUpSuccessUserCaptor(String expectedUsername,
			String expectedEmail, String expectedLang,
			String expectedStringToken, byte[] expectedEncryptedPassword,
			ArgumentCaptor<User> userCaptor) {
		assertEquals(userCaptor.getValue().getUsername(), expectedUsername);
		assertEquals(userCaptor.getValue().getEmail(), expectedEmail);
		assertEquals(userCaptor.getValue().getIdiom().getCode(), expectedLang);
		assertEquals(userCaptor.getValue().getPassword(),
				expectedEncryptedPassword);
		assertEquals(userCaptor.getValue().getToken(), expectedStringToken);
	}

	class UserMatcher extends ArgumentMatcher<User> {

		@Override
		public boolean matches(Object argument) {
			return User.class.isInstance(argument);
		}

	}
}
