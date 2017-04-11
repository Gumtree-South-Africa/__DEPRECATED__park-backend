package com.ebay.park.service.conversation.command;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.ebay.park.event.conversation.ChatSentEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.aop.Notifiable;
import com.ebay.park.util.TextUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;

import com.ebay.park.db.dao.ChatDao;
import com.ebay.park.db.dao.RatingDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Chat;
import com.ebay.park.db.entity.Chat.ChatActionType;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.conversation.dto.SendChatRequest;
import com.ebay.park.service.conversation.dto.SendOfferRequest;
import com.ebay.park.service.conversation.dto.SmallChat;
import com.ebay.park.util.InternationalizationUtil;

import java.lang.reflect.Method;

public class ChatHelperTest {

	private static final Long ITEM_ID = 2L;
	private static final Long CONVER_ID = 23L;
	private static final String USR_TOKEN = "usrTok";
	private static final Long BUYER_ID = 5L;
	private static final Long SELLER_ID = 6L;
	
	//Accepted Offer chat messages
	private static final String NEGOCIATION_ACCEPTED_KEY = "negociation.accepted";
	private static final String NEGOCIATION_ACCEPTED_HINT_KEY = "negociation.accepted.hint";
	private static final String EXPECTED_NEGOCIATION_ACCEPTED_MSG = "Congratulations! You have made a deal!";
	private static final String EXPECTED_NEGOCIATION_ACCEPTED_HINT= "The item has been set as Sold, "
			+ "so now you should keep chatting and coordinate when and where do you want to complete the transaction";
	
	//Cancelled chat messages
	private static final String NEGOCIATION_CANCELLED_AUTOMATICALLY_KEY = "negociation.cancelled.automatically";
	private static final String EXPECTED_CANCELLED_AUTOMATICALLY_HINT = "This negotiation got canceled since the item is no longer available";
	private static final String NEGOCIATION_CANCELLED_KEY = "negociation.cancelled";
	private static final String EXPECTED_NEGOCIATION_CANCELLED_MSG = "The negotiation has been cancelled";
	private static final String NEGOCIATION_CANCELLED_NO_REASON_KEY = "negociation.cancelled.no.reason";
	private static final String EXPECTED_NEGOCIATION_CANCELLED_NO_REASON_HINT = "No reason provided";
	
	//Marked as Sold messages
	private static final String MARKED_AS_SOLD_MESSAGE = "negociation.marked.as.sold";
	private static final String EXPECTED_MARKED_AS_SOLD_MSG = "I have sold %s. Thank you for contacting me.";
	
	//Offer chat messages
	private static final String OFFER_MESSAGE = "negociation.offer.message";
	private static final String EXPECTED_OFFER_MSG = "Offer made for";
	private static final String LANG = "en";
	private static final String URL = "url";
	private static final String BUYER_USERNAME = "buyer_username";
	private static final long CONVERSATION_ID = 1L;
	private static final String SELLER_USERNAME = "seller_username";

	@Mock
	private UserDao userDao; 

	@Mock
	private ChatDao chatDao;

	@Mock
	private RatingDao ratingDao;

	@Mock
	protected InternationalizationUtil i18nUtil;

	@Mock
	private ApplicationContext applicationContext;

	@InjectMocks
	private ChatHelper chatHelper = new ChatHelper();

	@Mock
	private TextUtils textUtils;

	private DoubleRole role;
	private RoleFactory factory = mock(RoleFactory.class);
	private Conversation conversation;
	private Item item;

	@Before
	public void setUp(){
		initMocks(this);
		User buyer = createBuyer();
		User seller = createSeller();
		createItem(buyer);
		createConversation(buyer, seller);
		
		when(userDao.getOne(SELLER_ID)).thenReturn(seller);
		when(userDao.getOne(BUYER_ID)).thenReturn(buyer);

		role = new DoubleRole(item, BUYER_ID, conversation, factory);
	}

	private void createConversation(User buyer, User seller) {
		conversation = new Conversation(buyer, seller, item);
		conversation.setId(CONVERSATION_ID);

		SellerRole sellerRole = new SellerRole();
		sellerRole.setConversation(conversation);

		BuyerRole buyerrole = new BuyerRole();
		buyerrole.setConversation(conversation);
		when(factory.createSellerRole(conversation)).thenReturn(sellerRole);
		when(factory.createBuyerRole(conversation)).thenReturn(buyerrole);
	}

	private void createItem(User buyer) {
		item = new Item("itName", 234D, "v1.0", false, false);
		item.setId(ITEM_ID);
		item.setPublishedBy(buyer);
	}

