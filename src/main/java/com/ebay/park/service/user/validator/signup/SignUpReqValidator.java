package com.ebay.park.service.user.validator.signup;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.device.dto.DeviceRequestValidator;
import com.ebay.park.service.user.dto.signup.SignUpRequest;
import com.ebay.park.util.LocationUtil;

/**
 * Validates a SignUpRequest {@link SignUpRequest}
 * @author scalderon
 * @since 2.0.2
 *
 */
@Component
public class SignUpReqValidator implements ServiceValidator<SignUpRequest> {

	/**
	 * Default username minimum length
	 */
	@Value("${username.min.length}")
	private int defaultUsernameMinLength;
	
	/**
	 * Default username maximum length
	 */
	@Value("${username.max.length}")
	private int defaultUsernameMaxLength;

	@Autowired
	private DeviceRequestValidator deviceReqValidator;
	
	@Override
	public void validate(SignUpRequest toValidate) {
		if (toValidate != null) {

			//Validates username
			boolean hasUsername = StringUtils.isNotEmpty(toValidate.getUsername());
			if (hasUsername && !(toValidate.getUsername().length() >= defaultUsernameMinLength
					&& toValidate.getUsername().length() <= defaultUsernameMaxLength)) {
				throw createServiceException(ServiceExceptionCode.INVALID_USERNAME_LONG);
			}

			if (hasUsername && !toValidate.getUsername().matches("^[A-Za-z0-9]+$")) {
				throw createServiceException(ServiceExceptionCode.INVALID_USERNAME_PATTERN);
			}

			if (StringUtils.isBlank(toValidate.getLocationName())) {
				throw createServiceException(ServiceExceptionCode.EMPTY_LOCATION_NAME);
			}

			if (StringUtils.isBlank(toValidate.getZipCode())) {
				throw createServiceException(ServiceExceptionCode.INVALID_ZIP_CODE);
			}

			deviceReqValidator.validate(toValidate.getDevice());

			LocationUtil.validateLocation(toValidate.getLocation());

		} else {
			throw createServiceException(ServiceExceptionCode.INVALID_SIGNUP_REQ);
		}

	}
}

