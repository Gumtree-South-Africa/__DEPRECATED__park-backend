package com.ebay.park.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author federico.jaite
 *
 */
public abstract class ListedResponse {
	
	@JsonInclude(Include.NON_EMPTY)
	private String noResultsMessage;

	@JsonInclude(Include.NON_EMPTY)
	private String noResultsHintMessage;

	public ListedResponse() {
		super();
	}
	
	public ListedResponse(String noResultsMessage) {
		super();
		this.noResultsMessage = noResultsMessage;
	}
	
	public String getNoResultsMessage() {
		return noResultsMessage;
	}

	public void setNoResultsMessage(String noResultsMessage) {
		this.noResultsMessage = noResultsMessage;
	}
	
	public abstract boolean listIsEmpty();

	/**
	 * @return the noResultsHintMessage
	 */
	public String getNoResultsHintMessage() {
		return noResultsHintMessage;
	}

	/**
	 * @param noResultsHintMessage the noResultsHintMessage to set
	 */
	public void setNoResultsHintMessage(String noResultsHintMessage) {
		this.noResultsHintMessage = noResultsHintMessage;
	}
}
