package com.ebay.park.service.blacklist;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.event.item.ItemNotificationEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.aop.Notifiable;
import com.ebay.park.util.TextUtils;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link BlacklistServiceImpl}.
 * @author Julieta Salvadó
 */
public class BlacklistServiceImplTest {
    private static final long ITEM_ID = 1L;
    private static final String LANGUAGE = "es";
    @InjectMocks
    private BlacklistServiceImpl service;

    @Mock
    private ItemDao itemDao;

    @Mock
    private TextUtils textUtils;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test (expected = IllegalArgumentException.class)
    public void givenNullItemWhenBanningItemThenException() {
        service.bannedItem(null, LANGUAGE);
    }

    @Test
    public void givenValidItemWhenBanningItemThenBanTheItem() {
        Item item = mock(Item.class);
        service.bannedItem(item, LANGUAGE);

        verify(item).ban();
        verify(itemDao).save(item);
    }

    @Test
    public void givenValidItemWhenBanningItemThenAnnotationIsSpecified() throws NoSuchMethodException {
        Method method = BlacklistServiceImpl.class.getDeclaredMethod("bannedItem", Item.class, String.class);

        assertThat(method.getAnnotations().length, is(1));
        Notifiable annotation = (Notifiable) method.getAnnotations()[0];
        assertThat(annotation.annotationType().getName(), is(Notifiable.class.getName()));
        assertThat(annotation.action()[0], is(NotificationAction.ITEM_BANNED));
    }

    @Test
    public void givenValidItemWhenBanningItemThenReturnItemInEvent() {
        Item item = mock(Item.class);
        User user = mock(User.class);
        when(item.getId()).thenReturn(ITEM_ID);
        when(item.getPublishedBy()).thenReturn(user);
        ItemNotificationEvent event = service.bannedItem(item, LANGUAGE);

        assertThat(event.getItemId(), is(ITEM_ID));
        assertThat(event.getItem(), is(item));
    }

    @Test
    public void givenValidItemWhenBanningItemThenReturnRecipientsInEvent() {
        Item item = mock(Item.class);
        User user = mock(User.class);
        when(item.getId()).thenReturn(ITEM_ID);
        when(item.getPublishedBy()).thenReturn(user);
        ItemNotificationEvent event = service.bannedItem(item, LANGUAGE);

        assertThat("The publisher should be the recipient", event.getRecipients(), IsCollectionContaining.hasItem(user));
        assertThat("Only one recipient is expected", event.getRecipients().size(), is(1));
    }
}