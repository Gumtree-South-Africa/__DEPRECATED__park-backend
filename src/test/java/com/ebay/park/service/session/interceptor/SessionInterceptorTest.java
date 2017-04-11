package com.ebay.park.service.session.interceptor;

import com.ebay.park.db.dao.UserAdminDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserSessionDao;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.command.UserSessionUpdaterCmd;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.ParkConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class SessionInterceptorTest implements ParkConstants {

	private static final String VALID_AUTH_TOKEN = "aaaa-bbb-cccc";
	private static final String INVALID_AUTH_TOKEN = "";

	@InjectMocks
	private SessionInterceptor parkSessionInterceptor;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private SessionService sessionService;
	
	@Mock
	private UserDao userDao;
	
	@Mock
	private UserSessionDao userSessionDao;

	@Mock
	private UserAdminDao userAdminDao;
	
	@Mock
	private UserSessionUpdaterCmd userSessionUpdaterCmd;
	
	@Before
	public void setUp() {
		parkSessionInterceptor = new SessionInterceptor();
		initMocks(this);
	}

	@Test
	public void testPreHandleHappyPath() throws Exception {
		UserSessionCache userSessionMock = mock(UserSessionCache.class);

		when(request.getHeader(PARK_TOKEN_HEADER)).thenReturn(VALID_AUTH_TOKEN);
		when(sessionService.getUserSession(VALID_AUTH_TOKEN)).thenReturn(userSessionMock);

		boolean preHandle = parkSessionInterceptor.preHandle(request, response, new Object());

		assertTrue(preHandle);

		verify(request).getHeader(ParkConstants.PARK_TOKEN_HEADER);
		verify(sessionService).getUserSession(VALID_AUTH_TOKEN);
	}

	@Test
	public void testPreHandleEmptyToken() throws Exception {
		when(request.getHeader(PARK_TOKEN_HEADER)).thenReturn(INVALID_AUTH_TOKEN);

		try {
			parkSessionInterceptor.preHandle(request, response, new Object());
		} catch (ServiceException se) {
			assertEquals(se.getCode(), ServiceExceptionCode.EMPTY_PARK_TOKEN.getCode());		
		} finally {
			verify(request, times(1)).getHeader(PARK_TOKEN_HEADER);
		}
	}

	@Test
	public void testPreHandleUnauthorizedUser() throws Exception {
		when(request.getHeader(PARK_TOKEN_HEADER)).thenReturn(VALID_AUTH_TOKEN);
		when(sessionService.getUserSession(VALID_AUTH_TOKEN)).thenThrow(ServiceException.createServiceException(ServiceExceptionCode.SESSION_NOT_STORED));
		when(userDao.findByToken(VALID_AUTH_TOKEN)).thenReturn(null);
		when(userSessionDao.findUserSessionByToken(INVALID_AUTH_TOKEN)).thenReturn(null);
		try {
			parkSessionInterceptor.preHandle(request, response, new Object());
		} catch (ServiceException se) {
			assertEquals(se.getCode(), ServiceExceptionCode.USER_UNAUTHORIZED.getCode());
		} finally {
			verify(request, times(1)).getHeader(PARK_TOKEN_HEADER);
			verify(sessionService, times(1)).getUserSession(VALID_AUTH_TOKEN);
		}
	}

}
