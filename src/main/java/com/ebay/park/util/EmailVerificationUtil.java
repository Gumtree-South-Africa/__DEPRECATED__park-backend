package com.ebay.park.util;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.entity.NotificationConfig;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.notification.NotificationService;
import com.ebay.park.service.user.UserServiceHelper;

import java.util.List;

@Component
public class EmailVerificationUtil {

    @Resource(name = "NotificationServiceOp")
    private NotificationService notificationService;

    @Autowired
    private FacebookUtil facebookUtil;

    @Autowired
    private UserServiceHelper userHelper;

    /**
     * Sets email verified as true and adds notifications by email. The user is NOT saved.
     * @param user
     */
    public void verify(User user) {
        user.setEmailVerified(true);
        user.getAccess().setTemporaryToken(null);
        addEmailNotification(user);
    }

    private void addEmailNotification(User user) {
        for(NotificationConfig notification :notificationService.getEmailNotificationConfig()){
            // check if the user has the notification before add it.
            if (user.getNotificationConfig(notification.getNotificationAction(), notification.getNotificationType()) == null){
                user.getNotificationConfigs().add(notification);
            }
        }
    }

    private void deleteEmailNotification(User user) {
        List<NotificationConfig> emailNotificationConfig = notificationService.getEmailNotificationConfig();
        if (!CollectionUtils.isEmpty(emailNotificationConfig)) {
            for (NotificationConfig notification : emailNotificationConfig) {
                if (user.getNotificationConfig(notification.getNotificationAction(), notification.getNotificationType()) != null) {
                    user.getNotificationConfigs().remove(notification);
                }
            }
        }
    }

    /**
     * Sets email verified as true and adds notifications by email, only if FB data
     * is valid. The user is NOT saved.
     * @param user the user to be verified
     */
    public void verifyForFacebook(User user, String socialToken) {
        verify(user, facebookUtil.getEmail(socialToken));
    }

    /**
     * Sets email verified as true and adds notifications by email, only if the user is
     * registered with that email address. The user is NOT saved.
     * @param user the user to be verified
     * @param email the email address corresponding to the verification
     */
    public void verify(User user, String email) {
        User emailOwner = userHelper.findUserByEmail(email);
        if (!user.isEmailVerified() && user.equals(emailOwner)) {
            verify(user);
        }
    }

    /**
     * It deletes the user email, sets him/her as unverified for email and removes the notification settings
     * for email.
     * @param user the user. It must be not null.
     */
    public void unverify(User user) {
        userHelper.deleteUserEmail(user);
        user.setEmailVerified(false);
        deleteEmailNotification(user);
        userHelper.saveUser(user);
    }
}
