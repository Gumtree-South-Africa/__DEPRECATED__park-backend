package com.ebay.park.push;

import com.ebay.park.util.MessageUtil;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Null;

import java.io.IOException;

/**
 * @author gervasio.amy
 * @since 13/09/2016.
 */
public class AndroidNotificationPusherTest {

    @InjectMocks
    private AndroidNotificationPusher androidNotificationPusher = new AndroidNotificationPusher();;

    @Mock
    private Sender sender;

    @Mock
    private MessageUtil messageUtil;

    @Before
    public void beforeTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenEmptyPushNotificationThen() throws IOException {
        when(messageUtil.formatMessage(null, null, null)).thenReturn(null);
        PushNotification notification = new PushNotification();
        androidNotificationPusher.push(notification);
        verify(sender).send(any(Message.class), anyString(), eq(1));
    }

    @Test
    public void givenNonEmptyPushNotificationThen() throws IOException {
        PushNotification notification = new PushNotification();
        notification.setDeviceId("device_1");
        notification.setDeviceType("dev_type");
        notification.setTemplateMessage("some_template_message");
        when(messageUtil.formatMessage(null, null, null)).thenReturn(null);
        androidNotificationPusher.push(notification);
        verify(sender).send(any(Message.class), eq("device_1"), eq(1));
    }
}
