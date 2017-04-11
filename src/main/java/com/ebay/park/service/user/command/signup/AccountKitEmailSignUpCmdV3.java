package com.ebay.park.service.user.command.signup;

import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.session.UserSessionHelper;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.dto.SignUpResponse;
import com.ebay.park.service.user.dto.signup.AccountKitEmailSignUpRequest;
import com.ebay.park.util.EmailVerificationUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Command to perform an Account Kit with email. Version 3.
 * Sign up operation.
 *
 * @author scalderon
 * @since 2.0.2
 */
@Component
public class AccountKitEmailSignUpCmdV3 implements ServiceCommand<AccountKitEmailSignUpRequest, SignUpResponse> {

    @Autowired
    private SignUpCommand signUpCmd;

    @Autowired
    private UserServiceHelper userServiceHelper;

    @Autowired
    private UserSessionHelper userSessionHelper;

    @Autowired
    private EmailVerificationUtil emailVerificationUtil;

    @Override
    public SignUpResponse execute(AccountKitEmailSignUpRequest param) throws ServiceException {
        Assert.notNull(param, "The incoming request must be not null");
        User user = signUpCmd.execute(param);
        userSetup(param, user);

        return new SignUpResponse(
                user.getUsername(),
                UUID.fromString(userSessionHelper.createSession(user, param)));
    }

    private void userSetup(AccountKitEmailSignUpRequest param, User user) {
        String email = param.getEmail();
        userServiceHelper.setUserEmail(user, email);
        emailVerificationUtil.verify(user);

        if (StringUtils.isEmpty(user.getUsername())) {
            user.setUsername(createUsername(email));
        }
        userServiceHelper.saveUser(user);
    }

    private String createUsername(String email) {
        return userServiceHelper.createUsernameByEmail(email);
    }

}

