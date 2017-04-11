package com.ebay.park.service.item.schedule;

import com.ebay.park.util.ParkConstants;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>NOTE: This bean is defined in <code>scheduled</code> profile since it will be ony needed in the culster's
 * instance selected to execute scheduled jobs</p>
 */
@Component
@Profile(ParkConstants.SCHEDULED_PROFILE)
public class DeleteSoldOrExpiredItemsJob {

    private static Logger logger = LoggerFactory.getLogger(DeleteSoldOrExpiredItemsJob.class);

	@Value("${scheduler.daysToDeleteSoldItem}")
	private int daysToDeleteSoldItem;

	@Value("${scheduler.daysToDeleteExpiredItem}")
    private int daysToDeleteExpiredItem;

	@Autowired
	private DeleteSoldItemsExecutor deleteSoldItemsExecutor;

	@Autowired
	private DeleteExpiredItemsExecutor deleteExpiredItemsExecutor;

	@Scheduled(cron = "${scheduler.delete.cron.setup}")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void execute() {
	    logger.info("DeleteSoldOrExpiredItemsJob is about to start...");
		deleteSoldItemsExecutor.execute(DateTime.now().minusDays(daysToDeleteSoldItem));
		deleteExpiredItemsExecutor.execute(DateTime.now().minusDays(daysToDeleteExpiredItem));
        logger.info("DeleteSoldOrExpiredItemsJob is done!");
	}
}
