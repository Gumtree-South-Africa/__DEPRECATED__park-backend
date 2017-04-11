package com.ebay.park.service.user.validator;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.user.dto.EmailRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * Forget password request validator.
 * 
 * @author marcos.lambolay
 */
@Component
public class ForgetPwdRequestValidator implements ServiceValidator<EmailRequest> {
	/**
	 * @throws ServiceException
	 *             EMPTY_EMAIL if email is blank.
	 */
	@Override
	public void validate(EmailRequest toValidate) {
		if (StringUtils.isBlank(toValidate.getEmail())) {
			throw createServiceException(ServiceExceptionCode.EMPTY_EMAIL);
		}
	}

}
