package com.ebay.park.service.item.command;

import com.ebay.park.db.dao.ConversationDao;
import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.*;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.conversation.command.ChatHelper;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.BuyItemDirectlyResponse;
import com.ebay.park.service.item.dto.UserItemRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BuyItemDirectlyCmdTest {
	@Spy
	@InjectMocks
	private final BuyItemDirectlyCmd cmd = new BuyItemDirectlyCmd();

	@Mock
	private UserItemRequest request;

	@Mock
	private ItemDao itemDao;

	@Mock
	private UserDao userDao;

	@Mock
	private ConversationDao conversationDao;

	@Mock
	private SessionService sessionService;

	@Mock
	private User seller;

	@Mock
	private User buyer;
	
	@Mock
	private ChatHelper chatHelper;

	@Mock
    private ItemUtils itemUtils;

	@Before
	public void setUp() {
		initMocks(this);
		UserSessionCache userSession = mock(UserSessionCache.class);
		when(userSession.getUserId()).thenReturn(500l);
		when(sessionService.getUserSession(any(String.class))).thenReturn(userSession);
		when(seller.getUserId()).thenReturn(100l);
		when(buyer.getUserId()).thenReturn(500l);
		when(userDao.findOne(500l)).thenReturn(buyer);
	}

	@Test
	public void test() {
		when(request.getItemId()).thenReturn(1l);
		Item item = mock(Item.class);
		when(item.getPublishedBy()).thenReturn(seller);
		when(itemDao.findOne(1l)).thenReturn(item);
		when(item.getStatus()).thenReturn(StatusDescription.ACTIVE);
		when(itemDao.save(item)).thenReturn(item);
		Conversation acceptConversation = mock(Conversation.class);
		Chat chatToAccept = mock(Chat.class);
		when(acceptConversation.buildAcceptedMilestone(any(Long.class)))
				.thenReturn(chatToAccept);
		when(acceptConversation.getId()).thenReturn(1000l);
		when(conversationDao.saveAndFlush(any(Conversation.class))).thenReturn(acceptConversation);
		BuyItemDirectlyResponse response = cmd.execute(request);
		assertNotNull(response);
		assertEquals("1000", response.getConversationId());

	}

	@Test
	public void testInvalidItem() {
		when(itemDao.findOne(any(Long.class))).thenThrow(ServiceException.createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND));

		BuyItemDirectlyResponse response = null;
		try {
			response = cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.ITEM_NOT_FOUND.getCode(), e.getCode());
		}
		assertNull(response);
	}
}
