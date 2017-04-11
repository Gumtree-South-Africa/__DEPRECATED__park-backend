package com.ebay.park.util;

import com.ebay.park.db.entity.NotificationConfig;
import com.ebay.park.db.entity.User;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.service.notification.NotificationService;
import com.ebay.park.service.user.UserServiceHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link EmailVerificationUtil}.
 * @author Julieta Salvadó
 */
public class EmailVerificationUtilTest {

    private static String EMAIL = "email";

    @InjectMocks
    private EmailVerificationUtil util;

    @Mock
    private UserServiceHelper userHelper;

    @Mock
    private NotificationService notificationService;

    @Mock
    private User user;

    @Before
    public void setUp() {
        initMocks(this);
        when(user.getEmail()).thenReturn(EMAIL);
        when(user.isMobileVerified()).thenReturn(true);
    }

    @Test
    public void givenNullNotificationConfigWhenUnverifingThenUnverifyAndDeleteEmail() {
        when(notificationService.getEmailNotificationConfig()).thenReturn(null);
        util.unverify(user);
        verify(userHelper).saveUser(user);
        assertThat(user.isEmailVerified(), is(false));
        verify(userHelper).deleteUserEmail(user);
    }

    @Test
    public void givenEmptyNotificationConfigWhenUnverifingThenUnverifyAndDeleteEmail() {
        when(notificationService.getEmailNotificationConfig()).thenReturn(new ArrayList<NotificationConfig>());
        util.unverify(user);
        verify(userHelper).saveUser(user);
        assertThat(user.isEmailVerified(), is(false));
        verify(userHelper).deleteUserEmail(user);
    }

    @Test
    public void givenNotEmptyNotificationConfigWhenUnverifingThenUnverifyAndDeleteEmailAndRemoveNotificationConfig() {

        List<NotificationConfig> notificationConfigs = getNotificationConfigs();
        when(notificationService.getEmailNotificationConfig()).thenReturn(notificationConfigs);
        when(user.getNotificationConfig(any(NotificationAction.class), any(NotificationType.class)))
                .thenReturn(mock(NotificationConfig.class));
        util.unverify(user);

        verify(userHelper).saveUser(user);
        assertThat(user.isEmailVerified(), is(false));
        verify(userHelper).deleteUserEmail(user);
    }

    private List<NotificationConfig> getNotificationConfigs() {
        NotificationConfig not1 = mock(NotificationConfig.class);
        NotificationConfig not2 = mock(NotificationConfig.class);
        NotificationConfig not3 = mock(NotificationConfig.class);
        return Arrays.asList(not1, not2, not3);
    }
}