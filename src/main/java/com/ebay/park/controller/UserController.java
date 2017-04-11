/*
 * Copyright eBay, 2014
 */
package com.ebay.park.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.service.device.dto.DeviceRequest;
import com.ebay.park.service.device.dto.DeviceSwrveIdRequest;
import com.ebay.park.service.user.UserService;
import com.ebay.park.service.user.dto.ChangePwdRequest;
import com.ebay.park.service.user.dto.CheckValueRequest;
import com.ebay.park.service.user.dto.CheckValueResponse;
import com.ebay.park.service.user.dto.EmailRequest;
import com.ebay.park.service.user.dto.SignInRequest;
import com.ebay.park.service.user.dto.SignUpRequest;
import com.ebay.park.service.user.dto.signin.AccountKitEmailSignInRequest;
import com.ebay.park.service.user.dto.signin.AccountKitSMSSignInRequest;
import com.ebay.park.service.user.dto.signin.FacebookSignInRequest;
import com.ebay.park.service.user.dto.signup.AccountKitEmailSignUpRequest;
import com.ebay.park.service.user.dto.signup.AccountKitSMSSignUpRequest;
import com.ebay.park.service.user.dto.signup.FacebookSignUpRequest;
import com.ebay.park.util.ParkConstants;

/**
 * @author juan.pizarro
 */
@Controller
@RequestMapping(value = "/users/")
public class UserController implements ParkConstants {
	
	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Value("${signin.maxAttempts}")
	private Integer signInMaxAttempts;

