package com.ebay.park.service.conversation.dto;

import com.ebay.park.db.entity.Chat;
import com.ebay.park.service.item.dto.ItemSummary;
import com.ebay.park.service.item.dto.SmallUser;
import com.ebay.park.util.DataCommonUtil;

public class Offer {

	private ItemSummary item;

	private SmallUser user;

	private Double offeredPrice;

	private String comment;

	private boolean sold;

	private String postTime;

	private long chatId;

	public Offer() {
		super();
	}

	public static Offer fromChatSent(Chat chat) {
		Offer offer = new Offer();
		offer.setItem(ItemSummary.fromPublishedItem(chat.getItem()));
		offer.setUser(new SmallUser(chat.getReceiver()));
		offer.setOfferedPrice(chat.getOfferedPrice());
		offer.setComment(chat.getComment());
		offer.setPostTime(DataCommonUtil.getDateTimeAsUnixFormat(chat.getPostTime()));
		offer.setChatId(chat.getChatId());
		return offer;
	}

	public static Offer fromChatReceived(Chat chat) {
		Offer offer = new Offer();
		offer.setItem(ItemSummary.fromPublishedItem(chat.getItem()));
		offer.setUser(new SmallUser(chat.getSender()));
		offer.setOfferedPrice(chat.getOfferedPrice());
		offer.setComment(chat.getComment());
		offer.setPostTime(DataCommonUtil.getDateTimeAsUnixFormat(chat.getPostTime()));
		offer.setChatId(chat.getChatId());
		return offer;
	}

	public ItemSummary getItem() {
		return item;
	}

	public void setItem(ItemSummary item) {
		this.item = item;
	}

	public SmallUser getUser() {
		return user;
	}

	public void setUser(SmallUser user) {
		this.user = user;
	}

	public Double getOfferedPrice() {
		return offeredPrice;
	}

	public void setOfferedPrice(Double offeredPrice) {
		this.offeredPrice = offeredPrice;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isSold() {
		return sold;
	}

	public void setSold(boolean sold) {
		this.sold = sold;
	}

	public String getPostTime() {
		return postTime;
	}

	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}

	public long getChatId() {
		return chatId;
	}

	public void setChatId(long chatId) {
		this.chatId = chatId;
	}
}
