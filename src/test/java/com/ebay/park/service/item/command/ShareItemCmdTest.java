package com.ebay.park.service.item.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.SocialDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.*;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.item.dto.ShareItemRequest;
import com.ebay.park.util.FacebookUtil;
import com.ebay.park.util.InternationalizationUtil;
import com.ebay.park.util.TextUtils;
import com.ebay.park.util.TwitterUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;


public class ShareItemCmdTest {

	private static final String URL = "url";
	private static final String LANG = "es";
	private static final String CATEGORY = "category";
	private static final String AN_EXCEPTION_WAS_EXPECTED = "An exception was expected";
	private static final long ITEM_ID = 1L;
	private static final long USER_ID = 999l;

	@InjectMocks
	@Spy
	private final ShareItemCmd cmd = new ShareItemCmd();

	@Mock
	private ShareItemRequest request;

	@Mock
	private ItemDao itemDao;

	@Mock
	private UserDao userDao;

	@Mock
	private UserSocialDao userSocialDao;

	@Mock
	private SocialDao socialDao;

	@Mock
	private Item item;

	@Mock
	private User user;

	@Mock
	private UserSocial userSocial;

	@Mock
	private Social social;

	@Mock
	private FacebookUtil facebookUtil;

	@Mock
	private TwitterUtil twitterUtil;
	
	@Mock
	private InternationalizationUtil i18nUtil;
	
	@Mock
	private TextUtils textUtils;

	@Before
	public void setUp() {
		initMocks(this);
		when(request.getItemId()).thenReturn(ITEM_ID);
		when(request.getToken()).thenReturn("TOKEN");
		when(request.getLanguage()).thenReturn(LANG);

		when(item.getId()).thenReturn(ITEM_ID);
		when(item.getDescription()).thenReturn("ITEM");

		when(user.getId()).thenReturn(USER_ID);

		when(userSocial.getToken()).thenReturn("SOCIAL_TOKEN");
		when(userSocial.getTokenSecret()).thenReturn("SOCIAL_SECRET");

		when(itemDao.findOne(1l)).thenReturn(item);

		when(userDao.findByToken("TOKEN")).thenReturn(user);
		when(socialDao.findByDescription(any(String.class))).thenReturn(social);
		
		when(i18nUtil.internationalize((Category)any(), (String)any())).thenReturn(new Category());
		when(item.getCategory()).thenReturn(new Category() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				setName(CATEGORY);
			}
		});
		when(textUtils.createItemSEOURL((String)any(), (String)any(), (Long)any())).thenReturn(URL);
	}

	@Test
	public void givenAllValidEntriesAndFacebookRequestWhenSharingItemThenShare() {
		when(request.getSocialNetwork()).thenReturn("facebook");
		when(social.getDescription()).thenReturn("facebook");
		when(userSocialDao.findFacebookUser(USER_ID)).thenReturn(userSocial);
		doNothing().when(facebookUtil).shareOnFacebook(URL,
				"ITEM", "ITEM_DESC", "SOCIAL_TOKEN");
		cmd.execute(request);
	}

	@Test
	public void givenAllValidEntriesAndTwitterRequestWhenSharingItemThenShare() {
		when(request.getSocialNetwork()).thenReturn("twitter");
		when(social.getDescription()).thenReturn("twitter");
		when(userSocialDao.findTwitterUser(USER_ID)).thenReturn(userSocial);
		doNothing().when(twitterUtil).shareItemOnTwitter("SOCIAL_TOKEN",
				"SOCIAL_SECRET", URL);
		cmd.execute(request);
	}

	@Test
	public void givenSocialNotLinkedWhenSharingItemThenException() {
		when(request.getSocialNetwork()).thenReturn("twitter");
		when(social.getDescription()).thenReturn("twitter");
		try {
			cmd.execute(request);
			fail(AN_EXCEPTION_WAS_EXPECTED);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.INVALID_USER_SOCIAL.getCode(), e.getCode());
		}

	}

	@Test
	public void givenInvalidSocialNetworksWhenSharingItemThenException() {
		when(request.getSocialNetwork()).thenReturn("invalid");
		when(socialDao.findByDescription("invalid")).thenReturn(null);
		try {
			cmd.execute(request);
			fail(AN_EXCEPTION_WAS_EXPECTED);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.INVALID_SOCIAL_NETWORK.getCode(), e.getCode());
		}
	}

	@Test
	public void givenUnexistingItemWhenSharingItemThenException() {
		when(itemDao.findOne(ITEM_ID)).thenReturn(null);
		try {
			cmd.execute(request);
			fail(AN_EXCEPTION_WAS_EXPECTED);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.ITEM_NOT_FOUND.getCode(), e.getCode());
		}
	}

	@Test
	public void givenUnexistingUserWhenSharingItemThenException() {
		when(userDao.findByToken("TOKEN")).thenReturn(null);
		try {
			cmd.execute(request);
			fail(AN_EXCEPTION_WAS_EXPECTED);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.USER_UNAUTHORIZED.getCode(), e.getCode());
		}
	}

	@Test
	public void givenBlacklistedAndFBRequestItemWhenSharingItemThenDoNotShare() {
		when(item.is(StatusDescription.PENDING)).thenReturn(true);
		when(request.getSocialNetwork()).thenReturn("facebook");
		when(social.getDescription()).thenReturn("facebook");
		when(userSocialDao.findFacebookUser(USER_ID)).thenReturn(userSocial);

		cmd.execute(request);
		verify(cmd, never()).shareOnFacebook(user, item);
		verify(cmd, never()).shareOnTwitter(user, item);
	}

	@Test
	public void givenBlacklistedAndTWRequestItemWhenSharingItemThenDoNotShare() {
		when(item.is(StatusDescription.PENDING)).thenReturn(true);
		when(request.getSocialNetwork()).thenReturn("twitter");
		when(social.getDescription()).thenReturn("twitter");
		when(userSocialDao.findFacebookUser(USER_ID)).thenReturn(userSocial);

		cmd.execute(request);
		verify(cmd, never()).shareOnFacebook(user, item);
		verify(cmd, never()).shareOnTwitter(user, item);
	}
}
