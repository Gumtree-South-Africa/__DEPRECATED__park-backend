package com.ebay.park.service.conversation.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import com.ebay.park.db.entity.Item;
import com.ebay.park.util.LanguageUtil;
import com.ebay.park.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.ConversationDao;
import com.ebay.park.db.entity.Chat;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.conversation.dto.AcceptConversationRequest;
import com.ebay.park.service.conversation.validator.ConversationValidator;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import org.springframework.util.Assert;

/**
 * Accepts a conversation. That is, it marks the conversation as accepted for
 * the user that accepted it, marks the item as sold, and creates the ratings
 * to rate both users in the current conversation.
 * @author marcos.lambolay
 */
@Component
public class AcceptConversationCmd implements
ServiceCommand<AcceptConversationRequest, ServiceResponse> {

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

	/**
	 * It changes the state of the conversation to an ACCEPTED state. It means the conversation cannot
	 * be accepted again, cannot be cancelled and pending ratings will be generated.
	 * Followers are notified of the item sale.
	 * @throws ServiceExceptionCode with code CONVERSATION_NOT_FOUND_ERROR
     * @throws ServiceExceptionCode with code ITEM_ALREADY_DELETED when the item is deleted
     * @throws ServiceExceptionCode with code ALREADY_SOLD_ITEM_ERROR when the item is sold
     * @throws ServiceExceptionCode with code ITEM_EXPIRED when the item is expired
     * @throws ServiceExceptionCode with code ITEM_NOT_FOUND when the item cannot be found
	 */
	@Override
    public ServiceResponse execute(AcceptConversationRequest request) throws ServiceException {
		Assert.notNull(request, "'request' must be not null");
		UserSessionCache session = sessionService.getUserSession(request.getToken());
		Conversation conversation = null;

		synchronized (this) {
		    //conversation validation
			conversation = conversationDao.findOne(Long.parseLong(request.getConversationId()));
			ConversationValidator.validateFound(conversation);
			ConversationValidator.isNotClosed(conversation);

			//item validation
	        if (conversation.getItem() == null) {
	            throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);
	        }
	        ConversationValidator.assertItemStatus(conversation.getItem());

			Role role = roleFactory.createRole(conversation, session.getUserId());

			//update conversation status
			role.acceptConversation(getItemURL(request.getLanguage(), session, conversation.getItem()));
			conversationDao.save(conversation);

			// generate ratings
	        chatHelper.generateRatings(conversation.getBuyer(), conversation.getSeller(),
	                conversation.getItem());
		}

		//create a chat with action ChatActionType.ACCEPTED
		Chat chat = conversation.buildAcceptedMilestone(session.getUserId());
		chatHelper.persistChat(chat);

		return ServiceResponse.SUCCESS;
	}

	private String getItemURL(String languageFromRequest, UserSessionCache session, Item item) {
		String language = LanguageUtil.getLanguageForUserRequest(session, languageFromRequest);
		return textUtils.createItemSEOURL(item, language);
	}

}