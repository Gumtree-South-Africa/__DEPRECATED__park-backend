package com.ebay.park.service.profile;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.profile.command.GetUserProfileCmd;
import com.ebay.park.service.profile.command.UpdateUserInfoCmd;
import com.ebay.park.service.profile.dto.GetUserProfileRequest;
import com.ebay.park.service.profile.dto.UserInfoRequest;
import com.ebay.park.service.profile.dto.UserInfoResponse;
import com.ebay.park.service.profile.dto.UserProfile;
import com.ebay.park.service.profile.validator.UserInfoRequestValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProfileServiceImplTest {

	private static final String USERNAME = "johnjohn";
	private static final String TOKEN = "token";

	@InjectMocks
	private ProfileServiceImpl profileService;
	
	@Mock
	private UpdateUserInfoCmd updateUserInfoCmd;
	
	@Mock
	private UserInfoRequestValidator userInfoRequestValidator;
	
	@Mock
	private GetUserProfileCmd getUserProfileCmd;
	
	@Mock
	private GetUserProfileRequest request;
	
	@Before
	public void setUp() {
		profileService = new ProfileServiceImpl();
		initMocks(this);
		Mockito.when(request.getUsername()).thenReturn(USERNAME);
		Mockito.when(request.getToken()).thenReturn(TOKEN);
	}

	@Test
	public void updateUserInfoTest() {

		UserInfoRequest request = new UserInfoRequest();
		Mockito.doNothing().when(userInfoRequestValidator).validate(request);
		UserInfoResponse userInfoResponse = new UserInfoResponse();
		Mockito.when(updateUserInfoCmd.execute(request)).thenReturn(userInfoResponse);

		 UserInfoResponse response = profileService.updateUserInfo(request);

		assertNotNull(response);
		assertEquals(response, userInfoResponse);
		Mockito.verify(userInfoRequestValidator, Mockito.times(1)).validate(request);
		Mockito.verify(updateUserInfoCmd, Mockito.times(1)).execute(request);

	}
	
	@Test
	public void getUserProfileSuccessfulTest() {

		UserProfile userProfile = new UserProfile();
		userProfile.setUsername(USERNAME);
		Mockito.when(getUserProfileCmd.execute(request)).thenReturn(userProfile);

		UserProfile response = profileService.getUserProfile(request);

		assertNotNull(response);
		assertEquals(response, userProfile);
		Mockito.verify(getUserProfileCmd, Mockito.times(1)).execute(request);

	}
	
	@Test
	public void getUserProfileUnSuccessfulTest() {
		Mockito.when(request.getUsername()).thenReturn(null);
		UserProfile response = null;
		try{
			response = profileService.getUserProfile(request);
			fail("A service exception was expected. ");
		} catch(ServiceException se){
			assertNull(response);
			assertEquals(ServiceExceptionCode.EMPTY_USERNAME.getCode(), se.getCode());
		}

	}

}