	/**
	 * The legacy signIn. 
	 * @param request
	 * @return ServiceResponse
	 */
	@RequestMapping(value = {"v3/signin", "v3.0/signin"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ServiceResponse signInV3(@RequestBody SignInRequest request) {
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, userService.signIn(request));
		} catch (ServiceException e) {
			logger.error("error trying to signin.");
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	/**
	 * SignIn V4 includes validation to detect a non-existent email
	 * @since v2.0.6 - Smart Lock login
	 * @param request
	 * @return ServiceResponse
	 */
	@RequestMapping(value = {"v4/signin", "v4.0/signin"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ServiceResponse signInV4(@RequestBody SignInRequest request) {
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, userService.signInV4(request));
		} catch (ServiceException e) {
			logger.error("error trying to signin.");
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	/**
	 * SignIn to support signIn without password by Account Kit SMS.
	 * The user signs in into the app with his/her mobile phone
	 * @param request
	 * 				AccountKitSMSSignInRequest: user mobile phone number and 
	 * 				Account Kit access token.
	 * @return ServiceResponse 
	 * 				SignInResponse: park token, username and user profile
	 */
	@RequestMapping(value = {"v3/signin/phone", "v3.0/signin/phone"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ServiceResponse signInPhoneNumber(@RequestBody AccountKitSMSSignInRequest request) {
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, userService.accountKitSMSSignIn(request));
		} catch (ServiceException e) {
			logger.error("error trying to signin by Account kit SMS and phone number.");
			e.setRequestToContext(request);
			throw e;
		} 
	}

	/**
	 * SignIn to support signIn without password by Account Kit Email.
	 * The user signs in into the app with his/her email
	 * @param request
	 * 				AccountKitEmailSignInRequest: user email and 
	 * 				Account Kit access token.
	 * @return ServiceResponse 
	 * 				SignInResponse: park token, username and user profile
	 */
	@RequestMapping(value = {"v3/signin/email", "v3.0/signin/email"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ServiceResponse signInEmail(@RequestBody AccountKitEmailSignInRequest request) {
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, userService.accountKitEmailSignIn(request));
		} catch (ServiceException e) {
			logger.error("error trying to signin by Account kit email.");
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	/**
	 * Performs a  Facebook sign in.
	 * @param request
	 * 				FacebookSignInRequest: user Facebook credentials 
	 * @return ServiceResponse 
	 * 				SignInResponse: park token, username and user profile
	 */
	@RequestMapping(value = {"v3/signin/facebook", "v3.0/signin/facebook"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ServiceResponse signInFacebook(@RequestBody FacebookSignInRequest request) {
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, userService.facebookSignIn(request));
		} catch (ServiceException e) {
			logger.error("error trying to signin by facebook.");
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"v3/signup", "v3.0/signup"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ServiceResponse signUp(
			@RequestHeader(required = false, value = LANGUAGE_HEADER) String lang,
			@RequestBody SignUpRequest request) throws ServiceException {
		try {
			if (StringUtils.isNotEmpty(lang)) {
				request.setLang(lang);
			}
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, userService.signUp(request));
		} catch (ServiceException e) {
			logger.error("error trying to signup.");
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	/**
	 * It creates a user account by setting the mobile phone number.
	 * @param lang the language
	 * @param request the request information
	 * @return ServiceResponse
	 * @throws ServiceException
	 */
	@RequestMapping(value = {"v3/signup/phone", "v3.0/signup/phone"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ServiceResponse signUpPhoneNumber(
			@RequestHeader(required = false, value = LANGUAGE_HEADER) String lang,
			@RequestBody AccountKitSMSSignUpRequest request) throws ServiceException {
		try {
			if (StringUtils.isNotEmpty(lang)) {
				request.setLang(lang);
			}
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, userService.accountKitSMSSignUp(request));
		} catch (ServiceException e) {
			logger.error("error tring to signup by Account kit SMS and phone number.");
			e.setRequestToContext(request);
			throw e;
		} 
	}

	/**
     * It creates a user account by setting the email address.
     * @param lang the language
     * @param request the request information
     * @return ServiceResponse
     * @throws ServiceException
     */
    @RequestMapping(value = {"v3/signup/email", "v3.0/signup/email"}, method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ServiceResponse signUpEmailV3(
            @RequestHeader(required = false, value = LANGUAGE_HEADER) String lang,
            @RequestBody AccountKitEmailSignUpRequest request) throws ServiceException {
        try {
            if (StringUtils.isNotEmpty(lang)) {
                request.setLang(lang);
            }
            return new ServiceResponse(ServiceResponseStatus.SUCCESS,
                    StringUtils.EMPTY, userService.accountKitEmailSignUpV3(request));
        } catch (ServiceException e) {
        	logger.error("Error trying to signup by Account Kit email. Version: 3.");
            e.setRequestToContext(request);
            throw e;
        } 
    }

	/**
	 * It creates a user account by setting the email address.
	 * @param lang the language
	 * @param request the request information
	 * @return ServiceResponse
	 * @throws ServiceException
	 */
	@RequestMapping(value = {"v4/signup/email", "v4.0/signup/email"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ServiceResponse signUpEmailV4(
			@RequestHeader(required = false, value = LANGUAGE_HEADER) String lang,
			@RequestBody AccountKitEmailSignUpRequest request) throws ServiceException {
		try {
			if (StringUtils.isNotEmpty(lang)) {
				request.setLang(lang);
			}
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, userService.accountKitEmailSignUpV4(request));
		} catch (ServiceException e) {
			logger.error("Error trying to signup by Account Kit email. Version: 4.");
			e.setRequestToContext(request);
			throw e;
		}
	}

	/**
	 * Performs a facebook sign up.
	 * @param request
	 * 				FacebookSignUpRequest: user Facebook credentials 
	 * @return ServiceResponse 
	 * 				SignUpResponse: park token and username.
	 */
	@RequestMapping(value = {"v3/signup/facebook", "v3.0/signup/facebook"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ServiceResponse signUpFacebookV3(@RequestBody FacebookSignUpRequest request) {
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, userService.facebookSignUpV3(request));
		} catch (ServiceException e) {
			logger.error("Error trying to signup by Facebook. Version: 3.");
			e.setRequestToContext(request);
			throw e;
		} 
	}

	/**
	 * Performs a facebook sign up.
	 * @param request
	 * 				FacebookSignUpRequest: user Facebook credentials
	 * @return ServiceResponse
	 * 				SignUpResponse: park token and username.
	 */
	@RequestMapping(value = {"v4/signup/facebook", "v4.0/signup/facebook"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ServiceResponse signUpFacebookV4(@RequestBody FacebookSignUpRequest request) {
		try {
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, userService.facebookSignUpV4(request));
		} catch (ServiceException e) {
			logger.error("Error trying to signup by Facebook. Version: 4.");
			e.setRequestToContext(request);
			throw e;
		}
	}

	@RequestMapping(value = {"v3/check", "v3.0/check"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ServiceResponse checkValue(@RequestBody CheckValueRequest request)
			throws ServiceException {
		try {
			CheckValueResponse response = userService.checkValue(request);
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, response);
		} catch (ServiceException e) {
			logger.error("error trying to check value.");
			e.setRequestToContext(request);
			throw e;
		} 
	}

	/**
	 * ForgotPwdV3 returns a ServiceException with ServiceExceptionCode.USER_NOT_FOUND
	 * when user is null
	 * @param request
	 * @return ServiceResponse.SUCCESS or ServiceException 
	 */
	@RequestMapping(value = {"v3/forgotpwd", "v3.0/forgotpwd"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ServiceResponse forgotPwdV3(@RequestBody EmailRequest request) {
		try {
			return userService.forgetPwdV3(request);
		} catch (ServiceException e) {
			logger.error("error trying to execute forgot password method.");
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	/**
	 * ForgotPwdV4 returns a ServiceException with ServiceExceptionCode.NON_EXISTENT_EMAIL
	 * when user is null
	 * @param request
	 * @return ServiceResponse.SUCCESS or ServiceException 
	 */
	@RequestMapping(value = {"v4/forgotpwd", "v4.0/forgotpwd"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ServiceResponse forgotPwdV4(@RequestBody EmailRequest request) {
		try {
			return userService.forgetPwdV4(request);
		} catch (ServiceException e) {
			logger.error("error trying to execute forgot password method.");
			e.setRequestToContext(request);
			throw e;
		} 
	}


	@RequestMapping(value = {"v3/changepwd", "v3.0/changepwd"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ServiceResponse changePwd(
			@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@RequestBody ChangePwdRequest request) {
		try {
			request.setToken(parkToken);
			return new ServiceResponse(ServiceResponseStatus.SUCCESS,
					StringUtils.EMPTY, userService.changePwd(request));
		} catch (ServiceException e) {
            logger.error("error trying to change password for token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}

	@RequestMapping(value = {"v3/signout", "v3.0/signout"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ServiceResponse signOut(@RequestHeader(PARK_TOKEN_HEADER) String parkToken,
			@RequestBody DeviceRequest request) throws ServiceException {
		try {
			request.setToken(parkToken);
			return userService.signOut(request);
		} catch (ServiceException e) {
            logger.error("error trying to signup for token: {}", parkToken);
			e.setRequestToContext(request);
			throw e;
		} 
	}
	
	/**
	 * Sets the swrve id to user session given a park token.
	 * @param parkToken
	 *     the token to find the user session
	 * @param request
	 *     the request with the swrve id
	 * @return  ServiceResponse
	 * @throws ServiceException
	 */
	@RequestMapping(value = {"v3/device/swrve", "v3.0/device/swrve"}, method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ServiceResponse setSwrveIdToSession(@RequestHeader(PARK_TOKEN_HEADER) String parkToken, 
			@RequestBody DeviceSwrveIdRequest request) throws ServiceException {
		try {
			request.setToken(parkToken);
			return userService.setSwrveIdToSession(request);
		} catch(ServiceException e) {
			e.setRequestToContext(request);
			throw e;
		}
	}

}
