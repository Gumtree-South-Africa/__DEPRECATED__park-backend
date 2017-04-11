package com.ebay.park.email;

import com.ebay.park.db.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * It sends an email in case of auto generated password request.
 * @author Julieta Salvadó
 */
@Component
public class PasswordEmailSender {

    public static final String NEW_PASSWORD_PROPERTY = "newPassword";

    @Autowired
    private MailSender sender;

    private String subject;

    private String template;

    public PasswordEmailSender() {

    }

    public PasswordEmailSender(String subject, String template) {
        Assert.notNull(subject, "Subject must be not null here");
        Assert.notNull(template, "Template must be not null here");
        this.subject = subject;
        this.template = template;
    }

    public void sendEmail(User user, String newPassword) {
        Assert.notNull(user, "User asking for password update must be not null");
        Assert.notNull(newPassword, "Password to set must be not null");
        Map<String, String> props = user.toMap();
        props.put(NEW_PASSWORD_PROPERTY, newPassword);

        Email email = new Email();
        email.setTo(user.getEmail());
        email.setSubject(subject);
        email.setTemplate(template);
        email.setParams(props);

        sender.sendAsync(email);
    }
}
