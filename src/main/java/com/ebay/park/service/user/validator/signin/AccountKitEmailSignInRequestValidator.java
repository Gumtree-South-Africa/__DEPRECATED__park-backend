package com.ebay.park.service.user.validator.signin;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.user.dto.signin.AccountKitEmailSignInRequest;
import com.ebay.park.util.AccountKitUtil;

/**
 * Validates an Account Sign Up request {@link AccountKitEmailSignInRequest}
 * by email request.
 * @author Julieta Salvadó
 * @since 2.0.2
 *
 */
@Component
public class AccountKitEmailSignInRequestValidator implements ServiceValidator<AccountKitEmailSignInRequest> {

    @Autowired
    private AccountKitUtil accountKitUtil;

    @Autowired
    private SignInReqValidator signInReqValidator;

    @Override
    public void validate(AccountKitEmailSignInRequest toValidate) {
        boolean hasEmail = StringUtils.isNotBlank(toValidate.getEmail());
        boolean hasAccountKitToken = StringUtils.isNotBlank(toValidate.getAccountKitToken());

        if (!hasEmail || !hasAccountKitToken) {
            throw createServiceException(ServiceExceptionCode.BAD_REQ_INFO);
        }

        signInReqValidator.validate(toValidate);

        //Validates Account Kit access token
        accountKitUtil.validateAccessTokenByEmail(toValidate.getAccountKitToken(), toValidate.getEmail());
    }

}
