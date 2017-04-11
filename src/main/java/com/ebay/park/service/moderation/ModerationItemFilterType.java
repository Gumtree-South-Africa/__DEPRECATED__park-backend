package com.ebay.park.service.moderation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ModerationItemFilterType {
	BLACKLISTED("BLACKLISTED"), FLAGGED("FLAGGED"),ACTIVE("ACTIVE"), SOLD("SOLD"),EXPIRED("EXPIRED"),NOT_VERIFIED("NOT_VERIFIED");

	private String value;

	private static Logger logger = LoggerFactory
			.getLogger(ModerationItemFilterType.class);

	private ModerationItemFilterType(String value) {
		this.value = value;
	}

	public static ModerationItemFilterType getEnum(String value) {
		if (value != null) {
			for (ModerationItemFilterType type : values()) {
				if (type.value.equals(value)) {
					return type;
				}
			}
		}
        logger.info("Filter type value not found returning default, value= {}", value);
		return null;
	}

	public String toValue() {
		return value;
	}

}
