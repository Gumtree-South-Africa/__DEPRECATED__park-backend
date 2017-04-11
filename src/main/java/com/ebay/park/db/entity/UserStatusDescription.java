package com.ebay.park.db.entity;

/**
 * Enum to list possibly user status
 *
 * @author cbirge
 *
 */
public enum UserStatusDescription {
	ACTIVE("ACTIVE"), BANNED("BANNED"), LOCKED("LOCKED");

	private final String description;

	private UserStatusDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return description;
	}
}