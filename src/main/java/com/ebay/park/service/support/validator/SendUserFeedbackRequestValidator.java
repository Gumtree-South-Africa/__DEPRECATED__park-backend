package com.ebay.park.service.support.validator;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.support.dto.SendUserFeedbackRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * @author marcos.lambolay
 * 
 */
@Component
public class SendUserFeedbackRequestValidator implements
		ServiceValidator<SendUserFeedbackRequest> {

	@Override
	public void validate(SendUserFeedbackRequest toValidate) {
		if(StringUtils.isBlank(toValidate.getAppVersion())){
			throw createServiceException(ServiceExceptionCode.APP_VERSION_BLANK_ERROR);
		}
		
		if(StringUtils.isBlank(toValidate.getCountryCode())){
			throw createServiceException(ServiceExceptionCode.COUNTRY_CODE_BLANK_ERROR);
		}
		
		if(StringUtils.isBlank(toValidate.getDeviceModel())){
			throw createServiceException(ServiceExceptionCode.DEVICE_MODEL_BLANK_ERROR);
		}
		
		if(StringUtils.isBlank(toValidate.getMessage())){
			throw createServiceException(ServiceExceptionCode.FEEDBACK_MSG_BLANK_ERROR);
		}
		
	}
}
