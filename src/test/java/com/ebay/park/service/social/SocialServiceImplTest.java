package com.ebay.park.service.social;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import com.ebay.park.service.social.dto.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.event.user.UsersEvent;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.notification.FeedServiceHelper;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.service.social.command.AddFollowerCmd;
import com.ebay.park.service.social.command.DiscoverUsersCmd;
import com.ebay.park.service.social.command.GetFollowersCmd;
import com.ebay.park.service.social.command.GetFollowingsCmd;
import com.ebay.park.service.social.command.GetUserRatingsCmd;
import com.ebay.park.service.social.command.ListFacebookFriendsCmd;
import com.ebay.park.service.social.command.RemoveFollowerCmd;
import com.ebay.park.service.social.command.SearchUserCmd;
import com.ebay.park.service.social.command.ShareProfileOnFacebookCmd;
import com.ebay.park.service.social.command.ShareProfileOnTwitterCmd;
import com.ebay.park.service.social.command.SocialConnectCmd;
import com.ebay.park.service.social.command.SocialDisconnectCmd;
import com.ebay.park.service.social.validator.SocialConnectValidator;
import com.ebay.park.service.social.validator.SocialDisconnectValidator;
import com.ebay.park.service.social.validator.UserRatesRequestValidator;

public class SocialServiceImplTest {

	private static final String USERNAME_TO_FOLLOW = "johnjohn";
	private static final String FOLLOWER = "followerUser";
	private static final String EMPTY_STRING = "";

	@InjectMocks
	private SocialServiceImpl socialService;

	@Mock
	private GetFollowersCmd getFollowersCmd;

	@Mock
	private AddFollowerCmd addFollowerCmd;

	@Mock
	private GetFollowingsCmd getFollowingsCmd;

	@Mock
	private RemoveFollowerCmd removeFollowerCmd;

	@Mock
	private ServiceValidator<ShareProfileRequest> shareProfReqValidator;

	@Mock
	private ShareProfileOnFacebookCmd shareProfileOnFacebookCmd;

	@Mock
	private ShareProfileOnTwitterCmd shareProfileOnTwitterCmd;

	@Mock
	private SessionService sessionService;

	@Mock
	private User user;

	@Mock
	private UserDao userDao;

	@Mock
	private GetUserRatingsCmd getUserRatingsCmd;

	@Mock
	private UserRatesRequestValidator userRatesRequestValidator;

	@Mock
	private SocialConnectCmd socialConnectCmd;

	@Mock
	private SocialConnectValidator socialConnectValidator;

	@Mock
	private SocialDisconnectValidator socialDisconnectValidator;

	@Mock
	private SocialDisconnectCmd socialDisconnectCmd;

	@Mock
	private ListFacebookFriendsCmd listFriendsCmd;

	@Mock
	private DiscoverUsersCmd discoverUsersCmd;

	@Mock
	private SearchUserCmd searchUserCmd;
	
	@Mock
	private FeedServiceHelper feedServiceHelper;

	@Before
	public void setUp() {
		socialService = new SocialServiceImpl();
		initMocks(this);
	}

	@Test
	public void testGetFollowers() {
		ListUsersFollowedResponse expectedFollowers = mockCommonsForFollowers();
		GetFollowersRequest request = Mockito.mock(GetFollowersRequest.class);
		Mockito.when(request.getUsername()).thenReturn(USERNAME_TO_FOLLOW);
		Mockito.when(getFollowersCmd.execute(request)).thenReturn(expectedFollowers);

		ListUsersFollowedResponse followers = socialService.getFollowers(request);

		verifyCommonsForFollowers(followers);
		Mockito.verify(getFollowersCmd, Mockito.times(1)).execute(request);
	}

