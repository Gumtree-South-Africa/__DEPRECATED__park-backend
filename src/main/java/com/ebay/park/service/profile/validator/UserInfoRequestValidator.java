package com.ebay.park.service.profile.validator;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.profile.dto.UserInfoRequest;
import com.ebay.park.util.DataCommonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * Validates a UserInfoRequest {@link UserInfoRequest}
 * 
 * @author cbirge
 * 
 */

@Component
public class UserInfoRequestValidator implements
		ServiceValidator<UserInfoRequest> {

	@Override
	public void validate(UserInfoRequest toValidate) {
		if (toValidate != null) {

			if (!StringUtils.isBlank(toValidate.getPicture())) {
				UrlValidator validator = new UrlValidator();
				if (!validator.isValid(toValidate.getPicture())) {
					throw createServiceException(ServiceExceptionCode.INVALID_URL);
				}
			}

			if (!StringUtils.isBlank(toValidate.getLocation())) {
				if (StringUtils.isBlank(toValidate.getLocationName())) {
					throw createServiceException(ServiceExceptionCode.EMPTY_LOCATION_NAME);
				}
				if (StringUtils.isBlank(toValidate.getZipCode())) {
					throw createServiceException(ServiceExceptionCode.INVALID_ZIP_CODE);
				}
			}

			DataCommonUtil.validateLocation(toValidate.getLocation());

		} else {
			throw createServiceException(ServiceExceptionCode.INVALID_SIGNUP_REQ);
		}
	}
}
