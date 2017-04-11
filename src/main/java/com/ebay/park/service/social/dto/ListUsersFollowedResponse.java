package com.ebay.park.service.social.dto;

import com.ebay.park.service.ListedResponse;

import java.util.List;

/**
 * @author federico.jaite
 * 
 */
public class ListUsersFollowedResponse extends ListedResponse {

	private List<BasicUser> users;

	public ListUsersFollowedResponse(List<BasicUser> users) {
		super();
		this.users = users;
	}

	public List<BasicUser> getUsers() {
		return users;
	}

	@Override
	public boolean listIsEmpty() {
		return users == null || users.isEmpty();
	}

}