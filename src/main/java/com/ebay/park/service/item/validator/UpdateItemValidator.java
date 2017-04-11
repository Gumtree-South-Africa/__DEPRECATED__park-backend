package com.ebay.park.service.item.validator;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.UpdateItemRequest;
/**
 * 
 * @author marcos.lambolay
 */
@Component
public class UpdateItemValidator implements ServiceValidator<UpdateItemRequest> {

	@Autowired
	private ItemUtils itemUtils;

	@Override
	public void validate(UpdateItemRequest toValidate) {
		try {
			validateIfPresent(toValidate.getLocation(), "field.location");
			validateIfPresent(toValidate.getLocationName(), "field.locationName");
			validateIfPresent(toValidate.getName(), "field.name");
			validateIfPresent(toValidate.getPrice(), "field.price");
			validateIfPresent(toValidate.getLatitude(), "field.latitude");
			validateIfPresent(toValidate.getLongitude(), "field.longitude");
		} catch (IllegalArgumentException e) {
			ServiceException se = createServiceException(ServiceExceptionCode.EMPTY_DATA_UPDATE, new String[] { e.getMessage() });
			throw se;
		}
		
		if (StringUtils.isNotBlank(toValidate.getName()) && !itemUtils.isValidTextTitle(toValidate.getName())) {
			throw createServiceException(ServiceExceptionCode.INVALID_ITEM_NAME_CHARACTER);
		}

		if (StringUtils.isNotBlank(toValidate.getDescription())) {
			if (!itemUtils.isValidTextDescription(toValidate.getDescription())) {
				throw createServiceException(ServiceExceptionCode.INVALID_ITEM_DESCRIPTION_CHARACTER);
			}
		}
	}

	protected void validateIfPresent(String value, String errorMsg) {
		if (value != null) {
			Validate.notEmpty(value, errorMsg);
		}
	}
}
