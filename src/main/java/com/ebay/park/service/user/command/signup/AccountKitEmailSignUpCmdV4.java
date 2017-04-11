package com.ebay.park.service.user.command.signup;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.user.dto.EmailRequest;
import com.ebay.park.service.user.dto.SignUpResponse;
import com.ebay.park.service.user.dto.signup.AccountKitEmailSignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Command to perform an Account Kit with email. Version 4.
 * Sign up operation.
 * @author Julieta Salvadó
 */
@Component
public class AccountKitEmailSignUpCmdV4 implements ServiceCommand<AccountKitEmailSignUpRequest, SignUpResponse> {
    @Autowired
    private AccountKitEmailSignUpCmdV3 cmdV3;

    @Autowired
    private ServiceCommand<EmailRequest, Void> welcomeCmd;

    @Override
    public SignUpResponse execute(AccountKitEmailSignUpRequest request) throws ServiceException {
        Assert.notNull(request);
        SignUpResponse response = cmdV3.execute(request);

        String email = request.getEmail();
        if (!StringUtils.isEmpty(email)) {
            sendEmail(email);
        }
        return response;
    }

    private void sendEmail(String email) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail(email);
        welcomeCmd.execute(emailRequest);
    }
}
