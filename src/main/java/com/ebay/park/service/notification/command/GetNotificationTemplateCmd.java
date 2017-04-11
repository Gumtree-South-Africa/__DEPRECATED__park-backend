package com.ebay.park.service.notification.command;

import com.ebay.park.db.dao.NotificationConfigDao;
import com.ebay.park.db.entity.NotificationConfig;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.notification.dto.NotificationTemplateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;
@Component
public class GetNotificationTemplateCmd implements ServiceCommand<NotificationTemplateRequest, String>{
	
	@Autowired
	private NotificationConfigDao notificationConfigDao;
	
	@Override
	public String execute(NotificationTemplateRequest request) throws ServiceException {
		
		NotificationConfig config = notificationConfigDao.findNotification(request.getNotificationType(), request.getNotificationAction());
		
		if (config == null || config.getTemplateName() == null) {
			throw createServiceException(ServiceExceptionCode.NOTIFICATION_TEMPLATE_NOT_FOUND);
		}
		
		return config.getTemplateName();

	}

}
