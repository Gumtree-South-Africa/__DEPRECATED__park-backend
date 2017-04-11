package com.ebay.park.event.item;

import com.ebay.park.db.entity.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * Event when an item is banned.
 * @author Julieta Salvadó
 */
public class ItemBannedEvent extends ItemNotificationEvent {

    /**
     * URL to the item in the website.
     */
    private String url;

    public ItemBannedEvent(Item item, String url) {
        super(item);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> props = new HashMap<String, String>();
        props.put(ITEM_NAME, getItem().getName());
        props.put(ITEM_ID, getItem().getId().toString());
        props.put(USER_NAME, getItem().getPublishedBy().getUsername());
        props.put(URL, getUrl());
        return props;
    }
}
