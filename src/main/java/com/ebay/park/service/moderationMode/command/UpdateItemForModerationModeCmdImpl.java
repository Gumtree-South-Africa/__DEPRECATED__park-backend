package com.ebay.park.service.moderationMode.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.Iterator;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.CategoryDao;
import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Category;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.ItemGroup;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.ServiceResponseStatus;
import com.ebay.park.service.moderationMode.ModerationCacheHelper;
import com.ebay.park.service.moderationMode.dto.UpdateItemForModerationModeRequest;

/**
 * Command for updating an item from ModTool and label it as already moderated.
 * 
 * @author Julieta Salvadó
 *
 */
@Component
public class UpdateItemForModerationModeCmdImpl implements UpdateItemForModerationModeCmd {

	@Autowired
	private ModerationCacheHelper moderationCacheHelper;

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateItemForModerationModeCmdImpl.class);

	@Value("${createItem.maxAmountPictures}")
	private Integer maxAmountPictures;

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private GroupDao groupDao;

	@Override
	public ServiceResponse execute(UpdateItemForModerationModeRequest request) throws ServiceException {
		Item item = itemDao.findOne(request.getItemId());

		if (item == null) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);
		}

		if (!item.isPendingModeration()) {
			throw createServiceException(ServiceExceptionCode.ITEM_ALREADY_MODERATED);
		}

		Long publisherId = item.getPublishedBy().getId();
		if (!moderationCacheHelper.isItemAvailableToModerate(publisherId, request.getToken())) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_AVAILABLE_TO_MODERATE);
		}

		// lock user
		moderationCacheHelper.lockUser(publisherId, request.getToken());

		// the moderator edit the item.
		updateItem(request, item);

		try {
			item.setPendingModeration(false);
			itemDao.save(item);
			moderationCacheHelper.unlockUser(item.getPublishedBy().getId(), request.getToken());
			return new ServiceResponse(ServiceResponseStatus.SUCCESS, StringUtils.EMPTY, null);
		} catch (Exception e) {
            LOGGER.error("Error updating an item for moderation mode[{}]", request.getItemId(), e);
			throw createServiceException(ServiceExceptionCode.ERROR_UPDATING_ITEM);
		}
	}

	/**
	 * Perform the update of the item. The possible changes are: description,
	 * category, groups, pictures and name.
	 * 
	 * @param request
	 * @param item
	 */
	private void updateItem(UpdateItemForModerationModeRequest request, Item item) {

		boolean itemChanged = false;

		if (request.getDescription() != null) {
			if (!(item.getDescription().equals(request.getDescription()))) {
				item.setDescription(request.getDescription());
				itemChanged = true;
			}
		}
		if (request.getName() != null) {
			if (!(item.getName().equals(request.getName()))) {
				item.setName(request.getName());
				itemChanged = true;
			}
		}
		if (request.getCategory() != null) {
			if (!(item.getCategory().getCategoryId().equals(request.getCategory()))) {
				Category newCategory = categoryDao.getOne(request.getCategory());
				if (newCategory != null) {
					item.setCategory(newCategory);
					itemChanged = true;
				}
				else{
					throw createServiceException(ServiceExceptionCode.CATEGORY_NOT_FOUND);
				}
			}
		}
		if (request.getPicture1() != null) {
			if (!(request.getPicture1().equals(item.getPicture1Url())) && !(request.getPicture1().isEmpty())) {
				item.setPicture1Url(request.getPicture1());
				itemChanged = true;
			}
		}
		if (request.getPicture2() != null) {
			// if the moderator sends empty,its represents null.
			if (!(request.getPicture2().isEmpty() && item.getPicture2Url() == null)) {
				if (!(request.getPicture2().equals(item.getPicture2Url()))) {
					if (request.getPicture2().isEmpty()) {
						item.setPicture2Url(null);
					} else {
						item.setPicture2Url(request.getPicture2());
					}
					itemChanged = true;
				}
			}
		}
		if (request.getPicture3() != null) {
			// if the moderator sends empty,its represents null.
			if (!(request.getPicture3().isEmpty() && item.getPicture3Url() == null)) {
				if (!(request.getPicture3().equals(item.getPicture3Url()))) {
					if (request.getPicture3().isEmpty()) {
						item.setPicture3Url(null);
					} else {
						item.setPicture3Url(request.getPicture3());
					}
					itemChanged = true;
				}
			}
		}
		if (request.getPicture4() != null) {
			// if the moderator sends empty,its represents null.
			if (!(request.getPicture4().isEmpty() && item.getPicture4Url() == null)) {
				if (!(request.getPicture4().equals(item.getPicture4Url()))) {
					if (request.getPicture4().isEmpty()) {
						item.setPicture4Url(null);
					} else {
						item.setPicture4Url(request.getPicture4());
					}
					itemChanged = true;
				}
			}
		}
		if (request.getGroups() != null) {
			Iterator<ItemGroup> itemGroupIterator = item.getItemGroups().iterator();
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
			itemChanged = true;
		}
		if (itemChanged) {
			item.updateLastModificationDate();
		}
	}
}
