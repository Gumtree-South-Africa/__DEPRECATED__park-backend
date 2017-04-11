package com.ebay.park.social;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.api.FriendOperations;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.UserOperations;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.client.RestOperations;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.util.FacebookUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FacebookUtil.class)
public class FacebookUtilTest {

	@Spy
	private FacebookUtil facebookUtil = new FacebookUtil();
	@Mock
	private FacebookTemplate facebookTemplate;
	@Rule
	private ExpectedException exception = ExpectedException.none();
	@Mock
	private UserOperations userOperations;
	@Mock
	private FriendOperations friendOperations;
	@Mock
	private User user;
	@Mock
	private RestOperations restOperations;
	@Mock
	private ResponseEntity<String> restResponse;

	@Test
	public void givenAValidTokenWhenTokenIsValidThenSuccess() throws Exception {
		PowerMockito.doReturn(facebookTemplate).when(facebookUtil, "createFacebookTemplate", "token");
		when(facebookTemplate.userOperations()).thenReturn(userOperations);
		when(userOperations.getUserProfile()).thenReturn(user);
		when(user.getId()).thenReturn("userId");

		facebookUtil.tokenIsValid("token", "userId");

		verifyPrivate(facebookUtil).invoke("createFacebookTemplate", "token");
		verify(facebookTemplate).userOperations();
		verify(userOperations).getUserProfile();
		verify(user).getId();
	}
	
	@Test
	public void givenIValidTokenWhenTokenIsValidThenException() throws Exception {
		PowerMockito.doReturn(facebookTemplate).when(facebookUtil, "createFacebookTemplate", "token");
		when(facebookTemplate.userOperations()).thenReturn(userOperations);
		when(userOperations.getUserProfile()).thenReturn(user);

		exception.expect(ServiceException.class);
		exception.expectMessage(ServiceExceptionCode.INVALID_FB_TOKEN.getMessage());
		
		facebookUtil.tokenIsValid("token", "userId");
		
		verifyPrivate(facebookUtil).invoke("createFacebookTemplate", "token");
		verify(facebookTemplate).userOperations();
		verify(userOperations).getUserProfile();
		verify(facebookTemplate).userOperations().getUserProfile().getId();
	}
	
	@Test
	public void GivenAValidTokenWhenGetEmailThenSuccess() throws Exception {
		PowerMockito.doReturn(facebookTemplate).when(facebookUtil, "createFacebookTemplate", "token");
		when(facebookTemplate.userOperations()).thenReturn(userOperations);
		when(userOperations.getUserProfile()).thenReturn(user);
		when(user.getEmail())
				.thenReturn("email@email.com");	
		
		String email = facebookUtil.getEmail("token");
		
		 assertEquals("The email addres is not correct", "email@email.com",
		 email);
		
		 verifyPrivate(facebookUtil).invoke("createFacebookTemplate",
		 "token");
		 
        verify(user).getEmail();
	}
	
	@Test
	public void givenATokenWhenGetFriendIdsThenSuccess() throws Exception {
		//given
		List<String> ids = new ArrayList<>();
		ids.add("id");
		
		PagedList<String> expectedResult = new PagedList<>(ids, null, null);
		
		PowerMockito.doReturn(facebookTemplate).when(facebookUtil, "createFacebookTemplate", "token");
		when(facebookTemplate.friendOperations()).thenReturn(friendOperations);
		when(friendOperations.getFriendIds()).thenReturn(expectedResult);
		
		//when
		List<String> friendIds = facebookUtil.getFriendIds("token");
		
		//then
		verifyPrivate(facebookUtil).invoke("createFacebookTemplate", "token");
		verify(facebookTemplate).friendOperations();
		verify(friendOperations).getFriendIds();
		assertEquals(friendIds.get(0), expectedResult.get(0));
	}


}
