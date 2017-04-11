package com.ebay.park.service.conversation.validator;

import static com.ebay.park.service.ServiceException.createServiceException;

import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.ConversationStatus;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.conversation.dto.SendChatRequest;
import com.ebay.park.service.conversation.dto.SendOfferRequest;
/**
 * This validator is used along the purchase process.
 * It's used mostly to validate logic conditions than to validate inputs from request objects,
 * as many validators do.
 * @author marcos.lambolay
 *
 */
public class ConversationValidator {

    /**
     * Verifies that the conversation is not accepted or cancelled.
     * @param conversation
     *      the conversation under analysis
     */
	public static void isNotClosed(Conversation conversation) {
		if(conversation.is(ConversationStatus.ACCEPTED)) {
			throw createServiceException(ServiceExceptionCode.ALREADY_ACCEPTED_CONVERSATION_ERROR);
		}
		isNotCancelled(conversation);
	}

	/**
     * Verifies that the conversation is cancelled.
     * @param conversation
     *      the conversation under analysis
     */
	public static void isNotCancelled(Conversation conversation) {
	    if(conversation.is(ConversationStatus.CANCELLED)) {
            throw createServiceException(ServiceExceptionCode.ALREADY_CANCELLED_CONVERSATION_ERROR);
        }
    }
	
	public static void assertUserIsBuyerOrSeller(Conversation conversation, Long id) {
		if (!isUserBuyerOrSeller(conversation, id)) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_BUYER_NOR_SELLER_ERROR);
		}
	}
	
	public static boolean isUserBuyerOrSeller(Conversation conversation, Long id) {
		return conversation.getBuyer().getId().equals(id)
			|| conversation.getSeller().getId().equals(id);
	}
	
	public static void isActiveOrSold(Item item) {
		if(!(item.is(StatusDescription.ACTIVE) || item.is(StatusDescription.SOLD))){
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_ACTIVE_NOR_SOLD_ERROR);
		}
	}
	
	public static void isNotDeleted(Item item) {
		if(item.isDeleted()) {
			throw createServiceException(ServiceExceptionCode.ITEM_ALREADY_DELETED);
		}
	}

	/**
	 * It throws an exception if the conversation is not found.
	 * @param conversation
	 *         conversation that must be verified
	 * @throws ServiceExceptionCode.CONVERSATION_NOT_FOUND_ERROR
	 */
	public static void validateFound(Conversation conversation) {
		if(conversation == null) {
			throw createServiceException(ServiceExceptionCode.CONVERSATION_NOT_FOUND_ERROR);
		}
	}

	public static boolean verifyUserBuyerOrSeller(Conversation conversation, Long id) {
		return conversation.getBuyer().getId().equals(id)
			|| conversation.getSeller().getId().equals(id);
	}

	/**
	 * It verifies if the request is an offer and validates this is an open conversation.
	 * @param request
	 *     a plain chat or an offer
	 * @param conversation
	 *     the conversation where the message wants to be added
	 */
    public static void assertConversationStatus(SendChatRequest request,
            Conversation conversation) {
        if(request instanceof SendOfferRequest) {
            isNotClosed(conversation);
        }
    }

    /**
     * It verifies if the request is an offer and validates this is an active item.
     * @param item
     *      a plain chat or an offer
     * @param request
     *      a plain chat or an offer
     * @throws ALREADY_SOLD_ITEM_ERROR when the item is sold in an offer request
     */
    public static void assertItemStatus(Item item, SendChatRequest request) {
        if(request instanceof SendOfferRequest && item.is(StatusDescription.SOLD)) {
            throw createServiceException(ServiceExceptionCode.ALREADY_SOLD_ITEM_ERROR);
        }
    }

    /**
     * It verifies if the item was not sold, expired nor deleted.
     * @param item
     *      the item under analysis
     * @throws ITEM_ALREADY_DELETED when the item is deleted
     * @throws ALREADY_SOLD_ITEM_ERROR when the item is sold
     * @throws ITEM_EXPIRED when the item is expired
     */
    public static void assertItemStatus(Item item) {
            if (item.isDeleted()) {
                throw createServiceException(ServiceExceptionCode.ITEM_ALREADY_DELETED);
            } else if (item.is(StatusDescription.SOLD)) {
                throw createServiceException(ServiceExceptionCode.ALREADY_SOLD_ITEM_ERROR);
            } else if (item.is(StatusDescription.EXPIRED)) {
                throw createServiceException(ServiceExceptionCode.ITEM_EXPIRED);
            }
    }
}
