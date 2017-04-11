package com.ebay.park.service.conversation.command;

import com.ebay.park.db.dao.*;
import com.ebay.park.db.entity.*;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.conversation.dto.SendChatRequest;
import com.ebay.park.service.conversation.dto.SendChatResponse;
import com.ebay.park.service.conversation.dto.SendOfferRequest;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class SendChatCmdTest {

	private static final String TOKEN = "TOKEN";
	private static final long USER_ID = 1L;
	private static final long ITEM_ID = 1L;
	private static final double ITEM_PRICE = 10.0;
	private static final long SELLER_ID = 30L;
	private static final long BUYER_ID = 31L;
	private static final Long CHAT_ID = 1L;
	private static final String OFFERED_PRICE = "10.2";

	@Spy
	@InjectMocks
	private SendChatCmd cmd = new SendChatCmd();

	@Mock
	private ConversationDao conversationDao;

	@Mock
	private SessionService sessionService;

	@Mock
	private ItemDao itemDao;
	
	@Mock
	private RatingDao ratingDao;

	@Mock
	private SendChatRequest request;
	
	@Mock 
	private UserSessionCache session;
	
	@Mock
	private Item item;
	
	@Mock
	private UserDao userDao;

	@Mock
	private ChatDao chatDao;
	
	@Mock
	private RoleFactory roleFactory;
	
	@Mock
	private DoubleRole role;
	
	@Mock
	private ChatHelper chatHelper;
	
	@Mock
	private UserFollowsItemDao userFollowsItemDao;

	@Mock
	private ItemUtils itemUtils;

	@Mock
	private User buyer;

	@Mock
	private Role buyerRole;

	@Mock
	private User seller;

	@Mock
	private Role sellerRole;

	@Mock
	private Chat chat;
	
	@Before
	public void setUp(){
		initMocks(this);
		when(itemDao.findOne(ITEM_ID)).thenReturn(item);
		when(sessionService.getUserSession(TOKEN)).thenReturn(session);
		when(session.getUserId()).thenReturn(USER_ID);
		when(roleFactory.createDoubleRole(item, USER_ID, null)).thenReturn(role);
		
		when(buyer.getId()).thenReturn(BUYER_ID);
		when(role.getBuyer()).thenReturn(buyerRole);
		when(seller.getId()).thenReturn(SELLER_ID);
		when(role.getSeller()).thenReturn(sellerRole);
		when(userDao.getOne(BUYER_ID)).thenReturn(buyer);
		when(userDao.getOne(SELLER_ID)).thenReturn(seller);
		when(item.getPrice()).thenReturn(ITEM_PRICE);
		when(role.getMe()).thenReturn(buyerRole);

		when(chat.getChatId()).thenReturn(CHAT_ID);
		when(chatHelper.buildChat(any(SendChatRequest.class), any(Conversation.class), any(Item.class), any(DoubleRole.class), any(String.class))).thenReturn(chat);
		
		when(role.isSeller()).thenReturn(false);
		when(item.getId()).thenReturn(ITEM_ID);
		when(item.getStatus()).thenReturn(StatusDescription.ACTIVE);
		when(item.is(StatusDescription.ACTIVE)).thenReturn(true);
		
	}
	
	@Test
	public void givenNewConversationAndPlainChatRequestWhenExecutingThenSendChat() {
		when(request.getItemId()).thenReturn(ITEM_ID);
		when(request.getConversationId()).thenReturn(null);
		when(request.getToken()).thenReturn(TOKEN);
		
		SendChatResponse response = cmd.execute(request);
		assertThat(response.getChatId(), is(CHAT_ID));

		verify(itemDao).findOne(ITEM_ID);
		verify(roleFactory).createDoubleRole(item, USER_ID, null);
		verify(role).validateSenderAndReceiverNotTheSame();
		verify(role).validateIsNotSeller();
		verify(userDao, atLeastOnce()).findOne(any(Long.class));
		verify(role).setConversation(any(Conversation.class));

		verify(conversationDao).saveAndFlush(any(Conversation.class));
		verify(chatHelper).sendChat(any(Chat.class), any(Conversation.class), any(Item.class), any(DoubleRole.class), anyString());
		verify(role).isSeller();
	}
	
	@Test
	public void givenNewConversationAndOfferRequestWhenExecutingThenSendOffer() {
		request = new SendOfferRequest();
		SendOfferRequest offerRequest = (SendOfferRequest)request;
		offerRequest.setItemId(ITEM_ID);
		offerRequest.setConversationId(null);
		offerRequest.setOfferedPrice(OFFERED_PRICE);
		offerRequest.setToken(TOKEN);
		
		SendChatResponse response = cmd.execute(offerRequest);
		assertEquals(CHAT_ID, response.getChatId());
		verify(chatHelper).buildChat(any(SendChatRequest.class), any(Conversation.class), any(Item.class), any(DoubleRole.class), any(String.class));
		
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenNullRequestWhenExecutingThenException() {
		cmd.execute(null);
	}

	@Test
	public void givenNullItemWhenExecutingThenException() {
		when(itemDao.findOne(ITEM_ID)).thenReturn(null);
		try {
			cmd.execute(request);
		} catch (ServiceException e) {
			assertThat("ITEM_NOT_FOUND exception is expected", e.getCode(), is(ServiceExceptionCode.ITEM_NOT_FOUND.getCode()));
		}
	}

	@Test
	public void givenExpiredItemWhenExecutingThenException() {
		when(request.getItemId()).thenReturn(ITEM_ID);
		when(itemDao.findOne(ITEM_ID)).thenReturn(item);
		when(item.is(StatusDescription.EXPIRED)).thenReturn(true);
		try {
			cmd.execute(request);
		} catch (ServiceException e) {
			assertThat("ITEM_EXPIRED exception is expected", e.getCode(), is(ServiceExceptionCode.ITEM_EXPIRED.getCode()));
		}
	}
}
