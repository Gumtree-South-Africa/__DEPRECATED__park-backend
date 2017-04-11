package com.ebay.park.service.blacklist;

import static com.ebay.park.service.ServiceException.createServiceException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ebay.park.event.item.ItemBannedEvent;
import com.ebay.park.util.LanguageUtil;
import com.ebay.park.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.event.item.ItemNotificationEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.aop.Notifiable;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.blacklist.command.AddBlacklistedWordCmd;
import com.ebay.park.service.blacklist.command.RemoveBlacklistedWordCmd;
import com.ebay.park.service.blacklist.command.SearchBlacklistedWordsCmd;
import com.ebay.park.service.blacklist.command.UpdateBlacklistedWordCmd;
import com.ebay.park.service.blacklist.command.ValidateTextOnBlackListCmd;
import com.ebay.park.service.blacklist.dto.BlacklistedWord;
import com.ebay.park.service.blacklist.dto.SearchBlacklistedWordsRequest;
import com.ebay.park.service.blacklist.dto.SearchBlacklistedWordsResponse;
import com.ebay.park.service.moderation.dto.BlacklistedWordRequest;

/**
 * 
 * @author marcos.lambolay
 */
@Service
public class BlacklistServiceImpl implements BlacklistService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SearchBlacklistedWordsCmd searchBlacklistedWordsCmd;

	@Autowired
	private ValidateTextOnBlackListCmd validateTextOnBlackListCmd;

	@Autowired
	private AddBlacklistedWordCmd addBlacklistedWordCmd;

	@Autowired
	private UpdateBlacklistedWordCmd updateBlacklistedWordCmd;

	@Autowired
	private RemoveBlacklistedWordCmd removeBlacklistedWordCmd;

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private TextUtils textUtils;

	@Override
	public boolean isBlackListed(Item item) {
		return validateTextOnBlackListCmd.execute(item);
	}

	@Override
	@Notifiable(action = NotificationAction.ITEM_BANNED)
	public ItemBannedEvent bannedItem(Item item, String language) {
		Assert.notNull(item, "'item' must be not null");
		item.ban();
		itemDao.save(item);
		return new ItemBannedEvent(item, textUtils.createItemSEOURL(item, language));
	}

	@Override
	@Notifiable(action = NotificationAction.ITEM_BANNED)
	public ItemBannedEvent bannedItem(long itemId, String language) {
		Item item = itemDao.findOne(itemId);
		return this.bannedItem(item, language);
	}
	
	@Override
	public SearchBlacklistedWordsResponse search(
			SearchBlacklistedWordsRequest request) throws ServiceException {
		return searchBlacklistedWordsCmd.execute(request);
	}

	protected void blacklistUpdated() {
		validateTextOnBlackListCmd.generateBlacklistIndex();
	}

	@Override
	public BlacklistedWord addBlacklistedWord(BlacklistedWordRequest request)
			throws ServiceException {
		if (StringUtils.isEmpty(request.getWord())) {
			throw createServiceException(ServiceExceptionCode.BLACKLIST_WORD_EMPTY);
		}

		try {
			BlacklistedWord word = addBlacklistedWordCmd.execute(request);
			blacklistUpdated();
			return word;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public ServiceResponse updateBlacklistedWord(BlacklistedWordRequest request)
			throws ServiceException {
		if (StringUtils.isEmpty(request.getWord())) {
			throw createServiceException(ServiceExceptionCode.BLACKLIST_WORD_EMPTY);
		}

		ServiceResponse response = updateBlacklistedWordCmd.execute(request);

		if (response.equals(ServiceResponse.SUCCESS)) {
			blacklistUpdated();
		}

		return response;
	}

	@Override
	public ServiceResponse removeBlacklistedWord(BlacklistedWordRequest request)
			throws ServiceException {

		ServiceResponse response = removeBlacklistedWordCmd.execute(request);

		if (response.equals(ServiceResponse.SUCCESS)) {
			blacklistUpdated();
		}

		return response;
	}

	@Override
	public boolean isBlackListed(long itemId) {
		Item item = itemDao.findOne(itemId);
		return validateTextOnBlackListCmd.execute(item);
	}

}
