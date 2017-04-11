package com.ebay.park.service.group.validator;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.group.dto.SearchGroupRequest;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class SearchGroupRequestValidator implements
		ServiceValidator<SearchGroupRequest> {

	@Override
	public void validate(SearchGroupRequest toValidate) {

		boolean latitudeIsNull = toValidate.getLatitude() == null;
		boolean longitudeIsNull = toValidate.getLongitude() == null;

		if (latitudeIsNull ^ longitudeIsNull) {
			throw createServiceException(ServiceExceptionCode.INVALID_SEARCH_LOCATION);
		}

	}
}
