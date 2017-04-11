/**
 * 
 */
package com.ebay.park.service.user.validator;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.device.dto.DeviceRequestValidator;
import com.ebay.park.service.user.dto.SignInRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * @author jppizarro
 * 
 */
@Component
public class SignInRequestValidator implements ServiceValidator<SignInRequest> {

	@Autowired
	private DeviceRequestValidator deviceReqValidator;
	
	@Override
	public void validate(SignInRequest toValidate) {
		boolean hasEmail = StringUtils.isNotBlank(toValidate.getEmail());
		boolean hasUsername = StringUtils.isNotBlank(toValidate.getUsername());
		boolean hasPassword = StringUtils.isNotBlank(toValidate.getPassword());
		boolean hasFbToken = StringUtils.isNotBlank(toValidate.getFbToken());

		boolean isEmailOrUsernameAuthentication = (hasEmail || hasUsername) && hasPassword;
		boolean isValidAuthentication = isEmailOrUsernameAuthentication || hasFbToken;
		if (!isValidAuthentication) {
			throw createServiceException(ServiceExceptionCode.BAD_REQ_INFO);
		}
		
		deviceReqValidator.validate(toValidate.getDevice());
	}

}