	private User createSeller() {
		User seller = new User();
		seller.setId(SELLER_ID);
		seller.setUsername(SELLER_USERNAME);
		return seller;
	}

	private User createBuyer() {
		User buyer = new User();
		buyer.setUsername(BUYER_USERNAME);
		buyer.setId(BUYER_ID);
		return buyer;
	}

	@Test
	public void givenCommonChatRequestWhenBuildingChatThenBuildCommonChat(){
		SendChatRequest request = new SendChatRequest();
		request.setItemId(ITEM_ID);
		request.setComment("Comment of chat");
		request.setConversationId(CONVER_ID);
		request.setLanguage(LANG);
		request.setToken(USR_TOKEN);

		Chat chat = chatHelper.buildChat(request, conversation, item, role, LANG);
		
		assertEquals(chat.getAction(), ChatActionType.CHAT);
		assertEquals(chat.getConversation(), conversation);
		assertEquals(chat.getItem().getId(), ITEM_ID);
	}
	
	@Test
	public void givenOfferRequestWhenBuildingChatThenBuildOfferChat(){
		SendOfferRequest request = new SendOfferRequest();
		request.setItemId(ITEM_ID);
		request.setConversationId(CONVER_ID);
		request.setLanguage(LANG);
		request.setToken(USR_TOKEN);
		request.setOfferedPrice("34");

		Chat chat = chatHelper.buildChat(request, conversation, item, role, LANG);
		
		assertEquals(chat.getAction(), ChatActionType.OFFER);
		assertEquals(chat.getConversation(), conversation);
		assertEquals(chat.getItem().getId(), ITEM_ID);
	}
	
	@Test
	public void buildAcceptedOfferSmallChatTest() {
		//given
		Chat chat = createBasicChat();
		chat.setAction(ChatActionType.ACCEPTED);
		
		//when
		when(i18nUtil.internationalize(NEGOCIATION_ACCEPTED_KEY, LANG)).thenReturn(EXPECTED_NEGOCIATION_ACCEPTED_MSG);
		when(i18nUtil.internationalize(NEGOCIATION_ACCEPTED_HINT_KEY, LANG)).thenReturn(EXPECTED_NEGOCIATION_ACCEPTED_HINT);
		SmallChat smallChat = chatHelper.createaSmallChat(chat, LANG);
		
		//then
		assertEquals(smallChat.getAction(), ChatActionType.ACCEPTED.toString());
		assertEquals(smallChat.getHint(), EXPECTED_NEGOCIATION_ACCEPTED_HINT);
		assertEquals(smallChat.getComment(), EXPECTED_NEGOCIATION_ACCEPTED_MSG);
	}
	
	@Test
	public void buildAutomaticallyCancelledSmallChatTest() {
		//given
		Chat chat = createBasicChat();
		chat.setAction(ChatActionType.CANCELLED);
		chat.setAutomaticGeneratedAction(true);
		
		//when
		when(i18nUtil.internationalize(NEGOCIATION_CANCELLED_AUTOMATICALLY_KEY, LANG)).thenReturn(EXPECTED_CANCELLED_AUTOMATICALLY_HINT);
		when(i18nUtil.internationalize(NEGOCIATION_CANCELLED_KEY, LANG)).thenReturn(EXPECTED_NEGOCIATION_CANCELLED_MSG);
		SmallChat smallChat = chatHelper.createaSmallChat(chat, LANG);
		
		//then
		assertEquals(smallChat.getAction(), ChatActionType.CANCELLED.toString());
		assertEquals(smallChat.getHint(), EXPECTED_CANCELLED_AUTOMATICALLY_HINT);
		assertEquals(smallChat.getComment(), EXPECTED_NEGOCIATION_CANCELLED_MSG);
	}
	
	@Test
	public void buildCancelledWithReasonSmallChatTest() {
		//given
		Chat chat = createBasicChat();
		chat.setAction(ChatActionType.CANCELLED);
		chat.setComment("Reason to cancel");
		
		//when
		when(i18nUtil.internationalize(NEGOCIATION_CANCELLED_KEY, LANG)).thenReturn(EXPECTED_NEGOCIATION_CANCELLED_MSG);
		SmallChat smallChat = chatHelper.createaSmallChat(chat, LANG);
		
		//then
		assertEquals(smallChat.getAction(), ChatActionType.CANCELLED.toString());
		assertEquals(smallChat.getHint(), chat.getComment());
		assertEquals(smallChat.getComment(), EXPECTED_NEGOCIATION_CANCELLED_MSG);
	}
	