	@Test
	public void testGetFollowersByUser() {
		// given
		User userMock = Mockito.mock(User.class);
		List<User> users = new ArrayList<>();
		users.add(userMock);
		Mockito.when(user.getId()).thenReturn(1l);
		Mockito.when(userDao.findFollowers(user.getId())).thenReturn(users);
		// when
		List<User> followers = socialService.getFollowers(user);
		// then
		assertEquals(users, followers);
		Mockito.verify(userDao, Mockito.times(1)).findFollowers(user.getId());
	}

	@Test
	public void testGetUserRatesSuccess() {
		// given
		UserRatesRequest request = Mockito.mock(UserRatesRequest.class);
		SmallRating smallRating = Mockito.mock(SmallRating.class);
		List<SmallRating> smallRatings = new ArrayList<>();
		smallRatings.add(smallRating);
		Mockito.when(getUserRatingsCmd.execute(request)).thenReturn(smallRatings);
		// when
		List<SmallRating> result = socialService.getUserRates(request);
		// then
		assertEquals(smallRatings, result);
		Mockito.verify(getUserRatingsCmd, Mockito.times(1)).execute(request);
	}

	@Test
	public void testGetUserRatesFailNullRequest() {
		UserRatesRequest request = null;
		try {
			socialService.getUserRates(request);
		} catch (ServiceException se) {
			assertEquals(ServiceExceptionCode.INVALID_USER_RATES_REQ.getCode(), se.getCode());
		}
	}

	@Test
	public void testGetUserRatesFailEmptyUserName() {
		UserRatesRequest request = Mockito.mock(UserRatesRequest.class);
		request.setUsername(EMPTY_STRING);
		try {
			socialService.getUserRates(request);
		} catch (ServiceException se) {
			assertEquals(ServiceExceptionCode.EMPTY_USERNAME.getCode(), se.getCode());
		}
	}

	@Test
	public void testGetUserRatesFailInvalidUserRatesStatus() {
		UserRatesRequest request = Mockito.mock(UserRatesRequest.class);
		String status = "status";
		List<String> statuses = new ArrayList<>();
		statuses.add(status);
		Mockito.when(request.getRateStatus()).thenReturn(statuses);
		try {
			socialService.getUserRates(request);
		} catch (ServiceException se) {
			assertEquals(ServiceExceptionCode.INVALID_USER_RATES_STATUS.getCode(), se.getCode());
		}
	}

	@Test
	public void testConnectToSocialNetworkSuccess() {
		// given
		SocialNetworkConnectRequest request = Mockito.mock(SocialNetworkConnectRequest.class);
		UserSessionCache userSessionCache = Mockito.mock(UserSessionCache.class);
		Mockito.when(socialConnectCmd.execute(request)).thenReturn(Boolean.TRUE);
		Mockito.doNothing().when(socialConnectValidator).validate(request);
		Mockito.when(sessionService.getUserSession(request.getToken())).thenReturn(userSessionCache);
		// when
		Boolean response = socialService.connectToSocialNetwork(request);
		// then
		assertTrue(response);
		Mockito.verify(socialConnectCmd, Mockito.times(1)).execute(request);
	}

	@Test
	public void testDisconnectSocialNetworkSuccess() {
		// given
		SocialNetworkDisconnectRequest request = Mockito.mock(SocialNetworkDisconnectRequest.class);
		UserSessionCache userSessionCache = Mockito.mock(UserSessionCache.class);
		Mockito.doNothing().when(socialDisconnectValidator).validate(request);
		Mockito.when(sessionService.getUserSession(request.getToken())).thenReturn(userSessionCache);
		Mockito.when(socialDisconnectCmd.execute(request)).thenReturn(Boolean.TRUE);
		// when
		Boolean response = socialService.disconnectSocialNetwork(request);
		// then
		assertTrue(response);
		Mockito.verify(socialDisconnectCmd, Mockito.times(1)).execute(request);
	}

