package com.ebay.park.service.user.command;

import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.UserSessionHelper;
import com.ebay.park.service.social.SocialNotificationHelper;
import com.ebay.park.service.social.UserSocialHelper;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.command.signup.FacebookSignUpCmdV3;
import com.ebay.park.service.user.command.signup.SignUpCommand;
import com.ebay.park.service.user.dto.SignUpResponse;
import com.ebay.park.service.user.dto.signin.SignInRequest;
import com.ebay.park.service.user.dto.signup.FacebookSignUpRequest;
import com.ebay.park.util.EmailVerificationUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FacebookSignUpCmdTest {

	private static final String EMAIL = "abc@gmail.com";
	private static final String FACEBOOK_TOKEN = "facebook_token";
	private static final String FACEBOOK_USER_ID = "facebook_user_id";
	private static final String USER_NAME = "userName";
	private static final long USER_ID = 1L;
	private static final long SOCIAL_ID = 2L;
	private static final String UUID = "0f14d0ab-9605-4a62-a9e4-5ed26688389b";

	@InjectMocks
	private FacebookSignUpCmdV3 facebookSignUpCmd = new FacebookSignUpCmdV3();

	@Mock
	private SignUpCommand signUpCmd;

	@Mock
	private UserServiceHelper userServiceHelper;

	@Mock
	private ServiceCommand<SignInRequest, UserSession> createUserSession;

	@Mock
	private SessionService sessionService;

	@Mock
	private FacebookSignUpRequest request;

	@Mock
	private User user;

	@Mock
	private Social social;

	@Mock
	private UserSession userSession;

	@Mock
    private EmailVerificationUtil emailVerificationUtil;
	
	@Mock
	private SocialNotificationHelper socialNotificationHelper;

	@Mock
	private UserSocialHelper userSocialHelper;

	@Mock
	private UserSessionHelper userSessionHelper;

	@Before
	public void setUp() {
		initMocks(this);
	}

	@Test
	public void givenFacebookSignUpRequestWithUserSessionThenSignUpSuccess() {
		when(user.getUserId()).thenReturn(USER_ID);
		when(user.getUsername()).thenReturn(USER_NAME);
		when(social.getSocialId()).thenReturn(SOCIAL_ID);
		when(request.getEmail()).thenReturn(EMAIL);
		when(request.getFacebookToken()).thenReturn(FACEBOOK_TOKEN);
		when(request.getFacebookUserId()).thenReturn(FACEBOOK_USER_ID);
		when(signUpCmd.execute(request)).thenReturn(user);
		when(createUserSession.execute(request)).thenReturn(userSession);
		when(userSessionHelper.createSession(user, request)).thenReturn(UUID);

		SignUpResponse response = facebookSignUpCmd.execute(request);

		assertNotNull(response);
		assertEquals(USER_NAME, response.getUsername());
		assertEquals(UUID, response.getToken().toString());
	}

	//TODO add cases
}
