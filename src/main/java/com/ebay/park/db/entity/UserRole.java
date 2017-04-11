package com.ebay.park.db.entity;

public enum UserRole {

	SUPER_ADMIN("SUPER_ADMIN"), MODERATOR("MODERATOR");

	private final String name;

	private UserRole(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
