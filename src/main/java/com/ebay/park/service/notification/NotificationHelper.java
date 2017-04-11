/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.notification;


import com.ebay.park.db.dao.NotificationConfigDao;
import com.ebay.park.db.entity.NotificationConfig;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.service.ServiceExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * @author lucia.masola
 * 
 */
@Component
public class NotificationHelper {
	
	@Autowired
	private NotificationConfigDao notificationConfigDao;
	
	@Autowired
	private FeedServiceHelper feedServiceHelper;

	@Value("${email.template_location}")
	private String templateLocation;
	
	@Value("${email.subject_path}")
	private String emailSubjectPath;

	

	/**
	 * Return the template description for the given <code>type</code> and <code>action</action
	 * @param type a notification type
	 * @param action a notification action
	 * @return a single string representing the template message
	 */
	public String getTemplateMsg(NotificationType type, NotificationAction action) {
		NotificationConfig config = notificationConfigDao.findNotification(type, action);
		if (config == null || config.getTemplateName() == null) {
			throw createServiceException(ServiceExceptionCode.NOTIFICATION_TEMPLATE_NOT_FOUND);
		}
		return config.getTemplateName();
	}

	/**
	 * Returns the full email template for the given <code>type</code> and <code>action</action
	 * @param type a notification type
	 * @param action a notification action
	 * @return a single string representing the email template
	 */
	public String getEmailTemplate(NotificationType type, NotificationAction action) {
		StringBuilder path = new StringBuilder();
		path.append(templateLocation);
		path.append(this.getTemplateMsg(type, action));
		return path.toString();
	}

	
	/**
	 * Returns the full subject for the email for the given <code>type</code> and <code>action</action
	 * @param type a notification type
	 * @param action a notification action
	 * @return a single string representing the email subject
	 */
	public String getEmailSubject(NotificationType type, NotificationAction action){
		StringBuilder subjectPath = new StringBuilder();
		subjectPath.append(emailSubjectPath);
		subjectPath.append(getTemplateMsg(type, action));
		return subjectPath.toString();
	}	
	
	/**
	 * Returns the unread feeds counter.
	 * @param token the user token
	 * @return the unread feeds counter
	 */
	public String getBadge(String token) {
		return feedServiceHelper.countUnreadFeeds(token).toString();
	}

}
