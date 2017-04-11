package com.ebay.park.service.user.command.signup;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.user.dto.EmailRequest;
import com.ebay.park.service.user.dto.SignUpResponse;
import com.ebay.park.service.user.dto.signup.FacebookSignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Command to perform a Facebook sign up. Version 4.
 * @author Julieta Salvadó
 */
@Component
public class FacebookSignUpCmdV4 implements ServiceCommand<FacebookSignUpRequest, SignUpResponse> {

    @Autowired
    private FacebookSignUpCmdV3 cmdV3;

    @Autowired
    private ServiceCommand<EmailRequest, Void> welcomeCmd;

    @Override
    public SignUpResponse execute(FacebookSignUpRequest request) throws ServiceException {
        Assert.notNull(request, "The incoming request must be not null");
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
