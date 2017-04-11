package com.ebay.park.service.user.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.email.PasswordEmailSender;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.user.dto.EmailRequest;
import com.ebay.park.util.PasswdUtil;
import com.ebay.park.util.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * It welcomes a new user.
 * @author Julieta Salvadó
 */
@Component
public class WelcomeCmd implements ServiceCommand<EmailRequest, Void> {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswdUtil passwdUtil;

    @Autowired
    ApplicationContext context;

    @Override
    public Void execute(EmailRequest request) throws ServiceException {
        Assert.notNull(request);

        User user = userDao.findByEmail(request.getEmail());
        if (user == null) {
            throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
        }

        Password newPassword = new Password();
        user.setPassword(newPassword.getHashedPassword());
        user.getAccess().resetFailedSignInAttempts();

        userDao.save(user);

        sendNotification(user, newPassword);

        return null;
    }

    private void sendNotification(User user, Password newPassword) {
        PasswordEmailSender mailSender = (PasswordEmailSender) context.getBean("welcomePasswordEmailSender");
        mailSender.sendEmail(user, newPassword.getSimplePassword());
    }
}
