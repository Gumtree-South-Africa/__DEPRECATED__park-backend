package com.ebay.park.email;

import com.ebay.park.db.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit Test for {@link PasswordEmailSender}.
 * @author Julieta Salvadó
 */
public class PasswordEmailSenderTest {

    private static final String SUBJECT = "subject";
    private static final String TEMPLATE = "template";
    private static final String NEW_PASS = "new pass";

    @Mock
    private MailSender mailSender;

    @Mock
    private User user;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenSubjectWhenConstructionThenException() {
        PasswordEmailSender sender = new PasswordEmailSender(null, NEW_PASS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullTemplateWhenConstructionThenException() {
        PasswordEmailSender sender = new PasswordEmailSender(SUBJECT, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullSubjectAndTemplateWhenConstructionThenException() {
        PasswordEmailSender sender = new PasswordEmailSender(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullUserWhenSendingEmailThenException() {
        PasswordEmailSender sender = new PasswordEmailSender(SUBJECT, TEMPLATE);
        sender.sendEmail(null, NEW_PASS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullNewPasswordWhenSendingEmailThenException() {
        PasswordEmailSender sender = new PasswordEmailSender(SUBJECT, TEMPLATE);
        sender.sendEmail(user, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullNewPasswordAndUserWhenSendingEmailThenException() {
        PasswordEmailSender sender = new PasswordEmailSender(SUBJECT, TEMPLATE);
        sender.sendEmail(null, null);
    }
}