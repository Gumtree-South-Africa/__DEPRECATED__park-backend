package com.ebay.park.service.social.command;

import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.event.user.UserEvent;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.util.FacebookUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author giriarte
 * 
 */
public class FacebookTokenVerifierCmdTest {

	private static final String VALID_TOKEN = "validToken";

	private static final String USER_ID = "validUserId";

	private static final String INVALID_TOKEN = "invalidToken";

	@Mock
	private FacebookUtil facebookUtil;
	
	@Mock
	private UserSocialDao userSocialDao;

	private User user;
	
	private UserSocial userSocial;
	
	private FacebookTokenVerifierCmd facebookTokenVerifierCmd;
	
	@Before
	public void setUp(){
		facebookTokenVerifierCmd = new FacebookTokenVerifierCmd();
		initMocks(this);
		
		user = new User();
		Social socialFB = new Social();
		socialFB.setDescription(Social.FACEBOOK);
		userSocial = new UserSocial();
		userSocial.setSocial(socialFB);
		userSocial.setUser(user);
		
		List<UserSocial> userSocialList = new ArrayList<UserSocial>();
		userSocialList.add(userSocial);
		
		user.setUserSocials(userSocialList);

		Mockito.doNothing().when(facebookUtil).tokenIsValid(VALID_TOKEN, USER_ID);
		
		Mockito.doThrow(ServiceException.createServiceException(ServiceExceptionCode.INVALID_FB_TOKEN)).when(facebookUtil).tokenIsValid(INVALID_TOKEN, USER_ID);
		
		Mockito.doNothing().when(userSocialDao).delete(userSocial);
		
		facebookTokenVerifierCmd.setFacebookUtil(facebookUtil);
		facebookTokenVerifierCmd.setUserSocialDao(userSocialDao);
	}
	
	@Test
	public void succesfullyVerification(){
		userSocial.setToken(VALID_TOKEN);
		userSocial.setUserId(USER_ID);
		assertNull(facebookTokenVerifierCmd.execute(user));
	}
	
	@Test
	public void unsuccesfulVerification(){
		userSocial.setToken(INVALID_TOKEN);
		userSocial.setUserId(USER_ID);
		UserEvent userEvent = facebookTokenVerifierCmd.execute(user);
		assertEquals(userEvent.getUser(), user);
	}
	
}