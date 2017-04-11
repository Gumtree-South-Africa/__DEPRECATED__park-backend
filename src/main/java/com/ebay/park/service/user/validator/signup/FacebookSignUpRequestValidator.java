package com.ebay.park.service.user.validator.signup;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Social;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.user.dto.signup.FacebookSignUpRequest;
import com.ebay.park.util.FacebookUtil;

/**
 * Validates a Facebook sign up request {@link FacebookSignUpRequest}
 * @author scalderon
 *
 */
@Component
public class FacebookSignUpRequestValidator implements ServiceValidator<FacebookSignUpRequest> {

	@Autowired
	private SignUpReqValidator signUpRequestValidator;
	
	@Autowired
	private FacebookUtil facebookUtil;
	
	@Autowired
	private UserSocialDao userSocialDao;
	
	/**
	 * Validates facebook data by FacebookUtil {@link FacebookUtil}
	 */
	@Override
	public void validate(FacebookSignUpRequest toValidate) {

		if (toValidate != null) {
		    //username cannot be empty
            if (StringUtils.isBlank(toValidate.getUsername())) {
                throw createServiceException(ServiceExceptionCode.EMPTY_USERNAME);
            }

            //email cannot be empty
            if (StringUtils.isBlank(toValidate.getEmail())) {
                throw createServiceException(ServiceExceptionCode.EMPTY_EMAIL);
            }
            
			//Validates common sign up information (username, location, zipcode) 
			signUpRequestValidator.validate(toValidate);

			
			
			//Validates Facebook data
			String facebookId = toValidate.getFacebookUserId();
			String facebookToken = toValidate.getFacebookToken();
	
			if ((StringUtils.isBlank(facebookId) & !StringUtils.isBlank(facebookToken))
					|| (!StringUtils.isBlank(facebookId) & StringUtils.isBlank(facebookToken))) {
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
				
				if (StringUtils.isBlank(toValidate.getEmail())) {
					throw createServiceException(ServiceExceptionCode.EMPTY_EMAIL);
				}
				
				if(!toValidate.getEmail().equals(facebookUtil.getEmail(facebookToken))){
					throw createServiceException(ServiceExceptionCode.INVALID_FACEBOOK_EMAIL);
				}
			}
		}
	}
	
}
