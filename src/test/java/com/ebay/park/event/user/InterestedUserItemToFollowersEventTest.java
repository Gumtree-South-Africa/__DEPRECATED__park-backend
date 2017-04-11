package com.ebay.park.event.user;

import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link InterestedUserItemToFollowersEvent}.
 * @author Julieta Salvadó
 */
public class InterestedUserItemToFollowersEventTest {

    private static final String URL = "url";
    private static final long ID = 1L;

    @Test(expected = IllegalArgumentException.class)
    public void givenNullURLWhenCreatingEventThenException(){
        new InterestedUserItemToFollowersEvent(mock(Item.class), mock(User.class), null);
    }

    @Test
    public void givenValidEntriesWhenCreatingEventThenCreate(){
        Item item = mock(Item.class);
        User user = mock(User.class);
        when(user.getId()).thenReturn(ID);
        InterestedUserItemToFollowersEvent event = new InterestedUserItemToFollowersEvent(item, user, URL);
        assertThat("The url should match", event.getUrl(), is(URL));
        assertThat("The item should match", event.getItem(), is(item));
        assertThat("The user should match", event.getBasedUserId(), is(ID));
    }
}