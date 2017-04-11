package com.ebay.park.service.social.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ebay.park.db.dao.FollowerDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Follower;
import com.ebay.park.db.entity.FollowerPK;
import com.ebay.park.db.entity.Idiom;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.session.TestServiceUtil;
import com.ebay.park.service.social.dto.GetFollowingsRequest;
import com.ebay.park.service.social.dto.ListUsersFollowedResponse;
import com.ebay.park.util.InternationalizationUtil;
import com.ebay.park.util.LanguageUtil;

public class GetFollowingsCmdTest {

	private static final String USERNAME = "timMartins";
	private static final String TOKEN = "validToken";
	private static final Long KEY_FOLLOWER1 = 12345l;
	private static final Long KEY_USER1 = 67899l;
	private static final Long KEY_FOLLOWER2 = 54321l;
	private static final Long KEY_USER2 = 11111l;
	private static final String USERNAME2 = "johnjohn";
	private static final String EMAIL2 = "jj@gmail.com";
	private static final String USERNAME1 = "thomasjhon";
	private static final String EMAIL1 = "tj@gmail.com";
	private static final String FAIL_MSG = "An exception was expected";
	private FollowerPK pk1;
	private User user1;
	private Follower follower1;
	private FollowerPK pk2;
	private User user2;
	private Follower follower2;
	private User user;
	private List<Follower> followers;

	@InjectMocks
	private GetFollowingsCmd getFollowingsCmd;

	@Mock
	private InternationalizationUtil i18nUtil;

	@Mock
	private UserDao userDao;

	@Mock
	private FollowerDao followerDao;

	@Before
	public void setUp() {
		getFollowingsCmd = new GetFollowingsCmd();
		initMocks(this);
		pk1 = new FollowerPK(KEY_FOLLOWER1, KEY_USER1);
		user1 = TestServiceUtil.createUserMock(KEY_FOLLOWER1, USERNAME1, EMAIL1, null, null, null, null);
		follower1 = new Follower(pk1, user1);

		pk2 = new FollowerPK(KEY_FOLLOWER2, KEY_USER2);
		user2 = TestServiceUtil.createUserMock(11111l, USERNAME2, EMAIL2, null, null, null, null);
		follower2 = new Follower(pk2, user2);

		followers = new ArrayList<>(2);
		followers.add(follower1);
		followers.add(follower2);

		user = TestServiceUtil.createUserMock(888l, USERNAME, "UserFollowed@mail.com", null, null, null, null);
	}

	@Test
	public void testExecuteSuccess() {
		// given
		GetFollowingsRequest request = Mockito.mock(GetFollowingsRequest.class);
		Mockito.when(userDao.findByUsername(USERNAME)).thenReturn(user);
		follower1.setUserFollowed(user);
		follower2.setUserFollowed(user);
		Mockito.when(request.getUsername()).thenReturn(USERNAME);
		Mockito.when(user.getFollowers()).thenReturn(followers);
		Mockito.when(followerDao.findFollowings(user.getId())).thenReturn(followers);
		request.setLanguage(LanguageUtil.DEFAULT_LANGUAGE);
		// when
		ListUsersFollowedResponse executeResponse = getFollowingsCmd.execute(request);
		// then
		assertNotNull(executeResponse);
		assertNotNull(executeResponse.getUsers());
		assertEquals(2,executeResponse.getUsers().size());
		Mockito.verify(userDao, Mockito.times(1)).findByUsername(USERNAME);
		Mockito.verify(followerDao, Mockito.times(1)).findFollowings(user.getId());
	}

	@Test
	public void testExecuteWithTokenSuccess() {
		// given
		GetFollowingsRequest request = Mockito.mock(GetFollowingsRequest.class);
		Mockito.when(userDao.findByUsername(USERNAME)).thenReturn(user);
		follower1.setUserFollowed(user);
		follower2.setUserFollowed(user);
		Mockito.when(userDao.findByToken(TOKEN)).thenReturn(user);
		Mockito.when(request.getUsername()).thenReturn(USERNAME);
		Mockito.when(request.getToken()).thenReturn(TOKEN);

		Mockito.when(user.getFollowers()).thenReturn(followers);
		Idiom idiom = Mockito.mock(Idiom.class);
		Mockito.when(idiom.getCode()).thenReturn("idiom");
		Mockito.when(user.getIdiom()).thenReturn(idiom);

		Mockito.when(followerDao.findFollowings(user.getId())).thenReturn(followers);
		request.setLanguage(LanguageUtil.DEFAULT_LANGUAGE);
		// when
		ListUsersFollowedResponse executeResponse = getFollowingsCmd.execute(request);
		// then
		assertNotNull(executeResponse);
		assertNotNull(executeResponse.getUsers());
		assertEquals(executeResponse.getUsers().size(), 2);
		Mockito.verify(userDao, Mockito.times(1)).findByUsername(USERNAME);
		Mockito.verify(followerDao, Mockito.times(1)).findFollowings(user.getId());
	}

	@Test
	public void testExecuteFailNullUser() {
		// given
		GetFollowingsRequest request = Mockito.mock(GetFollowingsRequest.class);
		Mockito.when(userDao.findByUsername(USERNAME)).thenReturn(null);
		Mockito.when(request.getUsername()).thenReturn(USERNAME);
		try {
			getFollowingsCmd.execute(request);
			fail(FAIL_MSG);
		} catch (ServiceException se) {
			assertEquals(ServiceExceptionCode.USER_NOT_FOUND.getCode(), se.getCode());
		}

	}

}
