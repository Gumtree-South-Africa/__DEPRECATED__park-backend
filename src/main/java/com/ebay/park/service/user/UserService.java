/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service.user;

import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.device.dto.DeviceRequest;
import com.ebay.park.service.device.dto.DeviceSwrveIdRequest;
import com.ebay.park.service.user.dto.ChangePwdRequest;
import com.ebay.park.service.user.dto.ChangePwdResponse;
import com.ebay.park.service.user.dto.CheckValueRequest;
import com.ebay.park.service.user.dto.CheckValueResponse;
import com.ebay.park.service.user.dto.EmailRequest;
import com.ebay.park.service.user.dto.SignInRequest;
import com.ebay.park.service.user.dto.SignInResponse;
import com.ebay.park.service.user.dto.SignUpRequest;
import com.ebay.park.service.user.dto.SignUpResponse;
import com.ebay.park.service.user.dto.VerifyEmailRequest;
import com.ebay.park.service.user.dto.signin.AccountKitEmailSignInRequest;
import com.ebay.park.service.user.dto.signin.AccountKitSMSSignInRequest;
import com.ebay.park.service.user.dto.signin.FacebookSignInRequest;
import com.ebay.park.service.user.dto.signup.AccountKitEmailSignUpRequest;
import com.ebay.park.service.user.dto.signup.AccountKitSMSSignUpRequest;
import com.ebay.park.service.user.dto.signup.FacebookSignUpRequest;

/**
 * @author jppizarro
 */
public interface UserService {

	/**
	 * Performs a sign in process given the sign in request.
	 * 
	 * @param request
	 *            Sign in info
	 * @return SignInResponse Response object instance.
	 * @throws ServiceException
	 */
	public SignInResponse signIn(SignInRequest request) throws ServiceException;
	
	/**
	 * Performs a sign in process given the sign in request.
	 * 
	 * @param request
	 *            Sign in info
	 * @return SignInResponse Response object instance.
	 * @throws ServiceException
	 * @since v2.0.6 - Smart Lock
	 */
	public SignInResponse signInV4(SignInRequest request) throws ServiceException;

	/**
	 * Performs a sign in process by Account Kit SMS.
	 * @param request
	 * 			  AccountKitSMSSignInRequest: mobile phone number
	 * 			  and Account Kit access token.
	 * @return SignInResponse Response object instance.
	 * @throws ServiceException
	 */
	public SignInResponse accountKitSMSSignIn(AccountKitSMSSignInRequest request) throws ServiceException;

	/**
	 * Performs a sign in process by Facebook.
	 * @param request
	 * 			  FacebookSignInRequest: user facebook credentials
	 * @return SignInResponse Response object instance.
	 * @throws ServiceException
	 */
	public SignInResponse facebookSignIn(FacebookSignInRequest request) throws ServiceException;
	
	/**
	 * Performs a sign in process by Account Kit with email.
	 * @param request
	 * 			  AccountKitEmailSignInRequest: user email
	 * 			  and Account Kit access token.
	 * @return SignInResponse Response object instance.
	 * @throws ServiceException
	 */
	public SignInResponse accountKitEmailSignIn(AccountKitEmailSignInRequest request) throws ServiceException;
	
	/**
	 * Creates and return a User {@link User} from the given
	 * <code>request</code>
	 * 
	 * @param request
	 *            a single SignUpRequest
	 * @return a single User
	 * @throws ServiceException
	 */
	public SignUpResponse signUp(SignUpRequest request) throws ServiceException;
	
	/**
	 * Performs a sign up process by Account Kit SMS.
	 * @param request
	 * 			  AccountKitSMSSignUpRequest: mobile phone number, 
	 *          Account Kit access token and user info.
	 * @return SignUpResponse Response object instance.
	 * @throws ServiceException
	 */
	public SignUpResponse accountKitSMSSignUp(AccountKitSMSSignUpRequest request) throws ServiceException;

