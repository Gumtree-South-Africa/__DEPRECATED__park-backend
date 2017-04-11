package com.ebay.park.service.item;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserFollowsItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.Social;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserFollowsItem;
import com.ebay.park.db.entity.UserFollowsItemPK;
import com.ebay.park.event.item.ItemNotificationEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.aop.Notifiable;
import com.ebay.park.service.blacklist.BlacklistService;
import com.ebay.park.service.category.dto.SmallCategory;
import com.ebay.park.service.item.command.ShareItemCmd;
import com.ebay.park.service.item.dto.ItemSummary;
import com.ebay.park.service.item.dto.ShareItemRequest;
import com.ebay.park.service.moderationMode.dto.ItemSummaryForModeration;
import com.ebay.park.util.InternationalizationUtil;
import com.ebay.park.util.TextUtils;

@Component
public class ItemUtils {

	@Value("${createItem.maxAmountPictures}")
	private Integer maxAmountPictures;
	
	@Value("${republish.limitOfDays}")
	private Integer republishLimitOfDays;

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private ShareItemCmd shareItemCmd;

	private static Logger logger = LoggerFactory.getLogger(ItemUtils.class);

	@Autowired
	private InternationalizationUtil i18nUtil;

	@Autowired
	private BlacklistService blackListService;
	
	@Autowired
	private TextUtils textUtils;

	@Autowired
	private UserFollowsItemDao userFollowsItemDao;

	/**
	 * Replaces current pictures with the ones in the array
	 * @param photos
	 *         array of URLs of new pictures
	 * @param item
	 *         item to edit
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void setPictures(String[] photos, Item item)
			throws IllegalAccessException, InvocationTargetException {
		Validate.isTrue(photos.length <= maxAmountPictures,
				"photos.length can't be greater than " + maxAmountPictures);
		List<String> photoList = new LinkedList<>(Arrays.asList(photos));
		Iterator<String> iterator = photoList.iterator();

		//removes null values
		while (iterator.hasNext()) {
			if (StringUtils.isBlank(iterator.next())) {
				iterator.remove();
			}
		}

		//deletes old pictures
		for (int i = 1; i <= maxAmountPictures; i++) {
			BeanUtils.setProperty(item, "picture" + i + "Url", (String) null);
		}

		//sets new pictures
		for (int i = 1; i <= photoList.size(); i++) {
			BeanUtils.setProperty(item, "picture" + i + "Url",
					photoList.get(i - 1));
		}
	}

	public void setPictures(Map<Integer, String> photos, Item item)
			throws IllegalAccessException, InvocationTargetException {
		Validate.isTrue(photos.size() <= maxAmountPictures,
				"photos.length can't be greater than " + maxAmountPictures);

		for (Integer photoId : photos.keySet()) {
			String url = photos.get(photoId);
			if (StringUtils.isNotBlank(url)) {

				BeanUtils.setProperty(item, "picture" + photoId + "Url", url);
			}
		}

	}
	
	/**
	 * Converts the item list from the publisher into item summary elements.
	 * 
	 * @param items
	 *            item list
	 * @param lang
	 *            language defined for translation purposes
	 * @return item summary elements
	 */
	public List<ItemSummary> convertToItemSummaryFromPublisher(
			List<Item> items, String lang) {
		List<ItemSummary> itemList = new ArrayList<ItemSummary>();
		for (Item item : items) {
			item.setLocalizedStatus(i18nUtil.internationalizeMessage("item." + item.getStatus().toString().toLowerCase(), lang));
			ItemSummary itemSummary = ItemSummary.fromPublishedItem(item);
			itemSummary.setCategory(SmallCategory.fromCategory(i18nUtil
					.internationalize(item.getCategory(), lang)));
			itemSummary.setURL(textUtils.createItemSEOURL(itemSummary.getCategory().getName(),
					itemSummary.getName(), item.getId()));
			itemList.add(itemSummary);
		}
		return itemList;
	}

	/**
	 * Converts the item list into item summary elements.
	 * 
	 * @param items
	 *            item list
	 * @param lang
	 *            language defined for translation purposes
	 * @return item summary elements
	 */
	public List<ItemSummary> convertToItemSummary(List<Item> items, String lang) {
		List<ItemSummary> itemList = new ArrayList<ItemSummary>();
		for (Item item : items) {
			item.setLocalizedStatus(i18nUtil.internationalizeMessage("item." + item.getStatus().toString().toLowerCase(), lang));
			ItemSummary itemSummary = ItemSummary.fromItem(item);
			itemSummary.setCategory(SmallCategory.fromCategory(i18nUtil
					.internationalize(item.getCategory(), lang)));
			itemSummary.setURL(textUtils.createItemSEOURL(itemSummary.getCategory().getName(),
					itemSummary.getName(), item.getId()));
			itemList.add(itemSummary);
		}
		return itemList;
	}

	/**
	 * Converts the followed item list into item summary elements.
	 * 
	 * @param items
	 *            item list
	 * @param user
	 *            user who is following these items
	 * @param lang
	 *            language define for translation purposes
	 * @return item summary elements
	 */
	public List<ItemSummary> convertToItemSummary(List<Item> items, User user,
			String lang) {
		List<ItemSummary> itemList = new ArrayList<ItemSummary>();
		for (Item item : items) {
			item.setLocalizedStatus(i18nUtil.internationalizeMessage("item." + item.getStatus().toString().toLowerCase(), lang));
			ItemSummary itemSummary = ItemSummary.fromFollowedItem(item, user);
			itemSummary.setCategory(SmallCategory.fromCategory(i18nUtil
					.internationalize(item.getCategory(), lang)));
			itemSummary.setURL(textUtils.createItemSEOURL(itemSummary.getCategory().getName(),
					itemSummary.getName(), item.getId()));
			itemList.add(itemSummary);
		}
		return itemList;
	}
	
