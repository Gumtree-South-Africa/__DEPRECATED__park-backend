package com.ebay.park.service.rating.command;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ebay.park.db.dao.RatingDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Rating;
import com.ebay.park.db.entity.RatingStatus;
import com.ebay.park.db.entity.User;
import com.ebay.park.event.user.UsersEvent;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.rating.dto.RateUserRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class RateUserCmdImplTest {

	private static final String USER_TO_RATE = "123";
	private static final String ITEM_ID = "123";
	private static final String COMMENT = "comment";

	@InjectMocks
	private RateUserCmdImpl rateUserCmdImpl = new RateUserCmdImpl();

	@Mock
	private RatingDao ratingDao;

	@Mock
	private UserDao userDao;

	@Mock
	private SessionService sessionService;

	@Mock
	private RateUserRequest request;

	@Mock
	private UserSessionCache userSession;

	@Mock
	private User user;

	@Mock
	private User rater;

	@Mock
	private Rating rating;

	@Before
	public void setUp() {
		initMocks(this);
		when(sessionService.getUserSession(request.getToken())).thenReturn(userSession);
		when(request.getUserToRate()).thenReturn(USER_TO_RATE);
	}

	@Test
	public void givenUserAndRaterThenRateUserSuccess() {
		when(rating.getUser()).thenReturn(user);
		when(rating.getRater()).thenReturn(rater);
		when(request.getItemId()).thenReturn(ITEM_ID);
		when(userDao.findById(Long.parseLong(request.getUserToRate()))).thenReturn(user);
		when(userDao.findById(userSession.getUserId())).thenReturn(rater);
		when(ratingDao.findByUserIdAndRaterIdAndItemId(user.getId(), rater.getId(),
				Long.parseLong(request.getItemId()))).thenReturn(rating);
		UsersEvent response = rateUserCmdImpl.execute(request);
		assertNotNull(response);
		assertEquals(rating.getUser(), response.getBasedUser());
		assertEquals(rating.getRater(), response.getUserActionGenerator());

	}

	@Test
	public void givenNullUserAndRaterThenException() {
		when(userDao.findById(Long.parseLong(request.getUserToRate()))).thenReturn(null);
		try {
			rateUserCmdImpl.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_NOT_FOUND.getCode());
		}

	}

	@Test
	public void givenUserAndNullRaterThenException() {
		when(userDao.findById(Long.parseLong(request.getUserToRate()))).thenReturn(user);
		when(userDao.findById(userSession.getUserId())).thenReturn(null);
		try {
			rateUserCmdImpl.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_NOT_FOUND.getCode());
		}

	}

	@Test
	public void givenUserAndRaterWithRatingStatusPositiveThenException() {
		when(request.getItemId()).thenReturn(ITEM_ID);
		when(userDao.findById(Long.parseLong(request.getUserToRate()))).thenReturn(user);
		when(userDao.findById(userSession.getUserId())).thenReturn(rater);
		when(ratingDao.findByUserIdAndRaterIdAndItemId(user.getId(), rater.getId(),
				Long.parseLong(request.getItemId()))).thenReturn(rating);
		when(rating.getStatus()).thenReturn(RatingStatus.POSITIVE);
		try {
			rateUserCmdImpl.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_ALREADY_RATED_ERROR.getCode());
		}

	}
	
	@Test
	public void givenUserAndRaterWithRatingCommentsThenException() {
		when(request.getItemId()).thenReturn(ITEM_ID);
		when(userDao.findById(Long.parseLong(request.getUserToRate()))).thenReturn(user);
		when(userDao.findById(userSession.getUserId())).thenReturn(rater);
		when(ratingDao.findByUserIdAndRaterIdAndItemId(user.getId(), rater.getId(),
				Long.parseLong(request.getItemId()))).thenReturn(rating);
		when(rating.getComment()).thenReturn(COMMENT);
		try {
			rateUserCmdImpl.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.USER_ALREADY_RATED_ERROR.getCode());
		}

	}
}
