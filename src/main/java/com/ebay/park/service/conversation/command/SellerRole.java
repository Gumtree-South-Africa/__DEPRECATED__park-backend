package com.ebay.park.service.conversation.command;

import com.ebay.park.db.entity.BargainStatus;
import com.ebay.park.event.conversation.ConversationRejectedEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.aop.Notifiable;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 
 * @author marcos.lambolay
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class SellerRole extends Role {

	public SellerRole() {
	}

	@Override
	protected void acceptBargain() {
		this.getConversation().setSellerBargainStatus(BargainStatus.ACCEPTED);
	}

	@Override
	public boolean isBargainAccepted() {
		return BargainStatus.ACCEPTED.equals(getConversation().getSellerBargainStatus());
	}

	@Notifiable(action = NotificationAction.CONVERSATION_REJECTED)
	@Override
	protected ConversationRejectedEvent rejectBargain(String itemURL) {
		getConversation().setSellerBargainStatus(BargainStatus.REJECTED);
		return super.rejectBargain(itemURL);
	}

	@Override
	public Long getId() {
		return getConversation().getSeller().getId();
	}

	@Override
	public String getUsername() {
		return getConversation().getSeller().getUsername();
	}

	@Override
	public void setConversationCurrentPrice(Double price) {
		getConversation().setCurrentPriceProposedBySeller(price);
		
	}

	@Override
	public Double getConversationCurrentPrice() {
		return getConversation().getCurrentPriceProposedBySeller();
	}

	@Override
	public String getRoleName() {
		return Role.SELLER;
	}

}
