package com.ebay.park.service.user.validator.signin;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.user.dto.signin.AccountKitSMSSignInRequest;
import com.ebay.park.util.AccountKitUtil;

/**
 * Validates an Account Sign In request {@link AccountKitSMSSignInRequest}
 * by SMS with phone number request.
 * @author scalderon
 * @since 2.0.2
 *
 */
@Component
public class AccountKitSMSSignInRequestValidator implements ServiceValidator<AccountKitSMSSignInRequest> {

	private static final String PHONE_REGEX = "^\\+(?:[0-9]?){6,14}[0-9]$";
	
	@Autowired
	private AccountKitUtil accountKitUtil;
	
	@Autowired
	private SignInReqValidator signInReqValidator;

	@Override
	public void validate(AccountKitSMSSignInRequest toValidate) {
		if (toValidate != null) {
			boolean hasPhoneNumber = StringUtils.isNotBlank(toValidate.getMobilePhone());
			boolean hasAccountKitToken = StringUtils.isNotBlank(toValidate.getAccountKitToken());
			
			if (!hasPhoneNumber || !hasAccountKitToken) {
				throw createServiceException(ServiceExceptionCode.BAD_REQ_INFO);
			}
			
			if (!toValidate.getMobilePhone().matches(PHONE_REGEX)) {
				throw createServiceException(ServiceExceptionCode.INVALID_PHONE_NUMBER);
			}
			
			signInReqValidator.validate(toValidate);
			
			//Validates Account Kit access token
			accountKitUtil.validateAccessTokenByPhoneNumber(toValidate.getAccountKitToken(), toValidate.getMobilePhone());
		}
	}

}
