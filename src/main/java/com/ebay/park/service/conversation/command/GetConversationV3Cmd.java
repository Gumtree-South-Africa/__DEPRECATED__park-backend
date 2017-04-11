package com.ebay.park.service.conversation.command;

import org.springframework.stereotype.Component;

import com.ebay.park.db.entity.Chat.ChatActionType;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.conversation.dto.GetConversationRequest;
import com.ebay.park.service.conversation.dto.SmallChat;
import com.ebay.park.service.conversation.dto.SmallConversation;

@Component
public class GetConversationV3Cmd extends GetConversationCmd {

    @Override
    public SmallConversation execute(GetConversationRequest request) throws ServiceException {
        SmallConversation response = super.execute(request);

        //MARKED_AS_SOLD is not a valid action in version 3
        for (SmallChat chat : response.getChats()) {
            if (ChatActionType.MARKED_AS_SOLD.toString().equals(chat.getAction())) {
                chat.setAction(ChatActionType.CHAT.toString());
            }
        }

        return response;
    }
}
