package com.ebay.park.service.blacklist;

import com.ebay.park.db.entity.Item;
import com.ebay.park.event.item.ItemBannedEvent;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.blacklist.dto.BlacklistedWord;
import com.ebay.park.service.blacklist.dto.SearchBlacklistedWordsRequest;
import com.ebay.park.service.blacklist.dto.SearchBlacklistedWordsResponse;
import com.ebay.park.service.moderation.dto.BlacklistedWordRequest;

/**
 * @author marcos.lambolay
 */
public interface BlacklistService {
	/**
	 * Checks that the name and description of item does not pass the
	 * blacklisted words.
	 * 
	 * @param item
	 * @return true if the item is blacklisted.
	 */
	public boolean isBlackListed(Item item);
	/**
	 * Checks that the name and description of item does not pass the
	 * blacklisted words.
	 *
	 * @param itemId the id from the item
	 * @return true if the item is blacklisted.
	 */
	public boolean isBlackListed(long itemId);

	public SearchBlacklistedWordsResponse search(
			SearchBlacklistedWordsRequest request) throws ServiceException;

	public ItemBannedEvent bannedItem(Item item, String language);

	public BlacklistedWord addBlacklistedWord(BlacklistedWordRequest request)
			throws ServiceException;

	public ServiceResponse updateBlacklistedWord(BlacklistedWordRequest request)
			throws ServiceException;

	public ServiceResponse removeBlacklistedWord(BlacklistedWordRequest request)
			throws ServiceException;

	public ItemBannedEvent bannedItem(long itemId, String language);
}
