/*
 * Copyright eBay, 2014
 */
package com.ebay.park.push;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushNotificationPayload;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author lucia.masola
 * 
 */
@Component
public class IOSNotificationPusher extends NotificationPusher {

	private static final boolean PRODUCTION = true;
	private static final String SOUND = "push.aiff";

	private static Logger logger = LoggerFactory.getLogger(IOSNotificationPusher.class);

	@Value("${push_notifications.ios_cert_location}")
	private String iosCertificate;

	@Value("${push_notifications.ios_cert_password}")
	private String iosPassword;

	@Override
	public void push(PushNotification notification) {
	
		try {
			String deviceId = notification.getDeviceId();
			int badge = notification.getBadge();
			String message = createMessage(notification);
			logger.info("IOS notification push deviceId:{} template:{}  messageToSend:{}", deviceId,
					notification.getTemplateMessage(), message);
			
			String apnsCertificateLocation = System.getProperty("apns.certificate", iosCertificate);

			PushNotificationPayload payload = generatePushNotificationPayload(message, badge);

			Push.payload(payload, apnsCertificateLocation, iosPassword, PRODUCTION, deviceId);
			
		} catch (CommunicationException e) {
			logger.error("communication exception when trying to send an ios notification.", e);
		} catch (KeystoreException e) {
			logger.error("keystore exception when trying to send an ios notification.", e);
		} catch (Exception e) {
			logger.error("Unknown exception when trying to send an ios notification.", e);
		}
	}

	protected PushNotificationPayload generatePushNotificationPayload(String message, int badge) throws JSONException {
		PushNotificationPayload payload = new PushNotificationPayload();
		payload.addAlert(message);
		payload.addSound(SOUND);
		payload.addBadge(badge);
		return payload;
	}
}
