package com.ebay.park.service.moderationMode.dto;

import com.ebay.park.db.entity.Item;
import com.ebay.park.service.item.dto.AbstracterItemSummary;

/**
 * Summary of an item.
 * @author Julieta Salvadó
 *
 */
public class ItemSummaryForModeration extends AbstracterItemSummary {
	private String pictureUrl1;
	private String pictureUrl2;
	private String pictureUrl3;
	private String pictureUrl4;
	private boolean pendingModeration;

	/**
	 * @return the pictureUrl1
	 */
	public String getPictureUrl1() {
		return pictureUrl1;
	}
	/**
	 * @param pictureUrl1 the pictureUrl1 to set
	 */
	public void setPictureUrl1(String pictureUrl1) {
		this.pictureUrl1 = pictureUrl1;
	}
	/**
	 * @return the pictureUrl2
	 */
	public String getPictureUrl2() {
		return pictureUrl2;
	}
	/**
	 * @param pictureUrl2 the pictureUrl2 to set
	 */
	public void setPictureUrl2(String pictureUrl2) {
		this.pictureUrl2 = pictureUrl2;
	}
	/**
	 * @return the pictureUrl3
	 */
	public String getPictureUrl3() {
		return pictureUrl3;
	}
	/**
	 * @param pictureUrl3 the pictureUrl3 to set
	 */
	public void setPictureUrl3(String pictureUrl3) {
		this.pictureUrl3 = pictureUrl3;
	}
	/**
	 * @return the pictureUrl4
	 */
	public String getPictureUrl4() {
		return pictureUrl4;
	}
	/**
	 * @param pictureUrl4 the pictureUrl4 to set
	 */
	public void setPictureUrl4(String pictureUrl4) {
		this.pictureUrl4 = pictureUrl4;
	}
	
	/**
	 * Load data from an Item.
	 * Warning: category data needs internationalization.
	 * @param item
	 * 		Item containing the data to copy
	 * @return
	 * 		Data copied
	 */
	public static ItemSummaryForModeration fromItem(Item item) {
		ItemSummaryForModeration itemSummary = new ItemSummaryForModeration();
		itemSummary.setPrice(item.getPrice());
		itemSummary.setName(item.getName());
		itemSummary.setPictureUrl1(item.getPicture1Url());
		itemSummary.setPictureUrl2(item.getPicture2Url());
		itemSummary.setPictureUrl3(item.getPicture3Url());
		itemSummary.setPictureUrl4(item.getPicture4Url());
		itemSummary.setId(item.getId());
		itemSummary.setDescription(item.getDescription());
		itemSummary.setTotalOfFollowers(
				item.getFollowers() != null ? item.getFollowers().size() : 0);
		itemSummary.setPendingModeration(item.isPendingModeration());
		
		return itemSummary;
	}
	/**
	 * @return the pendingModeration
	 */
	public boolean isPendingModeration() {
		return pendingModeration;
	}
	/**
	 * @param pendingModeration the pendingModeration to set
	 */
	public void setPendingModeration(boolean pendingModeration) {
		this.pendingModeration = pendingModeration;
	}
}
