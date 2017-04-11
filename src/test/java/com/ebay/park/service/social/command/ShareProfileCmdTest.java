package com.ebay.park.service.social.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.social.dto.ShareProfileRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ShareProfileCmdTest {

	private static final String SHARER_USER = "sharerUser";
	private static final String USER_TO_SHARE = "userToShare";
	protected static final String SOCIAL = "social";
	private static final String TOKEN = "token";
	protected static final String USER_PROF_LINK = "userProfLink";

	@InjectMocks
	private ShareProfileCmd shareProfileCmd;
	
	@Mock
	protected UserDao userDao;
	
	@Mock
	private User sharerUser;
	
	@Mock
	private User userToShare;
	
	@Mock
	private ShareProfileRequest request;
	
	@Mock
	private UserSocial userSocial;
	
	@Mock
	private Social social;
	
	@Before
	public void setUp() {
		shareProfileCmd = new ShareProfileCmd(){

			@Override
			protected String getSocial() {
				return SOCIAL;
			}

			@Override
			protected ServiceException getTokenException() {
				return ServiceException.createServiceException(ServiceExceptionCode.INVALID_FB_TOKEN);
			}

			@Override
			protected void postLink(String link, User sharerUser, ShareProfileRequest request, String token, String tokenSecret) {
				
			}
			
			@Override
			protected String getUserProfileLink(String usernameToShare){
				return USER_PROF_LINK;
			}
			
		};
		initMocks(this);
	}
	
	@Test
	public void executeTest(){
		
		Mockito.when(request.getSharerUsername()).thenReturn(SHARER_USER);
		Mockito.when(userDao.findByUsername(SHARER_USER)).thenReturn(sharerUser);
		
		Mockito.when(request.getUsernameToShare()).thenReturn(USER_TO_SHARE);
		Mockito.when(userDao.findByUsername(USER_TO_SHARE)).thenReturn(userToShare);

		List<UserSocial> socials = new ArrayList<UserSocial>();
		socials.add(userSocial);
		Mockito.when(sharerUser.getUserSocials()).thenReturn(socials);
		Mockito.when(userSocial.getSocial()).thenReturn(social);
		Mockito.when(social.getDescription()).thenReturn(SOCIAL);
		Mockito.when(userSocial.getToken()).thenReturn(TOKEN);
		
		Boolean response = shareProfileCmd.execute(request);
		
		assertNotNull(response);
		assertTrue(response);
		
		Mockito.verify(request, Mockito.times(1)).getSharerUsername();
		Mockito.verify(userDao, Mockito.times(1)).findByUsername(SHARER_USER);
		
		Mockito.verify(request, Mockito.times(2)).getUsernameToShare();
		Mockito.verify(userDao, Mockito.times(1)).findByUsername(USER_TO_SHARE);
		
		Mockito.verify(sharerUser, Mockito.times(2)).getUserSocials();
		Mockito.verify(userSocial, Mockito.times(2)).getSocial();
		Mockito.verify(social, Mockito.times(2)).getDescription();
		Mockito.verify(userSocial, Mockito.times(1)).getToken();
		Mockito.verify(userSocial, Mockito.times(1)).getTokenSecret();

	}
	
	@Test
	public void executeSharerNotFoundTest(){
		
		Mockito.when(request.getSharerUsername()).thenReturn(SHARER_USER);
		Mockito.when(userDao.findByUsername(SHARER_USER)).thenReturn(sharerUser);
		
		Mockito.when(request.getUsernameToShare()).thenReturn(USER_TO_SHARE);
		Mockito.when(userDao.findByUsername(USER_TO_SHARE)).thenReturn(null);
		
		Boolean response = null;
		try{
			response = shareProfileCmd.execute(request);
			fail("A service exception was expected. ");
		} catch(ServiceException se){
			assertNull(response);
			assertEquals(ServiceExceptionCode.USER_NOT_FOUND.getCode(), se.getCode());
			Mockito.verify(request, Mockito.times(1)).getSharerUsername();
			Mockito.verify(userDao, Mockito.times(1)).findByUsername(SHARER_USER);
			Mockito.verify(request, Mockito.times(1)).getUsernameToShare();
			Mockito.verify(userDao, Mockito.times(1)).findByUsername(USER_TO_SHARE);
		}
	
	}
	
	@Test
	public void executeUserToShareNotFoundTest(){
		
		Mockito.when(request.getSharerUsername()).thenReturn(SHARER_USER);
		Mockito.when(userDao.findByUsername(SHARER_USER)).thenReturn(null);
		
		Boolean response = null;
		try{
			response = shareProfileCmd.execute(request);
			fail("A service exception was expected. ");
		} catch(ServiceException se){
			assertNull(response);
			assertEquals(ServiceExceptionCode.USER_NOT_FOUND.getCode(), se.getCode());
			Mockito.verify(request, Mockito.times(1)).getSharerUsername();
			Mockito.verify(userDao, Mockito.times(1)).findByUsername(SHARER_USER);
		}
	
	}
	
	@Test
	public void executeInvalidTokenTest(){
		
		Mockito.when(request.getSharerUsername()).thenReturn(SHARER_USER);
		Mockito.when(userDao.findByUsername(SHARER_USER)).thenReturn(sharerUser);
		
		Mockito.when(request.getUsernameToShare()).thenReturn(USER_TO_SHARE);
		Mockito.when(userDao.findByUsername(USER_TO_SHARE)).thenReturn(userToShare);

		List<UserSocial> socials = new ArrayList<UserSocial>();
		socials.add(userSocial);
		Mockito.when(sharerUser.getUserSocials()).thenReturn(socials);
		Mockito.when(userSocial.getSocial()).thenReturn(social);
		Mockito.when(social.getDescription()).thenReturn(SOCIAL);
		Mockito.when(userSocial.getToken()).thenReturn(null);

		
		Boolean response = null;
		try{
			response = shareProfileCmd.execute(request);
			fail("A service exception was expected. ");
		} catch(ServiceException se){
			assertNull(response);
			assertEquals(ServiceExceptionCode.INVALID_FB_TOKEN.getCode(), se.getCode());
			Mockito.verify(request, Mockito.times(1)).getSharerUsername();
			Mockito.verify(userDao, Mockito.times(1)).findByUsername(SHARER_USER);
			
			Mockito.verify(request, Mockito.times(1)).getUsernameToShare();
			Mockito.verify(userDao, Mockito.times(1)).findByUsername(USER_TO_SHARE);
			
			Mockito.verify(sharerUser, Mockito.times(1)).getUserSocials();
			Mockito.verify(userSocial, Mockito.times(1)).getSocial();
			Mockito.verify(social, Mockito.times(1)).getDescription();
			Mockito.verify(userSocial, Mockito.times(1)).getToken();
		}

	}

	
}
