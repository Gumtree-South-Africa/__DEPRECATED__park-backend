package com.ebay.park.service.social.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.event.user.UserEvent;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.util.TwitterUtil;

public class TwitterTokenVerifierCmdTest {
	private static final String VALID_TOKEN = "validToken";
	
	private static final String TOKEN_SECRET = "tokenSecret";

	private static final String USER_ID = "validUserId";

	private static final String INVALID_TOKEN = "invalidToken";

	@Mock
	private TwitterUtil twitterUtil;
	
	@Mock
	private UserSocialDao userSocialDao;

	private User user;
	
	private UserSocial userSocialTW;
	private UserSocial userSocialFB;
	
	private TwitterTokenVerifierCmd twitterTokenVerifierCmd;
	
	@Before
	public void setUp(){
		twitterTokenVerifierCmd = new TwitterTokenVerifierCmd();
		initMocks(this);
		
		user = new User();
		Social socialTW = new Social();
		socialTW.setDescription(Social.TWITTER);
		userSocialTW = new UserSocial();
		userSocialTW.setSocial(socialTW);
		userSocialTW.setUser(user);
		
		Social socialFB = new Social();
		socialFB.setDescription(Social.FACEBOOK);
        userSocialFB = new UserSocial();
        userSocialFB.setSocial(socialFB);
        userSocialFB.setUser(user);

		List<UserSocial> userSocialList = new ArrayList<UserSocial>();
		userSocialList.add(userSocialTW);
		userSocialList.add(userSocialFB);
		
		user.setUserSocials(userSocialList);
		
		doThrow(
				ServiceException
						.createServiceException(ServiceExceptionCode.INVALID_TWITTER_TOKEN))
				.when(twitterUtil).tokenIsValid(INVALID_TOKEN, TOKEN_SECRET);
	
		doNothing().when(userSocialDao).delete(userSocialTW);
		
		twitterTokenVerifierCmd.setTwitterUtil(twitterUtil);
		twitterTokenVerifierCmd.setUserSocialDao(userSocialDao);
	}
	
	@Test
	public void succesfullyVerification(){
		userSocialTW.setToken(VALID_TOKEN);
		userSocialTW.setUserId(USER_ID);
		assertNull(twitterTokenVerifierCmd.execute(user));
		verify(twitterUtil, times(1)).tokenIsValid(anyString(), anyString());
	}
	
	@Test
	public void unsuccesfulVerification(){
		userSocialTW.setToken(INVALID_TOKEN);
		userSocialTW.setTokenSecret(TOKEN_SECRET);
		UserEvent userEvent = twitterTokenVerifierCmd.execute(user);
		assertEquals(userEvent.getUser(), user);
	}
	
	@Test
    public void givenNullSocialThenNothing(){
	    user.setUserSocials(null);
        userSocialTW.setToken(VALID_TOKEN);
        userSocialTW.setUserId(USER_ID);
        assertNull(twitterTokenVerifierCmd.execute(user));
        verify(twitterUtil, never()).tokenIsValid(anyString(), anyString());
    }
	
	@Test
    public void givenEmptySocialThenNothing(){
        user.setUserSocials(new ArrayList<UserSocial>());
        userSocialTW.setToken(VALID_TOKEN);
        userSocialTW.setUserId(USER_ID);
        assertNull(twitterTokenVerifierCmd.execute(user));
        verify(twitterUtil, never()).tokenIsValid(anyString(), anyString());
    }
}