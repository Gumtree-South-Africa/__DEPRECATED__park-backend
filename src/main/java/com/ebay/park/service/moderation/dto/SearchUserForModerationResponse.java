package com.ebay.park.service.moderation.dto;

import java.util.List;

public class SearchUserForModerationResponse {

	private List<ModerationUserSummary> users;
	private int amountUsersFound;

	public List<ModerationUserSummary> getUsers() {
		return users;
	}

	public void setUsers(List<ModerationUserSummary> users) {
		this.users = users;
	}

	public int getAmountUsersFound() {
		return amountUsersFound;
	}

	public void setAmountUsersFound(int amountUsersFound) {
		this.amountUsersFound = amountUsersFound;
	}
}
