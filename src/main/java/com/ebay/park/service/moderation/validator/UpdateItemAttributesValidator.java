package com.ebay.park.service.moderation.validator;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.moderationMode.dto.UpdateItemForModerationModeRequest;

@Component
public class UpdateItemAttributesValidator implements ServiceValidator<UpdateItemForModerationModeRequest> {

	@Autowired
	private ItemUtils itemUtils;

	@Value("${itemName.minLong}")
	private int itemNameMinLong;

	@Value("${itemName.maxLong}")
	private int itemNameMaxLong;

	@Value("${itemDescription.maxLong}")
	private int itemDescriptionMaxLong;

	/**
	 * This method validates the information edited by the moderator before
	 * perform the update.
	 * 
	 * @param request the incoming request
	 * @return
	 */
	@Override
    public void validate(UpdateItemForModerationModeRequest request) {
		try {
			validateIfPresent(request.getName(), "field.name");

			if (request.getCategory() != null) {
				Validate.isTrue(request.getCategory() > 0, "field.category should not be empty");
			}
		} catch (IllegalArgumentException e) {
			ServiceException se = createServiceException(ServiceExceptionCode.EMPTY_DATA_UPDATE,
					new String[] { e.getMessage() });
			throw se;
		}
		if (request.getName() != null) {
			if (!(request.getName().length() >= itemNameMinLong && request.getName().length() <= itemNameMaxLong)) {
				throw createServiceException(ServiceExceptionCode.INVALID_ITEM_NAME_LONG);
			}
		}
		if (StringUtils.isNotBlank(request.getDescription())) {
			if (!itemUtils.isValidTextDescription(request.getDescription())) {
				throw createServiceException(ServiceExceptionCode.INVALID_ITEM_DESCRIPTION_CHARACTER);
			}
			if (request.getDescription().length() > itemDescriptionMaxLong) {
				throw createServiceException(ServiceExceptionCode.INVALID_ITEM_DESCRIPTION_LONG);
			}
		}

		if (StringUtils.isNotBlank(request.getName()) && !itemUtils.isValidTextTitle(request.getName())) {
			throw createServiceException(ServiceExceptionCode.INVALID_ITEM_NAME_CHARACTER);
		}
	}

	protected void validateIfPresent(String value, String errorMsg) {
		if (value != null) {
			Validate.notEmpty(value, errorMsg);
		}
	}
}
