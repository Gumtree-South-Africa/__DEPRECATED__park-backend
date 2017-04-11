/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service;

/**
 * @author diana.gazquez
 *
 */
public class PaginatedResponse extends ListedResponse {

	protected final long totalElements;
	
	protected final int numberOfElements; //pagesize
	
	public PaginatedResponse() {
		totalElements =0;
		numberOfElements =0;
	}
	
	/**
	 * @param totalElements
	 */
	public PaginatedResponse(long totalElements, int numberOfElements) {
		this.totalElements = totalElements;
		this.numberOfElements = numberOfElements;
	}
	
	public PaginatedResponse(String noResultsMessage) {
		super(noResultsMessage);
		this.numberOfElements = 0;
		this.totalElements = 0L;
	}

	/* (non-Javadoc)
	 * @see com.ebay.park.service.ListedResponse#listIsEmpty()
	 */
	@Override
	public boolean listIsEmpty() {
		return totalElements == 0;
	}

	/**
	 * Returns the total amount of elements.
	 * 
	 * @return the total amount of elements
	 */
	public long getTotalElements() {
		return totalElements;
	}

	/**
	 * Returns the number of elements currently on this page.
	 * 
	 * @return the number of elements currently on this page
	 */
	public int getNumberOfElements(){
		return numberOfElements;
	}
	
}
