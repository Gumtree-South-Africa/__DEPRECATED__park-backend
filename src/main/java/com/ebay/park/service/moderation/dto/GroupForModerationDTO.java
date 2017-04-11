package com.ebay.park.service.moderation.dto;

import com.ebay.park.db.entity.Group;

public class GroupForModerationDTO {

	private Long id;
	private String name;
	private String pictureUrl;
	private String creatorName;
	/**
	 * It represents if the group is either mobile or email verified.
	 */
	private boolean ownerVerified;
	private Integer totalSubscribers;
	private String locationName;


	public GroupForModerationDTO() {
	}

	public GroupForModerationDTO(Long id, String name, String pictureUrl) {
		this.id = id;
		this.name = name;
		this.pictureUrl = pictureUrl;
	}

	public static GroupForModerationDTO fromGroup(Group group) {
		GroupForModerationDTO smallGroup = new GroupForModerationDTO(group.getId(), group.getName(), group.getPicture());

		smallGroup.locationName = group.getLocationName();
		smallGroup.totalSubscribers = (group.getFollowers() != null ? group.getFollowers().size() : 0);
		smallGroup.creatorName = group.getCreator().getUsername();	
		smallGroup.ownerVerified = group.getCreator().isEmailVerified() || group.getCreator().isMobileVerified();

		return smallGroup;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public Integer getTotalSubscribers() {
		return totalSubscribers;
	}

	/**
	 * @return the locationName
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * @return the creatorName
	 */
	public String getCreatorName() {
		return creatorName;
	}

	public boolean getOwnerVerified() {
		return ownerVerified;
	}

	public void setOwnerVerified(boolean ownerVerified) {
		this.ownerVerified = ownerVerified;
	}

}
