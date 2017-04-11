package com.ebay.park.service.rating.command;

import com.ebay.park.db.dao.RatingDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Rating;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.rating.dto.RatingRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author gabriel.sideri
 */
public class DeletePendingRatingCmdTest {
	private static final String USR_TOKEN = "usrTok";
	private static final String RATER_NAME = "raterName";
	private static final Long RATER_ID = 11L;
	private static final Long PENDING_RATING_ID=1L;

	@InjectMocks
	private DeletePendingRatingCmd cmd = new DeletePendingRatingCmd();
	
	@Mock
	private RatingDao ratingDao;

	@Mock
	private UserDao userDao;

	private RatingRequest request = new RatingRequest(USR_TOKEN, PENDING_RATING_ID);
	
	private User rater = new User();
	
	private Rating rating = new Rating();
	
	@Before
	public void setUp(){
		initMocks(this);
		rater.setUsername(RATER_NAME);
		rater.setId(RATER_ID);	
		rating.setRateId(PENDING_RATING_ID);
	}
	
	@Test
	public void invalidUser(){
		when(userDao.findByToken(USR_TOKEN)).thenReturn(null);
		ServiceResponse response = null;
		try {
			response = cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.USER_NOT_FOUND.getCode(), e.getCode());
		}
		assertNull(response);
	}
	
	@Test
	public void invalidRaiting(){
		when(userDao.findByToken(USR_TOKEN)).thenReturn(rater);
		when(ratingDao.findPendingRatingByIdAndRaterId(PENDING_RATING_ID, rater.getId())).thenReturn(null);
		ServiceResponse response = null;
		try {
			response = cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.INVALID_PENDING_RATING.getCode(), e.getCode());
		}
		assertNull(response);
	}
	
	@Test
	public void deleteSuccess(){
		when(userDao.findByToken(USR_TOKEN)).thenReturn(rater);
		when(ratingDao.findPendingRatingByIdAndRaterId(PENDING_RATING_ID, rater.getId())).thenReturn(rating);
		ServiceResponse response = cmd.execute(request);
		assertEquals(ServiceResponse.SUCCESS, response);
		verify(userDao).findByToken(USR_TOKEN);
		verify(ratingDao).findPendingRatingByIdAndRaterId(PENDING_RATING_ID, RATER_ID);
		verify(ratingDao).delete(rating);
	}
	
}
