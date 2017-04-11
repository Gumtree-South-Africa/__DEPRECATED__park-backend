package com.ebay.park.push.dummy;

import com.ebay.park.push.AndroidNotificationPusher;
import com.ebay.park.push.PushNotification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * This is just a <i>dummy</i> {@link AndroidNotificationPusher} that does not actually push nothing, just log.
 * <p>Note that it's annotated as {@link Primary}, so spring will use this bean to resolve ambiguity for autowiring
 * AndroidNotificationPusher</p> only when profile <code>local</code> is activated
 *
 * @author gervasio.amy
 * @since 20/09/2016.
 */
@Primary
@Component
@Profile("local")
public class DummyAndroidNotificationPusher extends AndroidNotificationPusher {

    private static final Logger logger = LogManager.getLogger(DummyAndroidNotificationPusher.class);

    @Override
    public void push(PushNotification notification) {
        logger.info("######### Sending ANDROID push: {}", notification);
    }
}
