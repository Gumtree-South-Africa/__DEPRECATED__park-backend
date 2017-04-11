package com.ebay.park.elasticsearch.document.nested;

/**
 * @author l.marino on 6/26/15.
 */
public class ESNUserFollowsGroup {
    private Long userId;
    private Long groupId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public ESNUserFollowsGroup() {
    }

    public ESNUserFollowsGroup(Long userId, Long groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }
}
