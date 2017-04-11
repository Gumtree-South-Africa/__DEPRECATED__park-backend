package com.ebay.park.service.social;

import com.ebay.park.db.dao.SocialDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocialPK;
import com.ebay.park.service.social.validator.SocialHelper;
import com.ebay.park.service.user.UserServiceHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit Test for {@link UserSocialHelper}.
 * @author Julieta Salvadó
 */
public class UserSocialHelperTest {
    private static final String SOCIAL_NETWORK = "social network";
    private static final String TOKEN = "token";
    private static final long ID = 1L;
    private static final long SOCIAL_ID = 2L;

    @InjectMocks
    private UserSocialHelper helper;

    @Mock
    private UserServiceHelper userHelper;

    @Mock
    private User user;

    @Mock
    private SocialHelper socialHelper;

    @Mock
    private Social social;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullTokenWhenSearchingUserSocialIdByTokenAndSocialNetworkThenException() {
        helper.findUserSocialIdByTokenAndSocialNetwork(null, SOCIAL_NETWORK);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullSocialNetworkWhenSearchingUserSocialIdByTokenAndSocialNetworkThenException() {
        helper.findUserSocialIdByTokenAndSocialNetwork(TOKEN, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullSocialNetworkAndTokenWhenSearchingUserSocialIdByTokenAndSocialNetworkThenException() {
        helper.findUserSocialIdByTokenAndSocialNetwork(null, null);
    }

    @Test
    public void givenValidTokenWhenSearchingUserSocialIdByTokenAndSocialNetworkThenReturnUser() {
        when(user.getId()).thenReturn(ID);
        when(userHelper.findUserByToken(TOKEN)).thenReturn(user);
        when(socialHelper.findSocialByDescription(SOCIAL_NETWORK)).thenReturn(social);
        when(social.getSocialId()).thenReturn(SOCIAL_ID);

        UserSocialPK response = helper.findUserSocialIdByTokenAndSocialNetwork(TOKEN, SOCIAL_NETWORK);

        assertThat(response.getUserId(), is(user.getId()));
        assertThat(response.getSocialId(), is(SOCIAL_ID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullTokenWhenSearchingUserSocialIdByUserAndSocialNetworkThenException() {
        helper.findUserSocialIdByUserAndSocialNetwork(null, SOCIAL_NETWORK);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullSocialNetworkWhenSearchingUserSocialIdByUserAndSocialNetworkThenException() {
        helper.findUserSocialIdByUserAndSocialNetwork(user, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullSocialNetworkAndTokenWhenSearchingUserSocialIdByUserAndSocialNetworkThenException() {
        helper.findUserSocialIdByUserAndSocialNetwork(null, null);
    }

    @Test
    public void givenValidTokenWhenSearchingUserSocialIdByUserAndSocialNetworkThenReturnUser() {
        when(user.getId()).thenReturn(ID);
        when(userHelper.findUserByToken(TOKEN)).thenReturn(user);
        when(socialHelper.findSocialByDescription(SOCIAL_NETWORK)).thenReturn(social);
        when(social.getSocialId()).thenReturn(SOCIAL_ID);

        UserSocialPK response = helper.findUserSocialIdByUserAndSocialNetwork(user, SOCIAL_NETWORK);

        assertThat(response.getUserId(), is(user.getId()));
        assertThat(response.getSocialId(), is(SOCIAL_ID));
    }
}