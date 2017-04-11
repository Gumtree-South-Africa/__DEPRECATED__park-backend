package com.ebay.park.service.group.dto;

import java.util.List;

/**
 * Response with groups names and ids.
 * @author Julieta Salvadó
 *
 */
public class ListGroupsNamesResponse {
    private List<SmallGroupNameDTO> groups;

    /**
     * Constructor.
     * @param groups
     */
    public ListGroupsNamesResponse(List<SmallGroupNameDTO> groups) {
        setGroups(groups);
    }
    /**
     * @return the groups
     */
    public List<SmallGroupNameDTO> getGroups() {
        return groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(List<SmallGroupNameDTO> groups) {
        this.groups = groups;
    }

    /**
     * Return the size of the group list.
     * @return list size
     */
    public int getNumberOfElements() {
        if (groups == null) {
            return 0;
        } else {
            return groups.size();
        }
    }
}
