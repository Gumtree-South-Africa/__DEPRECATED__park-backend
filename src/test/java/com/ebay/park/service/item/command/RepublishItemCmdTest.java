package com.ebay.park.service.item.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.ItemGroupDao;
import com.ebay.park.db.entity.Chat;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.ItemGroup;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.event.user.UserItemToFollowersEvent;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.dto.RepublishItemRequest;
import com.ebay.park.service.item.dto.RepublishItemResponse;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;

/**
 * @author gabriel.sideri.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(CreateItemCmd.class)
public class RepublishItemCmdTest {

	@InjectMocks
	private final RepublishItemCmd cmd = new RepublishItemCmd();

	private static final String USERNAME = "my-username";
	private static final Long ITEM_ID = 1l;
	private static final Long USER_ID = 100l;

	@Mock
	private ItemDao itemDao;

	@Mock
	private RepublishItemRequest request;

	@Mock
	private SessionService sessionService;
	
	@Mock
	private ItemGroupDao itemGroupDao;

	@Mock
	private User user;

	@Mock
	private Item item;

	@Mock
	private RepublishItemResponse response;

	@Spy
	private Conversation conversation = new Conversation();

	@Before
	public void setUp() {
		when(request.getItemId()).thenReturn(1l);
		when(user.getUserId()).thenReturn(USER_ID);
		when(user.getUsername()).thenReturn(USERNAME);

		UserSessionCache userSession = mock(UserSessionCache.class);
		when(userSession.getUserId()).thenReturn(100l);
		when(sessionService.getUserSession(any(String.class))).thenReturn(userSession);

		when(user.getUserId()).thenReturn(100l);
		when(item.getPublishedBy()).thenReturn(user);
		when(itemDao.findOne(any(Long.class))).thenReturn(item);
		when(item.getId()).thenReturn(ITEM_ID);
	}

	@Test
	public void testExecuteShouldSucceedSoldStatus() {

		Mockito.doCallRealMethod().when(item).republish();
		Mockito.doCallRealMethod().when(conversation).buildCancelMilestone(0l, null);

		when(item.getStatus()).thenReturn(StatusDescription.SOLD);
		when(itemDao.save(item)).thenReturn(item);

		List<Conversation> conversations = Arrays.asList(conversation);
		when(item.getAcceptedConversations()).thenReturn(conversations);
		when(conversation.getSeller()).thenReturn(user);

		List<Chat> chats = new ArrayList<Chat>();
		conversation.setChats(chats);

		UserItemToFollowersEvent response = cmd.execute(request);

		Mockito.verify(item).republish();
		Mockito.verify(itemDao).save(item);

		assertNotNull(response);

	}
	
	@Test
	public void testExecuteShouldSucceedSoldStatusItemGroups() {

		Mockito.doCallRealMethod().when(item).republish();
		Mockito.doCallRealMethod().when(conversation).buildCancelMilestone(0l, null);
		Item item_group = Mockito.mock(Item.class);
		Group group = Mockito.mock(Group.class);
		ItemGroup itemGroup = new ItemGroup(item_group, group, new Date());
		List<ItemGroup> itemGroups = new ArrayList<>();
		itemGroups.add(itemGroup);
		when(item.getStatus()).thenReturn(StatusDescription.SOLD);
		
		when(item.getItemGroups()).thenReturn(itemGroups);
		when(itemDao.save(item)).thenReturn(item);
		when(itemGroupDao.save(itemGroup)).thenReturn(itemGroup);

		List<Conversation> conversations = Arrays.asList(conversation);
		when(item.getAcceptedConversations()).thenReturn(conversations);
		when(conversation.getSeller()).thenReturn(user);

		List<Chat> chats = new ArrayList<Chat>();
		conversation.setChats(chats);

		UserItemToFollowersEvent response = cmd.execute(request);

		Mockito.verify(item).republish();
		Mockito.verify(itemDao).save(item);
		Mockito.verify(itemGroupDao).save(itemGroup);

		assertNotNull(response);

	}
	
	@Test
	public void testExecuteShouldSucceedExpiredStatus() {

		Mockito.doCallRealMethod().when(item).republish();
		Mockito.doCallRealMethod().when(item).cancelAcceptedConversations();
		Mockito.doCallRealMethod().when(conversation).buildCancelMilestone(0l, null);

		when(item.getStatus()).thenReturn(StatusDescription.EXPIRED);
		when(itemDao.save(item)).thenReturn(item);

		List<Conversation> conversations = Arrays.asList(conversation);
		when(item.getAcceptedConversations()).thenReturn(conversations);
		when(conversation.getSeller()).thenReturn(user);

		List<Chat> chats = new ArrayList<Chat>();
		conversation.setChats(chats);

		UserItemToFollowersEvent response = cmd.execute(request);

		Mockito.verify(item).republish();
		Mockito.verify(itemDao).save(item);

		assertNotNull(response);

	}

	@Test
	public void testInvalidStateItem() {
		when(item.getStatus()).thenReturn(StatusDescription.ACTIVE);
		UserItemToFollowersEvent response = null;
		try {
			response = cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.ITEM_NOT_SOLD_OR_EXPIRED_ERROR.getCode(), e.getCode());
		}
		assertNull(response);
	}
	
	//TODO add test to verify conversation status does not change after republishing
}
