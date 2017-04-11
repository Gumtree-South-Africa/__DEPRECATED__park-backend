package com.ebay.park.service.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ItemFilterType {
	
    DISTANCE("DISTANCE"), CATEGORY("CATEGORY"), PRICE_RANGE("PRICE");

    private String value;
    private static Logger logger = LoggerFactory
            .getLogger(ItemFilterType.class);

    private ItemFilterType(String value) {
        this.value = value;
    }

    public static ItemFilterType getEnum(String value) {
        if (value != null) {
            for (ItemFilterType type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
        }
        logger.info("Filter type value not found returning default, value= {}", value);
        return getDefault();
    }

    public String toValue() {
        return value;
    }

    public static ItemFilterType getDefault() {
        return CATEGORY;
    }
}