	/**
	 * Performs a sign up process by Facebook. Version 3.
	 * @param request
	 * 			  FacebookSignUpRequest: user facebook credentials and user info.
	 * @return SignUpResponse Response object instance.
	 * @throws ServiceException with code EMPTY_USERNAME when the request does not have username
	 * @throws ServiceException with code EMPTY_EMAIL when the request does not contain an email address
	 * @throws ServiceException with code INVALID_FACEBOOK_INFO when a FB required field is missing
	 * @throws ServiceException with code INVALID_FACEBOOK_ID when the FB id is invalid
	 * @throws ServiceException with code USER_SOCIAL_ALREADY_REGISTERED_FB there is another user with that FB account
	 * @throws ServiceException with code INVALID_FACEBOOK_EMAIL the set email is not the one related to the FB account
	 * @throws ServiceException with code INVALID_LANGUAGE the language is not "es" or "en"
	 * @throws ServiceException with code IO_ERROR when the user session can't be created
	 * @throws ServiceException with code EMPTY_TOKEN when the token is not set
	 * @throws ServiceException with code USER_CANNOT_BE_NULL when the user cannot be found
	 */
	public SignUpResponse facebookSignUpV3(FacebookSignUpRequest request) throws ServiceException;

	/**
	 * Performs a sign up process by Facebook. Version 4.
	 * @param request
	 * 			  FacebookSignUpRequest: user facebook credentials and user info.
	 * @return SignUpResponse Response object instance.
	 * @throws ServiceException with code EMPTY_USERNAME when the request does not have username
	 * @throws ServiceException with code EMPTY_EMAIL when the request does not contain an email address
	 * @throws ServiceException with code INVALID_FACEBOOK_INFO when a FB required field is missing
	 * @throws ServiceException with code INVALID_FACEBOOK_ID when the FB id is invalid
	 * @throws ServiceException with code USER_SOCIAL_ALREADY_REGISTERED_FB there is another user with that FB account
	 * @throws ServiceException with code INVALID_FACEBOOK_EMAIL the set email is not the one related to the FB account
	 * @throws ServiceException with code INVALID_LANGUAGE the language is not "es" or "en"
	 * @throws ServiceException with code IO_ERROR when the user session can't be created
	 * @throws ServiceException with code EMPTY_TOKEN when the token is not set
	 * @throws ServiceException with code USER_CANNOT_BE_NULL when the user cannot be found
	 */
	public SignUpResponse facebookSignUpV4(FacebookSignUpRequest request) throws ServiceException;

	/**
	 * Checks if the given field and value exists in the User table.
	 * 
	 * @param request
	 *            Check request DTO.
	 * @return CheckValueResponse DTO with the check result.
	 * @throws ServiceException
	 */
	public CheckValueResponse checkValue(CheckValueRequest request) throws ServiceException;

	/**
	 * Inits the workflow to create a new password for a user that has forgotten
	 * her current password
	 * 
	 * @param request
	 *            a {@link EmailRequest} containing the email address for
	 *            the account that needs its password reset.
	 * @throws ServiceException
	 */
	public ServiceResponse forgetPwdV3(EmailRequest request) throws ServiceException;

	/**
	 * Inits the workflow to create a new password for a user that has forgotten
	 * her current password for V4
	 * 
	 * @param request
	 *            a {@link EmailRequest} containing the email address for
	 *            the account that needs its password reset.
	 * @throws ServiceException
	 */
	public ServiceResponse forgetPwdV4(EmailRequest request) throws ServiceException;

	
	/**
	 * Logs out the user from park app.
	 * 
	 * @param deviceRequest
	 *           device to log out
	 * @throws ServiceException
	 */
	public ServiceResponse signOut(DeviceRequest deviceRequest) throws ServiceException;

	/**
	 * Changes the current password given a the current password
	 * 
	 * @param request
	 *            a {@link ChangePwdRequest}
	 * @throws ServiceException
	 */
	public ChangePwdResponse changePwd(ChangePwdRequest request) throws ServiceException;

	/**
	 * Verifies the user's email account
	 * 
	 * @param request
	 *            Verify Email request DTO
	 * @throws ServiceException
	 */
	public String verifyEmail(VerifyEmailRequest request) throws ServiceException;
	
	
	/**
	 * Sends a verification email to the user
	 * 
	 * @param parkToken
	 *            a single String representing the park token
	 * @throws ServiceException
	 */
	public ServiceResponse sendVerificationEmail(String parkToken) throws ServiceException;

