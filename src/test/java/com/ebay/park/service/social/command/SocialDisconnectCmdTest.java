package com.ebay.park.service.social.command;

import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocialPK;
import com.ebay.park.service.social.UserSocialHelper;
import com.ebay.park.service.social.dto.SocialNetworkDisconnectRequest;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.util.EmailVerificationUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link SocialDisconnectCmd}.
 * @author Julieta Salvadó
 */
public class SocialDisconnectCmdTest {

	private static final String DESCRIPTION = "desc";
	private static final String TOKEN = "validToken";
	private static final long SOCIAL_ID = 888L;
	private static final long USER_ID = 1L;
	private static final String EMAIL = "email@globant.com";

	@Mock
	private User user;

	@InjectMocks
	private SocialDisconnectCmd socialDisconnectCmd;

	@Mock
	private UserSocialDao userSocialDao;

	@Mock
	private UserSocialHelper userSocialHelper;

	@Mock
	private UserServiceHelper userHelper;

	@Mock
	private SocialNetworkDisconnectRequest request;

	@Mock
	private EmailVerificationUtil emailVerificationUtil;

	@Before
	public void setUp() {
		initMocks(this);
		setRequest();
		when(user.getEmail()).thenReturn(EMAIL);
	}

	@Test
	public void givenValidEntriesWhenExecutingThenDisconnect() {
		// given
		UserSocialPK id = new UserSocialPK(USER_ID, SOCIAL_ID);
		when(userSocialHelper.findUserSocialIdByUserAndSocialNetwork(user, DESCRIPTION)).thenReturn(id);
		when(userHelper.findUserByToken(TOKEN)).thenReturn(user);
		when(user.isMobileVerified()).thenReturn(false);
		// when
		Boolean result = socialDisconnectCmd.execute(request);

		// then
		assertTrue(result);
		verify(userSocialDao).delete(id);
	}

	@Test
	public void givenMobileUserWhenExecutingThenDisconnectAndUnverifiedEmail() {
		// given
		UserSocialPK id = new UserSocialPK(USER_ID, SOCIAL_ID);
		when(userSocialHelper.findUserSocialIdByUserAndSocialNetwork(user, DESCRIPTION)).thenReturn(id);
		when(userHelper.findUserByToken(TOKEN)).thenReturn(user);
		when(user.isMobileVerified()).thenReturn(true);
		// when
		Boolean result = socialDisconnectCmd.execute(request);

		// then
		assertTrue(result);
		verify(emailVerificationUtil).unverify(user);
	}

	@Test
	public void givenNotMobileUserWhenExecutingThenDisconnectAndKeepEmailAddress() {
		// given
		UserSocialPK id = new UserSocialPK(USER_ID, SOCIAL_ID);
		when(userSocialHelper.findUserSocialIdByUserAndSocialNetwork(user, DESCRIPTION)).thenReturn(id);
		when(userHelper.findUserByToken(TOKEN)).thenReturn(user);
		when(user.isMobileVerified()).thenReturn(false);
		// when
		Boolean result = socialDisconnectCmd.execute(request);

		// then
		assertTrue(result);
		verify(userHelper, never()).deleteUserEmail(user);
	}

	private void setRequest() {
		when(request.getSocialNetwork()).thenReturn(DESCRIPTION);
		when(request.getToken()).thenReturn(TOKEN);
	}
}
