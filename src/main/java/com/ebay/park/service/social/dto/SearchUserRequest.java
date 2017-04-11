/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.social.dto;

import com.ebay.park.service.PaginatedRequest;

/**
 * @author federico.jaite
 * 
 */
public class SearchUserRequest extends PaginatedRequest {


	private String username;
	private Double latitude;
	private Double longitude;
	private Double radius;
	private String criteria;
	private String order;
	private boolean followed;
	private boolean followingMe;
    private Long groupId;  
	
	public SearchUserRequest(String token, String language, Integer page, Integer pageSize) {
		super(token, language, page, pageSize);
	}

	public SearchUserRequest(String token, String language, Integer page,
			Integer pageSize, String username, Double latitude,
			Double longitude, Double radius, String criteria, String order, boolean followed,
			boolean followingMe, Long groupId) {
		super(token, language, page, pageSize);
		this.setUsername(username);
		this.setLatitude(latitude);
		this.setLongitude(longitude);
		this.setRadius(radius);
		this.setCriteria(criteria);
		this.setPage(page);
		this.setPageSize(pageSize);
		this.setOrder(order);
		this.setFollowed(followed);
		this.setFollowingMe(followingMe);
		this.setGroupId(groupId);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public boolean isFollowed() {
		return followed;
	}

	public void setFollowed(boolean followed) {
		this.followed = followed;
	}

	public boolean isFollowingMe() {
		return followingMe;
	}

	public void setFollowingMe(boolean followingMe) {
		this.followingMe = followingMe;
	}
	
	@Override
    public String getLanguage() {
		return language;
	}

	@Override
    public void setLanguage(String language) {
		this.language = language;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SearchUserRequest [username= ")
			.append(this.username)
			.append(", latitude= ").append(this.latitude)
			.append(", longitude= ").append(this.longitude)
			.append(", radius= ").append(this.radius)
			.append(", criteria= ").append(this.criteria)
			.append(", order= ").append(this.order)
			.append(", followed= ").append(this.followed)
			.append(", followingMe= ").append(this.followingMe)
			.append(", groupId= ").append(this.groupId);
		
		return builder.toString();
	}
}
