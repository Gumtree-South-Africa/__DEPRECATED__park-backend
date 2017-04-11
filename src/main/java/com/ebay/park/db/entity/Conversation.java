/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import com.ebay.park.db.entity.Chat.ChatActionType;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author marcos.lambolay
 * 
 */
@Entity
@Table(name = "conversation")
public class Conversation extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "con_id")
	private Long id;

	@OneToOne
	@JoinColumn(name = "con_buyer_id", updatable = false)
	private User buyer;

	@Enumerated(EnumType.STRING)
	@Column(name = "con_buyer_bargain_status")
	private BargainStatus buyerBargainStatus;

	@OneToOne
	@JoinColumn(name = "con_seller_id", updatable = false)
	private User seller;

	@Enumerated(EnumType.STRING)
	@Column(name = "con_seller_bargain_status")
	private BargainStatus sellerBargainStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "con_status")
	private ConversationStatus status;

	// bi-directional many-to-one association to Item
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "con_ite_id", updatable = false)
	private Item item;

	@OneToMany(mappedBy = "conversation", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Chat> chats;

	@Column(name = "con_current_price_proposed_by_buyer")
	private Double currentPriceProposedByBuyer;
	
	@Column(name = "con_current_price_proposed_by_seller")
	private Double currentPriceProposedBySeller;

	public Conversation() {
	}

	public Conversation(User buyer, User seller, Item item) {
		this.chats = new ArrayList<Chat>();
		this.setStatus(ConversationStatus.OPEN);
		this.buyer = buyer;
		this.seller = seller;
		this.item = item;
	}

	public Item getItem() {
		return this.item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public User getBuyer() {
		return buyer;
	}

	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}

	public User getSeller() {
		return seller;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}

	public ConversationStatus getStatus() {
		return status;
	}

	public void setStatus(ConversationStatus status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Chat> getChats() {
		return chats;
	}

	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}

	public BargainStatus getBuyerBargainStatus() {
		return buyerBargainStatus;
	}

	public void setBuyerBargainStatus(BargainStatus buyerBargainStatus) {
		this.buyerBargainStatus = buyerBargainStatus;
	}

	public BargainStatus getSellerBargainStatus() {
		return sellerBargainStatus;
	}

	public void setSellerBargainStatus(BargainStatus sellerBargainStatus) {
		this.sellerBargainStatus = sellerBargainStatus;
	}

	public boolean is(ConversationStatus conversationStatus) {
		return getStatus().equals(conversationStatus);
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

    /**
     * Builds an special chat to have as some kind of notification of a rejection
     * @param userCancelledId the user that cancelled/rejected it
     * @param cancelReason the reason
     * @return resulting chat
     */
	public Chat buildCancelMilestone(Long userCancelledId, String cancelReason) {
		Conversation conversation = this;
		Chat chat = new Chat();
		chat.setConversation(conversation);
		chat.setComment(cancelReason);
		chat.setItem(conversation.getItem());
		chat.setPostTime(DateTime.now().toDate());
		if (conversation.getSeller().getId().equals(userCancelledId)) {
			chat.setReceiver(conversation.getBuyer());
			chat.setSender(conversation.getSeller());
		} else {
			chat.setReceiver(conversation.getSeller());
			chat.setSender(conversation.getBuyer());
		}
		chat.setAction(ChatActionType.CANCELLED);
		chats.add(chat);
		return chat;
	}
	
	/**
	 * Builds an special chat so it can be used more like a notification.
	 * @param userAcceptedId the user that accepted the conversation
	 * @return a chat
	 */
	public Chat buildAcceptedMilestone(Long userAcceptedId) {
		Conversation conversation = this;
		Chat chat = new Chat();
		chat.setConversation(conversation);
		chat.setItem(conversation.getItem());
		if (conversation.getSeller().getId().equals(userAcceptedId)) {
			chat.setOfferedPrice(conversation.getCurrentPriceProposedByBuyer());
			chat.setReceiver(conversation.getBuyer());
			chat.setSender(conversation.getSeller());
		} else {
			chat.setOfferedPrice(conversation.getCurrentPriceProposedBySeller());
			chat.setReceiver(conversation.getSeller());
			chat.setSender(conversation.getBuyer());
		}

		chat.setPostTime(DateTime.now().toDate());
		chat.setAction(ChatActionType.ACCEPTED);
		chats.add(chat);
		return chat;
	}

    public Chat buildSoldMilestone() {
        Chat chat = new Chat();
        chat.setConversation(this);
        chat.setItem(this.getItem());
        chat.setPostTime(DateTime.now().toDate());
        chat.setAction(ChatActionType.MARKED_AS_SOLD);
        chat.setReceiver(this.buyer);
        chat.setSender(this.seller);;
        chats.add(chat);
        return chat;
    }

}