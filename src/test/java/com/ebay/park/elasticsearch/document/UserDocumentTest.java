package com.ebay.park.elasticsearch.document;

import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserFollowsGroup;
import com.ebay.park.db.entity.UserFollowsGroupPK;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static com.ebay.park.db.entity.UserStatusDescription.ACTIVE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link UserDocument}.
 * @author Julieta Salvadó
 */
public class UserDocumentTest {

    private static final String USERNAME = "soyjulieta";
    private static final String EMAIL = "soyjulieta@globant.com";
    private static final boolean VERIFIED = false;
    private static final double LATITUDE = 23;
    private static final double LONGITUDE = 23;
    private static long ID = 1L;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullUserWhenConvertingFromUserToUserDocumentThenException() {
        new UserDocument(null);
    }

    @Test
    public void givenValidUserWhenConvertingFromUserToUserDocumentThenCreate() {
        Date creationDate = new Date();
        User user = mock(User.class);
        when(user.getUserId()).thenReturn(ID);
        when(user.getCreation()).thenReturn(creationDate);
        when(user.getUsername()).thenReturn(USERNAME);
        when(user.getStatus()).thenReturn(ACTIVE);
        when(user.getEmail()).thenReturn(EMAIL);
        when(user.isEmailVerified()).thenReturn(VERIFIED);
        when(user.isMobileVerified()).thenReturn(VERIFIED);
        when(user.getLongitude()).thenReturn(LONGITUDE);
        when(user.getLatitude()).thenReturn(LATITUDE);
        UserDocument userDocument = new UserDocument(user);

        assertThat("User Id must be copied", userDocument.getUserId(), is(ID));
        assertThat("Username Id must be copied", userDocument.getUsername(), is(user.getUsername()));
        assertThat("Email address must be copied", userDocument.getEmail(), is(EMAIL));
        assertThat("Creation date must be copied", userDocument.getCreation().getTime(), is(creationDate.getTime()));
        assertThat("User status must be copied", userDocument.getStatus(), is(ACTIVE.toString()));
        assertThat("User email verified must be copied", userDocument.getEmailVerified(), is(VERIFIED));
        assertThat("Latitude must be copied", userDocument.getLocation().getLat(), is(LATITUDE));
        assertThat("Longitude must be copied", userDocument.getLocation().getLon(), is(LONGITUDE));
        assertThat("User mobile verified must be copied", userDocument.getMobileVerified(), is(VERIFIED));
    }

    @Test
    public void givenValidUserWithEmptyGroupsWhenConvertingFromUserToUserDocumentThenCreate() {
        Date creationDate = new Date();
        User user = mock(User.class);
        when(user.getUserId()).thenReturn(ID);
        when(user.getGroups()).thenReturn(new ArrayList<UserFollowsGroup>());
        when(user.getCreation()).thenReturn(creationDate);
        when(user.getEmail()).thenReturn(EMAIL);
        when(user.isEmailVerified()).thenReturn(VERIFIED);
        when(user.getStatus()).thenReturn(ACTIVE);
        UserDocument userDocument = new UserDocument(user);

        assertThat("Group must be empty", userDocument.getGroups().size(), is(0));
    }

    @Test
    public void givenValidUserWithNullGroupsWhenConvertingFromUserToUserDocumentThenCreate() {
        Date creationDate = new Date();
        User user = mock(User.class);
        when(user.getUserId()).thenReturn(ID);
        when(user.getGroups()).thenReturn(null);
        when(user.getCreation()).thenReturn(creationDate);
        when(user.getEmail()).thenReturn(EMAIL);
        when(user.isEmailVerified()).thenReturn(VERIFIED);
        when(user.getStatus()).thenReturn(ACTIVE);
        UserDocument userDocument = new UserDocument(user);

        assertThat("Group must be empty", userDocument.getGroups().size(), is(0));
    }

    @Test
    public void givenValidUserWithNotEmptyGroupsWhenConvertingFromUserToUserDocumentThenCreate() {
        Date creationDate = new Date();
        User user = mock(User.class);
        when(user.getUserId()).thenReturn(ID);
        UserFollowsGroup userFollowsGroup = mock(UserFollowsGroup.class);
        UserFollowsGroupPK pk = mock(UserFollowsGroupPK.class);
        when(userFollowsGroup.getId()).thenReturn(pk);
        Group group = mock(Group.class);
        when(userFollowsGroup.getGroup()).thenReturn(group);
        when(pk.getUserId()).thenReturn(ID);
        when(group.getId()).thenReturn(ID);
        when(user.getGroups()).thenReturn(Arrays.asList(userFollowsGroup));
        when(user.getCreation()).thenReturn(creationDate);
        when(user.getEmail()).thenReturn(EMAIL);
        when(user.isEmailVerified()).thenReturn(VERIFIED);
        when(user.getStatus()).thenReturn(ACTIVE);
        UserDocument userDocument = new UserDocument(user);

        assertThat("Group must be not empty", userDocument.getGroups().size(), is(1));
    }
}