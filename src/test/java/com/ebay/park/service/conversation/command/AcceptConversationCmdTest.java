package com.ebay.park.service.conversation.command;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.util.TextUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import com.ebay.park.db.dao.ConversationDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Chat;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.conversation.dto.AcceptConversationRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;

/**
 * 
 * @author marcos.lambolay
 * 
 */
public class AcceptConversationCmdTest {
	private static final String URL = "url";
	private static final String LANG = "es";
	@Spy
	@InjectMocks
	private final AcceptConversationCmd cmd = new AcceptConversationCmd();

	@Mock
	private ConversationDao conversationDao;

	@Mock
	private SessionService sessionService;

	@Mock
	private AcceptConversationRequest request;

	@Mock
	private UserSessionCache session;

	@Mock
	private Conversation conversation;

	@Mock
	private RoleFactory roleFactory;

	@Mock
	private Role role;

	@Mock
	private ChatHelper chatHelper;

	@Mock
	private UserDao userDao;

	@Mock
	private TextUtils textUtils;
	
	private final String token = "token";
	private final Long userId = 1l;
	private final String conversationId = "2";
	private final Long conversationToCloseId = 3l;
	private final Long itemOwnerId = 100l;

	@Before
	public void setUp(){
		initMocks(this);
		when(request.getToken()).thenReturn(token);
		when(sessionService.getUserSession(token)).thenReturn(session);
		when(request.getConversationId()).thenReturn(conversationId);
		when(conversationDao.findOne(Long.parseLong(conversationId))).thenReturn(conversation);
	}
	
	@Test
	public void givenAllValidEntriesWhenExecutingThenAcceptingConversation() {
		when(session.getUserId()).thenReturn(userId);
		when(roleFactory.createRole(conversation, userId)).thenReturn(role);
		Item item = mock(Item.class);
		when(conversation.getItem()).thenReturn(item);
		List<Conversation> conversationsToClose = new ArrayList<Conversation>();
		Conversation conversationToClose = mock(Conversation.class);
		conversationsToClose.add(conversationToClose);
		when(conversationToClose.getId()).thenReturn(conversationToCloseId);
		when(conversation.getId()).thenReturn(Long.parseLong(conversationId));
		when(item.getOpenConversations()).thenReturn(conversationsToClose);
		User userOwner = mock(User.class);
		when(userOwner.getId()).thenReturn(itemOwnerId);
		when(item.getPublishedBy()).thenReturn(userOwner);
		Chat chatToAccept = mock(Chat.class);
		when(conversationToClose.buildAcceptedMilestone(userId)).thenReturn(
				chatToAccept);
		Chat chatToCancel = mock(Chat.class);
		when(conversationToClose.buildCancelMilestone(any(Long.class), any(String.class))).thenReturn(
				chatToCancel);
		when(userDao.getOne(userId)).thenReturn(mock(User.class));
		when(textUtils.createItemSEOURL(item, LANG)).thenReturn(URL);

		ServiceResponse response = cmd.execute(request);

		assertEquals(ServiceResponse.SUCCESS, response);
		verify(conversationDao).save(conversation);
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenNullRequestWhenExecutingThenException() {
		cmd.execute(null);
	}

	@Test
	public void givenNonExistingItemWhenExecutingThenException() {
		try {
			cmd.execute(request);
		} catch (ServiceException e) {
			assertThat("ITEM_NOT_FOUND exception is expected", e.getCode(), is(ServiceExceptionCode.ITEM_NOT_FOUND.getCode()));
		}
	}
}
