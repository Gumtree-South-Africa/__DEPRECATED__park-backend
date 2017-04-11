package com.ebay.park.event.item;

import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.notification.dto.NotifiableServiceResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link ItemNotificationEvent}.
 * @author Julieta Salvadó
 */
public class ItemNotificationEventTest {
    private static final long USER_ID = 1;
    private static final int NUMBER_OR_RECIPIENTS = 1;
    private static final long ITEM_ID = 4;
    private static final int NUMBER_OF_PROPERTIES = 3;
    private static final String ITEM_NAME = "name";

    @Mock
    private Item item;

    @Mock
    private User user;

    private ItemNotificationEvent event;

    @Before
    public void setUp() {
        initMocks(this);
        event = new ItemNotificationEvent(item);
        when(item.getPublishedBy()).thenReturn(user);
        when(user.getId()).thenReturn(USER_ID);
        when(item.getId()).thenReturn(ITEM_ID);
        when(item.getName()).thenReturn(ITEM_NAME);
    }

    @Test
    public void whenAskingForItemThenReturnItem() {
        assertThat("The item in the event should be the original item", event.getItem(), is(item));
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
    public void whenAskingForRecipientThenReturnItemOwner() {
        List<User> recipients = event.getRecipients();
        assertThat("Recipients should not be null", recipients, notNullValue());
        assertThat("Recipients should be only one", recipients.size(), is(NUMBER_OR_RECIPIENTS));
        assertThat("Recipient should be the item owner", event.getRecipients().get(0).getId(), is(user.getId()));
    }

    @Test
    public void whenAskingForMapThenReturnValidMap() {
        Map<String, String> map = event.toMap();
        assertThat("The event should contain three properties", map.size(), is(NUMBER_OF_PROPERTIES));
        assertThat("ITEM_NAME should be a property in the map", map.containsKey(NotifiableServiceResult.ITEM_NAME));
        assertThat("ITEM_ID should be a property in the map", map.containsKey(NotifiableServiceResult.ITEM_ID));
        assertThat("USER_NAME should be a property in the map", map.containsKey(NotifiableServiceResult.USER_NAME));
    }

    @Test
    public void whenSettingItemThenSetTheItem() {
        event.setItem(item);
        assertThat("The item in the event should be the original item", event.getItem(), is(item));
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullItemWhenCreatingThenException() {
        new ItemNotificationEvent(null);
    }
}