package com.ebay.park.service.notification.command;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ebay.park.db.dao.NotificationConfigDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Idiom;
import com.ebay.park.db.entity.NotificationConfig;
import com.ebay.park.db.entity.User;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.service.notification.dto.GetUserNotificationRequest;
import com.ebay.park.service.notification.dto.NotificationConfigRequest;
import com.ebay.park.util.InternationalizationUtil;

public class GetUserNotificationMessageConfigurationTest {

	private static final String LANG = "es";

	@InjectMocks
	private GetUserNotificationConfigurationCmd getUserNotificationConfiguration = new GetUserNotificationConfigurationCmd();

	@Mock
	private UserDao userDao;

	@Mock
	private NotificationConfigDao notificationConfigDao;

	@Mock
	private InternationalizationUtil i18nUtil;

	@Mock
	private GetUserNotificationRequest request;

	@Mock
	private User user;

	private Iterable<NotificationConfig> notificationConfig;

	private List<NotificationConfig> list;

	@Before
	public void setUp() {
		initMocks(this);
		list = new ArrayList<NotificationConfig>();
		NotificationConfig notification = new NotificationConfig(NotificationAction.CHAT_SENT, NotificationType.EMAIL);
		NotificationConfig second_notification = new NotificationConfig(NotificationAction.CHAT_SENT,
				NotificationType.PUSH);
		list.add(notification);
		list.add(second_notification);
		notificationConfig = list;
		when(userDao.findByUsername(request.getUsername())).thenReturn(user);
		when(notificationConfigDao.findAll()).thenReturn(notificationConfig);
		when(user.getNotificationConfigs()).thenReturn(list);
	}

	@Test
	public void givenExistingUserWithLanguageThenGetUserNotificationConfigurationSuccess() {
		when(request.getLanguage()).thenReturn(LANG);
		NotificationConfigRequest response = getUserNotificationConfiguration.execute(request);
		assertNotNull(response);
	}

	@Test
	public void givenExistingUserWithNullLanguageThenGetUserNotificationConfigurationSuccess() {
		Idiom idiom = Mockito.mock(Idiom.class);
		idiom.setCode(LANG);
		when(request.getLanguage()).thenReturn(null);
		when(user.getIdiom()).thenReturn(idiom);
		NotificationConfigRequest response = getUserNotificationConfiguration.execute(request);
		assertNotNull(response);
	}

}
