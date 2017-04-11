package com.ebay.park.service.conversation.command;

import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.Item;
import com.ebay.park.service.ServiceExceptionCode;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * Utility class to deal with two roles at the same time when sending chats
 * @author marcos.lambolay
 */
public class DoubleRole {
	
	private Role seller;
	private Role buyer;
	
	/**
	 * The role of the main user in the current conversation
	 */
	private Role me;
	
	/**
	 * The sender of the message for the current conversation
	 */
	private Role sender;
	
	/**
	 * The receiver of the message for the current conversation
	 */
	private Role receiver;
	
	/**
	 * 
	 * @param item the item in the conversation
	 * @param me the id of the main user in this conversation
	 * @param conversation 
	 * @param factory the role factory. needed because it can create the needed spring beans
	 */
	public DoubleRole(Item item, Long me, Conversation conversation, RoleFactory factory) {
				
		if(item.isSeller(me)) {
			this.me = factory.createSellerRole(conversation);
			
			seller = this.me;
			buyer = factory.createBuyerRole(conversation);

			sender = seller;
			receiver = buyer;
		} else {
			this.me = factory.createBuyerRole(conversation);
			
			seller = factory.createSellerRole(conversation);
			buyer = this.me;
			
			sender = buyer;
			receiver = seller;
		}
	}
	
	public void setConversation(Conversation c) {
		buyer.setConversation(c);
		seller.setConversation(c);
	}

	// if i am seller and there's no conversation, i can't start one, only the buyer
	/**
	 * It throws an exception if the seller is starting the conversation.
	 * @throws ServiceExceptionCode with code SELLER_CANT_START_CONVERSATION_ERROR when the conversation is not
	 * started by the buyer.
	 */
	public void validateIsNotSeller() {
		if(isSeller()) {
			throw createServiceException(ServiceExceptionCode.SELLER_CANT_START_CONVERSATION_ERROR);
		} 
	}
	
	public void validateSenderAndReceiverNotTheSame() {
		if(sender.equals(receiver)) {
			throw createServiceException(ServiceExceptionCode.CHAT_SENDER_SAME_AS_RECEIVER_ERROR);
		}
	}

	public boolean isSeller() {
		return me.equals(seller);
	}


	public Role getSeller() {
		return seller;
	}


	public Role getBuyer() {
		return buyer;
	}


	public Role getSender() {
		return sender;
	}


	public Role getReceiver() {
		return receiver;
	}

	public Role getMe() {
		return me;
	}

	public void setMe(Role me) {
		this.me = me;
	}
}
