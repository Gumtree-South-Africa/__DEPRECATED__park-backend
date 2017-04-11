package com.ebay.park.push;

import javapns.notification.PushNotificationPayload;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link IOSNotificationPusher}.
 * @author Julieta Salvadó
 */
public class IOSNotificationPusherTest {

    private static final String MESSAGE = "message";
    private static final String SOUND = "push.aiff";
    private static final int BADGE = 2;

    @InjectMocks
    private IOSNotificationPusher pusher;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void givenMessageAndBadgeWhenGeneratingPushThenPushWithSound() throws JSONException {
        PushNotificationPayload payload = pusher.generatePushNotificationPayload(MESSAGE, BADGE);
        assertThat("The sound name should be in the payload", payload.getPayload().getJSONObject("aps").get("sound"), is(SOUND));
    }

    @Test
    public void givenMessageAndBadgeWhenGeneratingPushThenPushWithMessage() throws JSONException {
        PushNotificationPayload payload = pusher.generatePushNotificationPayload(MESSAGE, BADGE);
        assertThat("The message should be in the payload", payload.getPayload().getJSONObject("aps").get("alert"), is(MESSAGE));
    }

    @Test
    public void givenMessageAndBadgeWhenGeneratingPushThenPushWithBadge() throws JSONException {
        PushNotificationPayload payload = pusher.generatePushNotificationPayload(MESSAGE, BADGE);
        assertThat("The sound name should be in the payload", payload.getPayload().getJSONObject("aps").get("badge"), is(BADGE));
    }
}