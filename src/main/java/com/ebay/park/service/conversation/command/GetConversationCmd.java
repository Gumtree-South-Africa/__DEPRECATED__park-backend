package com.ebay.park.service.conversation.command;

import com.ebay.park.db.dao.ChatDao;
import com.ebay.park.db.dao.ConversationDao;
import com.ebay.park.db.entity.Chat;
import com.ebay.park.db.entity.Chat.ChatActionType;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.conversation.dto.GetConversationRequest;
import com.ebay.park.service.conversation.dto.SmallChat;
import com.ebay.park.service.conversation.dto.SmallConversation;
import com.ebay.park.service.conversation.validator.ConversationValidator;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.DataCommonUtil;

import org.joda.time.DateTimeComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;
/**
 * 
 * @author marcos.lambolay
 */
@Component
public class GetConversationCmd implements
ServiceCommand<GetConversationRequest, SmallConversation> {

	@Autowired
	private ConversationDao conversationDao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	RoleFactory roleFactory;

	@Autowired
	private ChatDao chatDao;

	@Autowired
	private ChatHelper chatHelper;

	@Override
	public SmallConversation execute(GetConversationRequest request) throws ServiceException {

		UserSessionCache session = sessionService.getUserSession(request.getToken());
		String language = request.getLanguage() != null? request.getLanguage(): session.getLang();
		Conversation conversation = conversationDao.findOne(Long.parseLong(request
				.getConversationId()));

		ConversationValidator.validateFound(conversation);
		ConversationValidator.isNotDeleted(conversation.getItem());

		Role role = roleFactory.createInverseRole(conversation, session.getUserId());
		Date lastRequest;
		try {
			lastRequest = DataCommonUtil.parseUnixTime(request.getLastRequest());
		} catch (IllegalArgumentException e) {
			throw createServiceException(ServiceExceptionCode.LAST_REQUEST_BAD_FORMAT_ERROR);
		}
		SmallConversation response = new SmallConversation(conversation, role);
		Long nChats = chatDao.findForConversationChatsMoreRecentThan(conversation.getId(),
				lastRequest);
		response.setNewChats(nChats > 0);

		if (!conversation.getChats().isEmpty()) {
			Chat lastChat = conversation.getChats().get(0);
			List<Chat> chats = conversation.getChats();
			for (Chat chat : ChatHelper.sort(chats)) {
				if (DateTimeComparator.getInstance().compare(lastChat.getPostTime(),
						chat.getPostTime()) < 0) {
					lastChat = chat;
				}

				SmallChat smallChat = chatHelper.createaSmallChat(chat, language);
				response.addChat(smallChat);

				// if ACCEPTED, we need to add an extra message (Front End req).
				if (chat.getAction().equals(ChatActionType.ACCEPTED)) {
					response.addChat(chatHelper
							.createaSmallChatOfferAccepted(chat,language));
				}

				//if chat has an offer, we need to set a flag (Front End req).
				if (chat.getAction().equals(ChatActionType.OFFER)) {
					response.setHasOffer(Boolean.TRUE);
				}

			}
			response.setLastChat(chatHelper.createaSmallChat(lastChat, language));
		}
		
		return response;
	}
}