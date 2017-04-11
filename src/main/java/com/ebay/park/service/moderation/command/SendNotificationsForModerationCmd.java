package com.ebay.park.service.moderation.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.entity.DeviceType;
import com.ebay.park.feed.FeedBulkExecutor;
import com.ebay.park.push.AndroidPushBatchExecutor;
import com.ebay.park.push.IOSPushBatchExecutor;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.moderation.UserReceiverPush;
import com.ebay.park.service.moderation.dto.SendNotificationsForModerationRequest;
import com.ebay.park.service.moderation.dto.SendNotificationsForModerationResponse;
import com.ebay.park.util.GenericBuilder;

/**
 * CMD to send users notifications in bulk
 */
@Component
public class SendNotificationsForModerationCmd extends
		AbstractFiltererCmd<SendNotificationsForModerationRequest> {

	@Autowired
	private IOSPushBatchExecutor iOSPushExecutor;

	@Autowired
	private AndroidPushBatchExecutor androidPushExecutor;

	@Autowired
	private FeedBulkExecutor feedExecutor;

	private static Logger logger = LoggerFactory.getLogger(SendNotificationsForModerationCmd.class);

	@Override
    public SendNotificationsForModerationResponse execute(
			SendNotificationsForModerationRequest request) {

		try {
		    logger.info("Sending bulk push notifications");

			// Get Users
			List<UserReceiverPush> results = getFilteredUsers(request);
			setPushMessage(request.getMessage());

			//Feeds
            if (!results.isEmpty() && request.getOnlyPush() != null && !request.getOnlyPush()) {
                List<Long> userIds = results.stream()
                        .map(UserReceiverPush::getUserId)
                        .distinct()
                        .collect(Collectors.toCollection(ArrayList::new));
                feedExecutor.execute(userIds, request.getMessage());
            }

            //Pushes
            for (UserReceiverPush user : results) {
                if (user.getPlatform().getValue().equals(DeviceType.IOS.getValue())) {
                    iOSPushExecutor.addDevice(user.getDeviceId());
                }
                if (user.getPlatform().getValue().equals(DeviceType.ANDROID.getValue())) {
                    androidPushExecutor.addDevice(user.getDeviceId());
                }

                logger.info("{} - {} - {}", user.getUserId(), user.getPlatform(), user.getDeviceId());
            }

            flushPushes();

			SendNotificationsForModerationResponse response = GenericBuilder.of(
			        SendNotificationsForModerationResponse::new)
			        .with(SendNotificationsForModerationResponse::setReceivers, results.size())
			        .with(SendNotificationsForModerationResponse::setPushConfirmed,
			                iOSPushExecutor.getSuccessfulPushes()
		                    + androidPushExecutor.getSuccessfulPushes())
			        .build();

			clear();
			return response;
		} catch (Exception e)  {
			throw createServiceException(ServiceExceptionCode.ERROR_SENDING_PUSH_NOTIFICATIONS);
		}
	}

	/**
	 * Sets the message from the request.
	 * @param msg
	 *     the message to send
	 */
	private void setPushMessage(String msg) {
		iOSPushExecutor.setPushMessage(msg);
		androidPushExecutor.setPushMessage(msg);
	}

	/**
	 * Deletes values from previous executions.
	 */
	private void clear() {
        iOSPushExecutor.clear();
        androidPushExecutor.clear();
    }

	private void flushPushes() {
	    iOSPushExecutor.flushPush();
	    androidPushExecutor.flushPush();
	}
}