package com.ebay.park.service.item.dto;

import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ItemSummaryTest {

	@Test
	public void fromItem() {
		Item item = new Item("item name", Double.valueOf("12.35"), "version", false, false);
		item.setPicture1Url("pictur1");
		item.setPublishedBy(new User());
		item.getPublishedBy().setUserId(232L);
		item.getPublishedBy().setUsername("Jonh");
		item.setPublished(new Date());
		item.activate();


		ItemSummary itemSummary = ItemSummary.fromItem(item);

		assertEquals("item name", itemSummary.getName());
		assertEquals("pictur1", itemSummary.getPictureUrl());
		assertEquals(Double.valueOf("12.35"), itemSummary.getPrice());
		assertEquals(Long.valueOf(232), itemSummary.getUser().getId());
		assertEquals("Jonh", itemSummary.getUser().getUsername());

	}

}