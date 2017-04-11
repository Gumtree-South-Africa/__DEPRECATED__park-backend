package com.ebay.park.service.group.command;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.group.dto.RemoveGroupItemsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * @author gabriel.sideri
 */
@Component
public class RemoveGroupItemsCmd implements
ServiceCommand<RemoveGroupItemsRequest, ServiceResponse> {

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ItemDao itemDao;

	private static Logger logger = LoggerFactory.getLogger(RemoveGroupItemsCmd.class);
	
	@Value("${group.remove_group_items_limit}")
	private int MAX_ITEMS_TO_PROCCESS;
	
	@Override
	public ServiceResponse execute(RemoveGroupItemsRequest request)
			throws ServiceException {

		User user = userDao.findByToken(request.getToken());

		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}

		Group group = groupDao.findOne(request.getGroupId());

		if (group == null) {
			throw createServiceException(ServiceExceptionCode.INVALID_GROUP);
		}
		
		if (!group.getCreator().equals(user)) {
			throw createServiceException(ServiceExceptionCode.INVALID_GROUP_OWNER);
		}
		
		long millis = System.currentTimeMillis();

		List<Item> items = null;
		if (request.isDeleteByUsersIds()){
			//get all the items by user ids
			items = itemDao.getItemsByGroupIdAndUsersIds(request.getGroupId(),
					StatusDescription.ACTIVE, request.getIdsValidated());
		}else{
			//get items by ids limit MAX_ITEMS_TO_PROCCESS
			PageRequest pageReq = new PageRequest(0, MAX_ITEMS_TO_PROCCESS);
			items = itemDao.getItemsById(request.getGroupId(),
					StatusDescription.ACTIVE, request.getIdsValidated(), pageReq).getContent();
		}
		
		if (items.isEmpty()){
			throw createServiceException(ServiceExceptionCode.ITEMS_ARE_NOT_IN_THE_GROUP);
		}
		
		Iterator<Item> itemIterator = items.iterator();

		while (itemIterator.hasNext()) {
			Item item = itemIterator.next();
			item.removeGroup(group);
		}
		
		itemDao.save(items);

        logger.info("REMOVE BATCH GROUP ITEMS, Items modified: {}. Execution took {} seconds.", items.size(), (System.currentTimeMillis() - millis)
                / 1000);

		return ServiceResponse.SUCCESS;
		
	}

}