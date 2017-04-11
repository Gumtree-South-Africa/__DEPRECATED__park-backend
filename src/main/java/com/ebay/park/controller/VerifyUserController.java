/*
 * Copyright eBay, 2014
 */
package com.ebay.park.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.user.UserService;
import com.ebay.park.service.user.dto.VerifyEmailRequest;
import com.ebay.park.util.KeyAndValue;
import com.ebay.park.util.ParkConstants;

/**
 * @author juan.pizarro
 */
@Controller
@RequestMapping(value = {"/users/v3","/users/v3.0", "/users"})
public class VerifyUserController implements ParkConstants {


	@Autowired
	private UserService userService;

	@Value("${signin.maxAttempts}")
	private Integer signInMaxAttempts;

	@RequestMapping(value = "/verifyemail/{email}/{token}", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.FOUND)
	public String verifyEmail(@PathVariable("email") String email,
			@PathVariable("token") String token) {
		VerifyEmailRequest request = new VerifyEmailRequest(email, token);
		try {
			return "redirect:" + userService.verifyEmail(request);
		} catch (ServiceException e) {
			e.setKeyValueListToContext(KeyAndValue.from("email", email), KeyAndValue.from("token", token));
			throw e;
		} 
	}

	@RequestMapping(value = "/sendverification", method = RequestMethod.POST, consumes = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public ServiceResponse sendVerificationEmail(
			@RequestHeader(PARK_TOKEN_HEADER) String parkToken) {
		try {
			return userService.sendVerificationEmail(parkToken);
		} catch (ServiceException e) {
			e.setKeyValueListToContext(KeyAndValue.from("token", parkToken));
			throw e;
		} 
	}
}
