package com.ebay.park.service.social.validator;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.social.dto.ShareProfileRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;
@Component
public class ShareProfileRequestValidator implements ServiceValidator<ShareProfileRequest>{

	@Override
	public void validate(ShareProfileRequest toValidate) {
		
		if (toValidate == null){
			throw createServiceException(ServiceExceptionCode.INVALID_USER_RATES_REQ);
		}
		
		if (StringUtils.isBlank(toValidate.getSharerUsername())) {
			throw createServiceException(ServiceExceptionCode.EMPTY_USERNAME);
		}
		
		if (StringUtils.isBlank(toValidate.getUsernameToShare())) {
			throw createServiceException(ServiceExceptionCode.INVALID_USER_RATES_STATUS);
		}
		
	}  
	
}
