package com.ebay.park.service.group.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.UserFollowsGroupDao;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.UserFollowsGroup;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.group.dto.GetGroupCounterRequest;
import com.ebay.park.service.item.dto.SmallGroupDTO;

@Component
public class GetGroupCounterCmd implements ServiceCommand<GetGroupCounterRequest, ServiceResponse>{

	@Autowired
	private UserFollowsGroupDao userFollowsGroupDao;
	
	@Autowired
	private GroupDao groupDao;
	
	@Override
	public ServiceResponse execute(GetGroupCounterRequest request)
			throws ServiceException {
		List<SmallGroupDTO> groups = request.getGroups();
		Long userId = request.getUserId();
		
		for (SmallGroupDTO group: groups) {
			UserFollowsGroup element = userFollowsGroupDao.find(group.getId(), userId);
			
			if (element != null) {
				group.setNewItems(groupDao.getNewItemsCount(
						element.getLastAccess(), group.getId(), StatusDescription.ACTIVE));
			}
		}
		
		return ServiceResponse.SUCCESS;
	}

}
