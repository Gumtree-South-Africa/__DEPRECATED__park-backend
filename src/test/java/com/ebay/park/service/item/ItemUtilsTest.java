package com.ebay.park.service.item;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import com.ebay.park.db.entity.Item;


public class ItemUtilsTest {
	
	private static final String PICTURE1 = "url1";
	private static final String PICTURE2 = "url2";
	private static final String PICTURE3 = "url3";
	private static final String PICTURE4 = "url4";
	private static final String PICTURE5 = "url5";
    private static final String OLD_PICTURE = "old";
    private static final String ITEM_NAME = "name";
    private static final String VERSION = "versionPublish";
    private static final double PRICE = 10L;
    private static final Date ITEM_PUBLISHED_DATE_TODAY = DateTime.now().toDate();
    private static final Date ITEM_PUBLISHED_DATE_LAST_WEEK = DateTime.now().minusDays(8).toDate();
    
    ItemUtils itemUtils = new ItemUtils();
	private int maxAmountPictures = 4;
	private int republishLimitOfDays = 7;
	
	
	@Before
	public void setUp(){
		Whitebox.setInternalState(itemUtils, "maxAmountPictures", maxAmountPictures);
		Whitebox.setInternalState(itemUtils, "republishLimitOfDays", republishLimitOfDays);
	}
	
	@Test
	public void givenValidValuesWhenSettingPicturesThenSuccess() throws IllegalAccessException, InvocationTargetException {
		Item item = new Item("name", 10d, "versionPublish", false, false);
		Map<Integer, String> photos = new HashMap<>(maxAmountPictures);
		photos.put(1, PICTURE1);
		photos.put(2, PICTURE2);

		itemUtils.setPictures(photos, item);

		assertEquals(PICTURE1, item.getPicture1Url());
		assertEquals(PICTURE2, item.getPicture2Url());
        assertNull(item.getPicture3Url());
        assertNull(item.getPicture4Url());
	}

	@Test
    public void givenValidValuesWhenSettingPicturesOnExistingItemThenSuccess() throws IllegalAccessException, InvocationTargetException {
        Item item = new Item("name", 10d, "versionPublish", false, false);
        item.setPicture1Url(OLD_PICTURE);
        String[] photos = {PICTURE1, PICTURE2};

        itemUtils.setPictures(photos, item);

        assertEquals(PICTURE1, item.getPicture1Url());
        assertEquals(PICTURE2, item.getPicture2Url());
        assertNull(item.getPicture3Url());
        assertNull(item.getPicture4Url());
    }

	@Test
    public void givenNullValuesWhenSettingPicturesOnExistingItemThenSuccess() throws IllegalAccessException, InvocationTargetException {
        Item item = new Item("name", 10d, "versionPublish", false, false);
        item.setPicture1Url(OLD_PICTURE);
        String[] photos = {PICTURE1, null, PICTURE2};

        itemUtils.setPictures(photos, item);

        assertEquals(PICTURE1, item.getPicture1Url());
        assertEquals(PICTURE2, item.getPicture2Url());
        assertNull(item.getPicture3Url());
        assertNull(item.getPicture4Url());
    }

	@Test (expected = IllegalArgumentException.class)
    public void givenTooMuchPicturesWhenSettingPicturesOnExistingItemThenException() throws IllegalAccessException, InvocationTargetException {
        Item item = new Item(ITEM_NAME, PRICE, VERSION, false, false);
        item.setPicture1Url(OLD_PICTURE);
        String[] photos = {PICTURE1, PICTURE2, PICTURE3, PICTURE4, PICTURE5};

        itemUtils.setPictures(photos, item);
    }

	@Test
	public void givenNullPicturesWhenRearrangingThenSuccess() {
	    Item item = new Item(ITEM_NAME, PRICE, VERSION, false, false);
	    item.setPicture1Url(PICTURE1);
	    item.setPicture2Url(null);
	    item.setPicture3Url(PICTURE3);
	    item.setPicture4Url(null);

	    itemUtils.rearrangeItemPictures(item);
	    assertEquals(PICTURE1, item.getPicture1Url());
        assertEquals(PICTURE3, item.getPicture2Url());
        assertNull(item.getPicture3Url());
        assertNull(item.getPicture4Url());
	}

	/**
	 *  "isAbleToRepublish" method unit test
	 */
	@Test
	public void givenAnItemNotAbleToRepublishThenFalse() {
		//given
		Item item = new Item(ITEM_NAME, PRICE, VERSION, false, false); 
		item.setPublished(ITEM_PUBLISHED_DATE_TODAY);
		
		//when
		boolean response = itemUtils.isAbleToRepublish(item);
		
		//then
		assertEquals(false, response);
	}
	
	/**
	 *  "isAbleToRepublish" method unit test
	 */
	@Test
	public void givenAnItemAbleToRepublishThenFalse() {
		//given
		Item item = new Item(ITEM_NAME, PRICE, VERSION, false, false); 
		item.setPublished(ITEM_PUBLISHED_DATE_LAST_WEEK);
		
		//when
		boolean response = itemUtils.isAbleToRepublish(item);
		
		//then
		assertEquals(true, response);
	}
	
}
