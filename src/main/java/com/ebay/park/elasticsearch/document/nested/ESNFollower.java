package com.ebay.park.elasticsearch.document.nested;

/**
 * @author l.marino on 6/25/15.
 */
public class ESNFollower {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ESNFollower() {
    }

    public ESNFollower(Long id) {
        this.id = id;
    }
}
