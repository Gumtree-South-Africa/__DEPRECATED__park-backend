package com.ebay.park.service.item.command;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.ebay.park.db.dao.CategoryDao;
import com.ebay.park.db.dao.CityDao;
import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.ItemGroupDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserSocialDao;
import com.ebay.park.db.entity.Category;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.service.blacklist.BlacklistService;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.CreateItemRequest;
import com.ebay.park.util.TextUtils;

public class CreateItemCmdTest {

	private static final String URL = "url";

	@InjectMocks
	private final CreateItemCmd cmd = new CreateItemCmd();

	@Mock
	private ItemDao itemDao;

	@Mock
	private CategoryDao categoryDao;

	@Mock
	private UserDao userDao;

	@Mock
	private CityDao cityDao;

	@Mock
	private GroupDao groupDao;

	@Mock
	private BlacklistService blackListService;

	@Mock
	private CreateItemRequest request;

	@Mock
	private ItemGroupDao itemGroupDao;

	@Mock
	private UserSocialDao userSocialDao;

	@Mock
	private ItemUtils itemUtils;
	
	@Mock
	private TextUtils textUtils;
	
	@Before
	public void setUp() {
		initMocks(this);
		when(request.getBrandPublish()).thenReturn("brandPublish");
		when(request.getDescription()).thenReturn("description");
		when(request.getLocation()).thenReturn("locationUrl");
		when(request.getName()).thenReturn("name");
		when(request.getPrice()).thenReturn("10");
		when(request.getVersionPublish()).thenReturn("2.2");
		when(request.getShareOnFacebook()).thenReturn(false);
		when(request.getShareOnTwitter()).thenReturn(false);
	}

	/**
	 * Up to june 11, 2014 there is no test for fail conditions since the only
	 * fail conditions are those that belong to wrong format and empty data that
	 * is covered by the create item validator.
	 */

	@Test
	public void testExecuteShouldSucceed() {
		when(request.getToken()).thenReturn("token");
		User user = mock(User.class);
		when(user.isEmailVerified()).thenReturn(true);
		when(userDao.findByToken("token")).thenReturn(user);

		when(blackListService.isBlackListed(any(Item.class))).thenReturn(false);
		
		//when(addItemToGroupCmd.execute(Mockito.isA(AddItemToGroupRequest.class))).thenReturn(null);
		
		when(request.getCategoryId()).thenReturn(1l);
		Category category = mock(Category.class);
		when(categoryDao.findOne(1l)).thenReturn(category);

		when(textUtils.createItemSEOURL(any(String.class),
				any(String.class), any(Long.class))).thenReturn(URL);
		
		when(itemDao.save(any(Item.class))).thenAnswer(new Answer<Item>() {
			@Override
			public Item answer(InvocationOnMock invocation) throws Throwable {
				Object[] arg = invocation.getArguments();
				Item i = (Item) arg[0];
				i.setId(1l);
				return i;
			}
		});

		UserSocial userSocial = mock(UserSocial.class);
		when(userSocialDao.findFacebookUser(any(Long.class))).thenReturn(
				userSocial);
		when(userSocialDao.findTwitterUser(any(Long.class))).thenReturn(
				userSocial);

		String[] groups = { "1" };
		when(request.getGroups()).thenReturn(groups);
		Group group = mock(Group.class);
		when(groupDao.findOne(1l)).thenReturn(group);
		
		
		when(user.getId()).thenReturn(1l);

		cmd.execute(request);
	}

}
