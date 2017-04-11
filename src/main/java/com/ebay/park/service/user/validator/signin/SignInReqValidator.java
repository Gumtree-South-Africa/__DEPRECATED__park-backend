package com.ebay.park.service.user.validator.signin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.device.dto.DeviceRequestValidator;
import com.ebay.park.service.user.dto.signin.SignInRequest;

/**
 * Validates a sign in request {@link SignInRequest}
 * @author scalderon
 * @since 2.0.2
 *
 */
@Component
public class SignInReqValidator implements ServiceValidator<SignInRequest> {
	
	@Autowired
	private DeviceRequestValidator deviceReqValidator;
	
	@Override
	public void validate(SignInRequest toValidate) {
		if (toValidate != null) {
			deviceReqValidator.validate(toValidate.getDevice());
		}
	}

}
