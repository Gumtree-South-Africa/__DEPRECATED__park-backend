package com.ebay.park.service.conversation.command;

import com.ebay.park.db.dao.ChatDao;
import com.ebay.park.db.dao.ConversationDao;
import com.ebay.park.db.entity.*;
import com.ebay.park.db.entity.Chat.ChatActionType;
import com.ebay.park.service.conversation.dto.ListConversationsRequest;
import com.ebay.park.service.conversation.dto.ListConversationsResponse;
import com.ebay.park.service.conversation.dto.SmallChat;
import com.ebay.park.service.conversation.dto.SmallConversation;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.InternationalizationUtil;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ListConversationsCmdTest {
	@Spy
	@InjectMocks
	private final ListConversationsCmd cmd = new ListConversationsCmd();

	@Mock
	private ConversationDao conversationDao;

	@Mock
	private ChatDao chatDao;

	@Mock
	private SessionService sessionService;

	@Mock
	private ListConversationsRequest request;

	@Mock
	private UserSessionCache session;

	@Mock
	private Conversation conversation;

	@Mock
	private Role role;

	@Mock
	private RoleFactory roleFactory;

	@Mock
	private Item item;

	@Mock
	private ChatHelper chatHelper;
	
	@Mock
	private InternationalizationUtil i18nUtil;

	private final Long itemId = 1l;
	private final String token = "token";
	private final Long userId = 1l;
	private final Long conversationId = 2l;
	private final Long roleId = 1l;
	private final String roleUsername = "username";
	private final Double currentPrice = new Double(10.2);
	private final Long chatId = 1l;
	private final Long senderId = 1l;
	private User sender;
	private final Long receiverId = 1l;
	private User receiver;
	private final String comment = "comment";
	private Date postTime;
	private final String postTimeStr = "2014-07-07";
	private final Double offeredPrice = new Double(10.2);
	private final Chat chat = new Chat();
	private String lastRequestStr = "20140728T102000-0300";
	private Date lastRequest = null;
	private SmallChat smallChat;

	@Before
	public void setUp() throws ParseException {
		initMocks(this);
		postTime = DataCommonUtil.parseISODate(postTimeStr);
		lastRequest = DataCommonUtil.parseISOCompleteLocalDate(lastRequestStr);
		lastRequestStr = DataCommonUtil.getDateTimeAsUnixFormat(lastRequest);
		sender = new User();
		receiver = new User();
		sender.setUserId(senderId);
		receiver.setUserId(receiverId);
		smallChat = mock(SmallChat.class);
		buildChat();
	}

	private Chat buildChat() {
		chat.setChatId(chatId);
		chat.setSender(sender);
		chat.setReceiver(receiver);
		chat.setComment(comment);
		chat.setPostTime(DateTime.now().toDate());
		chat.setOfferedPrice(offeredPrice);
		chat.setAction(ChatActionType.OFFER);
		return chat;
	}

	@Test
	public void testShouldSucceed() {
		when(request.getPage()).thenReturn(0);
		when(request.getPageSize()).thenReturn(10);
		when(request.getToken()).thenReturn(token);
		when(request.getLastRequest()).thenReturn(lastRequestStr);
		when(sessionService.getUserSession(token)).thenReturn(session);
		when(session.getUserId()).thenReturn(userId);
		List<Conversation> conversations = new ArrayList<Conversation>(1);
		conversations.add(conversation);
		when(request.getRole()).thenReturn("buyer");
		when(conversationDao.findConversationsForBuyer(any(Long.class), any(Pageable.class)))
				.thenReturn(conversations);
		when(conversationDao.findConversationsForBuyerCount(any(Long.class)))
		.thenReturn(1);
		
		when(session.getUserId()).thenReturn(userId);
		when(session.getLang()).thenReturn("en");
		when(roleFactory.createInverseRole(conversation, userId)).thenReturn(role);
		when(conversation.getItem()).thenReturn(item);
		when(item.getId()).thenReturn(itemId);
		when(conversation.getId()).thenReturn(conversationId);
		when(role.getId()).thenReturn(roleId);
		when(role.getUsername()).thenReturn(roleUsername);
		when(conversation.getCurrentPriceProposedByBuyer()).thenReturn(currentPrice);
		when(conversation.getCurrentPriceProposedBySeller()).thenReturn(currentPrice);
		when(conversation.getStatus()).thenReturn(ConversationStatus.OPEN);

		User buyerMock = Mockito.mock(User.class);
		when(conversation.getBuyer()).thenReturn(buyerMock);
		when(buyerMock.getPicture()).thenReturn("buyer picture");

		User sellerMock = Mockito.mock(User.class);
		when(conversation.getSeller()).thenReturn(sellerMock);
		when(sellerMock.getPicture()).thenReturn("seller picture");

		when(chatDao.findForConversationChatsMoreRecentThan(conversationId, lastRequest))
				.thenReturn(1l);

		List<Chat> chats = new ArrayList<Chat>(1);
		chats.add(chat);
		when(chatDao.findLastChatsOfConversation(any(Long.class), any(Pageable.class))).thenReturn(
				chats);

		when(smallChat.getChatId()).thenReturn(chatId);
		when(smallChat.getSenderId()).thenReturn(senderId);
		when(smallChat.getReceiverId()).thenReturn(receiverId);
		when(smallChat.getComment()).thenReturn(comment);
		when(smallChat.getPostTime()).thenReturn(DataCommonUtil.getDateTimeAsUnixFormat(postTime));
		when(smallChat.getOfferedPrice()).thenReturn(offeredPrice);

		when(chatHelper.createaSmallChat(chat, session.getLang())).thenReturn(smallChat);

		ListConversationsResponse response = cmd.execute(request);

		SmallConversation smallConversation = response.getConversations().get(0);

		assertEquals(itemId, smallConversation.getItemId());
		assertEquals(conversationId, smallConversation.getConversationId());
		assertEquals(userId, smallConversation.getUserId());
		assertEquals(roleUsername, smallConversation.getUsername());
		assertEquals(currentPrice, smallConversation.getCurrentPriceProposedByBuyer());
		assertEquals(currentPrice, smallConversation.getCurrentPriceProposedBySeller());

		verify(request, atLeastOnce()).getPage();
		verify(request, atLeastOnce()).getPageSize();
		verify(request, atLeastOnce()).getToken();
		verify(request).getLastRequest();
		verify(sessionService).getUserSession(token);
		verify(session, atLeastOnce()).getUserId();
		verify(conversationDao).findConversationsForBuyer(any(Long.class), any(Pageable.class));
		verify(conversation, atLeastOnce()).getItem();
		verify(item).getId();
		verify(conversation, atLeastOnce()).getId();
		verify(role).getId();
		verify(role).getUsername();
		verify(conversation).getCurrentPriceProposedBySeller();
		verify(conversation).getCurrentPriceProposedByBuyer();
		verify(conversation, atLeastOnce()).getStatus();
		verify(chatDao).findLastChatsOfConversation(any(Long.class), any(Pageable.class));
		verify(chatDao).findForConversationChatsMoreRecentThan(conversationId, lastRequest);
	}
}
