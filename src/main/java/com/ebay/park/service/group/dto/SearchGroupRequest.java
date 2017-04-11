package com.ebay.park.service.group.dto;

import com.ebay.park.service.GeoPaginatedRequest;

/**
 * 
 * @author federico.jaite
 */
public class SearchGroupRequest extends GeoPaginatedRequest {

	private String criteria;
	private boolean findOnlyUserFollowsGroup;
	private boolean onlyOwned;
	private String order;
	private String requestTime;

	public SearchGroupRequest(String token, String language, Integer page, Integer pageSize) {
		super(token, language, page, pageSize);
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public boolean findOnlyUserFollowsGroup() {
		return findOnlyUserFollowsGroup;
	}

	public void setFindOnlyUserFollowsGroup(boolean findOnlyUserFollowsGroup) {
		this.findOnlyUserFollowsGroup = findOnlyUserFollowsGroup;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public boolean isOnlyOwned() {
		return onlyOwned;
	}

	public void setOnlyOwned(boolean onlyOwned) {
		this.onlyOwned = onlyOwned;
	}

    /**
     * @return the requestTime
     */
    @Override
    public String getRequestTime() {
        return requestTime;
    }

    /**
     * @param requestTime the requestTime to set
     */
    @Override
    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	StringBuilder builder = new StringBuilder();
    	builder.append("SearchGroupRequest [criteria= ")
    		.append(this.criteria).append(", latitude= ")
    		.append(this.findOnlyUserFollowsGroup).append(", onlyOwned= ")
    		.append(this.onlyOwned).append(", order= ")
    		.append(this.requestTime).append("]");
    		
    return builder.toString();
    }
}
