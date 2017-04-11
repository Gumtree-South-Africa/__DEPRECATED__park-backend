package com.ebay.park.service.user.validator.signup;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.dto.signup.AccountKitEmailSignUpRequest;
import com.ebay.park.util.AccountKitUtil;

/**
 * Validates an Account Sign Up request {@link AccountKitEmailSignUpRequest}
 * by email address request.
 * @author Julieta Salvadó
 * @since 2.0.2
 *
 */
@Component
public class AccountKitEmailSignUpRequestValidator implements ServiceValidator<AccountKitEmailSignUpRequest>{

    @Autowired
    private AccountKitUtil accountKitUtil;

    @Autowired
    private SignUpReqValidator signUpReqValidator;

    @Autowired
    private UserServiceHelper userServiceHelper;

    /**
     * Validates the common sign up request information and
     * account kit access token
     */
    @Override
    public void validate(AccountKitEmailSignUpRequest toValidate) {
        if (toValidate != null) {

            //Validates common sign up information too (username, location, zipcode)
            signUpReqValidator.validate(toValidate);

            boolean hasEmail = StringUtils.isNotBlank(toValidate.getEmail());
            boolean hasAccountKitToken = StringUtils.isNotBlank(toValidate.getAccountKitToken());

            if (!hasEmail || !hasAccountKitToken) {
                throw createServiceException(ServiceExceptionCode.BAD_REQ_INFO);
            }

            User user = userServiceHelper.findUserByEmail(toValidate.getEmail());
            if (user != null) {
                throw createServiceException(ServiceExceptionCode.EMAIL_DUPLICATED);
            }

            //Validates account kit access token
            accountKitUtil.validateAccessTokenByEmail(toValidate.getAccountKitToken(), toValidate.getEmail());
        }
    }

}
