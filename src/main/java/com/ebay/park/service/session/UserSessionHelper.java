package com.ebay.park.service.session;

import com.ebay.park.db.dao.UserSessionDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.session.command.CreateUserSessionCmd;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.command.signup.AccountKitEmailSignUpCmdV3;
import com.ebay.park.service.user.dto.signin.SignInRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * Helper methods for user session.
 * @author Julieta Salvadó
 */
@Component
public class UserSessionHelper {
    private static Logger LOGGER = LoggerFactory.getLogger(AccountKitEmailSignUpCmdV3.class);

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserServiceHelper userServiceHelper;

    @Autowired
    private CreateUserSessionCmd createUserSessionCmd;
    

    public String createSession(User user, SignInRequest param) {
        Assert.notNull(user, "User must be not null");
        Assert.notNull(param, "The incoming request must be not null");
        try {
            UserSession userSession = createUserSessionCmd.execute(param);
            if (userSession != null) {
                user.addUserSession(userSession);
                sessionService.createUserSessionCache(userSession);
                userServiceHelper.saveUser(user);
            }
            return userSession.getToken();
        } catch (Exception e) {
            LOGGER.error("Error trying to create a user session for username: {}", user.getUsername(), e);
            throw createServiceException(ServiceExceptionCode.IO_ERROR);
        }
    }
}
