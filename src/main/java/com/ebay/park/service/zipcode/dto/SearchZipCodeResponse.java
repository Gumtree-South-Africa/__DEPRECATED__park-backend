/**
 * 
 */
package com.ebay.park.service.zipcode.dto;

import java.util.ArrayList;
import java.util.List;

import com.ebay.park.service.ListedResponse;

/**
 * @author jppizarro
 *
 */
public class SearchZipCodeResponse extends ListedResponse {

	private List<String> zipCodes = new ArrayList<>();
	
	private int totalElements;
	
	public SearchZipCodeResponse(List<String> zipCodes, int totalElements) {
		this.zipCodes = zipCodes;
		this.totalElements = totalElements;
	}

	public List<String> getZipCodes() {
		return zipCodes;
	}

	public void setZipCodes(List<String> zipCodes) {
		this.zipCodes = zipCodes;
	}
	
	public int getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}

	@Override
	public boolean listIsEmpty() {
		return zipCodes.isEmpty();
	}

}
