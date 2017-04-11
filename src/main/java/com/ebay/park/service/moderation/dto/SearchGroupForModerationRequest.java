/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service.moderation.dto;

import com.ebay.park.service.PaginatedRequest;

/**
 * @author diana.gazquez
 *
 */
public class SearchGroupForModerationRequest  extends PaginatedRequest{

	private final String name;
	private final String creatorName;
	
	public SearchGroupForModerationRequest(String name, String creatorName, Integer page, Integer pageSize) {
		super(page, pageSize);
		this.name = name;
		this.creatorName = creatorName;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the creatorName
	 */
	public String getCreatorName() {
		return creatorName;
	}

}