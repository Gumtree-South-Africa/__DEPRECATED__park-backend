package com.ebay.park.service.notification.command;

import com.ebay.park.db.dao.NotificationConfigDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.NotificationConfig;
import com.ebay.park.db.entity.User;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.notification.dto.ActionConfigurationDTO;
import com.ebay.park.service.notification.dto.ConfigurationValue;
import com.ebay.park.service.notification.dto.NotificationConfigRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Map.Entry;

import static com.ebay.park.service.ServiceException.createServiceException;
@Component
public class UpdateNotificationConfigCmd implements ServiceCommand<NotificationConfigRequest, Boolean>{

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private NotificationConfigDao notificationDao;
	
	@Override
	public Boolean execute(NotificationConfigRequest request) throws ServiceException {

		User user = userDao.findByUsername(request.getUsername());
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}
		
		for (Map<NotificationAction,ActionConfigurationDTO> actionsPerGroup : request.getUserConfigurationPerGroup().values()) {
			for ( Entry<NotificationAction, ActionConfigurationDTO> mapConfig : actionsPerGroup.entrySet()) {
				ActionConfigurationDTO actionConfig = mapConfig.getValue();
				NotificationAction action = mapConfig.getKey();
				if (!ConfigurationValue.DISABLED.equals(actionConfig.getEmailConfig())) {
					updateNotificationProperty(user, action, actionConfig
							.getEmailConfig().getChoosen(), NotificationType.EMAIL);
				}
				if (!ConfigurationValue.DISABLED.equals(actionConfig.getPushConfig())) {
					updateNotificationProperty(user, action, actionConfig
							.getPushConfig().getChoosen(), NotificationType.PUSH);
				}
			}
		}

		userDao.save(user);
		
		return true;
	}


	protected void updateNotificationProperty(User user, NotificationAction action, Boolean value, NotificationType type) {

		NotificationConfig notification = user.getNotificationConfig(action, type);

		if (notification != null && !value) { //Disable notification
			user.getNotificationConfigs().remove(notification);

		} else if (notification == null && value) { //Enable notification
			NotificationConfig notToAdd = notificationDao.findNotification(type, action);
			if (notToAdd != null) {
				user.getNotificationConfigs().add(notToAdd);
			}
		}

	}

	
}
