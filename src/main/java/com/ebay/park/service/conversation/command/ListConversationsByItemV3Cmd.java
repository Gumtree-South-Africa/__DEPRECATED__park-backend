package com.ebay.park.service.conversation.command;

import org.springframework.stereotype.Component;

import com.ebay.park.db.entity.Chat.ChatActionType;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.conversation.dto.ListConversationsByItemRequest;
import com.ebay.park.service.conversation.dto.ListConversationsResponse;
import com.ebay.park.service.conversation.dto.SmallChat;
import com.ebay.park.service.conversation.dto.SmallConversation;

/**
 * Returns the conversations related to an item. Version 3.
 * Version 3 does not uses MARKED_AS_SOLD, so it was translated to CHAT.
 * @author Julieta Salvadó
 *
 */
@Component
public class ListConversationsByItemV3Cmd extends ListConversationsByItemCmd {
    
    @Override
    public ListConversationsResponse execute(ListConversationsByItemRequest request)
            throws ServiceException {
        ListConversationsResponse response = super.execute(request);

        for (SmallConversation conversation : response.getConversations()) {
          //MARKED_AS_SOLD is not a valid action in version 3
            for (SmallChat chat : conversation.getChats()) {
                translateToKnownAction(chat);
            }
            translateToKnownAction(conversation.getLastChat());
        }

        return response;
    }

    private void translateToKnownAction(SmallChat chat) {
        if (ChatActionType.MARKED_AS_SOLD.toString().equals(chat.getAction())) {
            chat.setAction(ChatActionType.CHAT.toString());
        }
    }
}