	@Test
	public void testlistFacebookFriends() {
		// given
		FacebookFriend user = Mockito.mock(FacebookFriend.class);
		List<FacebookFriend> users = new ArrayList<>();
		users.add(user);
		ListFacebookFriendsRequest request = Mockito.mock(ListFacebookFriendsRequest.class);
		UserSessionCache userSessionCache = Mockito.mock(UserSessionCache.class);
		Mockito.when(sessionService.getUserSession(request.getToken())).thenReturn(userSessionCache);
		ListFacebookFriendsResponse expectedResponse = new ListFacebookFriendsResponse();
		expectedResponse.setFriends(users);
		Mockito.when(listFriendsCmd.execute(request)).thenReturn(expectedResponse);
		// when
		ListFacebookFriendsResponse result = socialService.listFacebookFriends(request);
		// then
		assertEquals(users, result.getFriends());
		Mockito.verify(listFriendsCmd).execute(request);
	}

	@Test
	public void testDiscoverUsers() {
		// given
		ListUsersFollowedResponse listUserFollowed = Mockito.mock(ListUsersFollowedResponse.class);
		DiscoverUsersRequest request = Mockito.mock(DiscoverUsersRequest.class);
		UserSessionCache userSessionCache = Mockito.mock(UserSessionCache.class);
		Mockito.when(sessionService.getUserSession(request.getToken())).thenReturn(userSessionCache);
		Mockito.when(discoverUsersCmd.execute(request)).thenReturn(listUserFollowed);
		// when
		ListUsersFollowedResponse response = socialService.discoverUsers(request);
		// then
		assertEquals(listUserFollowed, response);
		Mockito.verify(discoverUsersCmd, Mockito.times(1)).execute(request);
	}

	@Test
	public void testSearchUsers() {
		// given
		SearchUserResponse response = Mockito.mock(SearchUserResponse.class);
		SearchUserRequest request = Mockito.mock(SearchUserRequest.class);
		UserSessionCache userSessionCache = Mockito.mock(UserSessionCache.class);
		Mockito.when(sessionService.getUserSession(request.getToken())).thenReturn(userSessionCache);
		Mockito.when(searchUserCmd.execute(request)).thenReturn(response);
		// when
		SearchUserResponse executeReponse = socialService.searchUsers(request);
		// then
		assertEquals(response, executeReponse);
		Mockito.verify(searchUserCmd, Mockito.times(1)).execute(request);
	}

	@Test
	public void testGetFollowersNoUser() {
		ListUsersFollowedResponse response = null;
		GetFollowersRequest request = Mockito.mock(GetFollowersRequest.class);
		Mockito.when(request.getUsername()).thenReturn(EMPTY_STRING);
		try {
			response = socialService.getFollowers(request);
			fail("A service exception was expected. ");
		} catch (ServiceException se) {
			assertNull(response);
			assertEquals(ServiceExceptionCode.EMPTY_USERNAME.getCode(), se.getCode());
		}
	}

	@Test
	public void testGetFollowings() {
		ListUsersFollowedResponse expectedResponse = mockCommonsForFollowers();
		GetFollowingsRequest request = Mockito.mock(GetFollowingsRequest.class);
		Mockito.when(request.getUsername()).thenReturn(USERNAME_TO_FOLLOW);
		Mockito.when(getFollowingsCmd.execute(request)).thenReturn(expectedResponse);

		ListUsersFollowedResponse response = socialService.getFollowings(request);

		verifyCommonsForFollowers(response);
		Mockito.verify(getFollowingsCmd, Mockito.times(1)).execute(request);

	}

	@Test
	public void testGetFollowingsNoUser() {
		ListUsersFollowedResponse followers = null;
		try {
			GetFollowingsRequest request = Mockito.mock(GetFollowingsRequest.class);
			Mockito.when(request.getUsername()).thenReturn(null);
			followers = socialService.getFollowings(request);
			fail("A service exception was expected. ");
		} catch (ServiceException se) {
			assertNull(followers);
			assertEquals(ServiceExceptionCode.EMPTY_USERNAME.getCode(), se.getCode());
		}

	}

