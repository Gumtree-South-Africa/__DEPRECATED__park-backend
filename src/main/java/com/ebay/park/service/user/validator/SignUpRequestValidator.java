package com.ebay.park.service.user.validator;

import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.device.dto.DeviceRequestValidator;
import com.ebay.park.service.user.dto.SignUpRequest;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.FacebookUtil;
import com.ebay.park.util.LocationUtil;
import com.ebay.park.util.ParkConstants;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * Validates a SingUpRequest {@link SignUpRequest}
 * 
 * @author lucia.masola
 * 
 */

@Component
public class SignUpRequestValidator implements ServiceValidator<SignUpRequest>,
		ParkConstants {

	@Value("${username.min.length}")
	private int usernameMinLength;

	@Value("${username.max.length}")
    private int usernameMaxLength;

	@Autowired
	private FacebookUtil facebookUtil;

	@Autowired
	private DeviceRequestValidator deviceReqValidator;
	
	@Autowired
	private UserSocialDao userSocialDao;
	
	@Override
	public void validate(SignUpRequest toValidate) {

		if (toValidate != null) {

			if (StringUtils.isBlank(toValidate.getUsername())) {
				throw createServiceException(ServiceExceptionCode.EMPTY_USERNAME);
			}

			if (!(toValidate.getUsername().length() >= usernameMinLength
					&& toValidate.getUsername().length() <= usernameMaxLength)) {
				throw createServiceException(ServiceExceptionCode.INVALID_USERNAME_LONG);
			}

			if (!toValidate.getUsername().matches("^[A-Za-z0-9]+$")) {
				throw createServiceException(ServiceExceptionCode.INVALID_USERNAME_PATTERN);
			}

			if (StringUtils.isBlank(toValidate.getEmail())) {
				throw createServiceException(ServiceExceptionCode.EMPTY_EMAIL);
			}

			validatePassword(toValidate);

			if (StringUtils.isBlank(toValidate.getLocationName())) {
				throw createServiceException(ServiceExceptionCode.EMPTY_LOCATION_NAME);
			}

			if (StringUtils.isBlank(toValidate.getZipCode())) {
				throw createServiceException(ServiceExceptionCode.INVALID_ZIP_CODE);
			}

			deviceReqValidator.validate(toValidate.getDevice());

			LocationUtil.validateLocation(toValidate.getLocation());

			validateFacebookData(toValidate);

		} else {
			throw createServiceException(ServiceExceptionCode.INVALID_SIGNUP_REQ);
		}

	}

	public void validatePassword(SignUpRequest toValidate) {
		if (StringUtils.isBlank(toValidate.getPassword())) {
			throw createServiceException(ServiceExceptionCode.EMPTY_PASSWORD);
		}

		DataCommonUtil.validatePassword(toValidate.getPassword());
	}

	/**
	 * Validates whether the facebook data is complete or not. If one of the
	 * facebook fields are present, both must be. If both fields are present, it
	 * asks facebook if the information is valid.
	 * 
	 * @param toValidate
	 *            a single SignUpRequest
	 */
	private void validateFacebookData(SignUpRequest toValidate) {

		String facebookId = toValidate.getFacebookUserId();
		String facebookToken = toValidate.getFacebookToken();

		if ((StringUtils.isBlank(facebookId) & !StringUtils
				.isBlank(facebookToken))
				|| (!StringUtils.isBlank(facebookId) & StringUtils
						.isBlank(facebookToken))) {

			throw createServiceException(ServiceExceptionCode.INVALID_FACEBOOK_INFO);

		}

		if (StringUtils.isNotBlank(facebookToken)) {

			facebookUtil.tokenIsValid(facebookToken, facebookId);
			String expectedId = facebookUtil
					.getUserIdAssociatedWithToken(facebookToken);
			if (!facebookId.equals(expectedId)) {
				throw createServiceException(ServiceExceptionCode.INVALID_FACEBOOK_ID);
			}
			
			if(!userSocialDao.findBySocialUserIdAndNetwork(facebookId, Social.FACEBOOK).isEmpty()){
				throw createServiceException(ServiceExceptionCode.USER_SOCIAL_ALREADY_REGISTERED_FB);
			}
			
			if(!toValidate.getEmail().equals(facebookUtil.getEmail(facebookToken))){
				throw createServiceException(ServiceExceptionCode.INVALID_FACEBOOK_EMAIL);
			}
		}

	}

}