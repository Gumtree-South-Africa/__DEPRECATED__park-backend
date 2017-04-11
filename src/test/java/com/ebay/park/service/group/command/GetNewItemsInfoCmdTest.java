package com.ebay.park.service.group.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserFollowsGroupDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserFollowsGroup;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.group.dto.GetNewItemsInfoRequest;
import com.ebay.park.service.group.dto.GetNewItemsInfoResponse;

public class GetNewItemsInfoCmdTest {

	private static final String TOKEN = "token";
	private static final Long GROUP_ID = 1L;
	private static final Long ITEMS = 1L;

	@InjectMocks
	private GetNewItemsInfoCmd getNewItemsinfoCmd = new GetNewItemsInfoCmd();

	@Mock
	private UserFollowsGroupDao userFollowsGroupDao;

	@Mock
	private UserDao userDao;

	@Mock
	private GroupDao groupDao;

	@Mock
	private GetNewItemsInfoRequest request;

	@Mock
	private User user;

	@Mock
	private Group group;

	@Before
	public void setUp() {
		initMocks(this);
	}

	@Test
	public void givenNonExitingUserThenException() {
		when(request.getToken()).thenReturn(TOKEN);
		when(userDao.findByToken(request.getToken())).thenReturn(null);
		try {
			getNewItemsinfoCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_UNAUTHORIZED.getCode());
		}
	}

	@Test
	public void givenExitingUserThenGetNewItemsInfoSuccess() {
		List<UserFollowsGroup> ownedGroupList = new ArrayList<UserFollowsGroup>();
		List<UserFollowsGroup> subscribedGroupList = new ArrayList<UserFollowsGroup>();
		UserFollowsGroup userFollowsGroup = Mockito.mock(UserFollowsGroup.class);
		ownedGroupList.add(userFollowsGroup);
		subscribedGroupList.add(userFollowsGroup);
		when(userFollowsGroup.getGroup()).thenReturn(group);
		when(group.getId()).thenReturn(GROUP_ID);
		when(userFollowsGroupDao.findGroups(user.getUserId())).thenReturn(subscribedGroupList);
		when(request.getToken()).thenReturn(TOKEN);
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(userFollowsGroupDao.findOwnedGroups(user.getUserId())).thenReturn(ownedGroupList);
		when(groupDao.getNewItemsCount(userFollowsGroup.getLastAccess(), userFollowsGroup.getGroup().getGroupId(),
				StatusDescription.ACTIVE)).thenReturn(ITEMS);
		GetNewItemsInfoResponse response = getNewItemsinfoCmd.execute(request);
		assertNotNull(response);
		assertEquals(ITEMS.longValue(), response.getOwnedGroupsItems().longValue());
		assertEquals(ITEMS.longValue(), response.getSubscribedGroupItems().longValue());

	}

}
