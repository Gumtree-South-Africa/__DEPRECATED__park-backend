/*
 * Copyright eBay, 2014
 */
package com.ebay.park.notification.factory;

import java.util.List;

import com.ebay.park.db.entity.Device;
import com.ebay.park.db.entity.User;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.notification.dto.NotifiableServiceResult;
import com.ebay.park.service.device.dto.DeviceDTO;
import com.ebay.park.service.notification.NotificationHelper;
import com.ebay.park.service.social.SocialService;
import com.ebay.park.service.user.finder.UserInfoUtil;
import com.ebay.park.util.Identifiable;

/**
 * @author jpizarro
 * 
 */
public class NotificationContext {

	private NotificationType type;
	private NotificationAction action;
	private NotifiableServiceResult result;
	private UserInfoUtil userInfoUtil;
	private User userRecipient;
	private NotificationHelper notificationHelper;
	private SocialService socialService;
	private DeviceDTO device;
	private long badge;


	public NotificationContext(NotifiableServiceResult result,
			NotificationAction action, UserInfoUtil userInfoUtil,
			NotificationHelper notificationHelper,
			SocialService socialService) {
		this.action = action;
		this.result = result;
		this.userInfoUtil = userInfoUtil;
		this.notificationHelper = notificationHelper;
		this.socialService = socialService;
	}

	public NotificationAction getNotificationAction() {
		return action;
	}

	public NotificationType getNotificationType() {
		return type;
	}

	public void setNotificationType(NotificationType notifType) {
		this.type = notifType;
	}

	public NotifiableServiceResult getNotifiableResult() {
		return result;
	}

	public User findUser(Identifiable<String> identifiable) {
		return userInfoUtil.findUser(identifiable);
	}

	public User findUserById(Long id) {
		return userInfoUtil.findUserById(id);
	}
	
	public List<User> findUsersById(List <Long> userIds) {
		return userInfoUtil.findUsersByIds(userIds);
	}
	
	/**
	 * It finds the devices linked to a user.
	 * @param userId
	 * 		user to find devices linked to
	 * @param signedIn
	 * 		true for signed-in devices; otherwise, false.
	 * @return
	 * 		list of devices
	 */
	public List<Device> findDevicesByUser(Long userId, boolean signedIn) {
		return userInfoUtil.findDevicesByUser(userId, signedIn);
	}

	public void setRecipient(DeviceDTO deviceDTO, User recipient) {
		this.setUserRecipient(recipient);
		this.setDevice(deviceDTO);
	}
	
	public void setRecipient(User recipient) {
		this.setUserRecipient(recipient);
	}

	/**
	 * Return the template description
	 * 
	 * @return a single string representing the template message
	 */
	public String getTemplateMsg() {
		return notificationHelper.getTemplateMsg(type, action);
	}

	/**
	 * Returns the full email template
	 * 
	 * @return a single string representing the email template
	 */
	public String getEmailTemplate() {
		return notificationHelper.getEmailTemplate(type, action);
	}

	/**
	 * Returns the full email subject
	 * 
	 * @return a single string representing the email subject
	 */
	public String getEmailSubject() {
		return notificationHelper.getEmailSubject(type, action);
	}

	public SocialService getSocialService() {
		return socialService;
	}

	public void setSocialService(SocialService socialService) {
		this.socialService = socialService;
	}

	public DeviceDTO getDevice() {
		return device;
	}

	public void setDevice(DeviceDTO deviceDTO) {
		this.device = deviceDTO;
	}

	public User getUserRecipient() {
		return userRecipient;
	}

	public void setUserRecipient(User userRecipient) {
		this.userRecipient = userRecipient;
	}

	public void setBadge(long badge) {
		this.badge = badge;
	}

	public String getBadge() {
		return Long.toString(badge);
	}

}
