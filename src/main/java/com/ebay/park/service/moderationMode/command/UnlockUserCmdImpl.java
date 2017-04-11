package com.ebay.park.service.moderationMode.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.service.moderationMode.ModerationCacheHelper;
import com.ebay.park.service.moderationMode.dto.UnlockUserRequest;

@Component
public class UnlockUserCmdImpl implements UnlockUserCmd {
	
	@Autowired
	ModerationCacheHelper moderationCacheHelper;
	
	@Autowired
	ItemDao itemDao;

	@Override
	public ServiceResponse execute(UnlockUserRequest request) throws ServiceException {
		Long itemId = request.getItemId();
		if (itemId == null) {
			throw createServiceException(ServiceExceptionCode.ERROR_UPDATING_ITEM);
		}
		
		Item item = itemDao.findOne(itemId);
		
		if (item == null) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);
		}
		
		moderationCacheHelper.unlockUser(item.getPublishedBy().getId(), request.getToken());
		
		return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY, null);
	}

}
