package com.ebay.park.service.social.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Idiom;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.session.TestServiceUtil;
import com.ebay.park.service.social.dto.DiscoverUsersRequest;
import com.ebay.park.service.social.dto.ListUsersFollowedResponse;
import com.ebay.park.util.InternationalizationUtil;

public class DiscoverUsersCmdTest {

	private static final String TOKEN = "validToken";
	private static final String USERNAME = "timMartins";
	private static final Double LATITUD = 50.0;
	private static final Double LONGITUDE = 50.0;
	private static final Double DEFAULTRADIUS = 40.0;
	private static final String FAIL_MSG = "An exception was expected";
	private User user;

	@InjectMocks
	private DiscoverUsersCmd discoverUsersCmd;

	@Mock
	private UserDao userDao;

	@Mock
	private UserSocialDao userSocialDao;

	@Mock
	private InternationalizationUtil i18nUtil;

	@Before
	public void setUp() {
		discoverUsersCmd = new DiscoverUsersCmd();
		initMocks(this);
		user = TestServiceUtil.createUserMock(888l, USERNAME, "UserFollowed@mail.com", null, null, null, null);
		ReflectionTestUtils.setField(discoverUsersCmd, "maxUsers", 10);
		ReflectionTestUtils.setField(discoverUsersCmd, "defaultRadius", 40.0);
	}

	@Test
	public void testExecuteUserNotNullSuccess() {
		// given
		DiscoverUsersRequest request = Mockito.mock(DiscoverUsersRequest.class);
		List<User> userList = new ArrayList<>();
		Idiom mockIdiom = Mockito.mock(Idiom.class);
		userList.add(user);
		Mockito.when(request.getToken()).thenReturn(TOKEN);
		Mockito.when(request.getLatitude()).thenReturn(LATITUD);
		Mockito.when(request.getLongitude()).thenReturn(LONGITUDE);
		Mockito.when(request.getUsername()).thenReturn(USERNAME);
		Mockito.when(user.getIdiom()).thenReturn(mockIdiom);
		Mockito.when(userDao.findByUsername(USERNAME)).thenReturn(user);
		Mockito.when(userSocialDao.getRecommendedUsers(user.getId(), StatusDescription.ACTIVE, request.getLatitude(),
				request.getLongitude(), DEFAULTRADIUS, new PageRequest(0, 10))).thenReturn(userList);
		// when
		ListUsersFollowedResponse response = discoverUsersCmd.execute(request);
		// then
		assertNotNull(response);
		assertEquals(1, response.getUsers().size());
		assertEquals(user.getId(), response.getUsers().get(0).getUserId());
		Mockito.verify(userDao, Mockito.times(1)).findByUsername(USERNAME);
		Mockito.verify(userSocialDao, Mockito.times(1)).getRecommendedUsers(user.getId(), StatusDescription.ACTIVE,
				request.getLatitude(), request.getLongitude(), DEFAULTRADIUS, new PageRequest(0, 10));

	}

	@Test
	public void testExecuteUserNullFail() {
		DiscoverUsersRequest request = Mockito.mock(DiscoverUsersRequest.class);
		Mockito.when(request.getToken()).thenReturn(TOKEN);
		try {
			discoverUsersCmd.execute(request);
			fail(FAIL_MSG);
		} catch (ServiceException se) {
			assertEquals(ServiceExceptionCode.USER_NOT_FOUND.getCode(), se.getCode());
		}
	}

	@Test
	public void testExecuteNullUser() {
		// given
		DiscoverUsersRequest request = Mockito.mock(DiscoverUsersRequest.class);
		List<User> userList = new ArrayList<>();
		userList.add(user);
		Mockito.when(request.getLatitude()).thenReturn(LATITUD);
		Mockito.when(request.getLongitude()).thenReturn(LONGITUDE);
		Mockito.when(userSocialDao.getPublicRecommendedUsers(StatusDescription.ACTIVE, request.getLatitude(),
				request.getLongitude(), DEFAULTRADIUS, new PageRequest(0, 10))).thenReturn(userList);
		// when
		ListUsersFollowedResponse response = discoverUsersCmd.execute(request);
		// then
		assertNotNull(response);
		assertEquals(1, response.getUsers().size());
		Mockito.verify(userSocialDao, Mockito.times(1)).getPublicRecommendedUsers(StatusDescription.ACTIVE,
				request.getLatitude(), request.getLongitude(), DEFAULTRADIUS, new PageRequest(0, 10));
	}
}
