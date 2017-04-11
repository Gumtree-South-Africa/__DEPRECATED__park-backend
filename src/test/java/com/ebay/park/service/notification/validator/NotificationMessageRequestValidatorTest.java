package com.ebay.park.service.notification.validator;

import com.ebay.park.notification.NotificationAction;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.notification.dto.ActionConfigurationDTO;
import com.ebay.park.service.notification.dto.ConfigurationValue;
import com.ebay.park.service.notification.dto.NotificationConfigRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;

public class NotificationMessageRequestValidatorTest {

	private static final String USERNAME = "johnThomas";

	@InjectMocks
	private NotificationRequestValidator notificationReqValidator;
	
	@Mock
	private NotificationConfigRequest notificationReq;
	
	private Map<String,Boolean> properties;

	
	@Before
	public void setUp(){	
		notificationReqValidator = new NotificationRequestValidator();
		initMocks(this);
		properties = new HashMap<String,Boolean>();
	}
	
	@Test
	public void validateSuccessfulTest() {
		Map<String, SortedMap<NotificationAction, ActionConfigurationDTO>> userConfigurationPerGroup = new HashMap<String, SortedMap<NotificationAction, ActionConfigurationDTO>>();
		SortedMap<NotificationAction, ActionConfigurationDTO> map = new TreeMap<NotificationAction, ActionConfigurationDTO>();
		map.put(NotificationAction.DELETE_AN_ITEM, new ActionConfigurationDTO("Name",
				NotificationAction.DELETE_AN_ITEM, ConfigurationValue.FALSE, ConfigurationValue.DISABLED));
		userConfigurationPerGroup.put("group", map);

		Mockito.when(notificationReq.getUsername()).thenReturn(USERNAME);
		properties.put(NotificationAction.NEW_ITEM.toValue(), true);
		Mockito.when(notificationReq.getUserConfigurationPerGroup()).thenReturn(userConfigurationPerGroup);

		notificationReqValidator.validate(notificationReq);

		Mockito.verify(notificationReq, Mockito.times(1)).getUsername();
		Mockito.verify(notificationReq, Mockito.times(1)).getUserConfigurationPerGroup();

	}
	
	@Test
	public void validateInvalidaReqTest1(){
		try {

			notificationReqValidator.validate(null);
			fail("a service exception was expected. ");

		} catch (ServiceException re) {

			assertEquals(re.getCode(), ServiceExceptionCode.INVALID_NOT_REQ.getCode());

		}
	}
	
	@Test
	public void validateInvalidaReqTest2(){
		try {

			notificationReqValidator.validate(new NotificationConfigRequest());
			fail("a service exception was expected. ");

		} catch (ServiceException re) {

			assertEquals(re.getCode(), ServiceExceptionCode.EMPTY_USERNAME.getCode());

		}
	}
	
	
	@Test
	public void validateInvalidaReqTest4(){

		Mockito.when(notificationReq.getUsername()).thenReturn(USERNAME);
		Mockito.when(notificationReq.getUserConfigurationPerGroup()).thenReturn(null);
		
		try {

			notificationReqValidator.validate(notificationReq);
			fail("a service exception was expected. ");

		} catch (ServiceException re) {

			assertEquals(re.getCode(), ServiceExceptionCode.INVALID_NOTIFICATION_PROPERTIES.getCode());
			Mockito.verify(notificationReq, Mockito.times(1)).getUsername();
			Mockito.verify(notificationReq, Mockito.times(1)).getUserConfigurationPerGroup();

		}
	}
	
	
}
