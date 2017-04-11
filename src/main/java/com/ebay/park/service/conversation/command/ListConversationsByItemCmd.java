/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.conversation.command;

import com.ebay.park.db.dao.ChatDao;
import com.ebay.park.db.dao.ConversationDao;
import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Chat;
import com.ebay.park.db.entity.Chat.ChatActionType;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.Item;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.conversation.dto.ListConversationsByItemRequest;
import com.ebay.park.service.conversation.dto.ListConversationsResponse;
import com.ebay.park.service.conversation.dto.SmallChat;
import com.ebay.park.service.conversation.dto.SmallConversation;
import com.ebay.park.service.conversation.validator.ConversationValidator;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.InternationalizationUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.ebay.park.service.ServiceException.createServiceException;
/**
 * @author jpizarro
 * 
 */
@Component
public class ListConversationsByItemCmd implements
		ServiceCommand<ListConversationsByItemRequest, ListConversationsResponse> {
	
    private static final String NO_RESULTS_MESSAGE = "emptylist.conversation_list";

	private static final int DEFAULT_PAGE_NUMBER = 0;
	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final int DEFAULT_LAST_CHATS_SIZE = 30;

	@Autowired
	private ConversationDao conversationDao;

	@Autowired
	private ChatDao chatDao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private ItemDao itemDao;

	@Autowired
	RoleFactory roleFactory;

	@Autowired
	private ChatHelper chatHelper;
	
    @Autowired
    private InternationalizationUtil i18nUtil;

	@Override
	public ListConversationsResponse execute(ListConversationsByItemRequest request)
			throws ServiceException {
		UserSessionCache session = sessionService.getUserSession(request.getToken());
		Item item = itemDao.findOne(request.getItemId());
		
		if (item == null) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);
		}

		if (request.getPage() == null || request.getPage() < 0) {
			request.setPage(DEFAULT_PAGE_NUMBER);
		}

		if (request.getPageSize() == null || request.getPageSize() < 1) {
			request.setPageSize(DEFAULT_PAGE_SIZE);
		}

		Date lastRequest;
		try {
			lastRequest = DataCommonUtil.parseUnixTime(request.getLastRequest());
		} catch (IllegalArgumentException e) {
			throw createServiceException(ServiceExceptionCode.LAST_REQUEST_BAD_FORMAT_ERROR);
		}

		PageRequest pageRequest = new PageRequest(request.getPage(), request.getPageSize());
		List<Conversation> conversations = null;
		int totalResult = 0;
		if (StringUtils.isBlank(request.getRole())) {
			conversations = conversationDao.findConversationsByItemId(item.getId(), pageRequest);
			totalResult = conversationDao.findConversationsByItemIdCount(item.getId());
		} else {
			if (request.getRole().equals(Role.BUYER)) {
				conversations = conversationDao.findConversationsByItemIdForBuyer(
						session.getUserId(), item.getId(), pageRequest);
				totalResult = conversationDao.findConversationsByItemIdForBuyerCount(
						session.getUserId(), item.getId());
			} else if (request.getRole().equals(Role.SELLER)) {
				conversations = conversationDao.findConversationsByItemIdForSeller(
						session.getUserId(), item.getId(), pageRequest);
				totalResult = conversationDao.findConversationsByItemIdForSellerCount(
						session.getUserId(), item.getId());
			}
		}

		List<SmallConversation> conversationsDTOs =  new ArrayList<SmallConversation>();
		for (Conversation conversation : conversations) {
			if (ConversationValidator.isUserBuyerOrSeller(conversation, session.getUserId())) {
				Role role = roleFactory.createInverseRole(conversation, session.getUserId());
				SmallConversation smallConversation = new SmallConversation(conversation, role);
				List<Chat> chats = chatDao.findLastChatsOfConversation(conversation.getId(),
						new PageRequest(0, DEFAULT_LAST_CHATS_SIZE));
				Long count = chatDao.findForConversationChatsMoreRecentThan(conversation.getId(),
						lastRequest);
				smallConversation.setNewChats(count > 0);
				if (!chats.isEmpty()) {
					
					String userLang = request.getLanguage() != null ? request
							.getLanguage() : session.getLang();
					
					for (Chat chat : chats) {
						
						SmallChat smallChat = chatHelper.createaSmallChat(chat,
								userLang);
						smallConversation.addChat(smallChat);
					
						// if ACCEPTED, we need to add an extra message (Front End req).
						if (chat.getAction().equals(ChatActionType.ACCEPTED)) {
							smallConversation.addChat(chatHelper
									.createaSmallChatOfferAccepted(chat,userLang));
						}
						
						//if chat has an offer, we need to set a flag (Front End req).
						if (chat.getAction().equals(ChatActionType.OFFER)) {
							smallConversation.setHasOffer(Boolean.TRUE);
						}
					}
					smallConversation.setLastChat(chatHelper.createaSmallChat(chats.get(0),
							request.getLanguage() != null ? request.getLanguage() : session.getLang()));
				}
				conversationsDTOs.add(smallConversation);
			}
		}
		
		addSort(conversationsDTOs);
		
		ListConversationsResponse response = new ListConversationsResponse(conversationsDTOs, totalResult);	
		i18nUtil.internationalizeListedResponse(
				response,
				NO_RESULTS_MESSAGE,
				request.getLanguage() != null ? request.getLanguage() : session
						.getLang());

		return response;
	}
	
	/**
     * It sorts the conversation list from newest to latest.
	 * @param conversationsDTOs the conversation to sort
	 */
	private void addSort(List<SmallConversation> conversationsDTOs) {
		Collections.sort(conversationsDTOs, new Comparator<SmallConversation>() {
			@Override
			public int compare(SmallConversation sc1, SmallConversation sc2) {
				if(sc1.getLastChat() == null || sc2.getLastChat() == null) {
					return -1;
				}
				return sc2.getLastChat().getChatId().compareTo(sc1.getLastChat().getChatId());
			}
		});
	}
	
}
