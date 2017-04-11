package com.ebay.park.service.moderation.dto;

import com.ebay.park.db.entity.Item;
import com.ebay.park.util.DataCommonUtil;

public class ModerationItemSummary {

	private Long id;

	private String name;
	private String description;
	private String picture1Url;
	private String picture2Url;
	private String picture3Url;
	private String picture4Url;
	private String status;
	private String publishDate;
	private String lastModificationDate;
	private Integer countOfReports;
	private String publishedBy;
	private String email;//Item's owner email
	private String testing;
	private String URL;

	public static ModerationItemSummary fromItem(Item item) {
		ModerationItemSummary itemSummary = new ModerationItemSummary();
		itemSummary.setId(item.getId());
		itemSummary.setName(item.getName());
		itemSummary.setDescription(item.getDescription());
		itemSummary.setPicture1Url(item.getPicture1Url());
		itemSummary.setPicture2Url(item.getPicture2Url());
		itemSummary.setPicture3Url(item.getPicture3Url());
		itemSummary.setPicture4Url(item.getPicture4Url());
		if (item.getPublished() != null) {
			itemSummary.setPublishDate(DataCommonUtil.getDateTimeAsISO(item
					.getPublished()));
		}
		if (item.getLastModificationDate() != null) {
			itemSummary.setLastModificationDate(DataCommonUtil
					.getDateTimeAsISO(item.getLastModificationDate()));
		}
		if (item.getStatus() != null) {
			itemSummary.setStatus(item.getStatus().toString());
		}
		if (item.getPublishedBy() != null) {
			itemSummary.setPublishedBy(item.getPublishedBy().getUsername());
			itemSummary.email = item.getPublishedBy().getEmail();
		}
		itemSummary.setCountOfReports(item.getCountOfReports());

		return itemSummary;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicture1Url() {
		return picture1Url;
	}

	public void setPicture1Url(String picture1Url) {
		this.picture1Url = picture1Url;
	}

	public String getPublishedBy() {
		return publishedBy;
	}

	public void setPublishedBy(String publishedBy) {
		this.publishedBy = publishedBy;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(String lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	public Integer getCountOfReports() {
		return countOfReports;
	}

	public void setCountOfReports(Integer countOfReports) {
		this.countOfReports = countOfReports;
	}

	public String getEmail() {
		return email;
	}

	public String getPicture2Url() {
		return picture2Url;
	}

	public void setPicture2Url(String picture2Url) {
		this.picture2Url = picture2Url;
	}

	public String getPicture3Url() {
		return picture3Url;
	}

	public void setPicture3Url(String picture3Url) {
		this.picture3Url = picture3Url;
	}

	public String getPicture4Url() {
		return picture4Url;
	}

	public void setPicture4Url(String picture4Url) {
		this.picture4Url = picture4Url;
	}

	/**
	 * @return the testing
	 */
	public String getTesting() {
		return testing;
	}

	/**
	 * @param testing the testing to set
	 */
	public void setTesting(String testing) {
		this.testing = testing;
	}

	public String getURL() {
		return URL;
	}
	
	public void setURL(String URL) {
		this.URL = URL;
	}

}
