package com.ebay.park.service.group.dto;

import com.ebay.park.db.entity.Group;

public class SmallGroupNameDTO {
    /**
     * Group id.
     */
    private Long id;
    /**
     * Group name.
     */
    private String name;

    public SmallGroupNameDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public static SmallGroupNameDTO fromGroup(Group group) {
        return new SmallGroupNameDTO(group.getGroupId(), group.getName());
    }
}
