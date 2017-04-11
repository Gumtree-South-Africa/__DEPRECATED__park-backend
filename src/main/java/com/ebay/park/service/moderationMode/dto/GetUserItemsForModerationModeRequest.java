package com.ebay.park.service.moderationMode.dto;

import com.ebay.park.service.ParkRequest;

/**
 * Request for defining the user items to get.
 * @author Julieta Salvadó
 *
 */
public class GetUserItemsForModerationModeRequest extends ParkRequest{

	private Long userId;
	private Long itemIdExcluded;
	private Integer page;
	private Integer pageSize;

	public GetUserItemsForModerationModeRequest(String parkToken, String language, Long userId,
			Long itemIdExcluded, Integer page, Integer pageSize) {
		super(parkToken, language);
		this.setUserId(userId);
		this.setItemIdExcluded(itemIdExcluded);
		this.setPage(page);
		this.setPageSize(pageSize);
	}

	/**
	 * @return the itemIdExcluded
	 */
	public Long getItemIdExcluded() {
		return itemIdExcluded;
	}

	/**
	 * @param itemIdExcluded the itemIdExcluded to set
	 */
	public void setItemIdExcluded(Long itemIdExcluded) {
		this.itemIdExcluded = itemIdExcluded;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the page
	 */
	public Integer getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(Integer page) {
		this.page = page;
	}

	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
