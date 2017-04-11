package com.ebay.park.service.rating.command;

import com.ebay.park.db.dao.RatingDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.Rating;
import com.ebay.park.db.entity.RatingStatus;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.rating.dto.ListPendingRatingsRequest;
import com.ebay.park.service.rating.dto.ListPendingRatingsResponse;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.util.InternationalizationUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ListPendingRatingsCmdTest {

	private static final String USR_TOKEN = "usrTok";

	private static final Long USR_ID = 1L;

	private static final String BUYER_NAME = "buyrName";

	private static final Long BUYER_ID = 2L;

	private static final String SELLER_NAME = "sellerName";

	private static final Long SELLER_ID = 4L;

	@InjectMocks
	private ListPendingRatingsCmd cmd = new ListPendingRatingsCmd();
	
	@Mock
	private RatingDao ratingDao;
	
	@Mock
	private UserDao userDao;

	@Mock
	private SessionService sessionService;
	
	@Mock
	private InternationalizationUtil i18nUtil;
	
	private int defaultPageSize = 20;
	
	ListPendingRatingsRequest request = new ListPendingRatingsRequest(0, defaultPageSize, USR_TOKEN, "en");
	
	@Before
	public void setUp(){
		initMocks(this);
		
		User user = Mockito.mock(User.class);
		when(userDao.findByToken(request.getToken())).thenReturn(user);
		when(user.getUserId()).thenReturn(USR_ID);
		
		PageRequest pageReq = new PageRequest(0, defaultPageSize);
		
		User buyer = new User();
		buyer.setUsername(BUYER_NAME);
		buyer.setId(BUYER_ID);
		
		User seller = new User();
		seller.setUsername(SELLER_NAME);
		seller.setId(SELLER_ID);
		
		Item item = new Item("itName", 222D, "v1.0", false, false);
		
		Rating rating1 = new Rating();
		Rating rating2 = new Rating();
		Rating rating3 = new Rating();
		Rating rating4 = new Rating();
		
		rating1.setComment("rating 1 comentario");
		rating1.setItem(item);
		rating1.setRater(buyer);
		rating1.setUser(buyer);
		rating1.setRateId(1L);
		rating1.setStatus(RatingStatus.POSITIVE);
		rating1.setRateDate(new Date());
		
		rating2.setComment("rating 2 comentario");
		rating2.setItem(item);
		rating2.setRater(buyer);
		rating2.setUser(buyer);
		rating2.setRateId(2L);
		rating2.setStatus(RatingStatus.POSITIVE);
		rating2.setRateDate(new Date());
		
		rating3.setComment("rating 3 comentario");
		rating3.setItem(item);
		rating3.setRater(buyer);
		rating3.setUser(buyer);
		rating3.setRateId(3L);
		rating3.setStatus(RatingStatus.POSITIVE);
		rating3.setRateDate(new Date());
		
		rating4.setComment("rating 4 comentario");
		rating4.setItem(item);
		rating4.setRater(buyer);
		rating4.setUser(buyer);
		rating4.setRateId(4L);
		rating4.setStatus(RatingStatus.POSITIVE);
		rating4.setRateDate(new Date());
		
		List<Rating> ratings = new ArrayList<Rating>();
		ratings.add(rating3);
		ratings.add(rating2);
		ratings.add(rating1);
		ratings.add(rating4);
		
		when(ratingDao.findPendingRatingsForRater(USR_ID, pageReq)).thenReturn(ratings);
		when(ratingDao.findPendingRatingsForRaterQty(USR_ID)).thenReturn(2L);
	}
	
	@Test
	public void searchPendingRates(){
		ListPendingRatingsResponse response = cmd.execute(request);
		assertEquals(2, response.getTotalElements());
	}
}