	@Test
	public void testAddFollowerToUser() {
		FollowUserRequest request = new FollowUserRequest();
		request.setUserToFollow(USERNAME_TO_FOLLOW);
		request.setFollower(FOLLOWER);
		UsersEvent resp = Mockito.mock(UsersEvent.class);
		Mockito.when(addFollowerCmd.execute(request)).thenReturn(resp);
		UserSessionCache sessionMock = Mockito.mock(UserSessionCache.class);
		Mockito.when(sessionService.getUserSession(request.getToken())).thenReturn(sessionMock);
		Mockito.when(sessionMock.getUsername()).thenReturn(FOLLOWER);

		FollowUserResponse result = socialService.addFollowerToUser(request);

		assertNotNull(result);
		assertTrue(result.getSuccessfull());
		assertEquals(FOLLOWER, result.getUsername());
		assertEquals(USERNAME_TO_FOLLOW, result.getUserToFollow());
		assertEquals(FOLLOWER, result.getFollower());
		Mockito.verify(addFollowerCmd, Mockito.times(1)).execute(request);

	}

	@Test
	public void testAddFollowerToUserMissingUser() {
		FollowUserRequest request = new FollowUserRequest();
		UserSessionCache sessionMock = Mockito.mock(UserSessionCache.class);
		Mockito.when(sessionService.getUserSession(request.getToken())).thenReturn(sessionMock);
		Mockito.when(sessionMock.getUsername()).thenReturn(request.getFollower());

		FollowUserResponse response = null;
		try {
			response = socialService.addFollowerToUser(request);
		} catch (ServiceException se) {
			assertNull(response);
			assertEquals(ServiceExceptionCode.EMPTY_USERNAME.getCode(), se.getCode());
		}
	}

	@Test
	public void testAddFollowerToUserFollowSelfReferential() {
		FollowUserRequest request = new FollowUserRequest();
		request.setUserToFollow(FOLLOWER);
		request.setFollower(FOLLOWER);
		UsersEvent resp = Mockito.mock(UsersEvent.class);
		Mockito.when(addFollowerCmd.execute(request)).thenReturn(resp);
		UserSessionCache sessionMock = Mockito.mock(UserSessionCache.class);
		Mockito.when(sessionService.getUserSession(request.getToken())).thenReturn(sessionMock);
		Mockito.when(sessionMock.getUsername()).thenReturn(FOLLOWER);
		FollowUserResponse response = null;
		try {
			response = socialService.addFollowerToUser(request);
		} catch (ServiceException se) {
			assertNull(response);
			assertEquals(ServiceExceptionCode.FOLLOW_SELF_REFERENTIAL.getCode(), se.getCode());
		}
	}

	@Test
	public void testRemoveFollowerToUser() {
		UnfollowUserRequest request = new UnfollowUserRequest();
		request.setUserToUnfollow(USERNAME_TO_FOLLOW);
		request.setFollower(FOLLOWER);
		UserSessionCache sessionMock = Mockito.mock(UserSessionCache.class);
		Mockito.when(sessionService.getUserSession(request.getToken())).thenReturn(sessionMock);
		Mockito.when(sessionMock.getUsername()).thenReturn(FOLLOWER);

		Boolean result = socialService.removeFollowToUser(request);

		assertNotNull(result);
		assertTrue(result);
		Mockito.verify(removeFollowerCmd, Mockito.times(1)).execute(request);
	}

	@Test
	public void testRemoveFollowerToUserMissingFollowed() {
		UnfollowUserRequest request = new UnfollowUserRequest();
		request.setFollower(FOLLOWER);
		UserSessionCache sessionMock = Mockito.mock(UserSessionCache.class);
		Mockito.when(sessionService.getUserSession(request.getToken())).thenReturn(sessionMock);
		Mockito.when(sessionMock.getUsername()).thenReturn(FOLLOWER);

		Boolean response = null;
		try {
			response = socialService.removeFollowToUser(request);
		} catch (ServiceException se) {
			assertNull(response);
			assertEquals(ServiceExceptionCode.EMPTY_USERNAME.getCode(), se.getCode());
		}
	}

