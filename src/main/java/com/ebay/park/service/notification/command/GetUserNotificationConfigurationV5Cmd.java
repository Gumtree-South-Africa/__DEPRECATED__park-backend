package com.ebay.park.service.notification.command;

import org.springframework.stereotype.Component;

/**
 * Version 5.
 * Finds the notification configuration for the current user.
 * This configuration stores the user selection regarding he/she wants/doens't want to receive a notification type.
 * Feeds are  compulsory, but pushes are optional. 
 * @author scalderon
 * @since v2.0.4
*/
@Component
public class GetUserNotificationConfigurationV5Cmd extends GetUserNotificationConfigurationCmd {
	
    protected static final long CURRENT_VERSION = 5;

  //TODO Refactor this when adding full integration with the retrocompatibility solution
    @Override
    protected Long getCmdVersion() {
        return CURRENT_VERSION;
    }

}
