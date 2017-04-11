package com.ebay.park.service.group.validator;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.group.dto.UpdateGroupRequest;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.util.ParkConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class UpdateGroupValidator implements
		ServiceValidator<UpdateGroupRequest>, ParkConstants {
	
	@Autowired
	private ItemUtils itemUtils;

	@Override
	public void validate(UpdateGroupRequest toValidate) {

		if (StringUtils.isBlank(toValidate.getName())
				&& StringUtils.isBlank(toValidate.getDescription())
				&& StringUtils.isBlank(toValidate.getLocation())) {
			throw createServiceException(ServiceExceptionCode.EMPTY_DATA_UPDATE_GROUP);
		}

		if (StringUtils.isNotBlank(toValidate.getLocation())) {

			if ((!toValidate.getLocation().matches(LOCATION_FORMAT))) {
				throw createServiceException(ServiceExceptionCode.INVALID_LOCATION);
			}

			if (StringUtils.isBlank(toValidate.getLocationName())) {
				throw createServiceException(ServiceExceptionCode.EMPTY_LOCATION_NAME);
			}

			if (StringUtils.isBlank(toValidate.getZipCode())) {
				throw createServiceException(ServiceExceptionCode.INVALID_ZIP_CODE);
			}

		}
		
		if (StringUtils.isNotBlank(toValidate.getName())
				&& (!itemUtils.isValidTextTitle(toValidate.getName()))){
			throw createServiceException(ServiceExceptionCode.INVALID_GROUP_NAME_CHARACTER);
		}
		
		if (StringUtils.isNotBlank(toValidate.getDescription())
				&& (!itemUtils.isValidTextDescription(toValidate.getDescription()))){
			throw createServiceException(ServiceExceptionCode.INVALID_GROUP_DESCRIPTION_CHARACTER);
		}

	}
	
}
