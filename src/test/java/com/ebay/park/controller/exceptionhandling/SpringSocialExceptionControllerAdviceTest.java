package com.ebay.park.controller.exceptionhandling;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.servlet.http.HttpServletRequest;

import com.ebay.park.config.MessagesConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.social.ApiException;
import org.springframework.social.DuplicateStatusException;
import org.springframework.social.ExpiredAuthorizationException;
import org.springframework.social.InsufficientPermissionException;
import org.springframework.social.InvalidAuthorizationException;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.RateLimitExceededException;
import org.springframework.social.RevokedAuthorizationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebay.park.service.ServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MessagesConfig.class})
public class SpringSocialExceptionControllerAdviceTest {
	
	@Mock
	private ParkErrorHandler parkErrorHandler;
	
	@InjectMocks
    private SpringSocialExceptionControllerAdvice controller = new SpringSocialExceptionControllerAdvice();
	
	@Before
	public void setup() {
		initMocks(this);
	}
	
	
	@Test
	public void invalidAuthorizationExceptionTest() {
		controller.invalidAuthorizationException(any(InvalidAuthorizationException.class), any(HttpServletRequest.class));
		verify(parkErrorHandler).serviceExceptionHandler(any(ServiceException.class), any(HttpServletRequest.class));
	}
	
	@Test
	public void expiredAuthorizationExceptionTest() {
		controller.expiredAuthorizationException(any(ExpiredAuthorizationException.class), any(HttpServletRequest.class));
		verify(parkErrorHandler).serviceExceptionHandler(any(ServiceException.class), any(HttpServletRequest.class));
	}

	@Test
	public void insufficientPermissionExceptionTest() {
		controller.insufficientPermissionException(any(InsufficientPermissionException.class), any(HttpServletRequest.class));
		verify(parkErrorHandler).serviceExceptionHandler(any(ServiceException.class), any(HttpServletRequest.class));
	}
	
	@Test
	public void revokedAuthorizationExceptionTest() {
		controller.revokedAuthorizationException(any(RevokedAuthorizationException.class), any(HttpServletRequest.class));
		verify(parkErrorHandler).serviceExceptionHandler(any(ServiceException.class), any(HttpServletRequest.class));
	}

	@Test
	public void missingAuthorizationExceptionTest() {
		controller.missingAuthorizationException(any(MissingAuthorizationException.class), any(HttpServletRequest.class));
		verify(parkErrorHandler).serviceExceptionHandler(any(ServiceException.class), any(HttpServletRequest.class));
	}
	
	@Test
	public void duplicateStatusExceptionTest() {
		controller.duplicateStatusException(any(DuplicateStatusException.class), any(HttpServletRequest.class));
		verify(parkErrorHandler).serviceExceptionHandler(any(ServiceException.class), any(HttpServletRequest.class));
	}
	
	@Test
	public void rateLimitExceededExceptionTest() {
		controller.rateLimitExceededException(any(RateLimitExceededException.class), any(HttpServletRequest.class));
		verify(parkErrorHandler).serviceExceptionHandler(any(ServiceException.class), any(HttpServletRequest.class));
	}

	@Test
	public void apiExceptionTest() {
		controller.apiException(any(ApiException.class), any(HttpServletRequest.class));
		verify(parkErrorHandler).serviceExceptionHandler(any(ServiceException.class), any(HttpServletRequest.class));
	}

}
