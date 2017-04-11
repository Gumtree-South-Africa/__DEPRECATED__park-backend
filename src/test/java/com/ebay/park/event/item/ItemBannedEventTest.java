package com.ebay.park.event.item;

import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.notification.dto.NotifiableServiceResult;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link ItemBannedEvent}
 * @author Julieta Salvadó
 */
public class ItemBannedEventTest {

    private static final String ITEM_NAME = "name";
    private static final int NUMBER_OR_RECIPIENTS = 1;
    private static final long ITEM_ID = 1L;
    private static final String USERNAME = "username";
    private static final String URL = "url";
    private ItemBannedEvent event;

    @Mock
    private Item item;

    @Mock
    private User user;

    @Before
    public void setup() {
        initMocks(this);
        event = new ItemBannedEvent(item, URL);
        when(item.getName()).thenReturn(ITEM_NAME);
        when(item.getId()).thenReturn(ITEM_ID);
        when(item.getPublishedBy()).thenReturn(user);
        when(user.getUsername()).thenReturn(USERNAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullItemWhenCreatingThenException() {
        new ItemBannedEvent(null, URL);
    }

    @Test
    public void givenAllValidEntriesWhenAskingForMapThenIncludeItemNameInTheMap() {
        Map<String, String> map = event.toMap();
        assertThat("The item name should be part of the Map", map.get(NotifiableServiceResult.ITEM_NAME), is(item.getName()));
    }

    @Test
    public void givenAllValidEntriesWhenAskingForMapThenIncludeItemIdInTheMap() {
        Map<String, String> map = event.toMap();
        assertThat("The item id should be part of the Map", map.get(NotifiableServiceResult.ITEM_ID), is(item.getId().toString()));
    }

    @Test
    public void givenAllValidEntriesWhenAskingForMapThenIncludeUsernameInTheMap() {
        Map<String, String> map = event.toMap();
        assertThat("The username should be part of the Map", map.get(NotifiableServiceResult.USER_NAME), is(item.getPublishedBy().getUsername()));
    }

    @Test
    public void givenAllValidEntriesWhenAskingForMapThenIncludeURLInTheMap() {
        Map<String, String> map = event.toMap();
        assertThat("The username is part of the Map", map.get(NotifiableServiceResult.URL), is(URL));
    }

    @Test
    public void whenAskingForItemThenReturnItem() {
        assertThat("The item in the event should be the original item", event.getItem(),is(item));
    }

    @Test
    public void whenAskingForURLThenReturnURL() {
        assertThat("The URLm in the event should be the original URL", event.getUrl(), is(URL));
    }

    @Test
    public void whenAskingForItemIdThenReturnItemId() {
        assertThat("The item id in the event should be the original item id",
                event.getItemId(), is(item.getId()));
    }

    @Test
    public void whenAskingForUserThenReturnItemOwner() {
        assertThat("The user in the event should be the item owner", event.getBasedUserId(), is(user.getId()));
    }

    @Test
    public void whenSettingItemThenSetTheItem() {
        event.setItem(item);
        assertThat("The item in the event should be the original item", event.getItem(), is(item));
    }

    @Test
    public void whenAskingForRecipientThenReturnItemOwner() {
        List<User> recipients = event.getRecipients();
        assertThat("Recipients should not be null", recipients, notNullValue());
        assertThat("Recipients should be only one", recipients.size(), is(NUMBER_OR_RECIPIENTS));
        assertThat("Recipient should be the item owner", event.getRecipients().get(0).getId(), is(user.getId()));
    }
}