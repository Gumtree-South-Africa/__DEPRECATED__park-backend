package com.ebay.park.service.item.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.ItemUtils;

/**
 * Command to support republish a sold, expired or active item
 * @see <a href=https://jira.globant.com/browse/EPA001-10504 />
 * @author scalderon
 *
 */
@Component
public class RepublishItemV4Cmd extends RepublishItemCmd {
	
	@Autowired
	private ItemUtils itemUtils;
	
	/**
	 * Republishes an item if the status is SOLD, EXPIRED
	 * or ACTIVE.
	 * Limit amount of republish = 1 every 7 days
	 * @param item
	 *          the item to republish
	 */
	@Override
	protected void republish(Item item) {
		if (!(StatusDescription.SOLD.equals(item.getStatus()) || 
				StatusDescription.ACTIVE.equals(item.getStatus()) ||
				StatusDescription.EXPIRED.equals(item.getStatus()))) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_SOLD_EXPIRED_OR_ACTIVE_ERROR);
		}
		
		if (itemUtils.isAbleToRepublish(item)) {
			item.republish();
		} else {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_ABLE_TO_REPUBLISH); 
		}
	}

}