	@Test
	public void testRemoveFollowerToUserMissingFollower() {
		UnfollowUserRequest request = new UnfollowUserRequest();
		request.setUserToUnfollow(USERNAME_TO_FOLLOW);

		Mockito.when(sessionService.getUserSession(request.getToken())).thenReturn(null);

		Boolean response = null;
		try {
			response = socialService.removeFollowToUser(request);
		} catch (ServiceException se) {
			assertNull(response);
			assertEquals(ServiceExceptionCode.EMPTY_USERNAME.getCode(), se.getCode());
		}
	}

	@Test
	public void testRemoveFollowerToUserInexistingFollowing() {
		UnfollowUserRequest request = new UnfollowUserRequest();
		request.setUserToUnfollow(USERNAME_TO_FOLLOW);
		request.setFollower(FOLLOWER);
		Mockito.when(removeFollowerCmd.execute(request))
				.thenThrow(ServiceException.createServiceException(ServiceExceptionCode.FOLLOWING_NOT_FOUND));
		UserSessionCache sessionMock = Mockito.mock(UserSessionCache.class);
		Mockito.when(sessionService.getUserSession(request.getToken())).thenReturn(sessionMock);
		Mockito.when(sessionMock.getUsername()).thenReturn(FOLLOWER);

		Boolean result = null;
		try {
			result = socialService.removeFollowToUser(request);
		} catch (ServiceException se) {
			assertNull(result);
			assertEquals(ServiceExceptionCode.FOLLOWING_NOT_FOUND.getCode(), se.getCode());
		}
		Mockito.verify(removeFollowerCmd, Mockito.times(1)).execute(request);
	}

	@Test
	public void testShareProfileOnFacebook() {
		ShareProfileRequest request = new ShareProfileRequest();
		Mockito.doNothing().when(shareProfReqValidator).validate(request);
		Mockito.when(shareProfileOnFacebookCmd.execute(request)).thenReturn(true);
		UserSessionCache sessionMock = Mockito.mock(UserSessionCache.class);
		Mockito.when(sessionService.getUserSession(request.getToken())).thenReturn(sessionMock);
		Mockito.when(sessionMock.getUsername()).thenReturn(request.getSharerUsername());

		Boolean response = socialService.shareProfileOnFacebook(request);

		assertTrue(response);
		Mockito.verify(shareProfReqValidator, Mockito.times(1)).validate(request);
		Mockito.verify(shareProfileOnFacebookCmd, Mockito.times(1)).execute(request);

	}

	@Test
	public void testShareProfileOnTwitter() {
		ShareProfileRequest request = new ShareProfileRequest();
		Mockito.doNothing().when(shareProfReqValidator).validate(request);
		Mockito.when(shareProfileOnTwitterCmd.execute(request)).thenReturn(true);
		UserSessionCache sessionMock = Mockito.mock(UserSessionCache.class);
		Mockito.when(sessionService.getUserSession(request.getToken())).thenReturn(sessionMock);
		Mockito.when(sessionMock.getUsername()).thenReturn(request.getSharerUsername());

		Boolean response = socialService.shareProfileOnTwitter(request);

		assertTrue(response);
		Mockito.verify(shareProfReqValidator, Mockito.times(1)).validate(request);
		Mockito.verify(shareProfileOnTwitterCmd, Mockito.times(1)).execute(request);
	}

	private ListUsersFollowedResponse mockCommonsForFollowers() {
		List<BasicUser> expectedFollowers = new ArrayList<BasicUser>();
		expectedFollowers.add(new BasicUser());
		ListUsersFollowedResponse response = new ListUsersFollowedResponse(expectedFollowers);
		return response;
	}

	private void verifyCommonsForFollowers(ListUsersFollowedResponse response) {
		assertNotNull(response);
		assertNotNull(response.getUsers());
		assertEquals(response.getUsers().size(), 1);
	}
}
