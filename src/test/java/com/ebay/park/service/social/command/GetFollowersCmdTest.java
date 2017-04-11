package com.ebay.park.service.social.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Follower;
import com.ebay.park.db.entity.FollowerPK;
import com.ebay.park.db.entity.Idiom;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.session.TestServiceUtil;
import com.ebay.park.service.social.dto.GetFollowersRequest;
import com.ebay.park.service.social.dto.ListUsersFollowedResponse;
import com.ebay.park.util.InternationalizationUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

public class GetFollowersCmdTest {

	private static final String USERNAME = "timMartins";

	private static final String USERNAME1 = "thomasjhon";
	private static final String EMAIL1 = "tj@gmail.com";

	private static final String USERNAME2 = "johnjohn";
	private static final String EMAIL2 = "jj@gmail.com";

	private static final Long KEY_FOLLOWER1 = 12345l;
	private static final Long KEY_USER1 = 67899l;

	private static final Long KEY_FOLLOWER2 = 54321l;
	private static final Long KEY_USER2 = 11111l;

	@InjectMocks
	private GetFollowersCmd getFollowersCmd;

	@Mock
	private UserDao userDao;

	@Mock
	private InternationalizationUtil i18nUtil;

	@Before
	public void setUp() {
		getFollowersCmd = new GetFollowersCmd();
		initMocks(this);
	}

	@Test
	public void executeTest() {
		User userFollowed = TestServiceUtil.createUserMock(888l, USERNAME, "UserFollowed@mail.com",
				null, null, null, null);

		FollowerPK pk1 = new FollowerPK(KEY_FOLLOWER1, KEY_USER1);
		User user1 = TestServiceUtil.createUserMock(KEY_FOLLOWER1, USERNAME1, EMAIL1, null, null,
				null, null);
		Follower follower1 = new Follower(pk1, user1);
		User userFollower1 = TestServiceUtil.createUserMock(KEY_FOLLOWER1, "ImFollowerOne",
				"ImFollowerOne@mail.com", null, null, null, null);
		FollowerPK pk2 = new FollowerPK(KEY_FOLLOWER2, KEY_USER2);
		User user2 = TestServiceUtil.createUserMock(11111l, USERNAME2, EMAIL2, null, null, null,
				null);
		Follower follower2 = new Follower(pk2, user2);
		User userFollower2 = TestServiceUtil.createUserMock(KEY_FOLLOWER2, "ImFollowerTwo",
				"ImFollowerTwo@mail.com", null, null, null, null);

		Mockito.when(userFollowed.getUsername()).thenReturn(USERNAME);
		List<Follower> followers = new ArrayList<>(2);
		followers.add(follower1);
		followers.add(follower2);
		Mockito.when(userFollowed.getFollowers()).thenReturn(followers);

		Mockito.when(userDao.findByUsername(USERNAME)).thenReturn(userFollowed);
		Mockito.when(userDao.findOne(KEY_FOLLOWER1)).thenReturn(userFollower1);
		Mockito.when(userDao.findOne(KEY_FOLLOWER2)).thenReturn(userFollower2);

		Idiom idiom = Mockito.mock(Idiom.class);
		Mockito.when(idiom.getCode()).thenReturn("idiom");
		Mockito.when(userFollowed.getIdiom()).thenReturn(idiom);

		GetFollowersRequest request = Mockito.mock(GetFollowersRequest.class);
		Mockito.when(request.getUsername()).thenReturn(USERNAME);

		ListUsersFollowedResponse response = getFollowersCmd.execute(request);

		assertNotNull(response);
		assertNotNull(response.getUsers());
		assertEquals(response.getUsers().size(), 2);

		Mockito.verify(userDao, Mockito.times(1)).findByUsername(USERNAME);
		Mockito.verify(userDao, Mockito.times(1)).findOne(KEY_FOLLOWER1);
		Mockito.verify(userDao, Mockito.times(1)).findOne(KEY_FOLLOWER2);

	}

	@Test
	public void executeNoFollowersTest() {

		Idiom idiom = Mockito.mock(Idiom.class);
		Mockito.when(idiom.getCode()).thenReturn("idiom");

		User user = new User();
		user.setIdiom(idiom);
		user.setUsername(USERNAME);

		Mockito.when(userDao.findByUsername(USERNAME)).thenReturn(user);

		GetFollowersRequest request = Mockito.mock(GetFollowersRequest.class);
		Mockito.when(request.getUsername()).thenReturn(USERNAME);

		ListUsersFollowedResponse response = getFollowersCmd.execute(request);

		assertNotNull(response);
		assertEquals(response.getUsers().size(), 0);

	}

}
