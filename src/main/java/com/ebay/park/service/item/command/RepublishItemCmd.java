package com.ebay.park.service.item.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.ItemGroupDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.ItemGroup;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.event.user.UserItemToFollowersEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.aop.Notifiable;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.dto.RepublishItemRequest;
import com.ebay.park.util.DataCommonUtil;

/**
 * @author marcos.lambolay | gabriel.sideri
 */
@Component("republishItemCmd")
public class RepublishItemCmd extends UserItemCmd<RepublishItemRequest, UserItemToFollowersEvent> {

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private ItemGroupDao itemGroupDao;

	@Override
	@Notifiable(action = NotificationAction.UPDATE_AN_ITEM)
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public UserItemToFollowersEvent execute(RepublishItemRequest request)
			throws ServiceException {
		Item item = getItemUser(request);

		if (request.getPrice() != null && 
				!request.getPrice().equals(item.getPrice())) {
			//Set new item price
			item.setPrice(request.getPrice());
			item.updateLastModificationDate();
		}
		
		republish(item);
		
		//date on relation item-group has to be updated
		for (ItemGroup itemGroup: item.getItemGroups()) {
			itemGroup.setDate(DataCommonUtil.getCurrentTime());
			itemGroupDao.save(itemGroup);
		}
		
		item = itemDao.save(item);

		return new UserItemToFollowersEvent(item, item.getPublishedBy(), null);
	}
	
	/**
	 * Republishes an item if the status is SOLD or EXPIRED.
	 * @param item
	 *          the item to republish
	 */
	protected void republish(Item item) {
		if (!(StatusDescription.SOLD.equals(item.getStatus()) || 
				StatusDescription.EXPIRED.equals(item.getStatus()))) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_SOLD_OR_EXPIRED_ERROR);
		}
		
		item.republish();
	}

}