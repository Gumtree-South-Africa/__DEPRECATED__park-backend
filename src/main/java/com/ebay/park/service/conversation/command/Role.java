package com.ebay.park.service.conversation.command;

import javax.persistence.EntityManager;

import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.ConversationStatus;
import com.ebay.park.event.conversation.ConversationAcceptedEvent;
import com.ebay.park.event.conversation.ConversationRejectedEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.aop.Notifiable;
/**
 * Represents the role a user plays on a conversation.
 * 
 * @author marcos.lambolay
 */
public abstract class Role {

	public static final String SELLER = "seller";
	public static final String BUYER = "buyer";
	
	private String deviceId;
	
	/**
	 * The role that is inverse to this instance role.
	 */
	private Role inverseRole;
	
	private Conversation conversation;

	private EntityManager entityManager;
	
	public abstract Long getId();
	public abstract void setConversationCurrentPrice(Double price);
	public abstract Double getConversationCurrentPrice();
	public abstract String getUsername();
	public abstract String getRoleName();

	public Role() {
	} 

	protected Role(Conversation c) {
		this.conversation = c;
	}

	/**
	 * Changes the status of the conversation to ConversationStatus.ACCEPTED, sets who was the role
	 * accepting the conversation and creates a notification following the specifications of
	 * NotificationAction.CONVERSATION_ACCEPTED
	 * @param itemURL URL to the item page in the website
	 * @return the event for NotificationAction.CONVERSATION_ACCEPTED
	 */
	@Notifiable(action = NotificationAction.CONVERSATION_ACCEPTED)
	public ConversationAcceptedEvent acceptConversation(String itemURL) {
		conversation.setStatus(ConversationStatus.ACCEPTED);

		acceptBargain();
		//formatter:off
		return new ConversationAcceptedEvent(
				getInverseRole().getUsername(),
				getId(),
				getUsername(),
				getInverseRole().getId(),
				conversation.getItem().getId(),
				conversation.getItem().getName(),
				conversation.getId(),
				itemURL
			);
		//formatter:on
	}

	/**
	 * Accepts an offer or bargain
	 */
	protected abstract void acceptBargain();

	public abstract boolean isBargainAccepted();

	/**
	 * Rejects an offer or bargain. It may trigger a notification.
	 * @param itemURL the item page in the website
	 * @return The event with the information regarding the rejection.
	 */
	protected ConversationRejectedEvent rejectBargain(String itemURL) {
		//formatter:off
		return new ConversationRejectedEvent(
				getInverseRole().getUsername(),
				getId(),
				getUsername(),
				getInverseRole().getId(),
				conversation.getItem().getId(),
				conversation.getItem().getName(),
				conversation.getId(),
                itemURL
			);
		//formatter:on
	}

	public Role getInverseRole() {
		return inverseRole;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public void setInverseRole(Role inverseRole) {
		this.inverseRole = inverseRole;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}