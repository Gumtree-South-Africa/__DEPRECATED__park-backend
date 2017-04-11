package com.ebay.park.push;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

import org.apache.commons.lang.Validate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Class to send massive Android Push Messages
 * 
 * @author gabriel.sideri
 */
@Component
public class AndroidPushBatchExecutor extends PushBatchExecutor {

	private static final Logger logger = LogManager.getLogger(AndroidPushBatchExecutor.class);

	private static final String MESSAGE_KEY = "message";
	
	private static final int TEN_MINS = 600;
	
	private static final boolean NO = false;

	private Message messageToSend;

	@Autowired
	private Sender sender;
	
	public AndroidPushBatchExecutor(@Value("${push_notifications.ios_max_devices_to_send_per_request}") Integer maxDevicesToSend){
		this.maxDevicesToSend = maxDevicesToSend;
	}

	@Override
    public void setPushMessage(String message){
		try {
			this.messageToSend = new Message.Builder().timeToLive(TEN_MINS)
					.delayWhileIdle(NO).addData(MESSAGE_KEY, URLEncoder.encode(message, "UTF-8")).build();
		} catch (UnsupportedEncodingException e) {
			logger.error("An exception occurred when trying to encode notifications for android devices", e);
		}
	}
	
	/**
	 * Sends Android Push Notifications to the Devices Ids.
	 */
	@Override
    protected void sendPushNotification(){
		Validate.notNull(messageToSend, "Please set the message to send first!");

		try {
            logger.debug("Message to send: {}, Devices Ids: {}", messageToSend, this.devicesIds.toString());

			MulticastResult status = sender.send(messageToSend, this.devicesIds, 1);
			// Count successful sent pushes
            logger.debug("Devices to send count: {}", this.devicesIds.size());
			successfulPushes = status.getSuccess();
            logger.debug("Total - success + failure = {}", status.getTotal());

		} catch (Exception e) {
			logger.error("An exception occurred when trying to push batch notifications to android devices", e);
		}
	}
}
