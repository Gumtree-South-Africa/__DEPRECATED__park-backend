package com.ebay.park.service.social.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.Rating;
import com.ebay.park.db.entity.RatingStatus;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.session.TestServiceUtil;
import com.ebay.park.service.social.dto.SmallRating;
import com.ebay.park.service.social.dto.UserRatesRequest;

public class GetUserRatingsCmdTest {

	private static final String USERNAME = "timMartins";
	private static final String FAIL_MSG = "An exception was expected";
	private User user;
	private User user2;

	@InjectMocks
	private GetUserRatingsCmd getUserRatingsCmd;

	@Mock
	private UserDao userDao;

	@Before
	public void setUp() {
		getUserRatingsCmd = new GetUserRatingsCmd();
		initMocks(this);
		user = TestServiceUtil.createUserMock(888l, USERNAME, "UserFollowed@mail.com", null, null, null, null);
		user2 = TestServiceUtil.createUserMock(777l, USERNAME, "UserFollowed@mail.com", null, null, null, null);
	}

	@Test
	public void testExecuteSuccess() {
		// given
		UserRatesRequest request = Mockito.mock(UserRatesRequest.class);
		Mockito.when(request.getUsername()).thenReturn(USERNAME);
		Mockito.when(userDao.findByUsername(USERNAME)).thenReturn(user);

		Rating rating = new Rating();
		rating.setStatus(RatingStatus.POSITIVE);
		rating.setComment("comment");
		rating.setRateDate(new Date(1));
		rating.setRater(user2);
		Item item1 = new Item("item1", 50.0, "versionPublish", Boolean.FALSE, Boolean.FALSE);
		item1.setId(777l);
		rating.setItem(item1);

		Rating rating2 = new Rating();
		rating2.setStatus(RatingStatus.POSITIVE);
		rating2.setComment("comment2");
		rating2.setRateDate(new Date(2));
		rating2.setRater(user2);
		Item item2 = new Item("item2", 50.0, "versionPublish", Boolean.FALSE, Boolean.FALSE);
		item1.setId(777l);
		rating2.setItem(item2);

		List<Rating> listRatings = new ArrayList<>();
		listRatings.add(rating);
		listRatings.add(rating2);

		Mockito.when(user.getRatings()).thenReturn(listRatings);

		Mockito.when(request.getRateStatus()).thenReturn(null);
		// when
		List<SmallRating> result = getUserRatingsCmd.execute(request);
		// then
		assertNotNull(result);
		assertEquals(2, result.size());
		Mockito.verify(userDao, Mockito.times(1)).findByUsername(USERNAME);
	}

	@Test
	public void testExecuteFailNullUser() {
		UserRatesRequest request = Mockito.mock(UserRatesRequest.class);
		Mockito.when(request.getUsername()).thenReturn(USERNAME);
		Mockito.when(userDao.findByUsername(USERNAME)).thenReturn(null);
		try {
			getUserRatingsCmd.execute(request);
			fail(FAIL_MSG);
		} catch (ServiceException se) {
			assertEquals(ServiceExceptionCode.USER_NOT_FOUND.getCode(), se.getCode());
		}
	}
}
