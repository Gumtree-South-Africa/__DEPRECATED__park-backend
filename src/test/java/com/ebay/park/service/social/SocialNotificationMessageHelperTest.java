package com.ebay.park.service.social;

import com.ebay.park.db.dao.SocialDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.event.user.UserFBFriendEvent;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.util.FacebookUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class SocialNotificationMessageHelperTest {
	
	private static final Long SOCIAL_ID = 1L;
	private static final String USER_FRIEND_ID = "1";
	private static final String USER_ID = "2";
	private static final String FACEBOOK_TOKEN = "token";
	private static final String FB_USERNAME = "FB-username";
	private static final String PICTURE = "url";
	private static final String FACEBOOK_USER_ID = "id";

	@Mock
	private FacebookUtil facebookUtil;
	
	@Mock
	private UserServiceHelper userServiceHelper;

	@Mock
	private SocialDao socialDao;
	
	@Mock
	private ApplicationContext applicationContext;

	@Mock
	private SocialNotificationHelper proxyOfMe;

	@InjectMocks
	private SocialNotificationHelper socialNotificationHelper = new SocialNotificationHelper();
	
	private Social social;
	private List<String> facebookFriendIds;
	private User friend;
	private List<User> facebookFriends;
	private User user;
	private UserFBFriendEvent event;
	
	@Before
	public void setUp() {
		initMocks(this);
		
		social = new Social();
		social.setDescription(Social.FACEBOOK);
		social.setSocialId(SOCIAL_ID);
		
		facebookFriendIds = new ArrayList<>();
		
		friend = new User();
		friend.setId(Long.valueOf(USER_FRIEND_ID));
		
		facebookFriends = new ArrayList<>();
		
		user = new User();
		user.setId(Long.valueOf(USER_ID));
		
		event = new UserFBFriendEvent(user, friend, FB_USERNAME, PICTURE);
		when(facebookUtil.getUserPictureEscaped(anyString(), anyString())).thenReturn(PICTURE);
	}
	
	@Test
	public void givenAFriendListThenNotify() {
		//given
		facebookFriendIds.add(USER_FRIEND_ID);
		facebookFriends.add(friend);

		//when
		when(socialDao.findByDescription(Social.FACEBOOK)).thenReturn(social);
		when(facebookUtil.getFriendIds(FACEBOOK_TOKEN)).thenReturn(facebookFriendIds);
		when(userServiceHelper.findBySocialIdAndUserSocialIds(SOCIAL_ID, facebookFriendIds)).thenReturn(facebookFriends);
		when(facebookUtil.getFacebookUsername(FACEBOOK_TOKEN)).thenReturn(FB_USERNAME);
		when(proxyOfMe.notifyFBFriend(user, friend, FB_USERNAME, PICTURE)).thenReturn(event);

		socialNotificationHelper.notifyFBFriends(user, FACEBOOK_TOKEN, FACEBOOK_USER_ID);

		//then
		verify(proxyOfMe).notifyFBFriend(user, friend, FB_USERNAME, PICTURE);
	}
	
	@Test
	public void givenEmptyFacebookTokenThenException() {
		try {
			socialNotificationHelper.notifyFBFriends(user, null, FACEBOOK_USER_ID);
		} catch(ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.EMPTY_TOKEN.getCode());
		} finally {
			verify(proxyOfMe, never()).notifyFBFriend(user, friend, null, PICTURE);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenANullUserThenException() {
		socialNotificationHelper.notifyFBFriends(null, FACEBOOK_TOKEN, FACEBOOK_USER_ID);
	}

	@Test
	public void givenANullSocialThenNotNotify() {
		//given
		facebookFriendIds.add(USER_FRIEND_ID);
		facebookFriends.add(friend);

		//when
		when(socialDao.findByDescription(Social.FACEBOOK)).thenReturn(null);
		socialNotificationHelper.notifyFBFriends(user, FACEBOOK_TOKEN, FACEBOOK_USER_ID);

		//then
		verify(proxyOfMe, never()).notifyFBFriend(user, friend, FB_USERNAME, PICTURE);
	}

	@Test
	public void givenAnEmptyFBFriendListThenNotNotify() {
		//when
		when(facebookUtil.getFriendIds(FACEBOOK_TOKEN)).thenReturn(Collections.emptyList());
		socialNotificationHelper.notifyFBFriends(user, FACEBOOK_TOKEN, FACEBOOK_USER_ID);

		//then
		verify(proxyOfMe, never()).notifyFBFriend(user, friend, FB_USERNAME, PICTURE);
	}

    @Test
    public void givenANullFBFriendListThenNotNotify() {
        when(facebookUtil.getFriendIds(FACEBOOK_TOKEN)).thenReturn(null);
        socialNotificationHelper.notifyFBFriends(user, FACEBOOK_TOKEN, FACEBOOK_USER_ID);

        verify(proxyOfMe, never()).notifyFBFriend(user, friend, FB_USERNAME, PICTURE);
    }

    @Test
    public void givenAnNullFBUsernameWhenNotifyingFBFriendThenNotNotify() {
        when(facebookUtil.getFriendIds(FACEBOOK_TOKEN)).thenReturn(facebookFriendIds);
        when(facebookUtil.getFacebookUsername(FACEBOOK_TOKEN)).thenReturn(null);
        socialNotificationHelper.notifyFBFriends(user, FACEBOOK_TOKEN, FACEBOOK_USER_ID);

        verify(proxyOfMe, never()).notifyFBFriend(user, friend, FB_USERNAME, PICTURE);
    }

}
