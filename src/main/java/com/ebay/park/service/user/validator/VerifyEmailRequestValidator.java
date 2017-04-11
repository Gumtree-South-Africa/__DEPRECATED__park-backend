package com.ebay.park.service.user.validator;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Component;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.user.dto.VerifyEmailRequest;

/**
 * @author federico.jaite
 * 
 */
@Component
public class VerifyEmailRequestValidator implements
		ServiceValidator<VerifyEmailRequest> {

	@Override
	public void validate(VerifyEmailRequest toValidate) {
		try {
			Validate.notEmpty(toValidate.getEmail(),
					"toValidate.email should not be empty");
			Validate.notEmpty(toValidate.getTemporaryToken(),
					"toValidate.token should not be empty");
		} catch (IllegalArgumentException e) {
			throw createServiceException(ServiceExceptionCode.EMPTY_DATA_VERIFY_EMAIL);
		}

	}

}
