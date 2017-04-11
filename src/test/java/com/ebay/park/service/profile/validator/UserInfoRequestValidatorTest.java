package com.ebay.park.service.profile.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.profile.dto.UserInfoRequest;

public class UserInfoRequestValidatorTest {
	private static final String VALID_LOCATION = "33.234232,-23.342342";
	private static final String VALID_LOCATION_NAME = "CITY NAME, STATE";
	private static final String FAIL_MSG = "An exception was expected";
	
	@InjectMocks
	private ServiceValidator<UserInfoRequest> validator;
	
	@Before
	public void setUp() {
		validator = new UserInfoRequestValidator();
		initMocks(this);
	}
	
	@Test
	public void testNullZipCode() {
		UserInfoRequest request = new UserInfoRequest();
		request.setLocation(VALID_LOCATION);
		request.setLocationName(VALID_LOCATION_NAME);
		request.setZipCode(null);
		try {
			validator.validate(request);
			fail(FAIL_MSG);
		} catch(ServiceException e) {
			assertEquals(ServiceExceptionCode.INVALID_ZIP_CODE.getCode(), e.getCode());
		}
	}
	
	@Test
	public void testInvalidUrlPicture(){
		UserInfoRequest request = new UserInfoRequest();
		request.setLocation(VALID_LOCATION);
		request.setLocationName(VALID_LOCATION_NAME);
		request.setPicture("bad_url");
		try {
			validator.validate(request);
			fail(FAIL_MSG);
		} catch(ServiceException e) {
			assertEquals(ServiceExceptionCode.INVALID_URL.getCode(), e.getCode());
		}
	}
	
	@Test
	public void testNullUserInfoRequest(){
		try {
			validator.validate(null);
			fail(FAIL_MSG);
		} catch(ServiceException e) {
			assertEquals(ServiceExceptionCode.INVALID_SIGNUP_REQ.getCode(), e.getCode());
		}
	}
	
	@Test
	public void testLocationNameBlank(){
		UserInfoRequest request = new UserInfoRequest();
		request.setLocation(VALID_LOCATION);
		request.setLocationName("");
		try {
			validator.validate(request);
			fail(FAIL_MSG);
		} catch(ServiceException e) {
			assertEquals(ServiceExceptionCode.EMPTY_LOCATION_NAME.getCode(), e.getCode());
		}
	}
}