	@Test
	public void buildCancelledWithNoReasonSmallChatTest() {
		//given
		Chat chat = createBasicChat();
		chat.setAction(ChatActionType.CANCELLED);
		
		//when
		when(i18nUtil.internationalize(NEGOCIATION_CANCELLED_KEY, LANG)).thenReturn(EXPECTED_NEGOCIATION_CANCELLED_MSG);
		when(i18nUtil.internationalize(NEGOCIATION_CANCELLED_NO_REASON_KEY, LANG)).thenReturn(EXPECTED_NEGOCIATION_CANCELLED_NO_REASON_HINT);
		SmallChat smallChat = chatHelper.createaSmallChat(chat, LANG);
		
		//then
		assertEquals(smallChat.getAction(), ChatActionType.CANCELLED.toString());
		assertEquals(smallChat.getHint(), EXPECTED_NEGOCIATION_CANCELLED_NO_REASON_HINT);
		assertEquals(smallChat.getComment(), EXPECTED_NEGOCIATION_CANCELLED_MSG);
	}
	
	
	@Test
	public void buildMarkedAsSoldSmallChatTest() {
		//given
		Chat chat = createBasicChat();
		chat.setAction(ChatActionType.MARKED_AS_SOLD);
		
		//when
		when(i18nUtil.internationalize(MARKED_AS_SOLD_MESSAGE, LANG)).thenReturn(EXPECTED_MARKED_AS_SOLD_MSG);
		SmallChat smallChat = chatHelper.createaSmallChat(chat, LANG);
		
		//then
		assertEquals(smallChat.getAction(), ChatActionType.MARKED_AS_SOLD.toString());
		assertEquals(smallChat.getComment(), EXPECTED_MARKED_AS_SOLD_MSG.replace("%s", item.getName()));
	}
	
	@Test
	public void buildOfferSmallChatTest() {
		//given
		Chat chat = createBasicChat();
		chat.setAction(ChatActionType.OFFER);
		
		//when
		when(i18nUtil.internationalize(OFFER_MESSAGE, LANG)).thenReturn(EXPECTED_OFFER_MSG);
		SmallChat smallChat = chatHelper.createaSmallChat(chat, LANG);
		
		//then
		assertEquals(smallChat.getAction(), ChatActionType.OFFER.toString());
		assertEquals(smallChat.getComment(), EXPECTED_OFFER_MSG);
	}

	@Test
	public void givenAllValidEntriesWhenSendingChatThenSaveAndFlushChat() {
		//given
		when(textUtils.createItemSEOURL(item, LANG)).thenReturn(URL);
		Chat chat = createBasicChat();

		//when
		chatHelper.sendChat(chat, conversation, item, role, LANG);

		//then
		verify(chatDao).saveAndFlush(chat);
	}

	@Test
	public void givenAllValidEntriesWhenSendingChatThenCreateValidEvent() {
		//given
		when(textUtils.createItemSEOURL(item, LANG)).thenReturn(URL);
		Chat chat = createBasicChat();

		//when
		ChatSentEvent event = chatHelper.sendChat(chat, conversation, item, role, LANG);

		//then
		assertThat(event.getBasedUserId(), is(role.getMe().getId()));
		assertThat(event.getConversationId(), is(conversation.getId()));
		assertThat(event.getItemId(), is(item.getId()));
		assertThat(event.getItemName(), is(item.getName()));
		assertThat(event.getReceiverId(), is(role.getReceiver().getId()));
		assertThat(event.getSenderId(), is(role.getSender().getId()));
		assertThat(event.getSenderUsername(), is(role.getSender().getUsername()));
		assertThat(event.getUrl(), is(URL));
	}

	@Test
	public void givenAllValidEntriesWhenNotifyingOwnerDirectBuyChatThenAnnotationIsSpecified() throws NoSuchMethodException {
		Method method = ChatHelper.class.getMethod("notifyOwnerDirectBuy", Item.class, User.class, Conversation.class, String.class);
		assertThat(method.getAnnotations().length, is(1));
		Notifiable annotation = (Notifiable) method.getAnnotations()[0];
		assertThat(annotation.annotationType().getName(), is(Notifiable.class.getName()));
		assertThat(annotation.action()[0], is(NotificationAction.CONVERSATION_ACCEPTED));
	}

	private Chat createBasicChat() {
		Chat chat = new Chat();
		chat.setItem(item);
		chat.setConversation(conversation);
		chat.setSender(conversation.getSeller());
		chat.setReceiver(conversation.getBuyer());
		chat.setPostTime(DateTime.now().toDate());
		return chat;
	}
	
}
