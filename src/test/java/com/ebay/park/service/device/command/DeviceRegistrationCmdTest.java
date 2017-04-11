package com.ebay.park.service.device.command;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ebay.park.db.dao.OrphanedDeviceDao;
import com.ebay.park.db.dao.UserSessionDao;
import com.ebay.park.db.entity.DeviceType;
import com.ebay.park.db.entity.OrphanedDevice;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.device.dto.DeviceRegistrationRequest;

public class DeviceRegistrationCmdTest {

	private static final String DEVICE_ID = "deviceId";

	@InjectMocks
	private DeviceRegistrationCmd deviceRegistrationCmd = new DeviceRegistrationCmd();

	@Mock
	private UserSessionDao userSessionDao;

	@Mock
	private OrphanedDeviceDao orphanedDeviceDao;

	@Mock
	private DeviceRegistrationRequest request;

	@Before
	public void setUp() {
		initMocks(this);
	}

	@Test
	public void givenOrphaneDeviceThenDeviceRegistrationSuccess() {
		OrphanedDevice orphanedDevice = Mockito.mock(OrphanedDevice.class);
		when(orphanedDeviceDao.findByUniqueDeviceId(request.getUniqueDeviceId())).thenReturn(orphanedDevice);
		ServiceResponse response = deviceRegistrationCmd.execute(request);
		assertEquals(ServiceResponse.SUCCESS.getStatusCode(), response.getStatusCode());
	}

	@Test
	public void givenEmptyUserSessionsByUniqueDeviceIdThenDeviceRegistrationSuccess() {
		List<UserSession> existingSession = new ArrayList<UserSession>();
		when(orphanedDeviceDao.findByUniqueDeviceId(request.getUniqueDeviceId())).thenReturn(null);
		when(userSessionDao.findUserSessionsByUniqueDeviceId(request.getUniqueDeviceId())).thenReturn(existingSession);
		ServiceResponse response = deviceRegistrationCmd.execute(request);
		assertEquals(ServiceResponse.SUCCESS.getStatusCode(), response.getStatusCode());
	}

	@Test
	public void givenUserSessionsByUniqueDeviceIdThenDeviceRegistrationSuccess() {
		UserSession userSession = Mockito.mock(UserSession.class);
		// UserSession list by Unique Device Id.
		List<UserSession> existingSession = new ArrayList<UserSession>();
		existingSession.add(userSession);
		// UserSession list by Device Id.
		List<UserSession> userSessionByDevice = new ArrayList<UserSession>();
		when(request.getDeviceId()).thenReturn(DEVICE_ID);
		when(request.getDeviceType()).thenReturn(DeviceType.ANDROID);
		when(request.getNewInstall()).thenReturn(null);
		when(userSessionDao.findUserSessionsByDeviceId(request.getDeviceId(), request.getDeviceType()))
				.thenReturn(userSessionByDevice);
		when(orphanedDeviceDao.findByUniqueDeviceId(request.getUniqueDeviceId())).thenReturn(null);
		when(userSessionDao.findUserSessionsByUniqueDeviceId(request.getUniqueDeviceId())).thenReturn(existingSession);
		ServiceResponse response = deviceRegistrationCmd.execute(request);
		assertEquals(ServiceResponse.SUCCESS.getStatusCode(), response.getStatusCode());
	}

}
