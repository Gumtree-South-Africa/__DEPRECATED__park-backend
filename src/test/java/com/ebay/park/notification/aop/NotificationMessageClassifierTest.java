package com.ebay.park.notification.aop;

import com.ebay.park.db.entity.NotificationConfig;
import com.ebay.park.db.entity.User;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NotificationAction.class, NotificationType.class})
public class NotificationMessageClassifierTest {

	public NotificationClassifier classifier;
	
	@Mock
	private NotificationAction action;
	
	@Mock
	private User user;
	
	@Mock
	private NotificationConfig config1;
	@Mock
	private NotificationConfig config2;
	@Mock
	private NotificationConfig config3;
	
	@Mock
	private NotificationAction actionDesc1;
	@Mock
	private NotificationAction actionDesc2;
	
	@Mock
	private NotificationType typeDesc1;
	@Mock
	private NotificationType typeDesc2;

	@Before
	public void setUp(){
		initMocks(this);
		classifier = new NotificationClassifier();
	}
	
	//@Test
	//@Ignore // FIXME see how to fix it
	public void test(){
		configNots(config1, actionDesc1, typeDesc1);
		configNots(config2, actionDesc2, typeDesc2);
		configNots(config3, actionDesc1, typeDesc2);
		
		List<NotificationConfig> configs = new ArrayList<NotificationConfig>();
		configs.add(config1);
		configs.add(config2);
		configs.add(config3);
		
		when(user.getNotificationConfigs()).thenReturn(configs);
		List<NotificationType> types = classifier.classify(actionDesc1, user);
		
		assertNotNull(types);
		assertEquals(2, types.size());
		assertEquals(typeDesc1, types.get(0));
		assertEquals(typeDesc2, types.get(1));
	}
	
	private void configNots(NotificationConfig config,
							NotificationAction action,
							NotificationType type){
		when(config.getNotificationAction()).thenReturn(action);
		when(config.getNotificationType()).thenReturn(type);
	
	}
}
