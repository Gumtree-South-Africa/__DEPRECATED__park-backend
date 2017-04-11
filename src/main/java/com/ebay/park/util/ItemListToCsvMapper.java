package com.ebay.park.util;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ebay.park.service.item.dto.FacebookBusinessItem;

/**
 * Converts a list of {@link FacebookBusinessItem} to csv format.
 * @author scalderon
 *
 */
@Component
public class ItemListToCsvMapper {
	
	private static final String CSV_FIRST_ROW = "id,availability,condition," +
			"description,image_link,link,title,price,brand,google_product_category";
	
	/**
	 * Given a list of facebook Business items returns a csv format
	 * @param facebookBusinessItems
	 * 			a list of Facebook Business items
	 * @return a csv string
	 */
	public String getActiveItemsCommaDelimited(List<FacebookBusinessItem> facebookBusinessItems) {
		if (!StringUtils.isEmpty(facebookBusinessItems)) {
			StringBuilder builder = new StringBuilder();
			builder.append(CSV_FIRST_ROW);
			facebookBusinessItems.forEach(item -> builder.append(buildItemCommaDelimited(item)));
			return builder.toString();
		}
		return null;
	}
	
	private String buildItemCommaDelimited(FacebookBusinessItem item) {
		StringBuilder itemBuilder = new StringBuilder();
		itemBuilder.append(System.getProperty("line.separator"))
			.append(item.getId()).append(",")
			.append(item.getAvailability()).append(",")
			.append(item.getCondition()).append(",")
			.append(item.getDescription()).append(",")
			.append(item.getImageLink()).append(",")
			.append(item.getLink()).append(",")
			.append(item.getTitle()).append(",")
			.append(item.getPrice()).append(",")
			.append(item.getBrand()).append(",")
			.append(item.getGoogleProductCategory());
		return itemBuilder.toString();
	}
}
