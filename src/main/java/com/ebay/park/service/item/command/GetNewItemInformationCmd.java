package com.ebay.park.service.item.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.ItemGroupDao;
import com.ebay.park.db.dao.UserFollowsGroupDao;
import com.ebay.park.db.entity.ItemGroup;
import com.ebay.park.db.entity.UserFollowsGroup;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.item.dto.GetNewItemInformationRequest;
import com.ebay.park.service.item.dto.ItemSummary;

@Component
public class GetNewItemInformationCmd implements ServiceCommand<GetNewItemInformationRequest, ServiceResponse>{

	@Autowired
	private UserFollowsGroupDao userFollowsGroupDao;
	
	@Autowired
	private ItemGroupDao itemGroupDao;
	
	@Override
	public ServiceResponse execute(GetNewItemInformationRequest request)
			throws ServiceException {
		List<ItemSummary> itemsDTO = request.getItemsDTO();
		UserFollowsGroup userFollowsGroup = userFollowsGroupDao.find(
				request.getGroupId(), request.getUserId());
		
		if (userFollowsGroup != null) {
			for (ItemSummary item : itemsDTO) {
				item.setNewItem(isNew(item, userFollowsGroup, itemGroupDao.find(item.getId(), request.getGroupId())));
			}
		}
		return ServiceResponse.SUCCESS;
	}

	private Boolean isNew(ItemSummary item, UserFollowsGroup userFollowsGroup, ItemGroup itemGroup) {
		if (item != null && userFollowsGroup != null && itemGroup != null && itemGroup.getDate() != null && userFollowsGroup.getLastAccess() != null) {
			return itemGroup.getDate().after(userFollowsGroup.getLastAccess());
		}
		return false;
	}
}
