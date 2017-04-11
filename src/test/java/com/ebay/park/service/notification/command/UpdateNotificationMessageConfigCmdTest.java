package com.ebay.park.service.notification.command;

import com.ebay.park.db.dao.NotificationConfigDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.NotificationConfig;
import com.ebay.park.db.entity.User;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
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

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class UpdateNotificationMessageConfigCmdTest {

	private static final String USERNAME = "thomasjhon";

	@InjectMocks
	private UpdateNotificationConfigCmd updateNotConfigCmd;
	
	@Mock
	private UserDao userDao;
	
	@Mock
	private NotificationConfigDao notificationDao;
	
	@Mock
	private NotificationConfigRequest request;
	
	
	@Mock
	private NotificationConfig not;


	@Mock
	private NotificationConfig not1;
	

	@Mock
	private NotificationConfig not2;
	
	private User user;
	
	@Before
	public void setUp() {
		updateNotConfigCmd = new UpdateNotificationConfigCmd();
		initMocks(this);
	
		user = new User();
		user.setUsername(USERNAME);
		user.setNotificationConfigs(createNotificationList());
	}
	
	@Test
	public void executeAddNotificationTest(){

		Mockito.when(request.getUsername()).thenReturn(USERNAME);
		Mockito.when(userDao.findByUsername(USERNAME)).thenReturn(user);
		
		Map<String, SortedMap<NotificationAction, ActionConfigurationDTO>> userConfigurationPerGroup = new HashMap<String, SortedMap<NotificationAction, ActionConfigurationDTO>>();
		SortedMap<NotificationAction, ActionConfigurationDTO> map = new TreeMap<NotificationAction, ActionConfigurationDTO>();
		userConfigurationPerGroup.put("group", map);

		Mockito.when(request.getUserConfigurationPerGroup()).thenReturn(userConfigurationPerGroup);
	
		Boolean response = updateNotConfigCmd.execute(request);
		
		verifySuccessResponse(response, 3);

		verifyCommonCalls();
		Mockito.verify(userDao, Mockito.times(1)).save(Mockito.isA(User.class));
		
	}
	
	@Test
	public void executeDoNothing1Test(){

		Mockito.when(request.getUsername()).thenReturn(USERNAME);
		Mockito.when(userDao.findByUsername(USERNAME)).thenReturn(user);
		
		Map<String, SortedMap<NotificationAction, ActionConfigurationDTO>> userConfigurationPerGroup = new HashMap<String, SortedMap<NotificationAction, ActionConfigurationDTO>>();
		SortedMap<NotificationAction, ActionConfigurationDTO> map = new TreeMap<NotificationAction, ActionConfigurationDTO>();
		map.put(NotificationAction.CHAT_SENT, new ActionConfigurationDTO("Name", NotificationAction.CHAT_SENT,
				ConfigurationValue.TRUE, ConfigurationValue.DISABLED));
		
		userConfigurationPerGroup.put("group", map);
		Mockito.when(request.getUserConfigurationPerGroup()).thenReturn(userConfigurationPerGroup);
				
		Boolean response = updateNotConfigCmd.execute(request);
		
		verifySuccessResponse(response, 3);
		
		verifyNotifications(1, 1, 1);
		verifyCommonCalls();
		Mockito.verify(userDao, Mockito.times(1)).save(Mockito.isA(User.class));
		
	}
	
	@Test
	public void executeDoNothing2Test(){

		Mockito.when(request.getUsername()).thenReturn(USERNAME);
		Mockito.when(userDao.findByUsername(USERNAME)).thenReturn(user);
		
		Map<String, SortedMap<NotificationAction, ActionConfigurationDTO>> userConfigurationPerGroup = new HashMap<String, SortedMap<NotificationAction, ActionConfigurationDTO>>();
		SortedMap<NotificationAction, ActionConfigurationDTO> map = new TreeMap<NotificationAction, ActionConfigurationDTO>();
		map.put(NotificationAction.NEW_ITEM, new ActionConfigurationDTO("Name", NotificationAction.NEW_ITEM,
				ConfigurationValue.FALSE, ConfigurationValue.DISABLED));
		userConfigurationPerGroup.put("group", map);
		Mockito.when(request.getUserConfigurationPerGroup()).thenReturn(userConfigurationPerGroup);
		
		Boolean response = updateNotConfigCmd.execute(request);
		
		verifySuccessResponse(response, 3);

		verifyNotifications(1, 1, 1);
		verifyCommonCalls();
		Mockito.verify(userDao, Mockito.times(1)).save(Mockito.isA(User.class));
		
	}
	
	@Test
	public void executeRemoveNotTest(){

		Mockito.when(request.getUsername()).thenReturn(USERNAME);
		Mockito.when(userDao.findByUsername(USERNAME)).thenReturn(user);
		
		Map<String, SortedMap<NotificationAction, ActionConfigurationDTO>> userConfigurationPerGroup = new HashMap<String, SortedMap<NotificationAction, ActionConfigurationDTO>>();
		SortedMap<NotificationAction, ActionConfigurationDTO> map = new TreeMap<NotificationAction, ActionConfigurationDTO>();
		map.put(NotificationAction.DELETE_AN_ITEM, new ActionConfigurationDTO("Name", NotificationAction.DELETE_AN_ITEM,
				ConfigurationValue.FALSE, ConfigurationValue.DISABLED));
		userConfigurationPerGroup.put("group", map);
		Mockito.when(request.getUserConfigurationPerGroup()).thenReturn(userConfigurationPerGroup);
		
		Boolean response = updateNotConfigCmd.execute(request);
		
		verifySuccessResponse(response, 2);
		verifyNotifications(1, 0, 0);
		verifyCommonCalls();
		Mockito.verify(userDao, Mockito.times(1)).save(Mockito.isA(User.class));
		
	}
	
	
	@Test
	public void executeEmptyUserTest(){
		Mockito.when(request.getUsername()).thenReturn(USERNAME);
		Mockito.when(userDao.findByUsername(USERNAME)).thenReturn(null);
		Boolean response = null;
		try{
			response = updateNotConfigCmd.execute(request);
			fail("A service exception was expected. ");
		} catch(ServiceException se){
			assertNull(response);
			assertEquals(ServiceExceptionCode.USER_NOT_FOUND.getCode(), se.getCode());
			Mockito.verify(request, Mockito.times(1)).getUsername();
			Mockito.verify(userDao, Mockito.times(1)).findByUsername(USERNAME);
		}

	}
	
	
	private List<NotificationConfig> createNotificationList(){
		
		createNotification(not, NotificationAction.DELETE_AN_ITEM, NotificationType.EMAIL);
		createNotification(not1, NotificationAction.ITEM_BANNED, NotificationType.PUSH);
		createNotification(not2, NotificationAction.CHAT_SENT, NotificationType.EMAIL);
				
		List<NotificationConfig> notpushes = new ArrayList<NotificationConfig>();
		notpushes.add(not);
		notpushes.add(not1);
		notpushes.add(not2);
		return notpushes;
	}
	
	private void createNotification(NotificationConfig not, 
									NotificationAction action,
									NotificationType type){
		
		Mockito.when(not.getNotificationAction()).thenReturn(action);
		Mockito.when(not.getNotificationType()).thenReturn(type);
		
	}
	
	private void verifySuccessResponse(Boolean response, int notSize){
		assertTrue(response);
		assertNotNull(user.getNotificationConfigs());
		assertEquals(notSize, user.getNotificationConfigs().size());
	}
	
	private void verifyCommonCalls(){
		Mockito.verify(request, Mockito.times(1)).getUsername();
		Mockito.verify(userDao, Mockito.times(1)).findByUsername(USERNAME);
	}
	
	private void verifyNotifications(int notTimes, int not1Times, int not2Times){
		verifyNotification(notTimes, not, NotificationType.EMAIL, NotificationAction.DELETE_AN_ITEM);
		verifyNotification(not1Times, not1, NotificationType.PUSH, NotificationAction.ITEM_BANNED);
		verifyNotification(not2Times, not2, NotificationType.EMAIL, NotificationAction.CHAT_SENT);
	}
	
	private void verifyNotification(int times, NotificationConfig not, 
									NotificationType notType,
									NotificationAction notAct){
		Mockito.verify(not, Mockito.times(times)).getNotificationAction();
		Mockito.verify(not, Mockito.times(times)).getNotificationType();
		
	}
	
	
}
