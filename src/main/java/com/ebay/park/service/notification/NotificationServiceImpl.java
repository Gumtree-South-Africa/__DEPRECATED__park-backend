package com.ebay.park.service.notification;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ebay.park.db.dao.NotificationConfigDao;
import com.ebay.park.db.entity.NotificationConfig;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.notification.command.GetNotificationTemplateCmd;
import com.ebay.park.service.notification.command.UpdateNotificationConfigCmd;
import com.ebay.park.service.notification.dto.GetUserNotificationRequest;
import com.ebay.park.service.notification.dto.NotificationConfigRequest;
import com.ebay.park.service.notification.dto.NotificationTemplateRequest;
import com.ebay.park.service.notification.validator.NotTemplateReqValidator;
import com.ebay.park.service.notification.validator.NotificationRequestValidator;
import com.google.common.collect.Lists;

@Service("NotificationServiceOp")
public class NotificationServiceImpl implements NotificationService{

	private static final String NOT_TEMPLATES_CACHE = "notTemplatesCache";

	@Autowired
	private NotificationRequestValidator notificationReqValidator;
	
	@Autowired
	private UpdateNotificationConfigCmd updateNotConfigCmd;

	@Autowired
	private NotTemplateReqValidator notTemplateReqValidator;
	
	@Autowired
	private GetNotificationTemplateCmd getNotificationTemplateCmd; 

	@Autowired
	@Qualifier("getUserNotificationConfigurationCmd")
	private ServiceCommand<GetUserNotificationRequest, NotificationConfigRequest> getUserNotificationConfigurationV3;

	@Autowired
    @Qualifier("getUserNotificationConfigurationV4Cmd")
    private ServiceCommand<GetUserNotificationRequest, NotificationConfigRequest> getUserNotificationConfigurationV4;
	
	@Autowired
    @Qualifier("getUserNotificationConfigurationV5Cmd")
    private ServiceCommand<GetUserNotificationRequest, NotificationConfigRequest> getUserNotificationConfigurationV5;
	
	@Autowired
	private NotificationConfigDao notificationDao;
	
	@Override
	public Boolean updateNotificationsConfig(NotificationConfigRequest request){
		notificationReqValidator.validate(request);
		return updateNotConfigCmd.execute(request);
	}

	@Override
    @Cacheable(value = NOT_TEMPLATES_CACHE, key = "#action.concat(#type)")
	public String getTemplate(String action, String type) {
		NotificationTemplateRequest notTemplateReq = new NotificationTemplateRequest(type, action);
		notTemplateReqValidator.validate(notTemplateReq);
		return getNotificationTemplateCmd.execute(notTemplateReq);
	}

	@Override
	public List<NotificationConfig> getAllNotificationConfig() {
		return Lists.newArrayList(notificationDao.findAll());
	}


	@Override
	public NotificationConfigRequest getNotificationConfigV3(GetUserNotificationRequest request) {
		return getUserNotificationConfigurationV3.execute(request);
	}

	@Override
    public NotificationConfigRequest getNotificationConfigV4(GetUserNotificationRequest request) {
        return getUserNotificationConfigurationV4.execute(request);
    }
	
	@Override
    public NotificationConfigRequest getNotificationConfigV5(GetUserNotificationRequest request) {
        return getUserNotificationConfigurationV5.execute(request);
    }

	@Override
	public List<NotificationConfig> getAllApprovedNotificationConfig() {
		return Lists.newArrayList(notificationDao.findAllExceptEmailNofitications(NotificationType.EMAIL));
	}
	
	@Override
	public List<NotificationConfig> getEmailNotificationConfig() {
		return Lists.newArrayList(notificationDao.findAllEmailNotifications(NotificationType.EMAIL));
	}
	
}
