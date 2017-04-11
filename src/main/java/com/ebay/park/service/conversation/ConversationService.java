/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service.conversation;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.conversation.dto.*;

/**
 * @author marcos.lambolay
 */
public interface ConversationService {

	public ListConversationsResponse list(ListConversationsRequest request)
			throws ServiceException;

	public ListConversationsResponse listByItemIdV3(
			ListConversationsByItemRequest request) throws ServiceException;

    public ListConversationsResponse listByItemIdV4(
            ListConversationsByItemRequest request) throws ServiceException;;

	public ServiceResponse accept(AcceptConversationRequest request);

	public ServiceResponse reject(RejectConversationRequest request);

	public SmallConversation getV3(GetConversationRequest request);

	public SmallConversation getV4(GetConversationRequest request);

	public SendChatResponse sendChat(SendChatRequest request);

	public SendChatResponse sendOffer(SendOfferRequest request);

}
