package com.ebay.park.service.conversation.command;

import com.ebay.park.db.dao.ChatDao;
import com.ebay.park.db.dao.ConversationDao;
import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Chat;
import com.ebay.park.db.entity.Chat.ChatActionType;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.conversation.dto.ListConversationsByItemRequest;
import com.ebay.park.service.conversation.dto.ListConversationsResponse;
import com.ebay.park.service.conversation.dto.SmallChat;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.InternationalizationUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author giriarte
 *
 */
public class ListConversationsByItemCmdTest {

	private static final Long ITEM_ID = 1L;
	private static final String USR_TOKEN = "usrToken";
	private static final Long BUYER_ID = 1L;
	private static final Long SELLER_ID = 2L;
	private static final int PAGE_SIZE = 50;
	private static final int PAGE_NUMBER = 0;
    private static final String TIMESTAMP = "1464723444";

	@InjectMocks
	private ListConversationsByItemCmd listConversationByItemsCmd;
	
	@Mock
	private ConversationDao conversationDao;

	@Mock
	private ChatDao chatDao;

	@Mock
	private SessionService sessionService;

	@Mock
	private ItemDao itemDao;

	@Mock
	RoleFactory roleFactory;

	@Mock
	private ChatHelper chatHelper;
	
	@Mock
    private InternationalizationUtil i18nUtil;
	
	private UserSessionCache session = Mockito.mock(UserSessionCache.class);
	
	private ListConversationsByItemRequest request = new ListConversationsByItemRequest(ITEM_ID, USR_TOKEN, "en");
	
	private User buyer;
	private User seller;
	
	private PageRequest pageRequest = new PageRequest(PAGE_NUMBER, PAGE_SIZE);
	
	private Item item = new Item("itName", 333D, "v1.1.0", false, false);
	
    @Before
    public void setUp(){
    	initMocks(this);
    	
    	buyer = new User();
    	buyer.setUsername("the buyer");
    	buyer.setId(BUYER_ID);
    	
    	seller = new User();
    	seller.setUsername("the seller");
    	seller.setId(SELLER_ID);
    	
    	List<Conversation> conversations = new ArrayList<Conversation>();
    	conversations.add(new Conversation(buyer, seller, item));
    	conversations.get(0).setId(1L);
    	
    	Role buyerRole = new BuyerRole();
    	buyerRole.setConversation(conversations.get(0));
    	
    	Role sellerRole = new SellerRole();
    	sellerRole.setConversation(conversations.get(0));
    	
    	request.setPage(PAGE_NUMBER);
    	request.setPageSize(PAGE_SIZE);
    	request.setLastRequest(TIMESTAMP);
    	
    	item.setId(ITEM_ID);
    	when(conversationDao.findConversationsByItemIdForBuyer(buyer.getId(), item.getId(), pageRequest)).thenReturn(conversations);
    	when(conversationDao.findConversationsByItemIdForBuyerCount(buyer.getId(), item.getId())).thenReturn(1);
    	when(conversationDao.findConversationsByItemIdForSeller(seller.getId(), item.getId(), pageRequest)).thenReturn(conversations);
    	when(conversationDao.findConversationsByItemIdForSellerCount(buyer.getId(), item.getId())).thenReturn(1);
    	when(conversationDao.findConversationsByItemId(ITEM_ID, pageRequest)).thenReturn(conversations);
    	when(conversationDao.findConversationsByItemIdCount(ITEM_ID)).thenReturn(1);
    	
    	when(sessionService.getUserSession(request.getToken())).thenReturn(session);
    	
    	
    	when(roleFactory.createInverseRole(conversations.get(0), BUYER_ID)).thenReturn(sellerRole);
    	when(roleFactory.createInverseRole(conversations.get(0), SELLER_ID)).thenReturn(buyerRole);
    	
    	when(itemDao.findOne(ITEM_ID)).thenReturn(item);
    	
    	Chat chat = new Chat();
    	chat.setAction(ChatActionType.ACCEPTED);
    	
    	SmallChat smallChat = new SmallChat();
    	
    	when(roleFactory.createInverseRole(conversations.get(0), BUYER_ID)).thenReturn(sellerRole);
    	 	
    	when(chatHelper.createaSmallChat(chat, "en")).thenReturn(smallChat);
    	List<Chat> chats = new ArrayList<Chat>();
    	chats.add(chat);
    	
    	when(chatDao.findLastChatsOfConversation(conversations.get(0).getId(),new PageRequest(0, 30))).thenReturn(chats);
    	when(chatDao.findForConversationChatsMoreRecentThan((Long)any(), (Date)any())).thenReturn(1L);
    	
    	Mockito.doNothing().when(i18nUtil).internationalizeListedResponse((ListConversationsResponse)any(),(String)any(),(String)any());
    	
    }
    
    @Test
    public void getConversationsWithoutRole(){
    	when(session.getUserId()).thenReturn(567L);
    	ListConversationsResponse response = listConversationByItemsCmd.execute(request);
    	assertEquals(0, response.getConversations().size());
    }
    
    @Test
    public void getConversationsForBuyer(){
    	when(session.getUserId()).thenReturn(BUYER_ID);
    	ListConversationsResponse response = listConversationByItemsCmd.execute(request);
    	assertEquals(1, response.getConversations().size());
    }
 
    @Test
    public void getConversationsForSeller(){
    	when(session.getUserId()).thenReturn(SELLER_ID);
    	ListConversationsResponse response = listConversationByItemsCmd.execute(request);
    	assertEquals(1, response.getConversations().size());
    }
    
    
}
