package com.ebay.park.service.item.validator;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.CreateItemRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;
/**
 * 
 * @author marcos.lambolay
 */
@Component
public class CreateItemValidator implements ServiceValidator<CreateItemRequest>{

	@Autowired
	private ItemUtils itemUtils;
	
	@Override
	public void validate(CreateItemRequest toValidate) {
		if (toValidate.getLatitude() != null ^ toValidate.getLongitude() != null) {
			throw createServiceException(ServiceExceptionCode.INVALID_LATITUDE_LONGITUDE);
		}
		
		try {
			Validate.notEmpty(toValidate.getBrandPublish(),"toValidate.brandPublish should not be empty");
			Validate.notEmpty(toValidate.getName(), "toValidate.name should not be empty");
			Validate.notEmpty(toValidate.getPrice(), "toValidate.price should not be empty");
			Validate.notEmpty(toValidate.getVersionPublish(), "toValidate.versionPublish should not be empty");	
			Validate.notNull(toValidate.getCategoryId(), "toValidate.categoryId should not be empty");
			Validate.isTrue(toValidate.getCategoryId() > 0 , "toValidate.categoryId should not be empty");
			//TODO: Location will be removed in next releases because it was used only for foursquare	
			if(StringUtils.isNotBlank(toValidate.getLocation())){
				Validate.notEmpty(toValidate.getLocationName(), "toValidate.LocationName should not be empty");
				Validate.notNull(toValidate.getLatitude(), "toValidate.latitude should not be empty");
				Validate.notNull(toValidate.getLongitude(), "toValidate.longitude should not be empty");
			}
		} catch(IllegalArgumentException e) {
			throw createServiceException(ServiceExceptionCode.EMPTY_DATA_CREATEITEM);
		}
		
		if (!itemUtils.isValidTextTitle(toValidate.getName())) {
			throw createServiceException(ServiceExceptionCode.INVALID_ITEM_NAME_CHARACTER);
		}

		if (StringUtils.isNotBlank(toValidate.getDescription())) {
			if (!itemUtils.isValidTextDescription(toValidate.getDescription())) {
				throw createServiceException(ServiceExceptionCode.INVALID_ITEM_DESCRIPTION_CHARACTER);
			}
			if (toValidate.getDescription().length() > 255){
				throw createServiceException(ServiceExceptionCode.INVALID_ITEM_DESCRIPTION_LONG);
			}
		}
		
	}

}
