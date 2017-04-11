package com.ebay.park.service.conversation.dto;

import com.ebay.park.db.entity.Conversation;
import com.ebay.park.service.conversation.command.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author marcos.lambolay
 * 
 */
public class SmallConversation {
	private Long conversationId;
	private Long itemId;
	private String itemName;
	private Long userId;
	private String username;
	private String role;
	private List<SmallChat> chats;
	private Double currentPriceProposedByBuyer;
	private Double currentPriceProposedBySeller;
	private String buyerThumbnail;
	private String sellerThumbnail;
	private Boolean newChats;
	private String itemPicture;
	private String status;
	private SmallChat lastChat;
	private Boolean hasOffer = Boolean.FALSE;

	public SmallConversation() {
		this.chats = new ArrayList<SmallChat>();
	}

	public SmallConversation(Conversation conversation, Role role) {
		this();
		this.setItemId(conversation.getItem().getId());
		this.setConversationId(conversation.getId());
		this.setUserId(role.getId());
		this.setBuyerThumbnail(conversation.getBuyer().getPicture());
		this.setSellerThumbnail(conversation.getSeller().getPicture());
		this.setUsername(role.getUsername());
		this.setCurrentPriceProposedByBuyer(conversation.getCurrentPriceProposedByBuyer());
		this.setCurrentPriceProposedBySeller(conversation.getCurrentPriceProposedBySeller());
		this.setItemPicture(conversation.getItem().getPicture1Url());
		this.setStatus(conversation.getStatus().toString());
		this.setItemName(conversation.getItem().getName());
		this.setRole(role.getRoleName());
		this.status = conversation.getStatus().toString();

	}

	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<SmallChat> getChats() {
		return chats;
	}

	public void setChats(List<SmallChat> chats) {
		this.chats = chats;
	}

	public void addChat(SmallChat sc) {
		this.chats.add(sc);
	}

	public String getBuyerThumbnail() {
		return buyerThumbnail;
	}

	public void setBuyerThumbnail(String buyerThumbnail) {
		this.buyerThumbnail = buyerThumbnail;
	}

	public String getSellerThumbnail() {
		return sellerThumbnail;
	}

	public void setSellerThumbnail(String sellerThumbnail) {
		this.sellerThumbnail = sellerThumbnail;
	}

	public Double getCurrentPriceProposedByBuyer() {
		return currentPriceProposedByBuyer;
	}

	public void setCurrentPriceProposedByBuyer(Double currentPriceProposedByBuyer) {
		this.currentPriceProposedByBuyer = currentPriceProposedByBuyer;
	}

	public Double getCurrentPriceProposedBySeller() {
		return currentPriceProposedBySeller;
	}

	public void setCurrentPriceProposedBySeller(Double currentPriceProposedBySeller) {
		this.currentPriceProposedBySeller = currentPriceProposedBySeller;
	}

	public Boolean getNewChats() {
		return newChats;
	}

	public void setNewChats(Boolean newChats) {
		this.newChats = newChats;
	}

	public String getItemPicture() {
		return itemPicture;
	}

	public void setItemPicture(String itemPicture) {
		this.itemPicture = itemPicture;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public SmallChat getLastChat() {
		return lastChat;
	}

	public void setLastChat(SmallChat lastChat) {
		this.lastChat = lastChat;
	}

	public Boolean getHasOffer() {
		return hasOffer;
	}

	public void setHasOffer(Boolean hasOffer) {
		this.hasOffer = hasOffer;
	}
	
	
}
