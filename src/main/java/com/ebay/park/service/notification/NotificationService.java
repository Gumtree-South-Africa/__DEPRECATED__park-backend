package com.ebay.park.service.notification;

import java.util.List;

import com.ebay.park.db.entity.NotificationConfig;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.service.notification.dto.GetUserNotificationRequest;
import com.ebay.park.service.notification.dto.NotificationConfigRequest;

public interface NotificationService {

	public Boolean updateNotificationsConfig(NotificationConfigRequest request);

	public String getTemplate(String action, String type);

	public List<NotificationConfig> getAllNotificationConfig();

	public NotificationConfigRequest getNotificationConfigV3(GetUserNotificationRequest request);

	/**
	 * Returns notifications until version 4 (it includes {@link NotificationAction} SOLD_AN_ITEM_FOR_INTERESTED_FOLLOWERS
	 * @param request
	 * @return the notification config list
	 */
	public NotificationConfigRequest getNotificationConfigV4(GetUserNotificationRequest request);
	
	/**
	 * Returns notifications until version 5 (it includes {@link NotificationAction} FB_FRIEND_USING_THE_APP
	 * @param request
	 * @return the notification config list
	 */
	public NotificationConfigRequest getNotificationConfigV5(GetUserNotificationRequest request);

	public List<NotificationConfig> getAllApprovedNotificationConfig();

	public List<NotificationConfig> getEmailNotificationConfig();

}
