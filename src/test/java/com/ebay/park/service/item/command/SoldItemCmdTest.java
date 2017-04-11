package com.ebay.park.service.item.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import com.ebay.park.db.entity.Idiom;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.conversation.command.ChatHelper;
import com.ebay.park.service.item.dto.UserItemRequest;
import com.ebay.park.service.session.TestServiceUtil;
import com.ebay.park.util.UserUtils;

/**
 * Unit test for {@link SoldItemCmd}.
 */
public class SoldItemCmdTest {

	private static final String TOKEN = "validToken";
	private static final String USERNAME = "timMartins";
	private static final String FAIL_MSG = "An exception was expected";
	private static final String CODE = "code";
	private static final long ID = 888l;
	private static final String EMAIL = "UserFollowed@mail.com";
	private User user;

	@Spy
	@InjectMocks
	private final SoldItemCmd cmd = new SoldItemCmd();

	@Mock
	private ItemDao itemDao;

	@Mock
	private UserUtils userUtils;

	@Mock
	private ChatHelper chatHelper;

	@Before
	public void setUp() {
		initMocks(this);
		user = TestServiceUtil.createUserMock(ID, USERNAME, EMAIL, null, null, null, null);
		ReflectionTestUtils.setField(cmd, "itemDao", itemDao);
		Idiom idiomMock = mock(Idiom.class);
		when(user.getIdiom()).thenReturn(idiomMock);
		when(idiomMock.getCode()).thenReturn(CODE);
	}

	@Test
	public void testExecuteSuccess() {
		// given
		UserItemRequest request = Mockito.mock(UserItemRequest.class);
		Item item = Mockito.mock(Item.class);
		when(item.getStatus()).thenReturn(StatusDescription.ACTIVE);
		when(item.getPublishedBy()).thenReturn(user);
		when(request.getToken()).thenReturn(TOKEN);
		when(userUtils.getItemUser(request)).thenReturn(item);
		when(itemDao.save(item)).thenReturn(item);
		// when
		ServiceResponse result = cmd.execute(request);
		// then
		assertNotNull(result);
		verify(itemDao).save(item);
	}

	@Test
	public void testExecuteNotActiveFail() {
		// given
		UserItemRequest request = Mockito.mock(UserItemRequest.class);
		Item item = Mockito.mock(Item.class);
		when(item.getStatus()).thenReturn(StatusDescription.EXPIRED);
		when(request.getToken()).thenReturn(TOKEN);
		when(userUtils.getItemUser(request)).thenReturn(item);
		try {
			cmd.execute(request);
			fail(FAIL_MSG);
		} catch (ServiceException se) {
			assertEquals(ServiceExceptionCode.ITEM_NOT_ACTIVE_ERROR.getCode(), se.getCode());
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenNullRequestWhenExecutingThenException() {
		cmd.execute(null);
	}

	//TODO add more test
}
