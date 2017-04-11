package com.ebay.park.service.item.schedule;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
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

import java.util.Calendar;
import java.util.Date;

/**
 * <p>NOTE: This bean is defined in <code>scheduled</code> profile since it will be ony needed in the culster's
 * instance selected to execute scheduled jobs</p>
 */
@Component
@Profile(ParkConstants.SCHEDULED_PROFILE)
public class MoveInactiveItemsJob {

	private static final Logger LOGGER = LoggerFactory.getLogger(MoveInactiveItemsJob.class);

	@Value("${scheduler.daysToItemExpire}")
	private int daysToItemExpire;

	@Value("${scheduler.expire.batch.size}")
	private int batchSize;
	
	@Value("${scheduler.daysToNotifyBeforeItemExpire}")
	private int dayToNotify;

	@Autowired
	ItemDao itemDao;

	@Autowired
	InactiveItemsHelper inactiveItemsHelper;

	@Transactional
	@Scheduled(cron = "${scheduler.expire.cron.setup}")
	public void execute() {
        DateTime expirationDate = DateTime.now().minusDays(daysToItemExpire);
        LOGGER.info("Expiring Items with last update older than {}", expirationDate);
        int numItemToExpire = 1;
		int totalItemExpired = 0;
		while (numItemToExpire > 0) {
			PageRequest pageReq = new PageRequest(0, batchSize);

			Page<Item> itemsToExpire = itemDao.listElementsToExpire(
					StatusDescription.ACTIVE, expirationDate.toDate(),
					pageReq);

			numItemToExpire = itemsToExpire.getNumberOfElements();
			totalItemExpired += numItemToExpire;

			if (numItemToExpire > 0) {
				for (Item item : itemsToExpire) {
					item.expired();
				}

				itemDao.save(itemsToExpire);
				
				for (Item item : itemsToExpire) {
					inactiveItemsHelper.notifyExpired(item);
				}
			}
		}

        LOGGER.info("Items moved to {}: {}", StatusDescription.EXPIRED, totalItemExpired);
		
		//Send Alerts of item near expiration
		notifyCloseToExpire();
	}
	
	
	private void notifyCloseToExpire() {
		DateTime nearExpirationDate = DateTime.now().minusDays(daysToItemExpire - dayToNotify);
        Calendar calendar = Calendar.getInstance();
		calendar.setTime(nearExpirationDate.toDate());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		Date lowerExpDate = calendar.getTime();
		calendar.add(Calendar.DATE, 1);
		Date upperExpDate = calendar.getTime();

        LOGGER.info("Notify close to expire with last update older than {}", nearExpirationDate);

		int numItemToNotify = 1;
		int totalItemNotified = 0;
		int page = 0;
		while (numItemToNotify > 0) {
			PageRequest pageReq = new PageRequest(page, batchSize);
            page += 1;
		
			Page<Item> itemsToExpire = itemDao.listElementsCloseToExpire(StatusDescription.ACTIVE, lowerExpDate,
					upperExpDate, pageReq);

			numItemToNotify = itemsToExpire.getNumberOfElements();
			totalItemNotified += numItemToNotify;

			if (numItemToNotify > 0) {
				for (Item item : itemsToExpire) {
					inactiveItemsHelper.notifyItemAboutToExpired(item);
				}
			}
		}

        LOGGER.info("Items close to expiration: {}", totalItemNotified);
	}
}
