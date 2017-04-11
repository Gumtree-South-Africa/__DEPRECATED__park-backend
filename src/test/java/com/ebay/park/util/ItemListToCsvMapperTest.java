package com.ebay.park.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Spy;

import com.ebay.park.service.item.dto.FacebookBusinessItem;


public class ItemListToCsvMapperTest {
	
	private static final Long ITEM_ID = 1L;
	private static final String AVAILABILITY = "in stock";
	private static final String CONDITION = "used";
	private static final String DESCRIPTION = "description";
	private static final String IMAGE_LINK = "image_link";
	private static final String LINK = "link";
	private static final String TITLE = "title";
	private static final String PRICE = "price";
	private static final String BRAND = "N/A";
	private static final String GOOGLE_PRODUCT_CATEGORY = "google_product_category";

	@Spy
	private ItemListToCsvMapper itemListToCsvMapper = new ItemListToCsvMapper();
	
	@Test
	public void givenANullListWhenGettingActiveItemsCsvFormatThenReturnEmptyString() {
		String result = itemListToCsvMapper.getActiveItemsCommaDelimited(null);
		Assert.assertNull(result);
	}
	
	@Test
	public void givenAListWhenGettingActiveItemsCsvFormatThenReturnCsvFormat() {
		//given
		FacebookBusinessItem facebookBusinessItem = new FacebookBusinessItem(ITEM_ID,
				DESCRIPTION, IMAGE_LINK, LINK, TITLE, PRICE, GOOGLE_PRODUCT_CATEGORY);
		
		List<FacebookBusinessItem> activeItems = new ArrayList<>();
		activeItems.add(facebookBusinessItem);
		
		//when
		String result = itemListToCsvMapper.getActiveItemsCommaDelimited(activeItems);
		
		//then
		assertNotNull(result);
		assertEquals(result, buildExpectedResult());
	}
	
	private String buildExpectedResult() {
		StringBuilder builder = new StringBuilder();
		builder.append("id,availability,condition,description,image_link," +
				"link,title,price,brand,google_product_category")
				.append(System.getProperty("line.separator"))
				.append(ITEM_ID).append(",")
				.append(AVAILABILITY).append(",")
				.append(CONDITION).append(",")
				.append(DESCRIPTION).append(",")
				.append(IMAGE_LINK).append(",")
				.append(LINK).append(",")
				.append(TITLE).append(",")
				.append(PRICE).append(",")
				.append(BRAND).append(",")
				.append(GOOGLE_PRODUCT_CATEGORY);
		
		return builder.toString();
		
	}
		

}
