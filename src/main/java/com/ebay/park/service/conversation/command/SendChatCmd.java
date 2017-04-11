package com.ebay.park.service.conversation.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import com.ebay.park.util.LanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.ConversationDao;
import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserFollowsItemDao;
import com.ebay.park.db.entity.Chat;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.ConversationStatus;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.conversation.dto.SendChatRequest;
import com.ebay.park.service.conversation.dto.SendChatResponse;
import com.ebay.park.service.conversation.validator.ConversationValidator;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import org.springframework.util.Assert;

/**
 * 
 * @author marcos.lambolay
 */
@Component
/**
 * Command that adds a message (plain chat or offer) to a conversation with a defined item and par
 * of buyer-seller or creates a conversation of it not exists.
 */
public class SendChatCmd implements
		ServiceCommand<SendChatRequest, SendChatResponse> {

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private ConversationDao conversationDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleFactory roleFactory;

	@Autowired
	private ChatHelper chatHelper;

	@Autowired
	private UserFollowsItemDao userFollowsItemDao;

	@Autowired
	private ItemUtils itemUtils;

	/**
	 * It adds a message (plain chat or offer) to a conversation with a defined item and par of buyer-seller
	 * or creates a conversation of it not exists.
	 *
	 * @param request
	 *         the incoming SendChatRequest. It <b>mustn't</b> be null. If an offer is about to be added,
	 *         it was validated according to SendOfferValidator.
	 * @throws ServiceExceptionCode with code ITEM_NOT_FOUND when the requested item cannot be found.
	 * @throws ServiceExceptionCode with code ITEM_EXPIRED when the requested item is already expired.
	 * @throws ServiceExceptionCode with code CONVERSATION_NOT_FOUND_ERROR when a conversation id is set, but it
	 * does not correspond to an existing conversation.
	 * @throws ServiceExceptionCode with code CONVERSATION_EXIST when a conversation id is NOT set, but
	 * an existing conversation is already created.
	 * @throws ServiceExceptionCode with code ITEM_NOT_ACTIVE_NOR_SOLD_ERROR if a conversation is started, the item
	 * must be ACTIVE; if a conversation is continued, the item must be ACTIVE or SOLD.
	 * @throws ServiceExceptionCode with code CHAT_SENDER_SAME_AS_RECEIVER_ERROR when seller and buyer are the same user.
	 * @throws ServiceExceptionCode with code SELLER_CANT_START_CONVERSATION_ERROR when the user starting the
	 * conversation is not the buyer.
	 * @throws ServiceExceptionCode with code CANT_SEND_CHAT_ERROR when the conversation was already cancelled.
	 */
	@Override
    public SendChatResponse execute(SendChatRequest request)
			throws ServiceException {
		//FIXME this method is too long, refactor me!
		Assert.notNull(request, "'request' must be not null");
		Item item = itemDao.findOne(request.getItemId());
		if (item == null) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND
			);
		}

		if (item.is(StatusDescription.EXPIRED)) {
			throw createServiceException(ServiceExceptionCode.ITEM_EXPIRED);
		}

		UserSessionCache session = sessionService.getUserSession(request.getToken());
		Conversation conversation = null;

		if (request.getConversationId() != null) {
			// old conversation
			conversation = conversationDao.findOne(request.getConversationId());
			ConversationValidator.validateFound(conversation);
			//validates if logged user is buyer or seller
			ConversationValidator.assertUserIsBuyerOrSeller(conversation, session.getUserId());
			ConversationValidator.assertConversationStatus(request, conversation);
			ConversationValidator.assertItemStatus(item, request);
			
		}else{
			// new conversation. only one relationship (buyer - item) is possible
			conversation = conversationDao.findConversationByItemIdForBuyer(
					session.getUserId(), item.getId());
			if (conversation != null){
				throw createServiceException(ServiceExceptionCode.CONVERSATION_EXIST);
			}

		}

		// only receives chats if it is a new chat and the item is ACTIVE or the
		// buyer & seller wants to talk when the item is SOLD
		if ((conversation == null && !item.is(StatusDescription.ACTIVE))
				|| (conversation != null && (!item.is(StatusDescription.SOLD) && !item
						.is(StatusDescription.ACTIVE)))) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_ACTIVE_NOR_SOLD_ERROR);
		}

		DoubleRole role = roleFactory.createDoubleRole(item,
				session.getUserId(), conversation);
		role.validateSenderAndReceiverNotTheSame();

		//New Conversation
		if (conversation == null) {
			role.validateIsNotSeller();
			conversation = new Conversation(
					userDao.findOne(session.getUserId()),
					item.getPublishedBy(), item);
			conversation.setCurrentPriceProposedByBuyer(item.getPrice());
			conversation.setCurrentPriceProposedBySeller(item.getPrice());
			role.setConversation(conversation);
		}

		if (conversation.is(ConversationStatus.CANCELLED)) {
			throw createServiceException(ServiceExceptionCode.CANT_SEND_CHAT_ERROR);
		}

		conversationDao.saveAndFlush(conversation);
		String lang = LanguageUtil.getLanguageForUserRequest(session, request.getLanguage());
		Chat chat = chatHelper.buildChat(request, conversation, item, role, lang);
		chatHelper.sendChat(chat, conversation, item, role, lang);

		if (!role.isSeller()) {
		    itemUtils.setAsFollower(item.getId(), role.getMe().getId());
		}

		SendChatResponse response = new SendChatResponse(chat.getChatId());
		response.setConversationId(conversation.getId());
		return response;
	}
}