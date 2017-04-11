package com.ebay.park.push;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotifications;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Class to send massive IOS Push Messages
 * 
 * @author gabriel.sideri & Julieta Salvadó
 */
@Component
@Scope("prototype")
public class IOSPushBatchExecutor extends PushBatchExecutor {

	private static final Logger logger = LogManager.getLogger(IOSPushBatchExecutor.class);
	
	private static final boolean PRODUCTION = true;

	private String apnsCertificateLocation;
	
	private String message;

	private static final String SOUND = "push.aiff";

    @Value("${push_notifications.ios_cert_location}")
    private String iosCertificate;

    @Value("${push_notifications.ios_cert_password}")
    private String iosPassword;

	public IOSPushBatchExecutor(@Value("${push_notifications.ios_max_devices_to_send_per_request}") Integer maxDevicesToSend) {
		this.maxDevicesToSend = maxDevicesToSend;
	}

	@PostConstruct
	public void initialize() {
		this.apnsCertificateLocation = System.getProperty("apns.certificate", iosCertificate);
	}

	@Override
    public void setPushMessage(String message){
		this.message = message;
	}
	
	/**
	 * Sends IOS Push Notifications sending the Devices Ids.
	 */
	@Override
    protected void sendPushNotification() {
		try {
			//Send pushes to iOS
			PushNotificationPayload payload = generatePushNotificationPayload(message);
			PushedNotifications status = Push.payload(payload, apnsCertificateLocation, iosPassword, PRODUCTION, this.devicesIds);
			//Count successful sent pushes
			successfulPushes = status.getSuccessfulNotifications().size();

		}  catch (CommunicationException e) {
			logger.error("Communication exception when trying to send an ios notification.", e);
		} catch (KeystoreException e) {
			logger.error("Keystore exception when trying to send an ios notification.", e);
		} catch (JSONException e) {
			logger.error("Exception when building the payload before trying to send an ios notification.", e);
		} catch (Exception e) {
			logger.error("Unknown exception when trying to send an ios notification.", e);
		}
	}

	private PushNotificationPayload generatePushNotificationPayload(String message) throws JSONException {
		PushNotificationPayload payload = new PushNotificationPayload();
		payload.addAlert(message);
		payload.addSound(SOUND);
		return payload;
	}
}
