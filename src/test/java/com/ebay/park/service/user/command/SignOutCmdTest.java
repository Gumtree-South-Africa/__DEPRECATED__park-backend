/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.user.command;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserSessionDao;
import com.ebay.park.db.entity.DeviceType;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.device.dto.DeviceDTO;
import com.ebay.park.service.device.dto.DeviceRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.TestServiceUtil;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.service.user.dto.RemoveCurrentUserSessionRequest;

/**
 * @author juan.pizarro
 * 
 */
public class SignOutCmdTest {

	private static final String VALID_USERNAME = "validUserName";

	private static final byte[] VALID_ENCRYPTED_PASSWORD = { 10, 10, 20 };
	private static final String VALID_EMAIL = "blabla@google.com";
	private static final String VALID_LAT = "33.234232";
	private static final String VALID_LONG = "-23.342342";
	private static final String VALID_TOKEN = "aaa-bb-ccc";
	private static final String NO_USER_TOKEN = "aaa-bb-ccc";
	private static final Long INVALID_USER_ID = 1l;
	private static final Long VALID_USER_ID = 2l;
	protected static final String DEVICE_ID = "12345";
	protected static final String DEVICE_TYPE = "android";
	private static final String UUID = "abcdefghi";

	@InjectMocks
	private final SignOutCmd logOutCmd = new SignOutCmd();

	@Mock
	private UserDao userDao;
	
	@Mock
	private User user;
	
	@Mock
	private UserSessionDao userSessionDao;
	
	@Mock
	private RemoveCurrentUserSessionCmd removeCurrentUserSessionCmd;
	
	@Mock
	private SessionService sessionService;

	@Before
	public void setUp(){
		initMocks(this);
	}
	
	@Test
	public void testExecuteLogOutSuccessfullyWithoutDevice() throws Exception {

		User user = TestServiceUtil.createUserMock(VALID_USER_ID, VALID_USERNAME, VALID_EMAIL,
				VALID_ENCRYPTED_PASSWORD, Double.valueOf(VALID_LAT), Double.valueOf(VALID_LONG),
				VALID_TOKEN);
		UserSession userSession = new UserSession(NO_USER_TOKEN, UUID);
		userSession.setUser(user);

		UserSessionCache sessionCache = new UserSessionCache(userSession);
		sessionCache.setUserId(VALID_USER_ID);
		Mockito.when(sessionService.getUserSession(NO_USER_TOKEN)).thenReturn(sessionCache);
		
		Mockito.when(userDao.findById(VALID_USER_ID)).thenReturn(user);

		Mockito.when(removeCurrentUserSessionCmd.execute(
				Mockito.any(RemoveCurrentUserSessionRequest.class))).thenReturn(true);

		DeviceRequest deviceRequest = new DeviceRequest(UUID, DEVICE_ID, DEVICE_TYPE);
		deviceRequest.setToken(VALID_TOKEN);
		logOutCmd.execute(deviceRequest);

		Mockito.verify(removeCurrentUserSessionCmd, Mockito.times(1)).execute(
				Mockito.any(RemoveCurrentUserSessionRequest.class));
	}
	
	@Test
	public void testExecuteLogOutSuccessfullyWithDevice() throws Exception {

		User user = TestServiceUtil.createUserMock(VALID_USER_ID, VALID_USERNAME, VALID_EMAIL,
				VALID_ENCRYPTED_PASSWORD, Double.valueOf(VALID_LAT), Double.valueOf(VALID_LONG),
				VALID_TOKEN);
		UserSession userSession = new UserSession(VALID_TOKEN, UUID);
		userSession.setUser(user);

		UserSessionCache sessionCache = new UserSessionCache(userSession);
		sessionCache.setUserId(VALID_USER_ID);
		sessionCache.setDevice(new DeviceDTO() {
			private static final long serialVersionUID = 1L;
			{
				setDeviceId(DEVICE_ID);
				setDeviceType(DEVICE_TYPE);
			}
		});
		Mockito.when(sessionService.getUserSession(VALID_TOKEN)).thenReturn(sessionCache);
		
		Mockito.when(userDao.findById(VALID_USER_ID)).thenReturn(user);

		Mockito.when(removeCurrentUserSessionCmd.execute(
				Mockito.any(RemoveCurrentUserSessionRequest.class))).thenReturn(true);
		
		
		Mockito.when(userSessionDao.findUserSessionsByDeviceId(DEVICE_ID, DeviceType.ANDROID)).thenReturn(Arrays.asList(userSession));

		DeviceRequest deviceRequest = new DeviceRequest(UUID, DEVICE_ID, DEVICE_TYPE);
		deviceRequest.setToken(VALID_TOKEN);
		logOutCmd.execute(deviceRequest);

		Mockito.verify(sessionService, Mockito.times(1)).getUserSession(VALID_TOKEN);
		Mockito.verify(userDao, Mockito.times(1)).findById(VALID_USER_ID);
		Mockito.verify(userSessionDao, Mockito.times(1)).findUserSessionsByDeviceId(DEVICE_ID,
				DeviceType.ANDROID);
		Mockito.verify(userSessionDao, Mockito.times(1)).save(Mockito.any(UserSession.class));
	}


	@Test
	public void testExecuteLogOutUserNotFound1() throws Exception {
		Mockito.when(sessionService.getUserSession(NO_USER_TOKEN)).thenReturn(null);

		try {
			DeviceRequest deviceRequest = new DeviceRequest(UUID, DEVICE_ID, DEVICE_TYPE);
			deviceRequest.setToken(NO_USER_TOKEN);
			logOutCmd.execute(deviceRequest);
		} catch (ServiceException se) {
			assertEquals(se.getCode(), ServiceExceptionCode.USER_UNAUTHORIZED.getCode());
		} finally {
			Mockito.verify(sessionService, Mockito.times(1)).getUserSession(NO_USER_TOKEN);
		}

	}
	
	@Test
	public void testExecuteLogOutUserNotFound2() throws Exception {
		UserSession userSession = new UserSession(NO_USER_TOKEN, UUID);
		userSession.setUser(user);

		UserSessionCache sessionCache = new UserSessionCache(userSession);
		sessionCache.setUserId(INVALID_USER_ID);

		Mockito.when(sessionService.getUserSession(NO_USER_TOKEN)).thenReturn(sessionCache);
		Mockito.when(userDao.findById(INVALID_USER_ID)).thenReturn(null);
		try {
			DeviceRequest deviceRequest = new DeviceRequest(UUID, DEVICE_ID, DEVICE_TYPE);
			deviceRequest.setToken(NO_USER_TOKEN);
			logOutCmd.execute(deviceRequest);
		} catch (ServiceException se) {
			assertEquals(se.getCode(), ServiceExceptionCode.USER_UNAUTHORIZED.getCode());
		} finally {
			Mockito.verify(sessionService, Mockito.times(1)).getUserSession(NO_USER_TOKEN);
			Mockito.verify(userDao, Mockito.times(1)).findById(INVALID_USER_ID);
		}

	}


}
