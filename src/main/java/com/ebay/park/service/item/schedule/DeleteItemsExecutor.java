package com.ebay.park.service.item.schedule;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;

public abstract class DeleteItemsExecutor {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DeleteItemsExecutor.class);

    @Value("${scheduler.delete.batch.size}")
    private int batchSize;

    @Autowired
    ItemDao itemDao;

    public void execute(DateTime deletionDate, StatusDescription status) {
        int numItemToDelete = 1;
        int totalItemDelete = 0;
        while (numItemToDelete > 0) {
            PageRequest pageReq = new PageRequest(0, batchSize);

            // @formatter:off
            Page<Item> itemsToDelete = itemDao.listElementsToDelete(             
                    status,
                    deletionDate.toDate(),
                    pageReq);       
            // @formatter:off
        
            numItemToDelete = itemsToDelete.getNumberOfElements();
            totalItemDelete += numItemToDelete;

            if (numItemToDelete > 0) {
                for (Item item: itemsToDelete){
                    item.delete();          
                }
                
                itemDao.save(itemsToDelete);
            }   
        }
        LOGGER.info("Items deleted: {}", totalItemDelete);
    }

    public abstract void execute(DateTime deletionDate);
}
