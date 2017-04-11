package com.ebay.park.service.item.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Assert;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.ItemGroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserFollowsItemDao;
import com.ebay.park.db.dao.UserReportItemDao;
import com.ebay.park.db.entity.Category;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.Idiom;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.ItemGroup;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserReportItem;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.dto.GetItemRequest;
import com.ebay.park.service.item.dto.GetItemResponse;
import com.ebay.park.service.picture.ResetEPSPictureExpireDateService;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.util.DataCommonUtil;
import com.ebay.park.util.EPSUtils;
import com.ebay.park.util.InternationalizationUtil;
import com.ebay.park.util.TextUtils;

public class GetItemCmdTest {
	@Spy
	@InjectMocks
	private final GetItemCmd cmd = new GetItemCmd();

	@Mock
	private ItemDao itemDao;

	@Mock
	private UserDao userDao;

	@Mock
	private GetItemRequest request;

	@Mock
	private ItemGroupDao itemGroupDao;

	@Mock
	private UserFollowsItemDao userFollowsItemDao;

	@Mock
	private UserReportItemDao userReportItemDao;

	@Mock
	private InternationalizationUtil i18nUtil;

	@Mock
	private SessionService sessionService;
	
	@Mock
	private ResetEPSPictureExpireDateService resetEPSExpirateDate;

	@Mock
	private Item item;
	private static final Long itemId = 8l;
	private Date postDate = new Date();;
	
	@Mock
	private User owner;
	private static final Long ownerUserId = 45l;
	private static final String OWNER_TOKEN = "ownerToken";

	@Mock
	private User ownerIdiom;
	private static final Long userId = 40l;
	private static final String USER_TOKEN = "userToken";

	private static final String URL = "url";

	private static final String RESOLUTION = "$_3";
	private static final String DEFAULT_RESOLUTION = "$_1";
	
	
	@Mock
	private TextUtils textUtils;
	
	@Mock
	private EPSUtils epsUtils;

	@Before
	public void setUp() {
		initMocks(this);
		ReflectionTestUtils.setField(epsUtils, "defaultResolution", DEFAULT_RESOLUTION);
		ReflectionTestUtils.setField(cmd, "resolution", RESOLUTION);

		when(request.getId()).thenReturn("8");
		
		//Viewer, not owner
		when(ownerIdiom.getId()).thenReturn(userId);
		when(userDao.findByToken(USER_TOKEN)).thenReturn(ownerIdiom);

		when(ownerIdiom.getUsername()).thenReturn("username");
		when(ownerIdiom.getEmail()).thenReturn("email@email.com");
		Idiom idiom = new Idiom();
		when(ownerIdiom.getIdiom()).thenReturn(idiom);
		
		//OWNER
		when(owner.getId()).thenReturn(ownerUserId);
		when(userDao.findByToken(OWNER_TOKEN)).thenReturn(owner);
		when(owner.getUsername()).thenReturn("ownerUsername");
		when(owner.getEmail()).thenReturn("email@email.com");
		Idiom ownerIdiom = new Idiom();
		when(owner.getIdiom()).thenReturn(ownerIdiom);

		// ITEM
		when(itemDao.findOne(itemId)).thenReturn(item);
		when(item.getPublishedBy()).thenReturn(owner);
		when(item.getId()).thenReturn(itemId);
		when(item.getName()).thenReturn("name");
		when(item.getDescription()).thenReturn("description");
		when(item.getBrandPublish()).thenReturn("brandPublish");
		when(item.getVersionPublish()).thenReturn("2.2");
		when(item.getLocation()).thenReturn("locationUrl");
		when(item.getPrice()).thenReturn(Double.valueOf(100000.0));
		when(item.getPicture1Url()).thenReturn("http://picture1");
		when(item.getPicture2Url()).thenReturn("http://picture2");
		when(item.getPicture3Url()).thenReturn("http://picture3");
		when(item.getPicture4Url()).thenReturn("http://picture4");
		when(item.getLatitude()).thenReturn(1.0);
		when(item.getLongitude()).thenReturn(2.0);
		when(epsUtils.getPictureResolution("http://picture1", RESOLUTION)).thenReturn("http://picture1");
		when(epsUtils.getPictureResolution("http://picture2", RESOLUTION)).thenReturn("http://picture2");
		when(epsUtils.getPictureResolution("http://picture3", RESOLUTION)).thenReturn("http://picture3");
		when(epsUtils.getPictureResolution("http://picture4", RESOLUTION)).thenReturn("http://picture4");
		postDate.setTime(postDate.getTime() - 10000);
		when(item.getPublished()).thenReturn(postDate);
		
		when(textUtils.createItemSEOURL(
				any(String.class), any(String.class), any(Long.class))).thenReturn(URL);

		
		//Category
		Category category = mock(Category.class);
		when(category.getCategoryId()).thenReturn(1l);
		when(category.getName()).thenReturn("Category Name");
		when(i18nUtil.internationalize(category, "es")).thenReturn(category);
		when(item.getCategory()).thenReturn(category);
		
		Group group1 = mock(Group.class);
		when(group1.getGroupId()).thenReturn(100l);
		when(group1.getId()).thenReturn(100l);
		Group group2 = mock(Group.class);
		when(group2.getGroupId()).thenReturn(200l);
		when(group2.getId()).thenReturn(200l);

		ItemGroup itemGroup1 = mock(ItemGroup.class);
		when(itemGroup1.getGroup()).thenReturn(group1);
		ItemGroup itemGroup2 = mock(ItemGroup.class);
		when(itemGroup2.getGroup()).thenReturn(group2);
		List<ItemGroup> itemGroups = new ArrayList<ItemGroup>();
		itemGroups.add(itemGroup1);
		itemGroups.add(itemGroup2);
		when(item.getItemGroups()).thenReturn(itemGroups);

		List<Group> groups = new ArrayList<Group>();
		groups.add(group1);
		groups.add(group2);

		when(userFollowsItemDao.totalOfFollowersOfItem(itemId)).thenReturn(10l);

		User userComment = mock(User.class);
		
		when(userComment.getUsername()).thenReturn("Commenter");
		when(userComment.getPicture()).thenReturn("http://userPicture");

		UserReportItem report = mock(UserReportItem.class);
		when(userReportItemDao.findUserReportForItem(userId, itemId))
				.thenReturn(report);
	}

