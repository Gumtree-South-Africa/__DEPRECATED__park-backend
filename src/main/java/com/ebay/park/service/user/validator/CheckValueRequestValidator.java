/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.user.validator;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.user.dto.CheckValueRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;


/**
 * @author juan.pizarro
 * 
 */
@Component
public class CheckValueRequestValidator implements ServiceValidator<CheckValueRequest> {

	private static final String EMAIL_OR_USERNAME = "email|username";

	@Override
	public void validate(CheckValueRequest toValidate) {
		boolean success = StringUtils.isNotBlank(toValidate.getName());
		success = success && StringUtils.isNotBlank(toValidate.getValue());
		success = success && toValidate.getName().toLowerCase().matches(EMAIL_OR_USERNAME);

		if (!success) {
			throw createServiceException(ServiceExceptionCode.BAD_REQ_INFO);
		}
	}

}
