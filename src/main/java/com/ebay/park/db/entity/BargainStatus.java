package com.ebay.park.db.entity;

/**
 * @author marcos.lambolay
 */
public enum BargainStatus {
	REJECTED("REJECTED"), ACCEPTED("ACCEPTED");

	private final String description;

	private BargainStatus(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return description;
	}
}