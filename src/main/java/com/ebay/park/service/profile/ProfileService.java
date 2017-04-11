package com.ebay.park.service.profile;

import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.profile.dto.*;

/**
 * Defines the services to interact with a user's profile.
 * 
 * @author lucia.masola
 * 
 */
public interface ProfileService {

	/**
	 * Update basic profile information
	 * 
	 * @param request
	 *            data to update
	 * @return user data updated
	 */
	public UserInfoResponse updateUserInfo(UserInfoRequest request);

	/**
	 * Gets the user's profile for the given <code>username</code>
	 * 
	 * @param request
	 *            pair of username and parkToken
	 * @return a single UserProfile object.
	 */
	public UserProfile getUserProfile(GetUserProfileRequest request);

	/**
	 * Adds a profile picture to given username
	 * 
	 * @param request
	 *            pair of username and profile picture url.
	 * @return Instance of service response with success = true or false.
	 */
	public ServiceResponse addProfilePicture(ProfilePictureRequest request);
	
}
