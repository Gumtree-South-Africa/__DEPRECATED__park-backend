package com.ebay.park.service.group.validator;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.group.dto.CreateGroupRequest;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.util.ParkConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

@Component
public class CreateGroupValidator implements
		ServiceValidator<CreateGroupRequest>, ParkConstants {
	
	@Autowired
	GroupDao groupDao;
	
	@Autowired
	private ItemUtils itemUtils;
	
	@Override
	public void validate(CreateGroupRequest toValidate) {
		try {
			Validate.notEmpty(toValidate.getName(), "toValidate.name should not be empty");
			Validate.notEmpty(toValidate.getToken(), "toValidate.token should not be empty");
			Validate.notEmpty(toValidate.getLocation(), "toValidate.location should not be empty");
		} catch (IllegalArgumentException e) {
			throw createServiceException(ServiceExceptionCode.EMPTY_DATA_CREATEGROUP);
		}
		
		if (!itemUtils.isValidTextTitle(toValidate.getName())) {
			throw createServiceException(ServiceExceptionCode.INVALID_GROUP_NAME_CHARACTER);
		}
		
		if (!StringUtils.isBlank(toValidate.getDescription())
				&& (!itemUtils.isValidTextDescription(toValidate.getDescription()))){
			throw createServiceException(ServiceExceptionCode.INVALID_GROUP_DESCRIPTION_CHARACTER);
		}
				
		if ((!toValidate.getLocation().matches(LOCATION_FORMAT))) {
			throw createServiceException(ServiceExceptionCode.INVALID_LOCATION);
		}

		if (StringUtils.isBlank(toValidate.getLocationName())) {
			throw createServiceException(ServiceExceptionCode.EMPTY_LOCATION_NAME);
		}

		if (StringUtils.isBlank(toValidate.getZipCode())) {
			throw createServiceException(ServiceExceptionCode.INVALID_ZIP_CODE);
		}
		
		if( groupDao.findByName(toValidate.getName()) != null)  {
			throw createServiceException(ServiceExceptionCode.GROUP_ALREADY_EXISTS);
		}
	}

}
