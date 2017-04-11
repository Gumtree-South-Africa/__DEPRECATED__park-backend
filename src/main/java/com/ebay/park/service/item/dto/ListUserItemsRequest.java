package com.ebay.park.service.item.dto;

import com.ebay.park.service.PaginatedRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ListUserItemsRequest extends PaginatedRequest {

	@JsonIgnore
	/**
	 * Username of the user which items are going to be listed.
	 */
	private String username;

	/**
	 * One item can be excluded of the results item list.
	 * It can be null.
	 */
	private Long itemIdExcluded;

	@JsonIgnore
    /**
     * It indicates if the user is requesting its own item list.
     */
	private Boolean ownItems = false;

	/**
	 * Time of the first request.
	 * It avoids getting non-first pages with new items that break pagination. 
	 */
	private String requestTime;

	/**
	 * Constructor.
	 * @param token
	 *     user Park token
	 * @param language
	 *     request language
	 * @param page
	 *     number of page to get
	 * @param pageSize
	 *     number of items per page
	 */
	public ListUserItemsRequest(String token, String language, Integer page, 
	        Integer pageSize) {
		super(token, language, page, pageSize);
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * It indicates if the user is requesting its own item list.
	 * @return the ownItems
	 */
	public boolean isOwnItems() {
		return ownItems;
	}

	/**
	 * It sets the flag that indicates that the user is requesting its own item list.
	 * @param ownItems
	 *            the ownItems to set
	 */
	public void setOwnItems(Boolean ownItems) {
		this.ownItems = ownItems;
	}

	/**
	 * 
	 * @param requestTime the requestTime to set.
	 */
	@Override
    public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
		
	}

	/**
	 * 
	 * @return the requestTime.
	 */
	
	@Override
    public String getRequestTime() {
		return requestTime;
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
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	StringBuilder builder = new StringBuilder();
    	builder.append("ListUserItemsRequest [username= ")
    		.append(this.username).append(", itemIdExcluded= ")
    		.append(this.itemIdExcluded).append(", ownItems= ")
    		.append(this.ownItems).append(", requestTime= ")
    		.append(this.getRequestTime()).append("]");
    		
    return builder.toString();
    }
}
