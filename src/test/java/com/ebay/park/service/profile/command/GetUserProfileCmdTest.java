package com.ebay.park.service.profile.command;

import com.ebay.park.db.dao.ConversationDao;
import com.ebay.park.db.dao.FollowerDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.*;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.picture.ResetEPSPictureExpireDateService;
import com.ebay.park.service.profile.dto.GetUserProfileRequest;
import com.ebay.park.service.profile.dto.UserProfile;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.InternationalizationUtil;
import com.ebay.park.util.TextUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GetUserProfileCmdTest {

	private static final String NULL_TOKEN = null;
	private static final String VALID_TOKEN = "abcdefghij";
	private static final String USERNAME1 = "timMartins";
	private static final String USERNAME2 = "timonMartins";
	private static final Long KEY_USER1 = 67899L;
	private static final Long KEY_USER2 = 67898L;
	private static final String EMAIL1 = "tm@mail.com";
	private static final String EMAIL2 = "ttm@mail.com";
	private static final String PICTURE = "picture";
	private static final String LOCATION_NAME = "locationName";
	private static final Date CREATION_DATE = new Date();
	private static final City CITY = new City();
	private static final List<Item> ITEMS = new ArrayList<Item>(2);
	private static final String URL = "url";
	private static final String LANGUAGE = 	"es";
	private static final String AN_EXCEPTION_WAS_EXPECTED = "An exception was expected";

	@InjectMocks
	private GetUserProfileCmd getUserProfileCmd;

	@Mock
	private UserDao userDao;

	@Mock
	private FollowerDao followerDao;
	
	@Mock
	private ConversationDao conversationDao;
	
	@Mock
	private InternationalizationUtil i18nUtil;
	
	@Mock
	private ResetEPSPictureExpireDateService resetEPSExpirateDate;
	
	@Mock
	private TextUtils textUtils; 
	
	private User user;

	@Before
	public void setUp() {
		getUserProfileCmd = new GetUserProfileCmd();
		initMocks(this);
		
		user = createUser1();
		when(userDao.findByUsername(USERNAME1)).thenReturn(user);
		
		when(followerDao.findFollowings(KEY_USER1)).thenReturn(
				createFollowers(3));

		when(conversationDao.findConversationsForBuyerCount(user.getId())).thenReturn(2);
		when(conversationDao.findConversationsForSellerCount(user.getId())).thenReturn(2);
		
		when(textUtils.createProfileSEOURL(user.getUsername())).thenReturn(URL);
		
	}

	@Test
	public void givenLoggedUserWhenGettingOwnProfileThenReturnOwnProfile() {
		when(userDao.findByToken(VALID_TOKEN)).thenReturn(user);

		GetUserProfileRequest request = new GetUserProfileRequest(VALID_TOKEN, USERNAME1, LANGUAGE);
		when(i18nUtil.internationalizeItems(ITEMS, request)).thenReturn(ITEMS);
		UserProfile userProfile = getUserProfileCmd.execute(request);

		assertNotNull(userProfile);
		assertEquals(userProfile.getUsername(), USERNAME1);
		assertEquals(userProfile.getEmail(), EMAIL1);
		assertEquals(userProfile.getProfilePicture(), PICTURE);
		assertEquals(userProfile.getCreationDate(), DataCommonUtil.getDateTimeAsUnixFormat(CREATION_DATE));
		assertEquals(userProfile.getLocationName(), LOCATION_NAME);
		assertEquals(userProfile.getFollowers(), new Integer(2));
		assertEquals(userProfile.getFollowing(), new Integer(3));
		assertEquals(userProfile.getNegativeRatings(), new Integer(1));
		assertEquals(userProfile.getNeutralRatings(), new Integer(2));
		assertEquals(userProfile.getPositiveRatings(), new Integer(1));

		verify(userDao).findByUsername(USERNAME1);
		verify(followerDao).findFollowings(KEY_USER1);
	}
	
	@Test
	public void givenLoggedUserWhenGettingAnotherUsersProfileThenReturnProfile() {
		User anotherUser = createUser2();
		when(userDao.findByToken(VALID_TOKEN)).thenReturn(anotherUser);

		GetUserProfileRequest request = new GetUserProfileRequest(VALID_TOKEN, USERNAME1, LANGUAGE);
		when(i18nUtil.internationalizeItems(ITEMS, request)).thenReturn(ITEMS);
		UserProfile userProfile = getUserProfileCmd.execute(request);

		assertNotNull(userProfile);
		assertEquals(userProfile.getUsername(), USERNAME1);
        assertNull(userProfile.getEmail());
		assertEquals(userProfile.getProfilePicture(), PICTURE);
		assertEquals(userProfile.getCreationDate(), DataCommonUtil.getDateTimeAsUnixFormat(CREATION_DATE));
		assertEquals(userProfile.getLocationName(), LOCATION_NAME);
		assertEquals(userProfile.getFollowers(), new Integer(2));
		assertEquals(userProfile.getFollowing(), new Integer(3));
		assertEquals(userProfile.getNegativeRatings(), new Integer(1));
		assertEquals(userProfile.getNeutralRatings(), new Integer(2));
		assertEquals(userProfile.getPositiveRatings(), new Integer(1));

		verify(userDao).findByUsername(USERNAME1);
		verify(followerDao).findFollowings(KEY_USER1);
	}

	@Test
	public void givenNonExistingUserWhenGettingProfileThenException() {
		when(userDao.findByUsername(USERNAME1)).thenReturn(null);

		try {
			getUserProfileCmd.execute(new GetUserProfileRequest(
					NULL_TOKEN, USERNAME1, LANGUAGE));
			fail(AN_EXCEPTION_WAS_EXPECTED);
		} catch (ServiceException se) {
			assertEquals(ServiceExceptionCode.USER_NOT_FOUND.getCode(), se.getCode());
			verify(userDao).findByUsername(USERNAME1);
		}
	}

	private User createUser2() {
		User user2 = new User();
		
		user2.setUserId(KEY_USER2);
		user2.setUsername(USERNAME2);
		user2.setEmail(EMAIL2);
		
		return user2;
	}
	
	private User createUser1() {
		User user1 = createBasicUser();

		user1.setUserId(KEY_USER1);
		user1.setUsername(USERNAME1);
		user1.setEmail(EMAIL1);
		
		return user1;
	}

	private User createBasicUser() {
		User user = new User();
		
		user.setPicture(PICTURE);
		user.setCreation(CREATION_DATE);
		user.setCity(CITY);
		user.setPublishedItems(ITEMS);
		user.setPublicPublishedItems(ITEMS);
		user.setLocationName(LOCATION_NAME);
		user.setFollowers(createFollowers(2));
		List<Rating> ratings = new ArrayList<Rating>();
		ratings.add(createNegativeRating());
		ratings.add(createNeutralrating());
		ratings.add(createNeutralrating());
		ratings.add(createPositiveRating());
		user.setRatings(ratings);
		
		return user;
	}

	private List<Follower> createFollowers(int quantity) {
		List<Follower> followers = new ArrayList<Follower>();
		for (int i = 0; i < quantity; i++) {
			followers.add(new Follower());
		}
		return followers;
	}

	private Rating createNegativeRating() {
		Rating rating = new Rating();
		rating.setStatus(RatingStatus.NEGATIVE);
		return rating;
	}

	private Rating createNeutralrating() {
		Rating rating = new Rating();
		rating.setStatus(RatingStatus.NEUTRAL);
		return rating;
	}

	private Rating createPositiveRating() {
		Rating rating = new Rating();
		rating.setStatus(RatingStatus.POSITIVE);
		return rating;
	}

}
