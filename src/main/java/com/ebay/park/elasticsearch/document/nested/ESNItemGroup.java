package com.ebay.park.elasticsearch.document.nested;

/**
 * @author l.marino on 6/18/15.
 * ESN prefix:Elasticsearch Nested Object
 */
public class ESNItemGroup {

    private Long itemId;

    private Long groupId;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public ESNItemGroup(Long itemId, Long groupId) {
        this.itemId = itemId;
        this.groupId = groupId;
    }

    public ESNItemGroup() {
    }
}
