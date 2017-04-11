package com.ebay.park.service.conversation.dto;

/**
 * 
 * @author marcos.lambolay
 *
 */
public class SendChatResponse {
	private Long chatId;
	private Long conversationId;
	
	public SendChatResponse(){}
	
	public SendChatResponse(Long chatId) {
		this.chatId = chatId;
	}

	public Long getChatId() {
		return chatId;
	}

	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }
}
