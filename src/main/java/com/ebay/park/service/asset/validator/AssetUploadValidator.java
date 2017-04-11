/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.asset.validator;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.asset.dto.AssetUploadRequest;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * @author jpizarro
 * 
 */
@Component
public class AssetUploadValidator implements ServiceValidator<AssetUploadRequest> {

	@Override
	public void validate(AssetUploadRequest toValidate) {
		boolean error = Strings.isNullOrEmpty(toValidate.getName());
		error = error && toValidate.getFile() != null && toValidate.getFile().isEmpty();
		if (error) {
			throw createServiceException(ServiceExceptionCode.INVALID_UPLOAD_DATA);
		}
	}

}
