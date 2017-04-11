package com.ebay.park.service.group.command;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.SocialDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.group.dto.ShareGroupToSocialNetworkRequest;
import com.ebay.park.util.FacebookUtil;
import com.ebay.park.util.TextUtils;
import com.ebay.park.util.TwitterUtil;

public class ShareGroupCmdTest {

	private static final String TWITTER_TOKEN = "twitter_token";
	private static final String FACEBOOK_TOKEN = "facebook_token";
	private static final String INVALID_SOCIAL_NETWORK = "invalid";
	private static final Long USER_ID = 10l;

	@InjectMocks
	private ShareGroupCmd shareGroupCmd = new ShareGroupCmd();

	@Mock
	private GroupDao groupDao;

	@Mock
	private UserDao userDao;

	@Mock
	private UserSocialDao userSocialDao;

	@Mock
	private SocialDao socialDao;

	@Mock
	private FacebookUtil facebookUtil;

	@Mock
	private TwitterUtil twitterUtil;

	@Mock
	private TextUtils textUtils;

	@Mock
	private User user;

	@Mock
	private Group group;

	@Mock
	private Social social;

	@Mock
	private UserSocial userSocial;

	@Mock
	private ShareGroupToSocialNetworkRequest request;

	@Before
	public void setUp() {
		initMocks(this);
	}

	@Test
	public void givenTwitterRequestThenShareGroupOnTwitterSuccess() {
		when(user.getId()).thenReturn(USER_ID);
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getGroupId())).thenReturn(group);
		when(group.getCreator()).thenReturn(user);
		when(social.getDescription()).thenReturn(Social.TWITTER);
		when(socialDao.findByDescription(request.getSocialNetwork())).thenReturn(social);
		when(userSocialDao.findTwitterUser(user.getId())).thenReturn(userSocial);
		when(userSocial.getToken()).thenReturn(TWITTER_TOKEN);
		when(userSocial.getTokenSecret()).thenReturn(TWITTER_TOKEN);
		ServiceResponse response = shareGroupCmd.execute(request);
		assertEquals(ServiceResponse.SUCCESS.getStatusCode(), response.getStatusCode());
	}

	@Test
	public void givenFacebookRequestThenShareGroupOnFacebookSuccess() {
		when(user.getId()).thenReturn(USER_ID);
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getGroupId())).thenReturn(group);
		when(group.getCreator()).thenReturn(user);
		when(social.getDescription()).thenReturn(Social.FACEBOOK);
		when(socialDao.findByDescription(request.getSocialNetwork())).thenReturn(social);
		when(userSocialDao.findFacebookUser(user.getId())).thenReturn(userSocial);
		when(userSocial.getToken()).thenReturn(FACEBOOK_TOKEN);
		when(userSocial.getTokenSecret()).thenReturn(FACEBOOK_TOKEN);
		ServiceResponse response = shareGroupCmd.execute(request);
		assertEquals(ServiceResponse.SUCCESS.getStatusCode(), response.getStatusCode());
	}

	@Test
	public void givenInvalidTokenRequestThenException() {
		when(userDao.findByToken(request.getToken())).thenReturn(null);
		try {
			shareGroupCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_UNAUTHORIZED.getCode());
		}
	}

	@Test
	public void givenInvalidGroupRequestThenException() {
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getGroupId())).thenReturn(null);
		try {
			shareGroupCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.INVALID_GROUP.getCode());
		}
	}

	@Test
	public void givenUserNotOwnerOfGroupRequestThenException() {
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getGroupId())).thenReturn(group);
		when(group.getCreator()).thenReturn(Mockito.mock(User.class));
		try {
			shareGroupCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.INVALID_GROUP_OWNER.getCode());
		}
	}

	@Test
	public void givenInvalidSocialNetworkRequestThenException() {
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getGroupId())).thenReturn(group);
		when(group.getCreator()).thenReturn(user);
		when(socialDao.findByDescription(request.getSocialNetwork())).thenReturn(null);
		try {
			shareGroupCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.INVALID_SOCIAL_NETWORK.getCode());
		}
	}

	@Test
	public void givenInvalidSocialNetworkFromSocialThenException() {
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getGroupId())).thenReturn(group);
		when(group.getCreator()).thenReturn(user);
		when(socialDao.findByDescription(request.getSocialNetwork())).thenReturn(social);
		when(social.getDescription()).thenReturn(INVALID_SOCIAL_NETWORK);
		try {
			shareGroupCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.INVALID_SOCIAL_NETWORK.getCode());
		}
	}

	@Test
	public void givenInvalidFacebookTokenThenException() {
		when(user.getId()).thenReturn(USER_ID);
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getGroupId())).thenReturn(group);
		when(group.getCreator()).thenReturn(user);
		when(social.getDescription()).thenReturn(Social.FACEBOOK);
		when(socialDao.findByDescription(request.getSocialNetwork())).thenReturn(social);
		when(userSocialDao.findFacebookUser(user.getId())).thenReturn(userSocial);
		when(userSocial.getToken()).thenReturn(null);
		try {
			shareGroupCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.INVALID_USER_SOCIAL.getCode());
		}
	}

	@Test
	public void givenInvalidTwitterTokenThenException() {
		when(user.getId()).thenReturn(USER_ID);
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.findOne(request.getGroupId())).thenReturn(group);
		when(group.getCreator()).thenReturn(user);
		when(social.getDescription()).thenReturn(Social.TWITTER);
		when(socialDao.findByDescription(request.getSocialNetwork())).thenReturn(social);
		when(userSocialDao.findTwitterUser(user.getId())).thenReturn(userSocial);
		when(userSocial.getToken()).thenReturn(null);
		try {
			shareGroupCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.INVALID_USER_SOCIAL.getCode());
		}
	}

}
