package com.ebay.park.db.entity;

import org.apache.commons.lang3.StringUtils;

/**
 * Enum for describing the status of the user session.
 * @author Julieta Salvadó
 *
 */
public enum SessionStatusDescription {
	//@formatter:off
	ACTIVE_SESSION("ACTIVE_SESSION"),
	INACTIVE_SESSION("INACTIVE_SESSION"),
	NO_SESSION("NO_SESSION"),
	WITH_SESSION("WITH_SESSION");
	//@formatter:on
	
	private final String status;
	private static String defaultValue = "WITH_SESSION";

	private SessionStatusDescription(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return status;
	}
	
	public static SessionStatusDescription getSessionStatusByValue(String value) {
		if (validValue(value)) {
			return SessionStatusDescription.valueOf(value.toUpperCase());
		}
		return SessionStatusDescription.valueOf(defaultValue);
	}

	private static boolean validValue(String value) {
		return StringUtils.equalsIgnoreCase(value, SessionStatusDescription.ACTIVE_SESSION.status)
				|| StringUtils.equalsIgnoreCase(value, SessionStatusDescription.INACTIVE_SESSION.status) 
				|| StringUtils.equalsIgnoreCase(value, SessionStatusDescription.NO_SESSION.status)
				|| StringUtils.equalsIgnoreCase(value, SessionStatusDescription.WITH_SESSION.status);
	}
}
