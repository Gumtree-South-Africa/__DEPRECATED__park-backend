package com.ebay.park.service.moderationMode;

import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderationMode.dto.ApplyFiltersForModerationModeRequest;
import com.ebay.park.service.moderationMode.dto.ApplyFiltersForModerationModeResponse;
import com.ebay.park.service.moderationMode.dto.GetItemInformationRequest;
import com.ebay.park.service.moderationMode.dto.GetItemInformationResponse;
import com.ebay.park.service.moderationMode.dto.GetUserItemsForModerationModeRequest;
import com.ebay.park.service.moderationMode.dto.GetUserItemsForModerationModeResponse;
import com.ebay.park.service.moderationMode.dto.UnlockUserRequest;
import com.ebay.park.service.moderationMode.dto.UpdateItemForModerationModeRequest;

/**
 * 
 *
 */
public interface ModerationModeService {

	/**
	 * Returns the items published by a user.
	 * @param getUserItemsForModerationModeRequest
	 * @return
	 */
	GetUserItemsForModerationModeResponse getUserItemsForModerationMode(
			GetUserItemsForModerationModeRequest getUserItemsForModerationModeRequest);

	/**
	 * Updates the item information
	 * @param request
	 * @return 
	 */
	ServiceResponse updateItemForModerationMode(
			UpdateItemForModerationModeRequest request);

	/**
	 * Returns the pending of moderation items using a filter
	 * @param request
	 * @return
	 */
	ApplyFiltersForModerationModeResponse applyFiltersForModerationMode(
			ApplyFiltersForModerationModeRequest request);
	
	/**
	 * Returns item information (username, last update date, id, photos, 
	 * category, title, description, groups, price, likes, location and 
	 * zip code) given an ID.
	 * @param getItemInformationRequest the incoming request
	 * @return the item information
	 */
	GetItemInformationResponse getItemInformation(GetItemInformationRequest getItemInformationRequest);

	/**
	 * 
	 * @param request
	 * @return
	 */
	ServiceResponse unlockItem(UnlockUserRequest request);
	

}
