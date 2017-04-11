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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.Idiom;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.group.dto.SearchGroupResponse;
import com.ebay.park.util.InternationalizationUtil;
import com.ebay.park.util.TextUtils;

public class RecommendedGroupsCmdTest {

	private static final String TOKEN = "token";
	private static final String GROUP_NAME = "name";
	private static final String DESCRIPTION = "description";
	private static final String LANGUAGE = "en";
	private static final int LIST_RECOMMENDED_GROUPS_MAX = 15;
	private static final int NUMBER_OF_GROUPS = 1;
	private static final int FIRST = 0;
	private static final int PAGE = 0;

	private PageRequest pageRequest;

	@InjectMocks
	private RecommendedGroupsCmd recommendedGroupsCmd = new RecommendedGroupsCmd();

	@Mock
	private UserDao userDao;

	@Mock
	private InternationalizationUtil i18nUtil;

	@Mock
	private GroupDao groupDao;

	@Mock
	private TextUtils textUtils;

	@Mock
	private ParkRequest request;

	@Mock
	private User user;

	@Mock
	Idiom idiom;

	@Before
	public void setUp() {
		initMocks(this);
		ReflectionTestUtils.setField(recommendedGroupsCmd, "LIST_RECOMMENDED_GROUPS_MAX", LIST_RECOMMENDED_GROUPS_MAX);
		pageRequest = new PageRequest(PAGE, LIST_RECOMMENDED_GROUPS_MAX);
	}

	@Test
	public void givenRequestWithTokenThenGetRecommendedGroups() {
		Group group = new Group(GROUP_NAME, user, DESCRIPTION);
		List<Group> groups = new ArrayList<Group>();
		groups.add(group);
		when(request.getToken()).thenReturn(TOKEN);
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.getRecommendedGroup(pageRequest)).thenReturn(groups);
		when(request.getLanguage()).thenReturn(LANGUAGE);
		when(user.getIdiom()).thenReturn(idiom);
		when(user.getIdiom().getCode()).thenReturn(LANGUAGE);
		SearchGroupResponse response = recommendedGroupsCmd.execute(request);
		assertNotNull(response);
		assertEquals(response.getGroups().size(), NUMBER_OF_GROUPS);
		assertEquals(response.getGroups().get(FIRST).getName(), GROUP_NAME);
	}

	@Test
	public void givenRequestWithInvalidTokenThenException() {
		when(request.getToken()).thenReturn(TOKEN);
		when(userDao.findByToken(request.getToken())).thenReturn(null);
		try {
			recommendedGroupsCmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_UNAUTHORIZED.getCode());
		}
	}

	@Test
	public void givenRequestWithNullTokenThenGetRecommendedGroups() {
		Group group = new Group(GROUP_NAME, user, DESCRIPTION);
		List<Group> groups = new ArrayList<Group>();
		groups.add(group);
		when(request.getToken()).thenReturn(null);
		when(groupDao.getRecommendedGroup(pageRequest)).thenReturn(groups);
		when(request.getLanguage()).thenReturn(LANGUAGE);
		when(user.getIdiom()).thenReturn(idiom);
		when(user.getIdiom().getCode()).thenReturn(LANGUAGE);
		SearchGroupResponse response = recommendedGroupsCmd.execute(request);
		assertNotNull(response);
		assertEquals(response.getGroups().size(), NUMBER_OF_GROUPS);
		assertEquals(response.getGroups().get(FIRST).getName(), GROUP_NAME);
	}

	@Test
	public void givenRequestWithTokenAndNullLanguageThenGetRecommendedGroups() {
		Group group = new Group(GROUP_NAME, user, DESCRIPTION);
		List<Group> groups = new ArrayList<Group>();
		groups.add(group);
		when(request.getToken()).thenReturn(TOKEN);
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(groupDao.getRecommendedGroup(pageRequest)).thenReturn(groups);
		when(request.getLanguage()).thenReturn(null);
		when(user.getIdiom()).thenReturn(idiom);
		when(user.getIdiom().getCode()).thenReturn(LANGUAGE);
		SearchGroupResponse response = recommendedGroupsCmd.execute(request);
		assertNotNull(response);
		assertEquals(response.getGroups().size(), NUMBER_OF_GROUPS);
		assertEquals(response.getGroups().get(FIRST).getName(), GROUP_NAME);
	}
}
