package com.ebay.park.service.admin.dto;

import com.ebay.park.service.PaginatedResponse;

import java.util.List;

public class ListModeratorsResponse extends PaginatedResponse {

	private List<SmallUserAdmin> moderators;
	
	public ListModeratorsResponse(List<SmallUserAdmin> moderators,  long amountModerators) {
		super(amountModerators, moderators.size());
		this.moderators = moderators;
	}

	public List<SmallUserAdmin> getModerators() {
		return moderators;
	}


	@Override
	public boolean listIsEmpty() {
		return moderators == null || moderators.isEmpty();
	}
}
