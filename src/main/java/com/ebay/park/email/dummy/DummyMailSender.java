package com.ebay.park.email.dummy;

import com.ebay.park.email.Email;
import com.ebay.park.email.MailSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * This is just a <i>dummy</i> {@link MailSender} that does not actually send any email, just log.
 * <p>Note that it's annotated as {@link Primary}, so spring will use this bean to resolve ambiguity for autowiring
 * IOSNotificationPusher</p> only when profile <code>local</code> is activated
 *
 * @author gervasio.amy
 * @since 20/09/2016.
 */
@Primary
@Component
@Profile("local")
public class DummyMailSender implements MailSender {

    private static final Logger logger = LogManager.getLogger(DummyMailSender.class);

    @Override
    public void send(Email email) {
        logger.info("######### Sending email: {}", email);
    }

    @Override
    public void sendAsync(Email email) {
        this.send(email);
    }
}
