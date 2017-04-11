package com.ebay.park.service.item.dto;

import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.ItemGroup;
import com.ebay.park.service.group.dto.CreateGroupResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SmallGroupDTO {

	protected Long id;
	protected String name;
	protected String pictureUrl;
	protected Integer totalSubscribers;
	protected Integer totalItems;
	protected String locationName;
	protected String location;
	
	@JsonInclude(Include.NON_NULL) 
	protected Boolean subscribed;
	protected String zipCode; 
	protected SmallUser owner;
	protected String URL;

	@JsonInclude(Include.NON_NULL)
	protected Long newItems;

	public SmallGroupDTO() {
	}

	public SmallGroupDTO(Long id, String name, String pictureUrl) {
		this.id = id;
		this.name = name;
		this.pictureUrl = pictureUrl;
	}

	public static SmallGroupDTO fromGroup(Group group) {
		SmallGroupDTO smallGroup = new SmallGroupDTO(group.getId(), group.getName(), group.getPicture());
		smallGroup.setOwner(new SmallUser(group.getCreator()));
		setGroupFields( smallGroup,  group);
		return smallGroup;
	}
	
	public static SmallGroupDTO fromCreateGroupResponse(
			CreateGroupResponse createGroupResponse) {
		SmallGroupDTO smallGroup = new SmallGroupDTO(createGroupResponse
				.getGroup().getId(), createGroupResponse.getGroup().getName(),
				createGroupResponse.getGroup().getPicture());
		setGroupFields(smallGroup, createGroupResponse.getGroup());
		
		return smallGroup;
	}

	static protected void setGroupFields(SmallGroupDTO smallGroup, Group group) {		
		smallGroup.setZipCode(group.getZipcode());
	    smallGroup.setLocationName(group.getLocationName());
	    smallGroup.setLocation(group.getLatitude() + "," + group.getLongitude());
	   
		int followersSize = (group.getFollowers() != null ? group
				.getFollowers().size() : 0);
		smallGroup.setTotalSubscribers(followersSize);
		
		// FIXME Replace this with a query
		int totalItems = 0;
		if (group.getItems() != null){
			for ( ItemGroup ig: group.getItems()){
				if (ig.getItem().isActive()){
					totalItems++;
				}
			}
		}	
		smallGroup.setTotalItems(totalItems);
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	protected void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public Integer getTotalSubscribers() {
		return totalSubscribers;
	}

	public void setTotalSubscribers(Integer totalSubscribers) {
		this.totalSubscribers = totalSubscribers;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Boolean getSubscribed() {
		return subscribed;
	}

	public void setSubscribed(Boolean subscribed) {
		this.subscribed = subscribed;
	}
	
	/**
	 * @return the totalItems
	 */
	public Integer getTotalItems() {
		return totalItems;
	}

	/**
	 * @param totalItems the totalItems to set
	 */
	public void setTotalItems(Integer totalItems) {
		this.totalItems = totalItems;
	}

	@JsonInclude(Include.NON_NULL)
	public SmallUser getOwner() {
		return owner;
	}

	public void setOwner(SmallUser owner) {
		this.owner = owner;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	
	public void setURL(String URL) {
		this.URL = URL;		
	}	
	
	public String getURL() {
		return URL;
	}

	public void setNewItems(Long value) {
		this.newItems = value;
	}

	public Long getNewItems() {
		return newItems;
	}
}
