package com.ebay.park.service.social.validator;

import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.social.dto.SocialNetworkConnectRequest;
import com.ebay.park.util.FacebookUtil;
import com.ebay.park.util.TwitterUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author giriarte
 * 
 */
public class SocialConnectValidatorTest {

	private static final String VALID_FB_TOKEN = "validFBToken";

	private static final String VALID_FB_USERID = "validFBUserID";

	private static final String DUP_FACEBOOK_ID = "duplicatedFBID";

	private static final String VALID_TW_TOKEN = "validTWToken";

	private static final String VALID_TW_USERID = "validTWUserID";

	private static final String UNKNOWN_NETWORK = "unkonwnSocialNetwork";

	private static final String INVALID_FB_TOKEN = "invalidFBToken";

	private static final String INVALID_TW_TOKEN = "invalidTWToken";

	private static final String DUP_TW_ID = "duplicatedTwitterID";

	private static final String VALID_TW_SECRET = "validTwitTokenSecret";

	@Mock
	private FacebookUtil facebookUtil;

	@Mock
	private TwitterUtil twitterUtil;
	
	@Mock
	private UserSocialDao userSocialDao; 
	
	private SocialNetworkConnectRequest request;
	
	private SocialConnectValidator validator;
	
	@Before
	public void setUp(){
		initMocks(this);
		validator = new SocialConnectValidator();
		request = new SocialNetworkConnectRequest();
		validator.setUserSocialDao(userSocialDao);
		validator.setFacebookUtil(facebookUtil);
		validator.setTwitterUtil(twitterUtil);
		
		Mockito.when(
				userSocialDao
				.findBySocialUserIdAndNetwork(Social.FACEBOOK, VALID_FB_USERID) )
				.thenReturn(null);
		
		UserSocial userSoc = new UserSocial();
		List<UserSocial> userSocialArray = new ArrayList<UserSocial>();
		userSocialArray.add(userSoc);
		
		Mockito.when(
				userSocialDao
				.findBySocialUserIdAndNetwork(DUP_FACEBOOK_ID, Social.FACEBOOK) )
				.thenReturn(userSocialArray);
		
		Mockito.when(
				userSocialDao
				.findBySocialUserIdAndNetwork(DUP_TW_ID, Social.TWITTER) )
				.thenReturn(userSocialArray);
		
		Mockito.doNothing().when(
				facebookUtil)
				.tokenIsValid(VALID_FB_TOKEN, VALID_FB_USERID);
		
		Mockito.doNothing().when(
				twitterUtil)
				.tokenIsValid(VALID_TW_TOKEN, VALID_TW_USERID);
		
		Mockito.doNothing().when(
				facebookUtil)
				.tokenIsValid(VALID_FB_TOKEN, DUP_FACEBOOK_ID);
		
		Mockito.doNothing().when(
				twitterUtil)
				.tokenIsValid(VALID_TW_TOKEN, VALID_TW_SECRET);
		
		Mockito.doThrow(ServiceException.createServiceException(ServiceExceptionCode.INVALID_FB_TOKEN)).when(facebookUtil).tokenIsValid(INVALID_FB_TOKEN, VALID_FB_USERID);
		
		Mockito.doThrow(ServiceException.createServiceException(ServiceExceptionCode.INVALID_TWITTER_TOKEN)).when(twitterUtil).tokenIsValid(INVALID_TW_TOKEN, null);
	}
	
	@Test
	public void validateBlankSocialUserId(){
		try {
			validator.validate(request);
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.INVALID_SOCIAL_USERID.getCode());
			return;
		}
		fail();
		
	}
	
	@Test
	public void validateFacebookSuccesfully(){
		request.setSocialNetwork(Social.FACEBOOK);
		request.setSocialToken(VALID_FB_TOKEN);
		request.setSocialUserId(VALID_FB_USERID);
		
		validator.validate(request);
		
	}
	
	@Test
	public void validateTwitterSuccesfully(){
		request.setSocialNetwork(Social.TWITTER);
		request.setSocialToken(VALID_TW_TOKEN);
		request.setSocialUserId(VALID_TW_USERID);
		
		validator.validate(request);
		
	}
	
	@Test
	public void validateUnknownSocialNetwork(){
		request.setSocialNetwork(UNKNOWN_NETWORK);
		request.setSocialUserId(VALID_FB_USERID);
		
		try {
			validator.validate(request);
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.INVALID_SOCIAL_NETWORK.getCode());
			return;
		}
		fail();
	}
	
	@Test
	public void facebookInvalidToken(){
		request.setSocialNetwork(Social.FACEBOOK);
		request.setSocialUserId(VALID_FB_USERID);
		request.setSocialToken(INVALID_FB_TOKEN);
		
		try {
			validator.validate(request);
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.INVALID_FB_TOKEN.getCode());
			return;
		}
		fail();
	}
	
	@Test
	public void twitterInvalidToken(){
		request.setSocialNetwork(Social.TWITTER);
		request.setSocialUserId(VALID_TW_USERID);
		request.setSocialToken(INVALID_TW_TOKEN);
		
		try {
			validator.validate(request);
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.INVALID_TWITTER_TOKEN.getCode());
			return;
		}
		fail();
	}
	
	@Test
	public void userAlreadyRegisteredTwitter(){
		request.setSocialNetwork(Social.TWITTER);
		request.setSocialToken(VALID_TW_TOKEN);
		request.setSocialTokenSecret(VALID_TW_SECRET);
		request.setSocialUserId(DUP_TW_ID);
		try {
			validator.validate(request);
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_SOCIAL_ALREADY_REGISTERED_TW.getCode());
			return;
		}
		fail(); 
	}
	
	@Test
	public void userAlreadyRegisteredFB(){
		request.setSocialNetwork(Social.FACEBOOK);
		request.setSocialToken(VALID_FB_TOKEN);
		request.setSocialUserId(DUP_FACEBOOK_ID);
		try {
			validator.validate(request);
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_SOCIAL_ALREADY_REGISTERED_FB.getCode());
			return;
		}
		fail(); 
	}
	
}
