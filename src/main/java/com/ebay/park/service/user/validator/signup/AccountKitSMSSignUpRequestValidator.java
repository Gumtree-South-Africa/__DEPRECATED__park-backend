package com.ebay.park.service.user.validator.signup;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.dto.signup.AccountKitSMSSignUpRequest;
import com.ebay.park.util.AccountKitUtil;

/**
 * Validates an Account Sign Up request {@link AccountKitSMSSignUpRequest}
 * by SMS with phone number request.
 * @author scalderon
 * @since 2.0.2
 *
 */
@Component
public class AccountKitSMSSignUpRequestValidator implements ServiceValidator<AccountKitSMSSignUpRequest> {
	
	private static final String PHONE_REGEX = "^\\+(?:[0-9]?){6,14}[0-9]$";

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
	public void validate(AccountKitSMSSignUpRequest toValidate) {
		if (toValidate != null) {
		
			//Validates common sign up information too (username, location, zipcode)
			signUpReqValidator.validate(toValidate);
			
			boolean hasPhoneNumber = StringUtils.isNotBlank(toValidate.getMobilePhone());
			boolean hasAccountKitToken = StringUtils.isNotBlank(toValidate.getAccountKitToken());
	
			if (!hasPhoneNumber || !hasAccountKitToken) {
				throw createServiceException(ServiceExceptionCode.BAD_REQ_INFO);
			}
			
			if (!toValidate.getMobilePhone().matches(PHONE_REGEX)) {
				throw createServiceException(ServiceExceptionCode.INVALID_PHONE_NUMBER);
			}
			
			User user = userServiceHelper.findUserByPhoneNumber(toValidate.getMobilePhone());
			
			if (user != null) {
			    throw createServiceException(ServiceExceptionCode.DUPLICATED_MOBILE_PHONE);
			}

			//Validates account kit access token
			accountKitUtil.validateAccessTokenByPhoneNumber(toValidate.getAccountKitToken(), toValidate.getMobilePhone());
		}
	}

}
