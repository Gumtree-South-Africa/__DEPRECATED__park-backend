package com.ebay.park.service.conversation.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.ChatDao;
import com.ebay.park.db.dao.ConversationDao;
import com.ebay.park.db.entity.Chat;
import com.ebay.park.db.entity.Chat.ChatActionType;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.conversation.dto.ListConversationsRequest;
import com.ebay.park.service.conversation.dto.ListConversationsResponse;
import com.ebay.park.service.conversation.dto.SmallChat;
import com.ebay.park.service.conversation.dto.SmallConversation;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.InternationalizationUtil;
/**
 * 
 * @author marcos.lambolay
 */
@Component
public class ListConversationsCmd implements
		ServiceCommand<ListConversationsRequest, ListConversationsResponse> {

	@Autowired
	private ConversationDao conversationDao;

	@Autowired
	private ChatDao chatDao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	RoleFactory roleFactory;

	@Autowired
	private ChatHelper chatHelper;
	
    @Autowired
    private InternationalizationUtil i18nUtil;

	private static final int DEFAULT_PAGE_NUMBER = 0;
	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final int DEFAULT_LAST_CHATS_SIZE = 1;
	private static final String NO_RESULTS_BUYER = "emptylist.chat_offers_made";
	private static final String NO_RESULTS_SELLER = "emptylist.chat_offers_received";

	@Override
	public ListConversationsResponse execute(ListConversationsRequest request)
			throws ServiceException {

		if (request.getPage() == null || request.getPage() < 0) {
			request.setPage(DEFAULT_PAGE_NUMBER);
		}

		if (request.getPageSize() == null || request.getPageSize() < 1) {
			request.setPageSize(DEFAULT_PAGE_SIZE);
		}

		UserSessionCache session = sessionService.getUserSession(request.getToken());
		Date lastRequest;
		try {
			lastRequest = DataCommonUtil.parseUnixTime(request.getLastRequest());
		} catch (IllegalArgumentException e) {
			throw createServiceException(ServiceExceptionCode.LAST_REQUEST_BAD_FORMAT_ERROR);
		}

		PageRequest pageRequest = new PageRequest(request.getPage(), request.getPageSize());

		List<Conversation> conversations = Collections.emptyList();
		int totalResult = 0;
		if (request.getRole().equals(Role.BUYER)) {
			conversations = conversationDao.findConversationsForBuyer(session.getUserId(),
					pageRequest);
			totalResult  = conversationDao.findConversationsForBuyerCount(session.getUserId());
		} else if (request.getRole().equals(Role.SELLER)) {
			conversations = conversationDao.findConversationsForSeller(session.getUserId(),
					pageRequest);
			totalResult  = conversationDao.findConversationsForSellerCount(session.getUserId());
		}

		List<SmallConversation> conversationsDTOs =  new ArrayList<SmallConversation>();
		for (Conversation conversation : conversations) {
			Role role = roleFactory.createInverseRole(conversation, session.getUserId());
			SmallConversation smallConversation = new SmallConversation(conversation, role);
			List<Chat> chats = chatDao.findLastChatsOfConversation(conversation.getId(),
					new PageRequest(0, DEFAULT_LAST_CHATS_SIZE));
			Long count = chatDao.findForConversationChatsMoreRecentThan(conversation.getId(),
					lastRequest);
			smallConversation.setNewChats(count > 0);
			if (!chats.isEmpty()) {
				SmallChat lastChat = chatHelper.createaSmallChat(chats.get(0),
						request.getLanguage() != null ? request.getLanguage() : session.getLang());
				smallConversation.setLastChat(lastChat);
				
				//if chat has an offer, we need to set a flag (Front End req).
				if (lastChat != null && lastChat.getAction() != null 
						&& lastChat.getAction().equals(ChatActionType.OFFER)) {
					smallConversation.setHasOffer(Boolean.TRUE);
				}
			}
			
			conversationsDTOs.add(smallConversation);
		}
		
		addSort(conversationsDTOs);
		
		ListConversationsResponse response = new ListConversationsResponse(conversationsDTOs, totalResult);
		i18nUtil.internationalizeListedResponse(response,
				getNoResultMessageKey(request),
				request.getLanguage() != null ? request.getLanguage()
						: request.getLanguage() != null ? request.getLanguage() : session.getLang());

		return response;
	}
	
	private String getNoResultMessageKey(ListConversationsRequest request) {
		if(request.getRole().equals(Role.BUYER)) {
			return NO_RESULTS_BUYER;
		}
		else return NO_RESULTS_SELLER;
	}

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