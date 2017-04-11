package com.ebay.park.service.social.dto;

import com.ebay.park.db.entity.Follower;
import com.ebay.park.db.entity.User;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit Test for {@link FacebookFriend}.
 * @author Julieta Salvadó
 */
public class FacebookFriendTest {

    private static final String USERNAME = "username";
    private static final String LOCATION = "Tandil, BA";
    private static final String PICTURE = "picture";
    private static final String FRIEND_OF = "friend";

    private final User loggedUser = new User();

    @Test(expected = IllegalArgumentException.class)
    public void givenBothNullUserWhenCreatingThenException() {
        FacebookFriend instance = new FacebookFriend(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullLoggedUserWhenCreatingThenException() {
        FacebookFriend instance = new FacebookFriend(null, mock(User.class), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullFriendUserWhenCreatingThenException() {
        FacebookFriend instance = new FacebookFriend(mock(User.class), null, null);
    }

    @Test
    public void givenDirectFriendUserWhenCreatingThenFriendOfIsNotAField() {
        User loggedUser = mock(User.class);
        User friendUser = mock(User.class);
        when(friendUser.getUsername()).thenReturn(USERNAME);
        when(friendUser.getLocationName()).thenReturn(LOCATION);
        when(friendUser.getPicture()).thenReturn(PICTURE);

        FacebookFriend result = new FacebookFriend(loggedUser, friendUser, null);

        assertThat("Username should be the same", USERNAME, is(result.getUsername()));
        assertThat("Location should be the same", LOCATION, is(result.getLocation()));
        assertThat("Picture should be the same", PICTURE, is(result.getProfilePicture()));
        assertNull("FrienfOf field should have no content", result.getFriendOf());
    }

    @Test
    public void givenIndirectFriendUserWhenCreatingThenFriendOfHasValue() {
        User loggedUser = mock(User.class);
        User friendUser = mock(User.class);
        when(friendUser.getUsername()).thenReturn(USERNAME);
        when(friendUser.getLocationName()).thenReturn(LOCATION);
        when(friendUser.getPicture()).thenReturn(PICTURE);

        FacebookFriend result = new FacebookFriend(loggedUser, friendUser, FRIEND_OF);

        assertThat("Username should be the same", USERNAME, is(result.getUsername()));
        assertThat("Location should be the same", LOCATION, is(result.getLocation()));
        assertThat("Picture should be the same", PICTURE, is(result.getProfilePicture()));
        assertThat("FriendOf should be the same", FRIEND_OF, is(result.getFriendOf()));
    }

    @Test
    public void givenNotFollowedUserAndIndirectFriendWhenExecutingThenSetNotFollowed() {
        User loggedUser = mock(User.class);
        User friendUser = mock(User.class);
        Follower follower = mock(Follower.class);
        when(loggedUser.getFollowed()).thenReturn(Arrays.asList(follower));
        when(follower.getUserFollowed()).thenReturn(mock(User.class));
        when(friendUser.getUsername()).thenReturn(USERNAME);
        when(friendUser.getLocationName()).thenReturn(LOCATION);
        when(friendUser.getPicture()).thenReturn(PICTURE);

        FacebookFriend result = new FacebookFriend(loggedUser, friendUser, FRIEND_OF);

        assertFalse("The field should indicate the logged user is NOT following the friend user",
                result.getFollowedByUser());
    }

    @Test
    public void givenFollowedUserAndIndirectFriendWhenExecutingThenSetFollowed() {
        User loggedUser = mock(User.class);
        User friendUser = mock(User.class);
        Follower follower = mock(Follower.class);
        when(loggedUser.getFollowed()).thenReturn(Arrays.asList(follower));
        when(follower.getUserFollowed()).thenReturn(friendUser);
        when(friendUser.getUsername()).thenReturn(USERNAME);
        when(friendUser.getLocationName()).thenReturn(LOCATION);
        when(friendUser.getPicture()).thenReturn(PICTURE);

        FacebookFriend result = new FacebookFriend(loggedUser, friendUser, FRIEND_OF);

        assertTrue("The field should indicate the logged user is following the friend user",
                result.getFollowedByUser());
    }
}