package com.ebay.park.service.user.schedule;

import com.ebay.park.db.dao.UserSessionDao;
import com.ebay.park.db.entity.DeviceType;
import com.ebay.park.push.swrve.SwrvePusher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * This is the job to engage users that has items with no contact X days after published by swrve
 *
 * @author gervasio.amy
 * @since 20/12/2016.
 */
//@Component
public class UserEngagerJob {

    private static Logger logger = LoggerFactory.getLogger(UserEngagerJob.class);

    private static final String ENGAGE_NOCONTACT_MESSAGE = "engage.userWithNoContact.message";

    @Autowired
    private UserSessionDao userSessionDao;

    @Autowired
    private SwrvePusher swrvePusher;

    @Value("${scheduler.daysWithNoContactToEngage}")
    private int daysWithNoContactToEngage;

    @Value("${swrve.apikey.android}")
    private String swrveApikeyAndroid;

    @Value("${swrve.apikey.ios}")
    private String swrveApikeyIos;


    //@Scheduled(cron = "${scheduler.sendPushToEngageUsersWithNoContact.cron.setup}")
    public void executeJob() {
        logger.info("Ad abandonment job is about to start. It will consider items with no contact in last {} days", daysWithNoContactToEngage);
        this.sendPushToUsers(daysWithNoContactToEngage);
        logger.info("Ad abandonment job ended.");
    }


    /**
     * The core method. It calls {@link UserSessionDao#findSwrveIdsWithItemsWithoutContact(int)} to get the users to be notified.
     * It builds decides to which platform the push shold go and then tells swrve to send the push to each device
     *
     * @param days how many days without contact?
     * @see SwrvePusher#sendPush(String, String)
     */
    public void sendPushToUsers(int days) {
        List<Object[]> users = userSessionDao.findSwrveIdsWithItemsWithoutContact(days);
        logger.info("Swrve Ad abandonment push will be sent to {} users", users.size());
        for (Object[] user : users) {
            /*
            result[0] -> use_ses_swrve_id
            result[1] -> ite_publisher_id
            result[2] -> dev_platform
             */
            try {
                logger.info("User processed: {}", Arrays.toString(user));
                String swrveUserId = (String) user[0];
                DeviceType deviceType = DeviceType.getDeviceByValue((String) user[2]);
                switch (deviceType) {
                    case ANDROID:
                        swrvePusher.sendPush(swrveApikeyAndroid, swrveUserId);
                        break;
                    case IOS:
                        swrvePusher.sendPush(swrveApikeyIos, swrveUserId);
                        break;
                }
            } catch (IllegalArgumentException e) {
                // no need to abort, just log it and keep going...
                logger.warn("Invalid device type {} while trying to send push to engage users - Context: {}", user[2], Arrays.toString(user));
            } catch (Exception e) {
                // no need to abort, just log it and keep going...
                logger.warn("Unexpected exception while trying to send push to engage users - Context: {}", Arrays.toString(user), e);
            }
        }
    }
}