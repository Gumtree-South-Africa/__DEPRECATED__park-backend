package com.ebay.park.service.social.validator;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.social.dto.UserRatesRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserRatesRequestValidatorTest {

	private static final String USERNAME = "username";

	private static final String VALID_STATUS_1 = "negative";
	private static final String VALID_STATUS_2 = "neutral";
	private static final String VALID_STATUS_3 = "positive";
	private static final String INVALID_RATE_STATUS = "Good";

	private UserRatesRequestValidator userRatesRequestValidator;
	
	@Mock
	private UserRatesRequest userRatesRequest;
	
	@Before
	public void setUp(){
		userRatesRequestValidator = new UserRatesRequestValidator();
		initMocks(this);
	}
	
	@Test
	public void validateSuccessfulTest1(){
		Mockito.when(userRatesRequest.getUsername()).thenReturn(USERNAME);
		List<String> statuses = new ArrayList<String>();
		statuses.add(VALID_STATUS_1);
		Mockito.when(userRatesRequest.getRateStatus()).thenReturn(statuses);
		
		userRatesRequestValidator.validate(userRatesRequest);

		Mockito.verify(userRatesRequest, Mockito.times(1)).getUsername();
		Mockito.verify(userRatesRequest, Mockito.times(1)).getRateStatus();
	}
	
	@Test
	public void validateSuccessfulTest2(){
		Mockito.when(userRatesRequest.getUsername()).thenReturn(USERNAME);
		List<String> statuses = new ArrayList<String>();
		statuses.add(VALID_STATUS_2);
		Mockito.when(userRatesRequest.getRateStatus()).thenReturn(statuses);
		
		userRatesRequestValidator.validate(userRatesRequest);

		Mockito.verify(userRatesRequest, Mockito.times(1)).getUsername();
		Mockito.verify(userRatesRequest, Mockito.times(1)).getRateStatus();
	}
	
	@Test
	public void validateSuccessfulTest3(){
		Mockito.when(userRatesRequest.getUsername()).thenReturn(USERNAME);
		List<String> statuses = new ArrayList<String>();
		statuses.add(VALID_STATUS_3);
		Mockito.when(userRatesRequest.getRateStatus()).thenReturn(statuses);
		
		userRatesRequestValidator.validate(userRatesRequest);

		Mockito.verify(userRatesRequest, Mockito.times(1)).getUsername();
		Mockito.verify(userRatesRequest, Mockito.times(1)).getRateStatus();
	}
	
	@Test
	public void nullRequestTest() {

		try {
			userRatesRequestValidator.validate(null);
			fail("A service excpetion was expected");
		} catch (ServiceException re) {
			assertEquals(re.getCode(), ServiceExceptionCode.INVALID_USER_RATES_REQ.getCode());
		}
	}
	
	@Test
	public void emptyUsernameTest() {

		Mockito.when(userRatesRequest.getUsername()).thenReturn(null);
		
		try {
			userRatesRequestValidator.validate(userRatesRequest);
			fail("A service excpetion was expected");
		} catch (ServiceException re) {
			assertEquals(re.getCode(), ServiceExceptionCode.EMPTY_USERNAME.getCode());
			Mockito.verify(userRatesRequest, Mockito.times(1)).getUsername();
		}
	}
	
	
	@Test
	public void InvalidRateStatusRequestTest() {
		
		Mockito.when(userRatesRequest.getUsername()).thenReturn(USERNAME);
		List<String> statuses = new ArrayList<String>();
		statuses.add(INVALID_RATE_STATUS);
		Mockito.when(userRatesRequest.getRateStatus()).thenReturn(statuses);

		try {
			userRatesRequestValidator.validate(userRatesRequest);
			fail("A service excpetion was expected");
		} catch (ServiceException re) {
			assertEquals(re.getCode(), ServiceExceptionCode.INVALID_USER_RATES_STATUS.getCode());
			Mockito.verify(userRatesRequest, Mockito.times(1)).getUsername();
			Mockito.verify(userRatesRequest, Mockito.times(1)).getRateStatus();
		}
	}

	
	
}
