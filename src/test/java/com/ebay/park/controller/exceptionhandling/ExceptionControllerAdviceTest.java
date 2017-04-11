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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebay.park.service.ServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MessagesConfig.class})
public class ExceptionControllerAdviceTest {
	
	@Mock
	private ParkErrorHandler parkErrorHandler;
	
	@InjectMocks
    private ExceptionControllerAdvice controller = new ExceptionControllerAdvice();
	
	@Before
	public void setup() {
		initMocks(this);
	}
	
	@Test
	public void serviceExceptionTest() {
		controller.serviceException(any(ServiceException.class), any(HttpServletRequest.class));
		verify(parkErrorHandler).serviceExceptionHandler(any(ServiceException.class), any(HttpServletRequest.class));
	}
 
}
