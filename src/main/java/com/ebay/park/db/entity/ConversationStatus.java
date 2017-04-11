package com.ebay.park.db.entity;

/**
 * @author marcos.lambolay
 */
public enum ConversationStatus {
	OPEN("OPEN"), CANCELLED("CANCELLED"), ACCEPTED("ACCEPTED");

	private final String description;

	private ConversationStatus(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return description;
	}
}