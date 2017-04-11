package com.ebay.park.service.item.dto;

import java.util.ArrayList;
import java.util.List;

import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.ItemGroup;
import com.ebay.park.db.entity.User;
import com.ebay.park.util.DataCommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ItemSummary extends AbstracterItemSummary {

	private String pictureUrl;
	private Double latitude;
	private Double longitude;
	private String published;
	private SmallUser user;
	private List<SmallGroupDTO> groups;
	private String status;
	private String localizedStatus;
	private String zipCode;

	@JsonInclude(Include.NON_NULL)
	private Boolean newItem;


	@JsonInclude(Include.NON_EMPTY)
	private String publishedBy;

	@JsonInclude(Include.NON_NULL)
	private Boolean followedByUser;

	/**
	 * Load data from an Item.
	 * Warning: category data needs internationalization.
	 * @param item
	 * 		Item containing the data to copy
	 * @return
	 * 		Data copied
	 */
	public static ItemSummary fromItem(Item item) {
		ItemSummary itemSummary = new ItemSummary();
		itemSummary.setPrice(item.getPrice());
		itemSummary.setName(item.getName());
		itemSummary.setPictureUrl(item.getPicture1Url());
		itemSummary.setId(item.getId());

		itemSummary.setUser(new SmallUser(item));
		itemSummary.setLatitude(item.getLatitude());
		itemSummary.setLongitude(item.getLongitude());
		itemSummary.setZipCode(item.getZipCode());
		itemSummary.setPublished(item.getPublished() != null ? DataCommonUtil
				.getDateTimeAsISO(item.getPublished()) : null);
		itemSummary.setDescription(item.getDescription());
		itemSummary.setTotalOfFollowers(
				item.getFollowers() != null ? item.getFollowers().size() : 0);

		List<ItemGroup> itemGroups = item.getItemGroups();
		List<SmallGroupDTO> groups = new ArrayList<SmallGroupDTO>();
		if (itemGroups != null) {
			for (ItemGroup itemGroup : itemGroups) {
				groups.add(SmallGroupDTO.fromGroup(itemGroup.getGroup()));
			}
		}
		itemSummary.setGroups(groups);
		itemSummary.setStatus(item.getStatus().toString());
		itemSummary.setLocalizedStatus(item.getLocalizedStatus());
		
		return itemSummary;
	}

	/**
	 * Load item data.
	 * Warning: category data needs internationalization.
	 * @param item
	 * @return
	 */
	public static ItemSummary fromPublishedItem(Item item) {
		ItemSummary itemSummary = fromItem(item);
		itemSummary.setPublishedBy(item.getPublishedBy().getUsername());

		return itemSummary;
	}

	/**
	 * Load item data.
	 * Warning: category data needs internationalization.
	 * @param item
	 * @param user
	 * @return
	 */
	public static ItemSummary fromFollowedItem(Item item, User user) {
		ItemSummary itemSummary = fromItem(item);
		if (user != null){
			itemSummary.setFollowedByUser(item.isFollowedByUser(user));
		}
		itemSummary.setPublishedBy(item.getPublishedBy().getUsername());
		return itemSummary;
	}

	private void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	private void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getPublishedBy() {
		return publishedBy;
	}

	public void setPublishedBy(String publishedBy) {
		this.publishedBy = publishedBy;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	/**
	 * @return the user
	 */
	public SmallUser getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(SmallUser user) {
		this.user = user;
	}

	public Boolean getFollowedByUser() {
		return followedByUser;
	}

	public void setFollowedByUser(Boolean followedByUser) {
		this.followedByUser = followedByUser;
	}

	public List<SmallGroupDTO> getGroups() {
		return groups;
	}

	public void setGroups(List<SmallGroupDTO> groups) {
		this.groups = groups;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * @return the localizedStatus
	 */
	public String getLocalizedStatus() {
		return localizedStatus;
	}

	/**
	 * @param localizedStatus the localizedStatus to set
	 */
	public void setLocalizedStatus(String localizedStatus) {
		this.localizedStatus = localizedStatus;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public Boolean getNewItem() {
		return newItem;
	}
	
	public void setNewItem(Boolean newItem) {
		this.newItem = newItem;
	}
}
