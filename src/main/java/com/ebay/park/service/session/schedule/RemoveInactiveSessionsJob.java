package com.ebay.park.service.session.schedule;

import com.ebay.park.util.ParkConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ebay.park.service.user.command.RemoveInactiveSessionsCmd;
import com.ebay.park.service.user.dto.RemoveInactiveSessionsRequest;

/**
 * Job that deletes inactive web sessions.
 * <p>NOTE: This bean is defined in <code>scheduled</code> profile since it will be ony needed in the culster's
 * instance selected to execute scheduled jobs</p>
 *
 * @author Julieta Salvadó
 */
@Component
@Profile(ParkConstants.SCHEDULED_PROFILE)
public class RemoveInactiveSessionsJob {

    private static Logger logger = LoggerFactory.getLogger(RemoveInactiveSessionsJob.class);

	@Autowired
	private RemoveInactiveSessionsCmd removeInactiveSessionsCmd;
	
	@Transactional
	@Scheduled(cron = "${scheduler.deleteSessions.cron.setup}")
	public void execute() {
	    logger.info("RemoveInactiveSessionsJob is about to start...");
		removeInactiveSessionsCmd.execute(new RemoveInactiveSessionsRequest());
		logger.info("RemoveInactiveSessionsJob is done!");
	}
}
