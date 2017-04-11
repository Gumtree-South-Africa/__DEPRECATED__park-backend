package com.ebay.park.service.social.validator;

import com.ebay.park.db.entity.RatingStatus;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.social.dto.UserRatesRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class UserRatesRequestValidator implements ServiceValidator<UserRatesRequest>{

	@Override
	public void validate(UserRatesRequest toValidate) {
		
		if (toValidate == null){
			throw createServiceException(ServiceExceptionCode.INVALID_USER_RATES_REQ);
		}
		
		if (StringUtils.isBlank(toValidate.getUsername())) {
			throw createServiceException(ServiceExceptionCode.EMPTY_USERNAME);
		}
		
		List<String> statuses = toValidate.getRateStatus();
		if (statuses != null && statuses.size() >= 0){
			for (String status : statuses){
				if (RatingStatus.fromDescription(status) == null) {
					throw createServiceException(ServiceExceptionCode.INVALID_USER_RATES_STATUS);
				}
			}
			
		}
		
	} 
	
}
