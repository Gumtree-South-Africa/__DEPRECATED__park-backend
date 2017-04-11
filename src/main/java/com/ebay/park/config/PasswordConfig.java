package com.ebay.park.config;

import com.ebay.park.email.PasswordEmailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bean definition for password email notifications.
 * @author Julieta Salvadó
 */
@Configuration
public class PasswordConfig {
    @Value("${email.welcome_pwd.subject}")
    private String emailWelcomePwdSubject;

    @Value("${email.welcome_pwd.template}")
    private String emailWelcomePwdTemplate;

    @Value("${email.forgot_pwd.subject}")
    private String emailForgotPwdSubject;

    @Value("${email.forgot_pwd.template}")
    private String emailForgotPwdTemplate;

    @Bean(name = "welcomePasswordEmailSender")
    public PasswordEmailSender welcomeEmailSender() {
        return new PasswordEmailSender(emailWelcomePwdSubject, emailWelcomePwdTemplate);
    }

    @Bean(name = "forgotPasswordEmailSender")
    public PasswordEmailSender forgotEmailSender() {
        return new PasswordEmailSender(emailForgotPwdSubject, emailForgotPwdTemplate);
    }
}
