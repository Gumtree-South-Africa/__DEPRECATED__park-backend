package com.ebay.park.notification.factory.item;

import com.ebay.park.db.entity.User;
import com.ebay.park.event.user.NotInterestedUserItemToFollowersEvent;
import com.ebay.park.notification.factory.NotificationContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link NotInterestedItemFollowersNotificationFactory}.
 * @author Julieta Salvadó
 */
public class NotInterestedItemFollowersNotificationFactoryTest {
    private NotInterestedItemFollowersNotificationFactory factory;

    @Mock
    private NotificationContext context;

    @Mock
    private NotInterestedUserItemToFollowersEvent event;

    @Mock
    private User user1;

    @Mock
    private User user2;

    @Before
    public void setUp() {
        initMocks(this);
        factory = new NotInterestedItemFollowersNotificationFactory(context);
    }

    @Test
    public void whenAskingForRecipientsThenReturnRecipients() {
        List<User> recipients = Arrays.asList(user1, user2);
        when(context.getNotifiableResult()).thenReturn(event);
        when(event.getRecipients()).thenReturn(recipients);
        List<User> usersToNotify = factory.getRecipients();

        assertThat("The same amount of users should be retrieve", usersToNotify.size(), equalTo(recipients.size()));
        assertThat(user1, isIn(usersToNotify));
        assertThat(user2, isIn(usersToNotify));
    }

    @Test
    public void givenNullRecipientsWhenAskingForRecipientsThenReturnNull() {
        when(context.getNotifiableResult()).thenReturn(event);
        when(event.getRecipients()).thenReturn(null);
        List<User> usersToNotify = factory.getRecipients();

        assertThat(usersToNotify, is(nullValue()));
    }
}