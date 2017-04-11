package com.ebay.park.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import com.ebay.park.db.dao.DeviceDao;
import com.ebay.park.db.entity.Device;

public class UserUtilTest {
    private static final long USER_ID = 1l;
    private static final boolean SIGNED_IN = false;

    @InjectMocks
    @Spy
    private UserUtils utils;

    @Mock
    private DeviceDao deviceDao;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void givenValidValuesWhenFindingDevicesThenFindThem() {
        Device d1 = mock(Device.class), d2 = mock(Device.class); 
        List<Device> deviceList = Arrays.asList(d1, d2);
        when(deviceDao.findDevicesByUser(USER_ID, SIGNED_IN)).thenReturn(deviceList);

        assertEquals(utils.findDevices(USER_ID, SIGNED_IN), deviceList);
    }
}
