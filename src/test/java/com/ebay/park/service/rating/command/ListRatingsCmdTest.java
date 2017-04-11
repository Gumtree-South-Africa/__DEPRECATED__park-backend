package com.ebay.park.service.rating.command;

import com.ebay.park.db.dao.RatingDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.Rating;
import com.ebay.park.db.entity.RatingStatus;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.conversation.command.Role;
import com.ebay.park.service.rating.dto.ListRatingsRequest;
import com.ebay.park.service.rating.dto.ListRatingsResponse;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.util.InternationalizationUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ListRatingsCmdTest {
	private static final String USR_TOKEN = "usrTok";
	private static final String BUYER_NAME = "buyerName";
	private static final String SELLER_NAME = "sellerName";

	private static final Long BUYER_ID = 11L;
	private static final Long SELLER_ID = 12L;
	private int defaultPageSize = 20;

	@InjectMocks
	private ListRatingsCmd cmd = new ListRatingsCmd();
	
	@Mock
	private RatingDao ratingDao;

	@Mock
	private UserDao userDao;

	@Mock
	private SessionService sessionService;

	@Mock
	private InternationalizationUtil i18nUtil;

	private ListRatingsRequest request = new ListRatingsRequest(0, defaultPageSize, USR_TOKEN, "en");
	
	@Before
	public void setUp(){
		initMocks(this);
		Pageable pageable = new PageRequest(0, defaultPageSize);
		
		User buyer = new User();
		buyer.setUsername(BUYER_NAME);
		buyer.setId(BUYER_ID);
		
		User seller = new User();
		seller.setUsername(SELLER_NAME);
		seller.setId(SELLER_ID);
		
		Item item = new Item("itName", 222D, "v1.0", false, false);
		
		Rating rating1 = new Rating();
		Rating rating2 = new Rating();
		
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
		
		List<Rating> ratings = new ArrayList<Rating>();
		ratings.add(rating2);
		ratings.add(rating1);
		
		when(userDao.findByUsername(SELLER_NAME)).thenReturn(seller);
		when(userDao.findByUsername(BUYER_NAME)).thenReturn(buyer);
		when(ratingDao.findRatingForUserAsBuyer(BUYER_ID, pageable)).thenReturn(ratings);
		when(ratingDao.findRatingForUserAsBuyerQty(BUYER_ID)).thenReturn(2L);
		when(ratingDao.findRatingForUserAsSeller(SELLER_ID, pageable)).thenReturn(ratings);
		when(ratingDao.findRatingForUserAsSellerQty(SELLER_ID)).thenReturn(2L);
		
		when(sessionService.getUserSession(USR_TOKEN)).thenReturn(null);
		
	}
	
	@Test
	public void listRatingsBuyer(){
		request.setRole(Role.BUYER);
		request.setUsername(BUYER_NAME);
		ListRatingsResponse response = cmd.execute(request);
		assertEquals(2, response.getTotalElements());
	}
	
	@Test
	public void listRatingsSeller(){
		request.setRole(Role.SELLER);
		request.setUsername(SELLER_NAME);
		ListRatingsResponse response = cmd.execute(request);
		assertEquals(2, response.getTotalElements());
	}
}
