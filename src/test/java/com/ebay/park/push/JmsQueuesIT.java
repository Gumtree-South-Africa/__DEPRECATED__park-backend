package com.ebay.park.push;

import com.ebay.park.config.ActiveMQEmbeddedConfig;
import com.ebay.park.config.JmsConfig;
import com.ebay.park.config.JmsTestConfig;
import com.ebay.park.email.Email;
import com.ebay.park.email.MailSender;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationDispatcher;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.notification.consumer.PushNotificationConsumer;
import com.ebay.park.notification.dto.MailNotificationMessage;
import com.ebay.park.notification.dto.NotificationMessage;
import com.ebay.park.notification.dto.PushNotificationMessage;
import com.ebay.park.notification.dto.PushNotificationMessage.PushNotificationBuilder;
import com.ebay.park.service.device.dto.DeviceDTO;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

/**
 * This is an integration test for dispatching notifications
 *
 * @see NotificationDispatcher# dispatch(Set)
 * @author gervasio.amy
 * @since 14/09/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JmsTestConfig.class, JmsConfig.class, ActiveMQEmbeddedConfig.class })
@EnableAutoConfiguration
@WebAppConfiguration
public class JmsQueuesIT {

    private static final String BADGE = "0";
    @Autowired
    private NotificationDispatcher notificationDispatcher;

    @Autowired
    private PushNotificationConsumer pushNotificationConsumer;

    @Autowired
    private SmartPusher mockedSmartPusher;

    @Autowired
    private MailSender mailSender;

    @Captor
    ArgumentCaptor<PushNotification> pushCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); // for captors
    }

    @Test
    @Ignore
    public void givenPushWhenDispatchThen() throws IOException {
        Mockito.reset(mockedSmartPusher);
        List<NotificationMessage> notifications = new ArrayList<>();
        //let's create a push notification
        String template = "${senderUsername} sent you a new message about ${itemName}";
        String deviceId = "deviceId12345";
        PushNotificationMessage pushNotification = new PushNotificationBuilder(NotificationAction.CHAT_SENT,
                NotificationType.PUSH, new DeviceDTO(deviceId, "ANDROID"), template, BADGE).build();

        notifications.add(pushNotification);

        notificationDispatcher.dispatch(notifications);

        verify(mockedSmartPusher).push(pushCaptor.capture());
        assertEquals(template, pushCaptor.getValue().getTemplateMessage());
        assertEquals(deviceId, pushCaptor.getValue().getDeviceId());
        assertEquals(deviceId, pushCaptor.getValue().getDeviceType());

        /*Expected :deviceId12345
        Actual   :ANDROID*/
    }

    @Test
    @Ignore
    public void givenEmailWhenDispathThen() throws IOException {
        Mockito.reset(mailSender);
        List<NotificationMessage> notifications = new ArrayList<>();
        //let's create a mail notification
        String template = "${senderUsername} sent you a new message about ${itemName}";
        String to = "foo@bar.com";
        String subject = "email subject";
        String username = "johndoe";
        MailNotificationMessage mailNotification = new MailNotificationMessage.MailNotificationBuilder(NotificationAction.CHAT_SENT,
                NotificationType.EMAIL, to, subject, template, username).build();
        notifications.add(mailNotification);

        notificationDispatcher.dispatch(notifications);

        verify(mailSender).sendAsync(any(Email.class));
    }

}
