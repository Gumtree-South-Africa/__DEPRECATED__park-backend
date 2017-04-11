package com.ebay.park.service.group.dto;


public class SubscriberDTO {
	
	private long userId;
	private String username;
	private String picture;

	public SubscriberDTO(long userId, String username, String picture) {
		super();
		this.userId = userId;
		this.username = username;
		this.picture = picture;
	}

	public long getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public String getPicture() {
		return picture;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SubscriberDTO that = (SubscriberDTO) o;

		if (userId != that.userId) return false;
		return username.equals(that.username);

	}

	@Override
	public int hashCode() {
		int result = (int) (userId ^ (userId >>> 32));
		result = 31 * result + username.hashCode();
		return result;
	}
}
