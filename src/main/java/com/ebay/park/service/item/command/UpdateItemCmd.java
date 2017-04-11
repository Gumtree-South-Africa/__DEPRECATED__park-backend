package com.ebay.park.service.item.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.Iterator;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.util.LanguageUtil;
import com.ebay.park.util.UserUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.CategoryDao;
import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Category;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.ItemGroup;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.event.user.UserItemToFollowersEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.aop.Notifiable;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.blacklist.BlacklistService;
import com.ebay.park.service.item.dto.UpdateItemRequest;

/**
 *
 * @author marcos.lambolay
 */
@Component("updateItemCmd")
public class UpdateItemCmd implements ServiceCommand<UpdateItemRequest,UserItemToFollowersEvent> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UpdateItemCmd.class);

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private BlacklistService blackListService;

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private UserUtils userUtils;
	
	@Override
	@Notifiable(action = NotificationAction.UPDATE_AN_ITEM)
	public UserItemToFollowersEvent execute(UpdateItemRequest request)
			throws ServiceException {

		User user = userDao.findByToken(request.getToken());
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}

		Item item = userUtils.getItemUser(request);

		boolean itemChanged = false;

		if (request.getDescription() != null) {
			item.setDescription(request.getDescription());
			itemChanged = true;
		}
		if (request.getName() != null) {
			item.setName(request.getName());
			itemChanged = true;
		}
		if (request.getPrice() != null) {
			item.setPrice(Double.valueOf(request.getPrice()));
			itemChanged = true;
		}

		// Location fields
		if (request.getLocation() != null) {
			item.setLocation(request.getLocation());
			itemChanged = true;
		}
		if (request.getLocationName() != null) {
			item.setLocationName(request.getLocationName());
			itemChanged = true;
		}
		if (request.getLatitude() != null) {
			item.setLatitude(Double.valueOf(request.getLatitude()));
			itemChanged = true;
		}
		if (request.getLongitude() != null) {
			item.setLongitude(Double.valueOf(request.getLongitude()));
			itemChanged = true;
		}

		if (StringUtils.isNotBlank(request.getZipCode())) {
			item.setZipCode(request.getZipCode());
			itemChanged = true;
		}

		// //////////////////////////

		if (request.getCategoryId() != null) {
			// load and set category
			Category category = categoryDao.findOne(request.getCategoryId());
			if (category != null) {
				item.setCategory(category);
				itemChanged = true;
			} else {
				throw createServiceException(ServiceExceptionCode.CATEGORY_NOT_FOUND);
			}
		}

		if (request.getGroups() != null) {
			Iterator<ItemGroup> itemGroupIterator = item.getItemGroups()
					.iterator();
			while (itemGroupIterator.hasNext()) {
				ItemGroup ig = itemGroupIterator.next();
				String groupiId = ig.getGroup().getId().toString();
				if (!ArrayUtils.contains(request.getGroups(), groupiId)) {
					itemGroupIterator.remove();
				}
			}

			for (String groupId : request.getGroups()) {
				Group group = groupDao.findOne(new Long(groupId));
				if (group == null) {
					throw createServiceException(ServiceExceptionCode.INVALID_GROUP);
				}
				item.addGroup(group);
			}
		}

		//if the item was updated
		if (itemChanged || request.getGroups() != null) {
			item.setPendingModeration(true);
			boolean blacklisted = blackListService.isBlackListed(item);
			if (blacklisted) {
				//Ban the item except if the item is already banned and 
				//there is also a request about not sending feed
				if (!(!request.isFeedWhenItemBanned() && item.getStatus().
					         equals(StatusDescription.PENDING))) {
                    blackListService.bannedItem(item, getLanguage(request, user));
				}
			} else {
				item.activate();
			}
			//update lastUpdateDate if only groups changed
			 if (!itemChanged){
				item.updateLastModificationDate();
			}

			try {
				itemDao.save(item);
			} catch (Exception e) {
                LOGGER.error("Error updating item {}", request.getItemId(), e);
				throw createServiceException(ServiceExceptionCode.ERROR_UPDATING_ITEM);
			}
		}

		// Notify
		return new UserItemToFollowersEvent(item, item.getPublishedBy(), null);
	}

	private String getLanguage(UpdateItemRequest request, User user) {
		return LanguageUtil.getLanguageForUserRequest(user, request.getLanguage());
	}

}
