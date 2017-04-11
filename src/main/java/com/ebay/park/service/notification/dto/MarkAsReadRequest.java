package com.ebay.park.service.notification.dto;

import com.ebay.park.service.ParkRequest;

/**
 * Request to mark a feed as read.
 * @author Julieta Salvadó
 */
public class MarkAsReadRequest extends ParkRequest {

    private Long feedId;

    public MarkAsReadRequest(String parkToken, String lang, Long feedId) {
        super(parkToken, lang);
        this.feedId = feedId;
    }

    public Long getFeedId() {
        return feedId;
    }
}
