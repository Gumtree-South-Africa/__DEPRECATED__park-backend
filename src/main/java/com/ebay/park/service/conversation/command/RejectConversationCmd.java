package com.ebay.park.service.conversation.command;

import com.ebay.park.db.entity.Item;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.util.LanguageUtil;
import com.ebay.park.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.ConversationDao;
import com.ebay.park.db.entity.Chat;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.ConversationStatus;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.conversation.dto.RejectConversationRequest;
import com.ebay.park.service.conversation.validator.ConversationValidator;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;

/**
 * Rejects a conversation.
 * Sets the conversation as cancelled and builds a special chat message to be used
 * as a some kind of notification.
 * 
 * @author marcos.lambolay
 */
@Component
public class RejectConversationCmd implements
		ServiceCommand<RejectConversationRequest, ServiceResponse> {

	@Autowired
	private ConversationDao conversationDao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private RoleFactory roleFactory;

	@Autowired
	private ChatHelper chatHelper;

	@Autowired
	private TextUtils textUtils;

	@Override
	public ServiceResponse execute(RejectConversationRequest request) throws ServiceException {
		UserSessionCache session = sessionService.getUserSession(request.getToken());
		Conversation conversation = conversationDao.findOne(Long.parseLong(request
				.getConversationId()));
		ConversationValidator.validateFound(conversation);
		Role role = roleFactory
				.createRole(conversation, session.getUserId());
		ConversationValidator.isNotCancelled(conversation);

		role.rejectBargain(getItemURL(session, request.getLanguage(), conversation.getItem()));

		conversation.setStatus(ConversationStatus.CANCELLED);
		conversationDao.save(conversation);

		Chat chat = conversation.buildCancelMilestone(session.getUserId(), request.getExplanation());
		chat.setAutomaticGeneratedAction(false);
		chatHelper.persistChat(chat);
		
		return ServiceResponse.SUCCESS;
	}

	private String getItemURL(UserSessionCache session, String languageFromRequest, Item item) {
		String language = LanguageUtil.getLanguageForUserRequest(session, languageFromRequest);
		return textUtils.createItemSEOURL(item, language);
	}
}