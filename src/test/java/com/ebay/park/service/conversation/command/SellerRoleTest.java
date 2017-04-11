package com.ebay.park.service.conversation.command;

import com.ebay.park.db.entity.BargainStatus;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.event.conversation.ConversationAcceptedEvent;
import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.aop.Notifiable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class SellerRoleTest {

	private static final String INVERSE_ROLE = "inverse";
	private static final long INVERSE_ROLE_ID = 4L;
	private static final Long SELLER_ID = 1L;
	private static final String SELLER_NAME = "sellerName";
	private static final Long BUYER_ID = 1L;
	private static final String BUYER_NAME = "buyerName";
	private static final long CONVERSATION_ID = 9L;
	private static final long ITEM_ID = 7L;
	private static final String ITEM_NAME = "item";
	private static final String LANGUAGE = "language";
	private static final String USERNAME = "username";

	@Mock
	private Item item;

	@Mock
	private User user;

	Conversation conversation;
	SellerRole sellerRole = new SellerRole();
	
	@Before
	public void setUp(){
		initMocks(this);
		User seller = new User();
		seller.setId(SELLER_ID);
		seller.setUsername(SELLER_NAME);
		conversation = new Conversation();
		conversation.setSeller(seller);
		conversation.setSellerBargainStatus(BargainStatus.ACCEPTED);
		sellerRole.setConversation(conversation);
		when(item.getPublishedBy()).thenReturn(user);
		when(item.getName()).thenReturn(ITEM_NAME);
		when(item.getId()).thenReturn(ITEM_ID);
		when(user.getUsername()).thenReturn(USERNAME);
	}
	
	@Test
	public void testIsBargainAcceptedOk(){
		assertTrue(sellerRole.isBargainAccepted());
	}
	
	@Test
	public void testBargainNotAccepted(){
		conversation.setSellerBargainStatus(BargainStatus.REJECTED);
		assertFalse(sellerRole.isBargainAccepted());
	}
	
	@Test
	public void testGetId(){
		assertEquals(sellerRole.getId(), SELLER_ID);
	}
	
	@Test
	public void testGetUsername(){
		assertEquals(sellerRole.getUsername(), SELLER_NAME);
	}
	
	@Test
	public void setConversationCurrentPrice(){
		Double priceProposedBySeller = 34D;
		sellerRole.setConversationCurrentPrice(priceProposedBySeller);
		assertEquals(sellerRole.getConversation().getCurrentPriceProposedBySeller(), priceProposedBySeller);
	}

	@Test
	public void whenAcceptingConversationThenAnnotationIsSpecified() throws NoSuchMethodException {
		Method method = sellerRole.getClass().getMethod("acceptConversation", String.class);

		assertThat(method.getAnnotations().length, is(1));
		Notifiable annotation = (Notifiable) method.getAnnotations()[0];
		assertThat(annotation.annotationType().getName(), is(Notifiable.class.getName()));
		assertThat(annotation.action()[0], is(NotificationAction.CONVERSATION_ACCEPTED));
	}

	@Test
	public void whenAcceptingConversationThenAcceptBargain() {
		SellerRole role = mock(SellerRole.class, CALLS_REAL_METHODS);
		Conversation conversation = mock(Conversation.class);
		role.setConversation(conversation);
		when(conversation.getSeller()).thenReturn(user);
		when(conversation.getItem()).thenReturn(item);
		Role inverse_role = mock(Role.class);
		when(role.getInverseRole()).thenReturn(inverse_role);
		when(role.getId()).thenReturn(INVERSE_ROLE_ID);
		when(inverse_role.getUsername()).thenReturn(INVERSE_ROLE);

		role.acceptConversation(LANGUAGE);

		verify(role).acceptBargain();
	}

	@Test
	public void whenAcceptingConversationThenReturnValidItemInEvent() {
		BuyerRole role = mock(BuyerRole.class, CALLS_REAL_METHODS);
		Conversation conversation = mock(Conversation.class);
		role.setConversation(conversation);
		when(conversation.getBuyer()).thenReturn(user);
		when(conversation.getItem()).thenReturn(item);
		Role inverse_role = mock(Role.class);
		when(role.getInverseRole()).thenReturn(inverse_role);
		when(inverse_role.getId()).thenReturn(INVERSE_ROLE_ID);
		when(inverse_role.getUsername()).thenReturn(INVERSE_ROLE);

		ConversationAcceptedEvent event = role.acceptConversation(LANGUAGE);

		assertThat(event.getItemId(), is(ITEM_ID));
		assertThat(event.getItemName(), is(ITEM_NAME));
	}

	@Test
	public void whenAcceptingConversationThenReturnValidUserToNotifyInEvent() {
		BuyerRole role = mock(BuyerRole.class, CALLS_REAL_METHODS);
		Conversation conversation = mock(Conversation.class);
		role.setConversation(conversation);
		when(conversation.getBuyer()).thenReturn(user);
		when(conversation.getItem()).thenReturn(item);
		when(conversation.getId()).thenReturn(CONVERSATION_ID);
		Role inverse_role = mock(Role.class);
		when(role.getInverseRole()).thenReturn(inverse_role);
		when(inverse_role.getUsername()).thenReturn(INVERSE_ROLE);
		when(inverse_role.getId()).thenReturn(INVERSE_ROLE_ID);

		ConversationAcceptedEvent event = role.acceptConversation(LANGUAGE);

		assertThat(event.getUserIdToNotify(), is(INVERSE_ROLE_ID));
	}

	@Test
	public void whenAcceptingConversationThenReturnValidConversationInEvent() {
		BuyerRole role = mock(BuyerRole.class, CALLS_REAL_METHODS);
		Conversation conversation = mock(Conversation.class);
		role.setConversation(conversation);
		when(conversation.getBuyer()).thenReturn(user);
		when(conversation.getItem()).thenReturn(item);
		when(conversation.getId()).thenReturn(CONVERSATION_ID);
		Role inverse_role = mock(Role.class);
		when(role.getInverseRole()).thenReturn(inverse_role);
		when(inverse_role.getId()).thenReturn(INVERSE_ROLE_ID);
		when(inverse_role.getUsername()).thenReturn(INVERSE_ROLE);

		ConversationAcceptedEvent event = role.acceptConversation(LANGUAGE);

		assertThat(event.getConversationId(), is(CONVERSATION_ID));
	}

	@Test
	public void whenAcceptingConversationThenReturnValidUserThatAcceptedInEvent() {
		BuyerRole role = mock(BuyerRole.class, CALLS_REAL_METHODS);
		Conversation conversation = mock(Conversation.class);
		role.setConversation(conversation);
		when(user.getId()).thenReturn(BUYER_ID);
		when(user.getUsername()).thenReturn(BUYER_NAME);
		when(conversation.getBuyer()).thenReturn(user);
		when(conversation.getItem()).thenReturn(item);
		when(conversation.getId()).thenReturn(CONVERSATION_ID);
		Role inverse_role = mock(Role.class);
		when(role.getInverseRole()).thenReturn(inverse_role);
		when(inverse_role.getId()).thenReturn(INVERSE_ROLE_ID);
		when(inverse_role.getUsername()).thenReturn(INVERSE_ROLE);

		ConversationAcceptedEvent event = role.acceptConversation(LANGUAGE);

		assertThat(event.getUserIdThatAccepted(), is(BUYER_ID));
		assertThat(event.getUsernameThatAccepted(), is(BUYER_NAME));
	}
}
