/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the chat database table.
 * 
 * @author juan.pizarro
 * 
 */
@Entity
@Table(name = "chat")
public class Chat extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cha_id")
	private Long id;

	@Column(name = "cha_brandpublish")
	private String brandPublish;

	@Column(name = "cha_comment")
	private String comment;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cha_posttime")
	private Date postTime;

	@Column(name = "cha_versionpublish")
	private String versionPublish;

	@Column(name = "cha_offered_price")
	private Double offeredPrice;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cha_conversation_id", updatable = false)
	private Conversation conversation;

	@OneToOne
	@JoinColumn(name = "cha_sender_id", updatable = false)
	private User sender;

	@OneToOne
	@JoinColumn(name = "cha_receiver_id", updatable = false)
	private User receiver;

	// bi-directional many-to-one association to Item
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cha_ite_id")
	private Item item;

	@Enumerated(EnumType.STRING)
	@Column(name = "cha_action")
	private ChatActionType action;
	
	
	@Column(name = "cha_auto")
	private boolean automaticGeneratedAction;


	public Chat() {
	}

	public Long getChatId() {
		return this.id;
	}

	public void setChatId(Long chatId) {
		this.id = chatId;
	}

	public String getBrandPublish() {
		return this.brandPublish;
	}

	public void setBrandPublish(String brandPublish) {
		this.brandPublish = brandPublish;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getPostTime() {
		return this.postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public String getVersionPublish() {
		return this.versionPublish;
	}

	public void setVersionPublish(String versionPublish) {
		this.versionPublish = versionPublish;
	}

	public Item getItem() {
		return this.item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public Double getOfferedPrice() {
		return offeredPrice;
	}

	public void setOfferedPrice(Double offeredPrice) {
		this.offeredPrice = offeredPrice;
	}

	public ChatActionType getAction() {
		return action;
	}

	public void setAction(ChatActionType action) {
		this.action = action;
	}
	
	public boolean isAutomaticGeneratedAction() {
		return automaticGeneratedAction;
	}

	public void setAutomaticGeneratedAction(boolean automaticGeneratedAction) {
		this.automaticGeneratedAction = automaticGeneratedAction;
	}

	public enum ChatActionType {
		CHAT("CHAT"), OFFER("OFFER"), ACCEPTED("ACCEPTED"), CANCELLED("CANCELLED"), MARKED_AS_SOLD("MARKED_AS_SOLD");

		private final String description;

		private ChatActionType(String description) {
			this.description = description;
		}

		@Override
		public String toString() {
			return description;
		}
	}
}