package com.ebay.park.service.session.command;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verify;

import com.ebay.park.db.dao.OrphanedDeviceDao;
import com.ebay.park.db.entity.DeviceType;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.device.command.RemoveUserSessionsByDeviceCmd;
import com.ebay.park.service.device.dto.DeviceRequest;
import com.ebay.park.service.device.dto.RemoveUserSessionsByDeviceRequest;
import com.ebay.park.service.user.dto.signin.SignInRequest;
import com.ebay.park.util.ParkTokenUtil;

/**
 * Unit tests for {@link CreateUserSessionCmd}
 * @author scalderon
 *
 */
public class CreateUserSessionCmdTest {
	
	private static final String SESSION_TOKEN = "token"; 
	private static final String SWRVE_ID = "swrveId";
	private static final String DEVICE_ID = "deviceId";
	private static final String UNIQUE_DEVICE_ID = "uniqueDeviceId";

	@InjectMocks
	@Spy
	private CreateUserSessionCmd cmd;
	
	@Mock
	private ParkTokenUtil tokenUtil;
	
	@Mock
	private OrphanedDeviceDao orphanedDeviceDao;
	
	@Mock
	private RemoveUserSessionsByDeviceCmd removeUserSessionsByDeviceCmd;
	
	@Before
	public void setUp(){
		initMocks(this);
	}

	@Test
	public void givenARequestWithoutDeviceWhenExecuteThenCreateSessionWithoutDevice() {
		//given
		SignInRequest request = new SignInRequest();
		request.setDevice(null);
		
		when(tokenUtil.createSessionToken()).thenReturn(SESSION_TOKEN);
		
		//when
		UserSession session = cmd.execute(request);
		
		//then
		assertNotNull(session);
		assertEquals(session.getToken(), SESSION_TOKEN);
		assertNull(session.getDevice());
		assertNull(session.getUniqueDeviceId());
		assertNull(session.getSwrveId());
		verifyNoMoreInteractions(orphanedDeviceDao);
		verifyNoMoreInteractions(removeUserSessionsByDeviceCmd);
	}
	
	@Test
	public void givenARequestWithSwrveIdWhenExecuteThenCreateSessionWithSwrveId() {
		//given
		SignInRequest request = new SignInRequest();
		DeviceRequest deviceRequest = new DeviceRequest();
		deviceRequest.setSwrveId(SWRVE_ID);
		request.setDevice(deviceRequest);
		
		when(tokenUtil.createSessionToken()).thenReturn(SESSION_TOKEN);
		
		//when
		UserSession session = cmd.execute(request);
		
		//then
		assertNotNull(session);
		assertEquals(session.getToken(), SESSION_TOKEN);
		assertEquals(session.getSwrveId(), SWRVE_ID);
		verifyNoMoreInteractions(orphanedDeviceDao);
		verifyNoMoreInteractions(removeUserSessionsByDeviceCmd);
	}
	
	@Test
	public void givenACompleteRequestWhenExecuteThenCreateSession() {
		//given
		DeviceRequest deviceRequest = new DeviceRequest();
		deviceRequest.setDeviceId(DEVICE_ID);
		deviceRequest.setDeviceType(DeviceType.ANDROID.getValue());
		deviceRequest.setUniqueDeviceId(UNIQUE_DEVICE_ID);
		
		SignInRequest request = new SignInRequest();
		request.setDevice(deviceRequest);
		
		RemoveUserSessionsByDeviceRequest removeRequest = new RemoveUserSessionsByDeviceRequest(UNIQUE_DEVICE_ID, 
				DEVICE_ID, DeviceType.ANDROID);
		
		when(tokenUtil.createSessionToken()).thenReturn(SESSION_TOKEN);
		when(removeUserSessionsByDeviceCmd.execute(removeRequest)).thenReturn(true);
				
		//when
		UserSession session = cmd.execute(request);
		
		//then
		assertNotNull(session);
		assertEquals(session.getToken(), SESSION_TOKEN);
		assertEquals(session.getUniqueDeviceId(), UNIQUE_DEVICE_ID);
		verify(orphanedDeviceDao).delete(UNIQUE_DEVICE_ID);
	}
}
