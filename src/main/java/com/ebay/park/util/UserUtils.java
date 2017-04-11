package com.ebay.park.util;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.List;

import com.ebay.park.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.DeviceDao;
import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Device;
import com.ebay.park.db.entity.Item;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.dto.UserItemRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.service.user.finder.UserInfoUtil;

@Component
public class UserUtils {
	
	@Autowired
	private DeviceDao deviceDao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private ItemDao itemDao;
	
	private static Logger logger = LoggerFactory.getLogger(UserInfoUtil.class);

	/**
	 * It finds the devices linked to a user.
	 * @param userId
	 * 		user to find devices linked to
	 * @param signedIn
	 * 		true for signed-in devices; otherwise, false.
	 * @return
	 * 		list of devices
	 */
	public List<Device> findDevices(Long userId, boolean signedIn) {
		logger.debug("Searching user devices");
		return deviceDao.findDevicesByUser(userId, signedIn);
	}

	/**
     * It returns an item that must be owned by the requester.
     * @param request
     * @return
     *     the item
     * @exception ServiceException with code ITEM_NOT_FOUND when the item is deleted or cannot be found.
     * @exception ServiceException with code ITEM_DOESNT_BELONG_TO_USER when the item does not belong to the requester.
     */
    public Item getItemUser(UserItemRequest request) {
        UserSessionCache userSession = sessionService.getUserSession(request
                .getToken());
        Item item = itemDao.findOne(request.getItemId());
        if (item == null || item.isDeleted()) {
            throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);
        }

        if (!item.getPublishedBy().getUserId().equals(userSession.getUserId())) {
            throw createServiceException(ServiceExceptionCode.ITEM_DOESNT_BELONG_TO_USER);
        }
        return item;
    }
}
