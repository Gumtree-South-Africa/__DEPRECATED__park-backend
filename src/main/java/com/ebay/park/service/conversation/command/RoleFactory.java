package com.ebay.park.service.conversation.command;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.Item;
import com.ebay.park.service.conversation.validator.ConversationValidator;

/**
 * This class is a factory for the Role classes: {@link Role}, {@link SellerRole},
 * {@link BuyerRole} and {@link DoubleRole} .
 * This class allows to create the roles with the spring context and allow role classes
 * to have spring beans behaviors like advised notifications.
 * 
 * @author juan.pizarro
 */
@Component
public class RoleFactory {

	@Autowired
	ApplicationContext context;
	
	@Autowired
	private ItemDao itemDao;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private ChatHelper chatHelper;

	/**
	 * Creates the role that the user with id "me" has in "conversation"
	 * @param conversation
	 * @param me
	 * @return 
	 */
	public Role createRole(Conversation conversation, Long me) {
		ConversationValidator.assertUserIsBuyerOrSeller(conversation, me);
		return createRole(conversation, me, false);
	}

	/**
	 * A more complete create inverse role method. It ensures that "me" is the seller or the buyer in the conversation
	 * and returns its inverse role.
	 * 
	 * @param conversation
	 * @param me
	 * @return the inverse role of user with id "me" in the conversation 
	 */
	public Role createInverseRole(Conversation conversation, Long me) {
		ConversationValidator.assertUserIsBuyerOrSeller(conversation, me);
		return createRole(conversation, me, true);
	}

	/**
	 * Creates a role that is the rol that the user with id "me" plays in the conversation.
	 * If inverseRole is true, the inverse role will be returned.
	 * @param conversation
	 * @param me
	 * @param conversationValidator
	 * @param inverseRole
	 * @return a role
	 */
	public Role createRole(Conversation conversation, Long me, boolean inverseRole) {
		Role role = null;

		SellerRole seller = context.getBean(SellerRole.class);
		seller.setConversation(conversation);
		seller.setEntityManager(entityManager);

		BuyerRole buyer = context.getBean(BuyerRole.class);
		buyer.setConversation(conversation);
		buyer.setEntityManager(entityManager);

		if (conversation.getItem().isSeller(me) ^ inverseRole) {
			role = seller;
			role.setInverseRole(buyer);
		} else {
			role = buyer;
			role.setInverseRole(seller);
		}
		return role;
	}

	/**
	 * Creates a SellerRole for the conversation
	 * @param conversation
	 * @return a SellerRole
	 */
	public SellerRole createSellerRole(Conversation conversation){
		SellerRole seller = context.getBean(SellerRole.class);
		seller.setConversation(conversation);
		return seller;
	}

	/**
	 * Creates a BuyerRole for the conversation
	 * @param conversation
	 * @return a BuyerRole
	 */
	public BuyerRole createBuyerRole(Conversation conversation){
		BuyerRole buyer = context.getBean(BuyerRole.class);
		buyer.setConversation(conversation);
		return buyer;
	}
	
	public DoubleRole createDoubleRole(Item item, Long id, Conversation conversation){
		return new DoubleRole(item, id, conversation, this);
	}
}
