package com.ebay.park.service.moderation.command;

import com.ebay.park.event.item.ItemNotificationEvent;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.moderation.dto.ItemRequest;

public interface ActivateItemCmd extends
		ServiceCommand<ItemRequest, ItemNotificationEvent> {

	@Override
	ItemNotificationEvent execute(ItemRequest request) throws ServiceException;
}