	 /**
     * Performs a sign up process by Account Kit Email. Version 3.
     * @param request
     *            AccountKitEmailSignUpRequest: email,
     *          Account Kit access token and user info.
     * @return SignUpResponse Response object instance.
     * @throws ServiceException with code INVALID_USERNAME_LONG when the username is longer or shorther that expected
	 * @throws ServiceException with code INVALID_USERNAME_PATTERN when the username does not follow the username rule
	 * @throws ServiceException with code EMPTY_LOCATION_NAME when empty location name
	 * @throws ServiceException with code INVALID_ZIP_CODE when the zip code is invalid
	 * @throws ServiceException with code EMPTY_DEVICE_ID when the device id field is empty
	 * @throws ServiceException with code EMPTY_DEVICE_TYPE when the device type field is empty
	 * @throws ServiceException with code INVALID_DEVICE_TYPE when the device type field is invalid
	 * @throws ServiceException with code INVALID_SIGNUP_REQ when a null request was received
	 * @throws ServiceException with code BAD_REQ_INFO when the request has not email or this is not an Account Kit request
	 * @throws ServiceException with code EMAIL_DUPLICATED when the email is already register for a user
	 * @throws ServiceException with code EMPTY_ACCOUNT_KIT_TOKEN when AK token is
	 * @throws ServiceException with code EMAIL_USER_EMPTY_EMAIL when the email field is empty
	 * @throws ServiceException with code INVALID_ACCOUNT_KIT_TOKEN when the AK token is invalid
	 * @throws ServiceException with code ERROR_ACCOUNT_KIT_COMMUNICATION petition error when connecting to AK
	 * @throws ServiceException with code DUPLICATED_SIGNUP_DATA when there is an existing user with the data used
	  * in the request
	 * @throws ServiceException with code IO_ERROR when an error appears in user session creation
     */
    public SignUpResponse accountKitEmailSignUpV3(AccountKitEmailSignUpRequest request);

	/**
	 * Performs a sign up process by Account Kit Email. Version 4.
	 * @param request
	 *            AccountKitEmailSignUpRequest: email,
	 *          Account Kit access token and user info.
	 * @return SignUpResponse Response object instance.
	 * @throws ServiceException with code INVALID_USERNAME_LONG when the username is longer or shorther that expected
	 * @throws ServiceException with code INVALID_USERNAME_PATTERN when the username does not follow the username rule
	 * @throws ServiceException with code EMPTY_LOCATION_NAME when empty location name
	 * @throws ServiceException with code INVALID_ZIP_CODE when the zip code is invalid
	 * @throws ServiceException with code EMPTY_DEVICE_ID when the device id field is empty
	 * @throws ServiceException with code EMPTY_DEVICE_TYPE when the device type field is empty
	 * @throws ServiceException with code INVALID_DEVICE_TYPE when the device type field is invalid
	 * @throws ServiceException with code INVALID_SIGNUP_REQ when a null request was received
	 * @throws ServiceException with code BAD_REQ_INFO when the request has not email or this is not an Account Kit request
	 * @throws ServiceException with code EMAIL_DUPLICATED when the email is already register for a user
	 * @throws ServiceException with code EMPTY_ACCOUNT_KIT_TOKEN when AK token is
	 * @throws ServiceException with code EMAIL_USER_EMPTY_EMAIL when the email field is empty
	 * @throws ServiceException with code INVALID_ACCOUNT_KIT_TOKEN when the AK token is invalid
	 * @throws ServiceException with code ERROR_ACCOUNT_KIT_COMMUNICATION petition error when connecting to AK
	 * @throws ServiceException with code DUPLICATED_SIGNUP_DATA when there is an existing user with the data used
	 * in the request
	 * @throws ServiceException with code IO_ERROR when an error appears in user session creation
	 */
	public SignUpResponse accountKitEmailSignUpV4(AccountKitEmailSignUpRequest request);

	/**
	 * Sets the swreve id to user session
	 * @param request
	 * @return ServiceResponse
	 */
	public ServiceResponse setSwrveIdToSession(DeviceSwrveIdRequest request);
}
