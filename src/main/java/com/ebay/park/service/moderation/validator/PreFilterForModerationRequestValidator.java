package com.ebay.park.service.moderation.validator;

import static com.ebay.park.service.ServiceException.createServiceException;

import com.ebay.park.db.entity.SessionStatusDescription;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.moderation.dto.FilterForModerationRequest;
import com.ebay.park.util.ParkConstants;

public class PreFilterForModerationRequestValidator implements ServiceValidator<FilterForModerationRequest>,
ParkConstants {

	@Override
	public void validate(FilterForModerationRequest toValidate) {
		if (toValidate.getSessionStatus() != null && toValidate.getSessionStatus().equals(SessionStatusDescription.NO_SESSION) ) {
			if (toValidate.getHasActiveItems() != null || toValidate.getHasFacebook() != null || toValidate.getHasTwitter() != null ||
					toValidate.getIsGroupFollower() != null || toValidate.getIsGroupOwner() != null || toValidate.getIsVerified() != null ||
					toValidate.getAccountCreationFrom() != null || toValidate.getAccountCreationTo()  != null || 
					toValidate.getCategoryActiveItems() != null || toValidate.getZipCode() != null) {
				throw createServiceException(ServiceExceptionCode.INVALID_FILTER_REQUEST);
			}

		}
		
	}

}
