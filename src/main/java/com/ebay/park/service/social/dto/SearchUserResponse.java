package com.ebay.park.service.social.dto;

import com.ebay.park.service.PaginatedResponse;

import java.util.List;

public class SearchUserResponse extends PaginatedResponse {

	private List<BasicUser> users;
	
	//FIXME coordinate with FE use totalElements instead
	private int amountUsersFound;
	
	/**
	* Response time.
	*/
	private String serverTime;

	public SearchUserResponse(List<BasicUser> users, long total) {
		super(total, users.size());
		this.users = users;
		this.amountUsersFound = users.size(); // Page size
	}
	
	public SearchUserResponse(List<BasicUser> users, long total,String serverTime) {
		this(users,total);
		this.setServerTime(serverTime);
	}

	/**
    * @return the serverTime
	*/
	public String getServerTime() {
		return serverTime;
	}

	/**
	* @param serverTime the serverTime to set
	*/
	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}
	
	public List<BasicUser> getUsers() {
		return users;
	}

	public void setUsers(List<BasicUser> users) {
		this.users = users;
	}

	public int getAmountUsersFound() {
		return amountUsersFound;
	}

	public void setAmountUsersFound(int amountUsersFound) {
		this.amountUsersFound = amountUsersFound;
	}

	@Override
	public boolean listIsEmpty() {
		return users == null || users.isEmpty();
	}
}
