/**
 * 
 */
package com.ebay.park.service.common.dto;

/**
 * @author jppizarro
 *
 */
public class Pagination {

	private int page;
	private int pageSize;

	public Pagination(Integer page, Integer size) {
		this.page  = (size == null ? 0 : page);
		this.pageSize = ( size == null || size <= 0 ? 20 : size);
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
