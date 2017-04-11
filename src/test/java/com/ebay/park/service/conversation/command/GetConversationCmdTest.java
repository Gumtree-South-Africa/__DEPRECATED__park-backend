package com.ebay.park.service.conversation.command;

import com.ebay.park.db.dao.ChatDao;
import com.ebay.park.db.dao.ConversationDao;
import com.ebay.park.db.entity.*;
import com.ebay.park.db.entity.Chat.ChatActionType;
import com.ebay.park.service.conversation.dto.GetConversationRequest;
import com.ebay.park.service.conversation.dto.SmallChat;
import com.ebay.park.service.conversation.dto.SmallConversation;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.DataCommonUtil;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Role.class})
public class GetConversationCmdTest {
	@Spy
	@InjectMocks
	private final GetConversationCmd cmd = new GetConversationCmd();

	@Mock
	private ConversationDao conversationDao;

	@Mock
	private SessionService sessionService;

	@Mock
	private GetConversationRequest request;

	@Mock
	private UserSessionCache session;

	@Mock
	private Conversation conversation;

	@Mock
	private Role role;

	@Mock
	private RoleFactory roleFactory;

	@Mock
	private User buyer;

	@Mock
	private User seller;

	@Mock
	private ChatDao chatDao;

	@Mock
	private ChatHelper chatHelper;

	@Mock
	private Item item;
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
	private String lastRequestStr = "20140728T102000-0300";
	private Date lastRequest = null;
	private final Double offeredPrice = new Double(10.2);
	private final Chat chat = new Chat();
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
		buildChat();
		smallChat = mock(SmallChat.class);
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
		when(request.getToken()).thenReturn(token);
		when(sessionService.getUserSession(token)).thenReturn(session);
		when(request.getConversationId()).thenReturn(conversationId.toString());
		when(conversationDao.findOne(conversationId)).thenReturn(conversation);
		PowerMockito.mockStatic(Role.class);
		when(session.getUserId()).thenReturn(userId);
		when(roleFactory.createInverseRole(conversation, userId))
				.thenReturn(role);
		when(conversation.getItem()).thenReturn(item);
		when(item.getId()).thenReturn(itemId);
		when(conversation.getId()).thenReturn(conversationId);
		when(role.getId()).thenReturn(roleId);
		when(conversation.getBuyer()).thenReturn(buyer);
		when(conversation.getSeller()).thenReturn(seller);
		when(seller.getPicture()).thenReturn("sellerPicture");
		when(buyer.getPicture()).thenReturn("buyerPicture");
		when(role.getUsername()).thenReturn(roleUsername);
		when(conversation.getCurrentPriceProposedByBuyer()).thenReturn(currentPrice);
		when(conversation.getCurrentPriceProposedBySeller()).thenReturn(currentPrice);
		when(conversation.getStatus()).thenReturn(ConversationStatus.OPEN);
		when(request.getLastRequest()).thenReturn(lastRequestStr);
		when(chatDao.findForConversationChatsMoreRecentThan(conversationId, lastRequest))
				.thenReturn(1l);

		List<Chat> chats = new ArrayList<Chat>(1);
		chats.add(chat);
		when(conversation.getChats()).thenReturn(chats);

		when(smallChat.getChatId()).thenReturn(chatId);
		when(smallChat.getSenderId()).thenReturn(senderId);
		when(smallChat.getReceiverId()).thenReturn(receiverId);
		when(smallChat.getComment()).thenReturn(comment);
		when(smallChat.getPostTime()).thenReturn(DataCommonUtil.getDateTimeAsUnixFormat(postTime));
		when(smallChat.getOfferedPrice()).thenReturn(offeredPrice);

		when(chatHelper.createaSmallChat(chat, session.getLang())).thenReturn(smallChat);

		SmallConversation smallConversation = cmd.execute(request);

		assertEquals(itemId, smallConversation.getItemId());
		assertEquals(conversationId, smallConversation.getConversationId());
		assertEquals(userId, smallConversation.getUserId());
		assertEquals(roleUsername, smallConversation.getUsername());
		assertEquals(currentPrice, smallConversation.getCurrentPriceProposedByBuyer());
		assertEquals(currentPrice, smallConversation.getCurrentPriceProposedBySeller());

		SmallChat smallChat = smallConversation.getChats().get(0);
		assertEquals(chatId, smallChat.getChatId());
		assertEquals(senderId, smallChat.getSenderId());
		assertEquals(receiverId, smallChat.getReceiverId());
		assertEquals(comment, smallChat.getComment());
		assertEquals(DataCommonUtil.getDateTimeAsUnixFormat(postTime), smallChat.getPostTime());
		assertEquals(offeredPrice, smallChat.getOfferedPrice());

		verify(request).getToken();
		verify(sessionService).getUserSession(token);
		verify(request).getConversationId();
		verify(conversationDao).findOne(conversationId);
		verify(roleFactory).createInverseRole(conversation, userId);
		verify(request).getLastRequest();
		verify(conversation, atLeastOnce()).getItem();
		verify(item).getId();
		verify(conversation, times(2)).getId();
		verify(role).getId();
		verify(conversation).getBuyer();
		verify(conversation).getSeller();
		verify(buyer).getPicture();
		verify(seller).getPicture();
		verify(role).getUsername();
		verify(conversation).getCurrentPriceProposedBySeller();
		verify(conversation).getCurrentPriceProposedByBuyer();
		verify(conversation, atLeastOnce()).getChats();
		verify(conversation).getCurrentPriceProposedByBuyer();
		verify(conversation).getCurrentPriceProposedBySeller();
		verify(conversation, atLeastOnce()).getStatus();
		verify(request).getLastRequest();
		verify(chatDao).findForConversationChatsMoreRecentThan(conversationId, lastRequest);
	}
}