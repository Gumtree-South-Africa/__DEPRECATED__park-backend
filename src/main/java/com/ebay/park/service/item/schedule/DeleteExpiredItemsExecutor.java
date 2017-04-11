package com.ebay.park.service.item.schedule;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ebay.park.db.entity.StatusDescription;

@Component
public class DeleteExpiredItemsExecutor extends DeleteItemsExecutor {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DeleteExpiredItemsExecutor.class);

    @Override
    public void execute(DateTime deletionDate) {
        LOGGER.info("Deleting Items with Expiration date older than {}", deletionDate);
        execute(deletionDate, StatusDescription.EXPIRED);
    }

}
