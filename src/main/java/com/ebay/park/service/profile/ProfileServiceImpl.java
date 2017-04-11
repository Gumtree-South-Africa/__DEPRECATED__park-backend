package com.ebay.park.service.profile;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.profile.command.GetUserProfileCmd;
import com.ebay.park.service.profile.command.UpdateUserInfoCmd;
import com.ebay.park.service.profile.command.UpdateUserProfileCmd;
import com.ebay.park.service.profile.dto.*;
import com.ebay.park.service.profile.validator.UserInfoRequestValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ebay.park.service.ServiceException.createServiceException;

@Service
public class ProfileServiceImpl implements ProfileService {
	
	@Autowired
	private UpdateUserInfoCmd updateUserInfoCmd;
	
	@Autowired
	private UpdateUserProfileCmd updateUserProfileCmd;
	
	@Autowired
	private UserInfoRequestValidator userInfoRequestValidator;
	
	@Autowired
	private GetUserProfileCmd getUserProfileCmd;
	
	@Override
	public UserInfoResponse updateUserInfo(UserInfoRequest request) {
		userInfoRequestValidator.validate(request);
		return updateUserInfoCmd.execute(request);
	}

	@Override
	public UserProfile getUserProfile(GetUserProfileRequest request) {
		if (StringUtils.isBlank(request.getUsername())) {
			throw createServiceException(ServiceExceptionCode.EMPTY_USERNAME);
		}
		return getUserProfileCmd.execute(request);
	}

	@Override
	public ServiceResponse addProfilePicture(ProfilePictureRequest profile) {
		return updateUserProfileCmd.execute(profile);
	}

}
