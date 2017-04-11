package com.ebay.park.service.session.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.ebay.park.db.dao.OrphanedDeviceDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.DeviceType;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.device.command.RemoveUserSessionsByDeviceCmd;
import com.ebay.park.service.device.dto.DeviceRequest;
import com.ebay.park.service.device.dto.RemoveUserSessionsByDeviceRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.dto.SignRequest;
import com.ebay.park.util.ParkTokenUtil;

public class CreateSessionCmdTest {
	
	private static final String USERNAME = "username";
	private static final String PASSWORD = "123456";
	private static final String TOKEN = "token";
	private static final String EMAIL = "john@company.com";
	private static final String LANGUAGE = "es";
	private static final String DEVICE_ID = "abcdefg";
	private static final String DEVICE_TYPE = "android";
	private static final String SESSION_TOKEN = "session-token";
	private static final String FAIL_MESSAGE = "This should not produce exception";
	private static final String UUID = "abcdefghi";
	private static final String SWRVE_ID = "swrve_id";
	
	@InjectMocks
	@Spy
	private CreateSessionCmd cmd;
	
	@Mock
	private UserDao userDao;
	
	@Mock
	private User user;
	
	@Mock
	private ParkTokenUtil tokenUtil;
	
	@Mock
	private SessionService sessionService;
	
	@Mock
	private RemoveUserSessionsByDeviceCmd removeUserSessionsByDeviceCmd;
	
	@Mock
	private OrphanedDeviceDao orphanedDeviceDao;

	@Mock
	private UserServiceHelper userServiceHelper;
	
	@Before
	public void setUp(){
		initMocks(this);
	}
	
	@Test
	public void testExecutionsFailsByUserNotFound() {
		try {
			SignRequest request = createRequest();
			Mockito.when(userServiceHelper.findUserByUsernameOrEmail(USERNAME, EMAIL)).thenReturn(null);

			cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.USER_NOT_FOUND.getCode(), e.getCode());
		}
		
		Mockito.verify(userServiceHelper).findUserByUsernameOrEmail(USERNAME, EMAIL);
	}
	
	@Test
	public void testExecutionsSucceedsWithoutDevice() {
		String response = null;
		try {
			SignRequest request = createRequestWithoutDevice();
						
			Mockito.when(userServiceHelper.findUserByUsernameOrEmail(USERNAME, EMAIL)).thenReturn(user);
			Mockito.when(tokenUtil.createSessionToken()).thenReturn(SESSION_TOKEN);
			
			response = cmd.execute(request);
		} catch (ServiceException e) {
			fail(FAIL_MESSAGE);
		}
		
		Mockito.verify(userServiceHelper).findUserByUsernameOrEmail(USERNAME, EMAIL);
		Mockito.verify(tokenUtil).createSessionToken();
		Mockito.verify(user).addUserSession(Mockito.any(UserSession.class));
		Mockito.verify(sessionService).createUserSessionCache(Mockito.any(UserSession.class));
		Mockito.verifyNoMoreInteractions(removeUserSessionsByDeviceCmd);
		
		assertEquals(SESSION_TOKEN, response);
	}
	
	private SignRequest createRequestWithoutDevice() {
		SignRequest request = new SignRequest();
		request.setUsername(USERNAME);
		request.setDevice(null);
		request.setEmail(EMAIL);
		request.setLanguage(LANGUAGE);
		request.setPassword(PASSWORD);
		request.setToken(TOKEN);
		request.setTimestamp(new Date(System.currentTimeMillis()));
		return request;
	}

	@Test
	public void testExecutionSucceeds() {
		String response = null;
		try {
			SignRequest request = createRequest();
			RemoveUserSessionsByDeviceRequest removeRequest = createRemoveUserSessionsByDeviceRequest();
						
			Mockito.when(userServiceHelper.findUserByUsernameOrEmail(USERNAME, EMAIL)).thenReturn(user);
			Mockito.when(tokenUtil.createSessionToken()).thenReturn(SESSION_TOKEN);
			Mockito.when(removeUserSessionsByDeviceCmd.execute(removeRequest)).thenReturn(true);
			
			response = cmd.execute(request);
		} catch (ServiceException e) {
			fail(FAIL_MESSAGE);
		}
		
		Mockito.verify(userServiceHelper).findUserByUsernameOrEmail(USERNAME, EMAIL);
		Mockito.verify(tokenUtil).createSessionToken();
		Mockito.verify(removeUserSessionsByDeviceCmd).execute(Mockito.any(RemoveUserSessionsByDeviceRequest.class));
		Mockito.verify(user).addUserSession(Mockito.any(UserSession.class));
		Mockito.verify(sessionService).createUserSessionCache(Mockito.any(UserSession.class));
		
		assertEquals(SESSION_TOKEN, response);
	}

	private RemoveUserSessionsByDeviceRequest createRemoveUserSessionsByDeviceRequest() {
		return new RemoveUserSessionsByDeviceRequest(UUID, DEVICE_ID, DeviceType.getDeviceByValue(DEVICE_TYPE));
	}

	private SignRequest createRequest() {
		SignRequest request = new SignRequest();
		request.setUsername(USERNAME);
		request.setDevice(createDeviceRequest());
		request.setEmail(EMAIL);
		request.setLanguage(LANGUAGE);
		request.setPassword(PASSWORD);
		request.setToken(TOKEN);
		request.setTimestamp(new Date(System.currentTimeMillis()));
		return request;
	}

	private DeviceRequest createDeviceRequest() {
		return new DeviceRequest(DEVICE_ID, DEVICE_TYPE, UUID);
	}
	
	@Test
	public void givenSwrveIdWhenCreateUserSessionThenSuccess() {
		//given
		SignRequest request = createRequest();
		DeviceRequest deviceRequest = new DeviceRequest();
		deviceRequest.setSwrveId(SWRVE_ID);
		
		RemoveUserSessionsByDeviceRequest removeRequest = createRemoveUserSessionsByDeviceRequest();
		
		Mockito.when(userServiceHelper.findUserByUsernameOrEmail(USERNAME, EMAIL)).thenReturn(user);
		Mockito.when(tokenUtil.createSessionToken()).thenReturn(SESSION_TOKEN);
		Mockito.when(removeUserSessionsByDeviceCmd.execute(removeRequest)).thenReturn(true);
		
		//when
		String response = cmd.execute(request);
		
		//then
		assertEquals(SESSION_TOKEN, response);
	}
}
