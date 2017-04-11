/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PaginatedRequest extends ParkRequest {

	@JsonIgnore
	private Integer page;

	@JsonIgnore
	private Integer pageSize;
	private String requestTime;

	public PaginatedRequest(String token, String language) {
		super(token, language);
	}

	public PaginatedRequest(String token, String language,Integer page, Integer pageSize) {
		super(token, language);
		this.page = page;
		this.pageSize = pageSize;
	}

	public PaginatedRequest(Integer page, Integer pageSize) {
		super();
		this.page = page;
		this.pageSize = pageSize;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 
	 * @param requestTime the requestTime to set.
	 */
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	
	/**
	 * 
	 * @return the requestTime.
	 */
	public String getRequestTime() {
		return requestTime;
	}
}
