package com.ebay.park.service.rating.validator;

import com.ebay.park.db.entity.RatingStatus;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.rating.dto.RateUserRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;
@Component
public class RateUserRequestValidator implements ServiceValidator<RateUserRequest>{

	@Override
	public void validate(RateUserRequest toValidate) {
		
		if(StringUtils.isBlank(toValidate.getComment())) {
			throw createServiceException(ServiceExceptionCode.RATING_COMMENT_BLANK_ERROR);
		}
		if ( RatingStatus.fromDescription(toValidate.getRatingStatus()) == null){
			throw createServiceException(ServiceExceptionCode.INVALID_USER_RATES_STATUS);
		}
		
	}
}
