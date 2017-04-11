package com.ebay.park.service.user.validator.signin;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.user.dto.signin.FacebookSignInRequest;
import com.ebay.park.util.FacebookUtil;

/**
 * Validates a Facebook Sign In request {@link FacebookSignInRequest}
 * @author scalderon
 * @since 2.0.2
 *
 */
@Component
public class FacebookSignInRequestValidator implements ServiceValidator<FacebookSignInRequest> {

	@Autowired
	private FacebookUtil facebookUtil;

	@Autowired
	private SignInReqValidator signInReqValidator;

	@Override
	public void validate(FacebookSignInRequest toValidate) {
		if (toValidate != null) {
			signInReqValidator.validate(toValidate);
			
			if (StringUtils.isBlank(toValidate.getFbToken())) {
				throw createServiceException(ServiceExceptionCode.BAD_REQ_INFO);
			}
			
			//Validates facebook token
			facebookUtil.tokenIsValid(toValidate.getFbToken(), toValidate.getFbUserId());
		}
	}
}
