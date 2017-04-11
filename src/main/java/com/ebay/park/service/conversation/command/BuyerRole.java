package com.ebay.park.service.conversation.command;

import com.ebay.park.db.entity.BargainStatus;
import com.ebay.park.event.conversation.ConversationRejectedEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.aop.Notifiable;
import com.ebay.park.util.LanguageUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Interacts with a conversation as a buyer.
 * @author marcos.lambolay
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class BuyerRole extends Role {
	
	public BuyerRole() {
	}
	
	@Override
	protected void acceptBargain() {
		getConversation().setBuyerBargainStatus(BargainStatus.ACCEPTED);
	}
	
	@Override
	public boolean isBargainAccepted() {
		return BargainStatus.ACCEPTED.equals(getConversation().getBuyerBargainStatus());
	}
	
	@Notifiable(action=NotificationAction.CONVERSATION_REJECTED)
	@Override
	protected ConversationRejectedEvent rejectBargain(String itemUrl) {
		getConversation().setBuyerBargainStatus(BargainStatus.REJECTED);
		return super.rejectBargain(itemUrl);
	}
	
	@Override
	public Long getId() {
		return getConversation().getBuyer().getId();
	}

	@Override
	public String getUsername() {
		return getConversation().getBuyer().getUsername();
	}

	@Override
	public void setConversationCurrentPrice(Double price) {
		getConversation().setCurrentPriceProposedByBuyer(price);		
	}
	
	@Override
	public Double getConversationCurrentPrice() {
		return getConversation().getCurrentPriceProposedByBuyer();
	}

	@Override
	public String getRoleName() {
		return Role.BUYER;
	}
}