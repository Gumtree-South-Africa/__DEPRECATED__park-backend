package com.ebay.park.service.social.command;

import com.ebay.park.db.entity.Idiom;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.social.dto.ShareProfileRequest;
import com.ebay.park.util.FacebookUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class ShareProfileOnFacebookCmdTest {

	private static final String SHARER_USER = "sharerUser";
	private static final String USER_TO_SHARE = "userToShare";
	protected static final String SOCIAL = "social";
	private static final String TOKEN = "token";
	private static final String TOKEN_SECRET = "token-secret";
	protected static final String USER_PROF_LINK = "userProfLink";
	private static final String IDIOM_CODE = "en-us";
	private static final String SHARE_MESSAGE = "message to share";
	private static final String SHARE_DESCRIPTION = "description to share";
	
	protected static final String SHARE_PROFILE_MESSAGE = "social.facebook.share_profile.message";
	protected static final String SHARE_PROFILE_DESCRIPTION = "social.facebook.share_profile.description";

	@InjectMocks
	private ShareProfileCmd shareProfileOnFacebookCmd;
	
	@Mock
	private FacebookUtil facebookUtil;
	
	@Mock
	protected MessageSource messageSource;
	
	@Mock
	private User sharerUser;
	
	@Mock
	private User userToShare;
	
	@Mock
	private ShareProfileRequest request;
	
	@Mock
	private Idiom idiom;
	
	@Before
	public void setUp() {
		shareProfileOnFacebookCmd = new ShareProfileOnFacebookCmd();
		initMocks(this);
	}

	@Test
	public void getSocialTest(){
	
		assertEquals(Social.FACEBOOK, shareProfileOnFacebookCmd.getSocial());

	}
	
	@Test
	public void getTokenExceptionTest(){
	
		assertEquals(ServiceExceptionCode.INVALID_FB_TOKEN.getCode(), shareProfileOnFacebookCmd.getTokenException().getCode());

	}
	
	@Test
	public void postLinkTest(){
		
		Mockito.when(sharerUser.getIdiom()).thenReturn(idiom);
		Mockito.when(idiom.getCode()).thenReturn(IDIOM_CODE);

		Mockito.when(request.getSharerUsername()).thenReturn(SHARER_USER);
		Mockito.when(request.getUsernameToShare()).thenReturn(USER_TO_SHARE);

		Locale locale = new Locale(IDIOM_CODE);
		
		String[] users = {SHARER_USER, USER_TO_SHARE};
		Mockito.when(messageSource.getMessage(SHARE_PROFILE_MESSAGE, users, locale)).thenReturn(SHARE_MESSAGE);
		
		String[] user = {USER_TO_SHARE};
		Mockito.when(messageSource.getMessage(SHARE_PROFILE_DESCRIPTION, user, locale)).thenReturn(SHARE_DESCRIPTION);
		
		Mockito.doNothing().when(facebookUtil).postLink(TOKEN, SHARE_MESSAGE, USER_PROF_LINK, null, null, SHARE_DESCRIPTION);

		shareProfileOnFacebookCmd.postLink(USER_PROF_LINK, sharerUser, request, TOKEN, TOKEN_SECRET);
		
		Mockito.verify(sharerUser, Mockito.times(1)).getIdiom();
		Mockito.verify(idiom, Mockito.times(1)).getCode();
		
		Mockito.verify(request, Mockito.times(1)).getSharerUsername();
		Mockito.verify(request, Mockito.times(2)).getUsernameToShare();
		
		Mockito.verify(messageSource, Mockito.times(1)).getMessage(SHARE_PROFILE_MESSAGE, users, locale);
		Mockito.verify(messageSource, Mockito.times(1)).getMessage(SHARE_PROFILE_DESCRIPTION, user, locale);
		
		Mockito.verify(facebookUtil, Mockito.times(1)).postLink(TOKEN, SHARE_MESSAGE, USER_PROF_LINK, null, null, SHARE_DESCRIPTION);

	}
	
}
