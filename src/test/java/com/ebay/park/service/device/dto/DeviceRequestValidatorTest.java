package com.ebay.park.service.device.dto;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class DeviceRequestValidatorTest {
	
	private static final String VALID_DEVICE_ID = "123456";
	private static final String VALID_DEVICE_TYPE = "ios";
	private static final String INVALID_DEVICE_TYPE = "invalidDeviceType";
	
	@Mock
	private DeviceRequest deviceRequest;
	
	@InjectMocks
	private DeviceRequestValidator deviceRequestValidator;
	
	@Before
	public void setUp(){
		deviceRequestValidator = new DeviceRequestValidator();
		initMocks(this);
	}
	
	@Test
	public void testValidateHappyPath(){
		when(deviceRequest.getDeviceId()).thenReturn(VALID_DEVICE_ID);
		when(deviceRequest.getDeviceType()).thenReturn(VALID_DEVICE_TYPE);		
		
		deviceRequestValidator.validate(deviceRequest);
		
		verify(deviceRequest, times(2)).getDeviceId();
		verify(deviceRequest, times(2)).getDeviceType();
	}
	
	@Test
	public void testValidateHappyPath2(){
		when(deviceRequest.getDeviceId()).thenReturn(VALID_DEVICE_ID);
		when(deviceRequest.getDeviceType()).thenReturn(VALID_DEVICE_TYPE);		
		
		deviceRequestValidator.validate(deviceRequest);
		
		verify(deviceRequest, times(2)).getDeviceId();
		verify(deviceRequest, times(2)).getDeviceType();
	}
	
	@Test
	public void testValidateInvalidDeviceId(){
		
		when(deviceRequest.getDeviceId()).thenReturn(null);

		try{
			deviceRequestValidator.validate(deviceRequest);
		} catch(ServiceException se){
			assertEquals(ServiceExceptionCode.EMPTY_DEVICE_ID.getCode(), se.getCode());
		} finally{
			verify(deviceRequest, times(1)).getDeviceId();
		}
		
	}
	
	@Test
	public void testValidateInvalidDeviceType1(){
		
		when(deviceRequest.getDeviceId()).thenReturn(VALID_DEVICE_ID);
		when(deviceRequest.getDeviceType()).thenReturn(null);	

		try{
			deviceRequestValidator.validate(deviceRequest);
		} catch(ServiceException se){
			assertEquals(ServiceExceptionCode.EMPTY_DEVICE_TYPE.getCode(), se.getCode());
		} finally{
			verify(deviceRequest, times(2)).getDeviceId();
			verify(deviceRequest, times(1)).getDeviceType();
		}
		
	}
	
	@Test
	public void testValidateInvalidDeviceType2(){
		
		when(deviceRequest.getDeviceId()).thenReturn(VALID_DEVICE_ID);
		when(deviceRequest.getDeviceType()).thenReturn(INVALID_DEVICE_TYPE);	

		try{
			deviceRequestValidator.validate(deviceRequest);
		} catch(ServiceException se){
			assertEquals(ServiceExceptionCode.INVALID_DEVICE_TYPE.getCode(), se.getCode());
		} finally{
			verify(deviceRequest, times(2)).getDeviceId();
			verify(deviceRequest, times(2)).getDeviceType();
		}
		
	}
	
}
