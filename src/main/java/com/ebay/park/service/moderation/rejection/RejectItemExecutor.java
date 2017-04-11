package com.ebay.park.service.moderation.rejection;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.event.item.ItemNotificationEvent;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.moderation.dto.RejectItemRequest;

/**
 * It performs the deletion of the rejected item.
 * @author Julieta Salvadó
 *
 */

@Component
public abstract class RejectItemExecutor {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RejectItemExecutor.class);

	@Autowired
	private ItemDao itemDao;

	public abstract ItemNotificationEvent execute(RejectItemRequest request) throws ServiceException;	

	/**
	 * It performs the deletion of the rejected item
	 * @param request the request
	 * @return the deleted item
	 * @throws ServiceException when the item was not found, the item was already deleted or
	 * there was an error during the deletion.
	 */
	public Item rejectItem(RejectItemRequest request) throws ServiceException {
		Item item = itemDao.findOneDeletedOrNot(request.getItemId());

		if (item == null) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);
		}

		if (item.isDeleted()) {
			throw createServiceException(ServiceExceptionCode.ITEM_ALREADY_DELETED);
		}

		if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Attempting to delete item id: {}", request.getItemId());
		}
		try {
			item.delete();
			itemDao.save(item);

			if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Delete item id: {}successful", request.getItemId());
			}
			return item;
		} catch (Exception e) {
            LOGGER.error("Error deleting item [{}]", request.getItemId(), e);
			throw createServiceException(ServiceExceptionCode.ITEM_DELETION_ERROR);
		}
	}
}
