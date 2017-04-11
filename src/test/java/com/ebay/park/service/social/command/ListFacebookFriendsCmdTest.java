package com.ebay.park.service.social.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ebay.park.db.dao.SocialDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Idiom;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.social.dto.ListFacebookFriendsRequest;
import com.ebay.park.service.social.dto.ListFacebookFriendsResponse;
import com.ebay.park.util.FacebookUtil;
import com.ebay.park.util.InternationalizationUtil;

/**
 * Unit Test for {@link ListFacebookFriendsCmd}.
 * @author Julieta Salvadó
 */
public class ListFacebookFriendsCmdTest {

    private static final String USERNAME = "user";
	private static final long USER_ID = 1L;
	private static final String SOCIAL_ID1 = "12345";
    private static final String SOCIAL_ID2 = "12346";
    private static final String SOCIAL_ID3 = "12347";
	private static final String FB_TOKEN = "fb_token";
    private static final String AN_EXCEPTION_WAS_EXPECTED = "An exception was expected";
    private static final String LANG = "lang";
    private static final long FB_ID = 1L;

    @InjectMocks
	private ListFacebookFriendsCmd cmd = new ListFacebookFriendsCmd();

	@Mock
	private ListFacebookFriendsRequest request;

	@Mock
	private UserDao userDao;

	@Mock
	private UserSocialDao userSocialDao;

    @Mock
    private User user1, user2, user3;

	@Mock
	private SocialDao socialDao;

	@Mock
	private FacebookUtil facebookUtil;

	@Mock
	private UserSocial userSocial;

    private List<User> userList, indirectFriendsList;
    private List<String> indirectFriendsIds;

	@Mock
	private Social social;

    @Mock
    private InternationalizationUtil i18nUtil;

	@Before
	public void setUp() {
        initMocks(this);

        userList = Arrays.asList(user1, user2);
        indirectFriendsList = Arrays.asList(user3);
        when(request.getUsername()).thenReturn(USERNAME);
        when(request.getLanguage()).thenReturn(LANG);
        when(userDao.findByUsername(USERNAME)).thenReturn(user1);
        when(user1.getId()).thenReturn(USER_ID);
        when(userSocialDao.findFacebookUser(USER_ID)).thenReturn(userSocial);
        when(userSocial.getToken()).thenReturn(FB_TOKEN);
        doNothing().when(i18nUtil).internationalizeListedResponse(any(ListFacebookFriendsResponse.class), anyString(), anyString());
        when(userDao.findBySocialIdAndUserSocialIds(anyLong(), any(List.class))).thenReturn(userList);
        when(socialDao.findByDescription(Social.FACEBOOK)).thenReturn(social);
        when(social.getSocialId()).thenReturn(FB_ID);
        when(facebookUtil.getFriendIds(FB_TOKEN)).thenReturn(Arrays.asList(SOCIAL_ID1, SOCIAL_ID2));
        indirectFriendsIds = Arrays.asList(SOCIAL_ID3);
        when(facebookUtil.getFriendsOfFriend(FB_TOKEN, SOCIAL_ID1)).thenReturn(indirectFriendsIds);
    }

    @Test (expected = IllegalArgumentException.class)
    public void givenNullRequestWhenExecutingThenReturnException() {
        cmd.execute(null);
    }

    @Test
    public void givenInvalidUsernameWhenExecutingThenException() {
    	when(userDao.findByUsername(USERNAME)).thenReturn(null);

		try {
			cmd.execute(request);
            fail(AN_EXCEPTION_WAS_EXPECTED);
		} catch (ServiceException e) {
			assertThat(ServiceExceptionCode.USER_NOT_FOUND.getCode(), is(e.getCode()));
		}
    }

    @Test
	public void givenUserAccountNotBoundToFacebookWhenExecutingThenException() {
        when(userSocialDao.findFacebookUser(USER_ID)).thenReturn(null);

		try {
			cmd.execute(request);
			fail(AN_EXCEPTION_WAS_EXPECTED);
		} catch (ServiceException e) {
			assertThat(ServiceExceptionCode.INVALID_USER_SOCIAL.getCode(), is(e.getCode()));
		}
	}

    @Test
    public void givenNullFriendListWhenExecutingThenReturnMessage() {
        when(facebookUtil.getFriendIds(FB_TOKEN)).thenReturn(null);

        ListFacebookFriendsResponse response = cmd.execute(request);

        assertThat(response, notNullValue());
        verify(i18nUtil).internationalizeListedResponse(any(ListFacebookFriendsResponse.class), anyString(), anyString());
    }

    @Test
    public void givenEmptyFriendListWhenExecutingThenReturnMessage() {
        when(facebookUtil.getFriendIds(FB_TOKEN)).thenReturn(null);

        ListFacebookFriendsResponse response = cmd.execute(request);

        assertThat(response, notNullValue());
        verify(i18nUtil).internationalizeListedResponse(any(ListFacebookFriendsResponse.class), anyString(), anyString());
    }

    @Test
    public void givenEmptyFriendListAndNullReqLangWhenExecutingThenReturnMessage() {
        when(facebookUtil.getFriendIds(FB_TOKEN)).thenReturn(null);
        when(request.getLanguage()).thenReturn(null);
        Idiom idiom = new Idiom();
        idiom.setCode(LANG);
        when(user1.getIdiom()).thenReturn(idiom);

        ListFacebookFriendsResponse response = cmd.execute(request);

        assertThat(response, notNullValue());
        verify(i18nUtil).internationalizeListedResponse(any(ListFacebookFriendsResponse.class), anyString(), anyString());
    }

    @Test
    public void givenValidRequestWhenExecutingThenReturnFriendList() {
        List<User> tmpList = new ArrayList<>(userList);
        tmpList.addAll(indirectFriendsList);
        when(userDao.findBySocialIdAndUserSocialIds(anyLong(), any(List.class))).thenReturn(tmpList);
        ListFacebookFriendsResponse response = cmd.execute(request);
        assertEquals(userList.size() + indirectFriendsIds.size(), response.getFriends().size());
    }

    @Test (expected = ServiceException.class)
    public void givenInsufficientFBPermissionWhenExecutingThenException() {
        doThrow(ServiceException.class).when(facebookUtil).getFriendIds(FB_TOKEN);
        cmd.execute(request);
    }

    @Test
    public void givenEmptyListOfIndirectFriendsWhenExecutingThenReturnOnlyDirectFriends() {
        when(facebookUtil.getFriendsOfFriend(FB_TOKEN, SOCIAL_ID1)).thenReturn(new ArrayList<String>());
        ListFacebookFriendsResponse response = cmd.execute(request);
        assertEquals(userList.size(), response.getFriends().size());
    }

    @Test
    public void givenNullListOfIndirectFriendsWhenExecutingThenReturnOnlyDirectFriends() {
        when(facebookUtil.getFriendsOfFriend(FB_TOKEN, SOCIAL_ID1)).thenReturn(null);
        ListFacebookFriendsResponse response = cmd.execute(request);
        assertEquals(userList.size(), response.getFriends().size());
    }
}