	public List<ItemSummaryForModeration> convertToItemSummaryForModeration(
			List<Item> items, String lang) {
		List<ItemSummaryForModeration> itemList = new ArrayList<ItemSummaryForModeration>();
		for (Item item : items) {
			ItemSummaryForModeration itemSummary = ItemSummaryForModeration.fromItem(item);
			itemSummary.setCategory(SmallCategory.fromCategory(i18nUtil
					.internationalize(item.getCategory(), lang)));
			itemSummary.setURL(textUtils.createItemSEOURL(itemSummary.getCategory().getName(),
					itemSummary.getName(), item.getId()));
			itemList.add(itemSummary);
		}
		return itemList;
	}

	@Transactional
	@Notifiable(action = { NotificationAction.NEW_ITEM })
	public ItemNotificationEvent activateItem(Item item) {
		logger.info("Activating Item: {}", item);
		
		item.activate();
		item = itemDao.save(item);
			
		logger.info("Activating Item done: {}", item);
		return new ItemNotificationEvent(item);
	}
	

	public ItemNotificationEvent activateItem(long id) {
		Item item = itemDao.findOne(id);
		return this.activateItem(item);
	}
	
	/**
	 * Share new item.
	 * TODO:Needs re-factor. We need to review the process
	 * to share an item when it is created. 
	 * @param item
	 * @param token 
	 */
	@Transactional
	public void shareNewItem(Item item, String token){
		shareOnSocials(token, item,
				item.pendingFacebookShare(), item.pendingTwitterShare());
		itemDao.save(item);
		
	}
	
	public void rearrangeItemPictures(Item item) {
		try {
		    //constructs an array with the current valid pictures
			String[] photos = new String[maxAmountPictures];
			for (int i = 1; i <= maxAmountPictures; i++) {
				String pictureField = "picture" + i + "Url";
				photos[i - 1] = BeanUtils.getProperty(item, pictureField);
			}

			//sets the current pictures
			setPictures(photos, item);
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	private void shareOnSocials(String token, Item item, boolean shareOnFacebook,
			boolean shareOnTwitter) {

		if (shareOnFacebook) {

			try {
				shareItemCmd.execute(new ShareItemRequest(item.getId(),
						token, Social.FACEBOOK));
				item.facebookShareDone();
			} catch (Exception e) {
				logger.error("Item share on Facebook failed", e);
				item.facebookShareFailed();
			}

		}

		if (shareOnTwitter) {

			try {
				shareItemCmd.execute(new ShareItemRequest(item.getId(),
						token, Social.TWITTER));
				item.twitterShareDone();
			} catch (Exception e) {
				logger.error("Item share on Twitter failed", e);
				item.twitterShareFailed();
			}

		}

	}

	/**
	 * Verifies if text contains valid characters [a-zA-Z0-9\\t\\n¡¿!?_ñáéíóúü -].
	 * @param text to validate
	 * @return boolean
	 */
	public boolean isValidTextTitle(String text) {
		String p = "^[a-zA-Z0-9\\t\\n¡¿!?_ñáéíóúü -]+$";
		if (StringUtils.isBlank(text))
			return false;
		try {
			Validate.isTrue(text.toLowerCase().matches(p));
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	
	/**
	 * Verifies if text does not contain invalid characters [^;:].
	 * @param text to validate
	 * @return boolean
	 */
	public boolean isValidTextDescription(String text) {
		String p = "[^;:]+$";
		if (StringUtils.isBlank(text))
			return false;
		try {
			Validate.isTrue(text.toLowerCase().matches(p));
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	@Transactional
	public void blacklistItem(long id, String language) {
		if (blackListService.isBlackListed(id)) {
			logger.debug("Send a NotificationAction.ITEM_BANNED, from ItemUtil id={}", id);
			blackListService.bannedItem(id, language);
		}
	}

	/**
	 * Add the item-follower relation.
	 * @param itemId
	 *     the item in the relation
	 * @param userId
	 *     the user in the relation
	 */
    public void setAsFollower(Long itemId, Long userId) {
        if (itemId != null && userId != null) {
            UserFollowsItemPK pk = new UserFollowsItemPK();
            pk.setItemId(itemId);
            pk.setUserId(userId);
            UserFollowsItem userFollowsItem = userFollowsItemDao.findOne(pk);
            if (userFollowsItem == null) {
                userFollowsItem = new UserFollowsItem();
                userFollowsItem.setDateFollowed(DateTime.now().toDate());
                userFollowsItem.setId(pk);
                userFollowsItemDao.saveAndFlush(userFollowsItem);
            }
        }
    }
    
    /**
     * Validates if an item is able to be republished.
     * An item can be republished once a week
     * @param item
     * @return true if the item was republished once a week
     */
    public boolean isAbleToRepublish(Item item) {
    	Assert.notNull(item, "The item cannot be null");
    	DateTime today = DateTime.now();
    	if (today.minusDays(republishLimitOfDays).isAfter(item.getPublished().getTime()) ||
    			today.minusDays(republishLimitOfDays).equals(item.getPublished())) {
            logger.debug("Item: {} (id:{}) is able to republish (days limit:{})", item.getName(), item.getId(), republishLimitOfDays);
            return true;
        }
        logger.debug("Item: {} (id:{}) is NOT able to republish (days limit:{})", item.getName(), item.getId(), republishLimitOfDays);
        return false;
    }

}