package com.ebay.park.service.notification.schedule;

import com.ebay.park.db.dao.FeedDao;
import com.ebay.park.db.entity.Feed;
import com.ebay.park.util.ParkConstants;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>NOTE: This bean is defined in <code>scheduled</code> profile since it will be ony needed in the culster's
 * instance selected to execute scheduled jobs</p>
 */
@Component
@Profile(ParkConstants.SCHEDULED_PROFILE)
public class DeleteReadFeedsJob {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteReadFeedsJob.class);

	@Value("${scheduler.daysToDeleteFeeds}")
	private int daysToDeleteFeeds;

	@Value("${scheduler.deleteFeeds.batch.size}")
	private int batchSize;

	@Autowired
	private FeedDao feedDao;

	@Transactional
	@Scheduled(cron = "${scheduler.deleteFeeds.cron.setup}")
	public void execute() {
		DateTime expirationDate = DateTime.now().minusDays(daysToDeleteFeeds);
        LOGGER.info("Deleting feeds older than {}", expirationDate);
        int numFeedsToDelete = 1;
		int totalFeedsDeleted = 0;
		while (numFeedsToDelete > 0) {
			PageRequest pageReq = new PageRequest(0, batchSize);

			Page<Feed> feedsToDelete = feedDao.findByCreationDateBeforeAndRead(
					expirationDate.toDate(), true, pageReq);

			numFeedsToDelete = feedsToDelete.getNumberOfElements();
			totalFeedsDeleted += numFeedsToDelete;

			if (numFeedsToDelete > 0) {
				feedDao.delete(feedsToDelete);
			}
		}
        LOGGER.info("Feeds deleted: {}", totalFeedsDeleted);
	}
}
