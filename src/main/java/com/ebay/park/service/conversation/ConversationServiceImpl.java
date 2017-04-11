/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service.conversation;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.conversation.command.*;
import com.ebay.park.service.conversation.dto.*;
import com.ebay.park.service.conversation.validator.SendOfferValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author marcos.lambolay
 */
@Service
public class ConversationServiceImpl implements ConversationService {

	@Autowired
	private ListConversationsCmd listCmd;

	@Autowired
	private AcceptConversationCmd acceptCmd;

	@Autowired
	private RejectConversationCmd rejectCmd;

	@Autowired
	@Qualifier("getConversationV3Cmd")
	private ServiceCommand<GetConversationRequest, SmallConversation> getV3Cmd;

	@Autowired
	@Qualifier("getConversationCmd")
    private ServiceCommand<GetConversationRequest, SmallConversation> getV4Cmd;

	@Autowired
	private SendChatCmd sendChatCmd;
	
	@Autowired
	private SendOfferValidator sendOfferValidator;

	@Autowired
	@Qualifier("listConversationsByItemV3Cmd")
	private ServiceCommand<ListConversationsByItemRequest, ListConversationsResponse> listByItemV3Cmd;

	@Autowired
	@Qualifier("listConversationsByItemCmd")
    private ServiceCommand<ListConversationsByItemRequest, ListConversationsResponse> listByItemV4Cmd;

	@Override
	public ListConversationsResponse list(ListConversationsRequest request)
			throws ServiceException {
		return listCmd.execute(request);
	}

	@Override
	public ServiceResponse accept(AcceptConversationRequest request) {
		return acceptCmd.execute(request);
	}

	@Override
	public ServiceResponse reject(RejectConversationRequest request) {
		return rejectCmd.execute(request);
	}

	@Override
	public SmallConversation getV3(GetConversationRequest request) {
		return getV3Cmd.execute(request);
	}

	@Override
    public SmallConversation getV4(GetConversationRequest request) {
        return getV4Cmd.execute(request);
    }

	@Override
	public SendChatResponse sendChat(SendChatRequest request) {
		return sendChatCmd.execute(request);
	}

	@Override
	public SendChatResponse sendOffer(SendOfferRequest request) {
		sendOfferValidator.validate(request);
		return sendChatCmd.execute(request);
	}

	@Override
	public ListConversationsResponse listByItemIdV3(
			ListConversationsByItemRequest request) {
		return listByItemV3Cmd.execute(request);
	}

	@Override
    public ListConversationsResponse listByItemIdV4(
            ListConversationsByItemRequest request) {
        return listByItemV4Cmd.execute(request);
    }

}