	@Test
	public void testExecuteShouldSucceed() {

		when(request.getToken()).thenReturn(USER_TOKEN);
		when(request.getLanguage()).thenReturn("es");

		when(item.getStatus()).thenReturn(StatusDescription.ACTIVE);
		when(item.is(StatusDescription.ACTIVE)).thenReturn(true);

		GetItemResponse response = cmd.execute(request);
		Assert.isTrue(response.getId().equals(itemId));
		Assert.isTrue(response.getDescription().equals("description"));
		Assert.isTrue(response.getCategory().getId().equals(1l));
		Assert.isTrue(response.getLocation().equals("locationUrl"));
		Assert.isTrue(response.getName().equals("name"));
		Assert.isTrue(response.getTotalOfFollowers().equals(10l));
		Assert.isTrue(response.getPictures().get(0).equals("http://picture1"));
		Assert.isTrue(response.getPictures().get(1).equals("http://picture2"));
		Assert.isTrue(response.getPictures().get(2).equals("http://picture3"));
		Assert.isTrue(response.getPictures().get(3).equals("http://picture4"));
		Assert.isTrue(response.getPrice().equals("100000.0"));
		Assert.notNull(response.getGroups());
		Assert.isTrue(response.getPublished().equals(
				DataCommonUtil.getDateTimeAsISO(postDate)));

		Assert.isTrue(response.getUser().getUsername()
				.equals(owner.getUsername()));
		Assert.isTrue(response.getLatitude().equals(1.0));
		Assert.isTrue(response.getLongitude().equals(2.0));
	}

	@Test
	public void testInvalidItem() {
		when(itemDao.findOne(any(Long.class))).thenThrow(
				ServiceException.createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND));

		GetItemResponse response = null;
		try {
			response = cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.ITEM_NOT_FOUND.getCode(), e.getCode());
		}
		assertNull(response);
	}

	@Test
	public void testItemDeleted() {
		Item item = mock(Item.class);
		when(item.isDeleted()).thenReturn(true);
		when(itemDao.findOne(any(Long.class))).thenReturn(item);

		GetItemResponse response = null;
		try {
			response = cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.UNAUTHORIZED_TO_ACCESS_ITEM.getCode(), e.getCode());
		}
		assertNull(response);
	}
	
	@Test
	public void testItemPendingOnwer() {
		
		when(request.getToken()).thenReturn(OWNER_TOKEN);
		when(request.getLanguage()).thenReturn("es");

		
		when(item.getStatus()).thenReturn(StatusDescription.PENDING);
		when(item.is(StatusDescription.PENDING)).thenReturn(true);

		GetItemResponse response = null;
		response = cmd.execute(request);

		assertNotNull(response);
	}
	
	@Test
	public void testItemPendingOtherUser() {
		
		when(request.getToken()).thenReturn(USER_TOKEN);
		when(request.getLanguage()).thenReturn("es");

		
		when(item.getStatus()).thenReturn(StatusDescription.PENDING);
		when(item.is(StatusDescription.PENDING)).thenReturn(true);

		GetItemResponse response = null;
		try {
			response = cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.UNAUTHORIZED_TO_ACCESS_ITEM.getCode(), e.getCode());
		}
		assertNull(response);
	}
	
	@Test
	public void testItemImatePendingOther() {
		
		when(request.getToken()).thenReturn(USER_TOKEN);
		when(request.getLanguage()).thenReturn("es");

		
		when(item.getStatus()).thenReturn(StatusDescription.IMAGE_PENDING);
		when(item.is(StatusDescription.IMAGE_PENDING)).thenReturn(true);

		GetItemResponse response = null;
		try {
			response = cmd.execute(request);
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.UNAUTHORIZED_TO_ACCESS_ITEM.getCode(), e.getCode());
		}
		assertNull(response);
	}
}
