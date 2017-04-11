package com.ebay.park.service.conversation.command;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.ebay.park.db.entity.Item;
import com.ebay.park.util.TextUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import com.ebay.park.db.dao.ConversationDao;
import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.RatingDao;
import com.ebay.park.db.entity.Chat;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.ConversationStatus;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.conversation.dto.RejectConversationRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;

public class RejectConversationCmdTest {

	private static final long USER_ID = 1;
    private static final String LANGUAGE = "es";
	private static final String TOKEN = "TOKEN";
	private static final String CONVERSATION_ID = "2";
    private static final String URL = "url";
    private static final String EXPLANATION = "explanation";

    @Spy
	@InjectMocks
	private final RejectConversationCmd cmd = new RejectConversationCmd();

	@Mock
	private ConversationDao conversationDao;

	@Mock
	private SessionService sessionService;

	@Mock
	private ItemDao itemDao;

	@Mock
	private RatingDao ratingDao;

	@Mock
	private RejectConversationRequest request;

	@Mock
	private UserSessionCache session;

	@Mock
	private Conversation conversation;

	@Mock
	private Role role;

	@Mock
	private RoleFactory roleFactory;

	@Mock
	private ChatHelper chatHelper;

	@Mock
	private Chat chatToCancel;

    @Mock
    private TextUtils textUtils;

	@Before
	public void setUp(){
		initMocks(this);
		when(request.getToken()).thenReturn(TOKEN);
		when(request.getLanguage()).thenReturn(LANGUAGE);
        when(sessionService.getUserSession(TOKEN)).thenReturn(session);
		when(session.getLang()).thenReturn(LANGUAGE);
        when(request.getConversationId()).thenReturn(CONVERSATION_ID);
        when(conversationDao.findOne(Long.parseLong(CONVERSATION_ID))).thenReturn(conversation);
        when(session.getUserId()).thenReturn(USER_ID);
        when(conversation.buildCancelMilestone(any(Long.class),  any(String.class))).thenReturn(
                chatToCancel);
        when(roleFactory.createRole(conversation, USER_ID)).thenReturn(role);
	}
	
	@Test
	public void givenAllValidEntriesWhenExecutingThenChangeConversationStatus() {
		ServiceResponse response = cmd.execute(request);

		assertEquals(ServiceResponse.SUCCESS, response);
		verify(conversation).setStatus(ConversationStatus.CANCELLED);
		verify(conversationDao).save(conversation);
	}

    @Test
    public void givenAllValidEntriesWhenExecutingThenRejectBargain() {
        ServiceResponse response = cmd.execute(request);

        assertEquals(ServiceResponse.SUCCESS, response);
        verify(role).rejectBargain(anyString());
    }

    @Test
    public void givenAllValidEntriesWhenExecutingThenAddCancelMilestone() {
        when(request.getExplanation()).thenReturn(EXPLANATION);
        ServiceResponse response = cmd.execute(request);

        assertEquals(ServiceResponse.SUCCESS, response);
        verify(conversation).buildCancelMilestone(USER_ID, EXPLANATION);
        verify(chatHelper).persistChat(any(Chat.class));
    }

    @Test
    public void givenAllValidEntriesWhenExecutingThenValidItemURL() {
        Item item = mock(Item.class);
        when(conversation.getItem()).thenReturn(item);
        when(textUtils.createItemSEOURL(item, LANGUAGE)).thenReturn(URL);
        ServiceResponse response = cmd.execute(request);
        assertEquals(ServiceResponse.SUCCESS, response);

        verify(role).rejectBargain(URL);
    }

	@Test
    public void givenAcceptedConversationWhenRejectingThenCancelConversation() {
	    when(conversation.is(ConversationStatus.ACCEPTED)).thenReturn(true);
	    ServiceResponse response = cmd.execute(request);

	    assertEquals(ServiceResponse.SUCCESS, response);
        verify(conversation).setStatus(ConversationStatus.CANCELLED);
        verify(conversationDao).save(conversation);
	}
}