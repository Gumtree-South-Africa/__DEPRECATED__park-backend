package com.ebay.park.service.notification;

import com.ebay.park.db.dao.NotificationConfigDao;
import com.ebay.park.db.entity.NotificationConfig;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;



public class NotificationMessageHelperTest {


	@InjectMocks
	private NotificationHelper notHelper;
	

	@Mock
	private NotificationConfigDao notificationConfigDao;


	
	@Before
	public void setUp(){
		notHelper = new NotificationHelper();
		initMocks(this);
	}
	
	
	@Test
	public void getEmailTemplateTest(){
		
		Whitebox.setInternalState(notHelper, "templateLocation", "templateLocation");
		
		NotificationAction action = Mockito.mock(NotificationAction.class);
		NotificationType type = NotificationType.EMAIL;
		NotificationConfig config = Mockito.mock(NotificationConfig.class);
		when(notificationConfigDao.findNotification(type, action)).thenReturn(config);
		when(config.getTemplateName()).thenReturn("tempDescritpion");
		
		String emailTemplate = notHelper.getEmailTemplate(type, action);
		
		assertNotNull(emailTemplate);
		assertEquals("templateLocation" + "tempDescritpion", emailTemplate);
		
		verify(notificationConfigDao).findNotification(type, action);
		verify(config, Mockito.times(2)).getTemplateName();	
		
		
	}
	
	@Test
	public void getEmailSubjectTest(){
		
		Whitebox.setInternalState(notHelper, "emailSubjectPath", "emailSubjectPath");
		
		NotificationAction action = Mockito.mock(NotificationAction.class);
		NotificationType type = NotificationType.EMAIL;
		NotificationConfig config = Mockito.mock(NotificationConfig.class);
		when(notificationConfigDao.findNotification(type, action)).thenReturn(config);
		when(config.getTemplateName()).thenReturn("tempDescritpion");
		
		String emailSubject = notHelper.getEmailSubject(type, action);
		
		assertNotNull(emailSubject);
		assertEquals("emailSubjectPath" + "tempDescritpion", emailSubject);
		
		verify(notificationConfigDao).findNotification(type, action);
		verify(config, Mockito.times(2)).getTemplateName();	
		
	}


}
