package com.ebay.park.service.item.command;

import com.ebay.park.db.dao.*;
import com.ebay.park.db.entity.*;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.blacklist.BlacklistService;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.UpdateItemRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.UserUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link UpdateItemCmd}.
 */
public class UpdateItemCmdTest {

    private static final String USER_TOKEN = "token";
    private static final String DESCRIPTION = "description";
    private static final String LOCATION = "20, 20";
    private static final String ITEM_NAME = "name";
    private static final String PRICE = "10";
    private static final long ITEM_ID = 1L;
    private static final long CATEGORY_ID = 2L;
    private static final long GROUP_ID1 = 1;
    private static final long GROUP_ID2 = 2;
    private static final long USER_ID =100L;
    private static final long SESSION_ID = 100L;
    private static final String LATITUDE = "20";
    private static final String LONGITUDE = "20";
    private static final String ZIPCODE = "92010";
    private static final String LOCATION_NAME = "Tandil";

    private static final String MSG = "An exception was expected";
    private static final String LANGUAGE = "es";

    @InjectMocks
	@Spy
	private final UpdateItemCmd cmd = new UpdateItemCmd();

	@Mock
	private SessionService sessionService;

	@Mock
	private ItemDao itemDao;

	@Mock
	private CategoryDao categoryDao;

	@Mock
	private UserDao userDao;

	@Mock
	private ItemGroupDao itemGroupDao;
	
	@Mock
	private GroupDao groupDao;

	@Mock
	private UpdateItemRequest request;

	@Mock
	private Item item;

	@Mock
	private Category category;

	@Mock
	private User user;

	@Mock
	private ItemUtils itemUtils;

	@Mock
	private BlacklistService blacklistService;

	@Mock
	private ArrayList<ItemGroup> list;

    @Mock
    private UserUtils userUtils;
	
	@Before
	public void setUp() {
		initMocks(this);
		UserSessionCache userSession = mock(UserSessionCache.class);
		when(userSession.getUserId()).thenReturn(SESSION_ID);
		when(user.getUserId()).thenReturn(USER_ID);
        when(user.getIdiom()).thenReturn(mock(Idiom.class));
		when(item.getPublishedBy()).thenReturn(user);
		when(sessionService.getUserSession(any(String.class))).thenReturn(
				userSession);
        when(userDao.findByToken(USER_TOKEN)).thenReturn(user);
		when(request.getToken()).thenReturn(USER_TOKEN);
		when(request.getItemId()).thenReturn(1L);
		when(request.getDescription()).thenReturn(DESCRIPTION);
		when(request.getLocation()).thenReturn(LOCATION);
		when(request.getLocationName()).thenReturn(LOCATION_NAME);
		when(request.getLatitude()).thenReturn(LATITUDE);
		when(request.getZipCode()).thenReturn(ZIPCODE);
		when(request.getLongitude()).thenReturn(LONGITUDE);
		when(request.getName()).thenReturn(ITEM_NAME);
		when(request.getPrice()).thenReturn(PRICE);
		when(request.getCategoryId()).thenReturn(CATEGORY_ID);
		when(request.getGroups()).thenReturn(new String[] { 
		        String.valueOf(GROUP_ID1),
		        String.valueOf(GROUP_ID2)
		        });
        when(request.getLanguage()).thenReturn(LANGUAGE);
		when(request.isFeedWhenItemBanned()).thenReturn(true);
		when(blacklistService.isBlackListed(item)).thenReturn(false);
        when(userUtils.getItemUser(request)).thenReturn(item);
        when(categoryDao.findOne(CATEGORY_ID)).thenReturn(category);
        when(groupDao.findOne(GROUP_ID1)).thenReturn(mock(Group.class));
        when(groupDao.findOne(GROUP_ID2)).thenReturn(mock(Group.class));
        when(itemDao.save(item)).thenReturn(item);
	}

	@Test
	public void givenAllValidEntriesWhenUpdatingAnItemThenUpdate() {
		when(item.getStatus()).thenReturn(StatusDescription.EXPIRED);
        cmd.execute(request);

		verify(userDao).findByToken(USER_TOKEN);
        verify(categoryDao).findOne(CATEGORY_ID);
        verify(groupDao).findOne(GROUP_ID1);
        verify(groupDao).findOne(GROUP_ID2);
        verify(itemDao).save(item);
	}

	@Test
	/**
	 * Test the situation where the user updates an own item and this
	 * item is expired. The update should be successful.
	 */
	public void updateExpiredItem() {

		cmd.execute(request);

		verify(userDao).findByToken(USER_TOKEN);
		verify(categoryDao).findOne(CATEGORY_ID);
		verify(groupDao).findOne(GROUP_ID1);
		verify(groupDao).findOne(GROUP_ID2);
		verify(itemDao).save(item);
		verifyAllRequestFields();
	}

	@Test
	public void givenNullUserThenException() {
	    when(userDao.findByToken(USER_TOKEN)).thenReturn(null);
	    try {
	        cmd.execute(request);
	    } catch (ServiceException e) {
	        assertEquals(ServiceExceptionCode.USER_UNAUTHORIZED.getCode(), e.getCode());
	        verify(userDao).findByToken(USER_TOKEN);
	    }
	}

	@Test
    public void givenNullDescriptionThenUpdate() {
	    when(request.getDescription()).thenReturn(null);

        cmd.execute(request);

        verify(userDao).findByToken(USER_TOKEN);
        verify(categoryDao).findOne(CATEGORY_ID);
        verify(groupDao).findOne(GROUP_ID1);
        verify(groupDao).findOne(GROUP_ID2);
        verify(itemDao).save(item);
        verifyAllRequestFields();
    }

	@Test
    public void givenNullNameThenUpdate() {
        when(request.getName()).thenReturn(null);

        cmd.execute(request);

        verify(userDao).findByToken(USER_TOKEN);
        verify(categoryDao).findOne(CATEGORY_ID);
        verify(groupDao).findOne(GROUP_ID1);
        verify(groupDao).findOne(GROUP_ID2);
        verify(itemDao).save(item);
        verifyAllRequestFields();
    }

	@Test
    public void givenNullPriceThenUpdate() {
        when(request.getPrice()).thenReturn(null);

        cmd.execute(request);

        verify(userDao).findByToken(USER_TOKEN);
        verify(categoryDao).findOne(CATEGORY_ID);
        verify(groupDao).findOne(GROUP_ID1);
        verify(groupDao).findOne(GROUP_ID2);
        verify(itemDao).save(item);
        verifyAllRequestFields();
    }

	@Test
    public void givenNullLocationThenUpdate() {
        when(request.getLocation()).thenReturn(null);

        cmd.execute(request);

        verify(userDao).findByToken(USER_TOKEN);
        verify(categoryDao).findOne(CATEGORY_ID);
        verify(groupDao).findOne(GROUP_ID1);
        verify(groupDao).findOne(GROUP_ID2);
        verify(itemDao).save(item);
        verifyAllRequestFields();
    }

	@Test
    public void givenNullLocationNameThenUpdate() {
        when(request.getLocationName()).thenReturn(null);

        cmd.execute(request);

        verify(userDao).findByToken(USER_TOKEN);
        verify(categoryDao).findOne(CATEGORY_ID);
        verify(groupDao).findOne(GROUP_ID1);
        verify(groupDao).findOne(GROUP_ID2);
        verify(itemDao).save(item);
        verifyAllRequestFields();
    }

	@Test
    public void givenNullLatitudeThenUpdate() {
        when(request.getLatitude()).thenReturn(null);

        cmd.execute(request);

        verify(userDao).findByToken(USER_TOKEN);
        verify(categoryDao).findOne(CATEGORY_ID);
        verify(groupDao).findOne(GROUP_ID1);
        verify(groupDao).findOne(GROUP_ID2);
        verify(itemDao).save(item);
        verifyAllRequestFields();
    }

	@Test
    public void givenNullLongitudeThenUpdate() {
        when(request.getLongitude()).thenReturn(null);

        cmd.execute(request);

        verify(userDao).findByToken(USER_TOKEN);
        verify(categoryDao).findOne(CATEGORY_ID);
        verify(groupDao).findOne(GROUP_ID1);
        verify(groupDao).findOne(GROUP_ID2);
        verify(itemDao).save(item);
        verifyAllRequestFields();
    }
	
	@Test
    public void givenBlacklistedWhenBlacklistedItemNameThenUpdate() {
        when(blacklistService.isBlackListed(item)).thenReturn(true);

        cmd.execute(request);

        verify(userDao).findByToken(USER_TOKEN);
        verify(categoryDao).findOne(CATEGORY_ID);
        verify(groupDao).findOne(GROUP_ID1);
        verify(groupDao).findOne(GROUP_ID2);
        verify(itemDao).save(item);
        verify(blacklistService).isBlackListed(item);
        verify(request).isFeedWhenItemBanned();
        verifyAllRequestFields();
    }

    @Test
    public void givenBlacklistedWhenBlacklistedItemNameThenBanItem() {
        when(blacklistService.isBlackListed(item)).thenReturn(true);

        cmd.execute(request);

        verify(blacklistService).bannedItem(item, LANGUAGE);
    }

	@Test
    public void givenBlacklistedOnCreationProcessWhenBlacklistedItemNameThenUpdate() {
        when(request.isFeedWhenItemBanned()).thenReturn(false);

        when(item.getStatus()).thenReturn(StatusDescription.PENDING);
        when(blacklistService.isBlackListed(item)).thenReturn(true);
        cmd.execute(request);

        verify(userDao).findByToken(USER_TOKEN);
        verify(categoryDao).findOne(CATEGORY_ID);
        verify(groupDao).findOne(GROUP_ID1);
        verify(groupDao).findOne(GROUP_ID2);
        verify(itemDao).save(item);
        verify(item).getStatus();
        verify(blacklistService).isBlackListed(item);
        verify(request).isFeedWhenItemBanned();
        verifyAllRequestFields();
    }

	@Test
    public void givenActiveOnCreationProcessWhenBlacklistedItemNameThenUpdate() {
        when(request.isFeedWhenItemBanned()).thenReturn(false);
        when(item.getStatus()).thenReturn(StatusDescription.PENDING);
        when(blacklistService.isBlackListed(item)).thenReturn(true);

        cmd.execute(request);

        verify(userDao).findByToken(USER_TOKEN);
        verify(categoryDao).findOne(CATEGORY_ID);
        verify(groupDao).findOne(GROUP_ID1);
        verify(groupDao).findOne(GROUP_ID2);
        verify(itemDao).save(item);
        verify(item).getStatus();
        verify(blacklistService).isBlackListed(item);
        verify(request).isFeedWhenItemBanned();
        verifyAllRequestFields();
    }

	@Test
    public void givenNullZipCodeThenUpdate() {
        when(request.getZipCode()).thenReturn(null);

        cmd.execute(request);

        verify(userDao).findByToken(USER_TOKEN);
        verify(categoryDao).findOne(CATEGORY_ID);
        verify(groupDao).findOne(GROUP_ID1);
        verify(groupDao).findOne(GROUP_ID2);
        verify(itemDao).save(item);
        verifyAllRequestFields();
    }

	@SuppressWarnings("unchecked")
    @Test
    public void givenItemWithGroupsThenUpdate() {

	    ItemGroup itemGroup1 = mock(ItemGroup.class);
	    ItemGroup itemGroup2 = mock(ItemGroup.class);
	    Group group1 = mock(Group.class);
	    Group group2 = mock(Group.class);
	    when(group1.getId()).thenReturn(GROUP_ID1);

	    when(itemGroup1.getGroup()).thenReturn(group1);
	    when(itemGroup2.getGroup()).thenReturn(group2);

	    when(item.getItemGroups()).thenReturn(list);
	    Iterator<ItemGroup> it = mock(Iterator.class);
	    when(list.iterator()).thenReturn(it);
	    when(it.hasNext())
	        .thenReturn(true)
	        .thenReturn(true)
	        .thenReturn(false);

	    when(it.next())
	        .thenReturn(itemGroup1)
	        .thenReturn(itemGroup2);

	    doNothing().when(it).remove();

        cmd.execute(request);

        verify(userDao).findByToken(USER_TOKEN);
        verify(categoryDao).findOne(CATEGORY_ID);
        verify(groupDao).findOne(GROUP_ID1);
        verify(groupDao).findOne(GROUP_ID2);
        verify(itemDao).save(item);
        verifyAllRequestFields();
    }

	@SuppressWarnings("unchecked")
    @Test
    public void givenItemUnchangedThenNotify() {
        when(request.getDescription()).thenReturn(null);
        when(request.getLocation()).thenReturn(null);
        when(request.getLocationName()).thenReturn(null);
        when(request.getLatitude()).thenReturn(null);
        when(request.getZipCode()).thenReturn(null);
        when(request.getLongitude()).thenReturn(null);
        when(request.getName()).thenReturn(null);
        when(request.getPrice()).thenReturn(null);
        when(request.getCategoryId()).thenReturn(null);
        when(request.getGroups()).thenReturn(null);

        ItemGroup itemGroup1 = mock(ItemGroup.class);
        ItemGroup itemGroup2 = mock(ItemGroup.class);
        Group group1 = mock(Group.class);
        Group group2 = mock(Group.class);
        when(group1.getId()).thenReturn(GROUP_ID1);

        when(itemGroup1.getGroup()).thenReturn(group1);
        when(itemGroup2.getGroup()).thenReturn(group2);

        when(item.getItemGroups()).thenReturn(list);
        Iterator<ItemGroup> it = mock(Iterator.class);
        when(list.iterator()).thenReturn(it);
        when(it.hasNext())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);

        when(it.next())
            .thenReturn(itemGroup1)
            .thenReturn(itemGroup2);

        doNothing().when(it).remove();

        cmd.execute(request);

        verify(userDao).findByToken(USER_TOKEN);
        verify(itemDao, never()).save(item);
        verifyAllRequestFields();
    }

	@SuppressWarnings("unchecked")
    @Test
    public void givenItemOnlyWithGroupsThenUpdate() {
        when(request.getDescription()).thenReturn(null);
        when(request.getLocation()).thenReturn(null);
        when(request.getLocationName()).thenReturn(null);
        when(request.getLatitude()).thenReturn(null);
        when(request.getZipCode()).thenReturn(null);
        when(request.getLongitude()).thenReturn(null);
        when(request.getName()).thenReturn(null);
        when(request.getPrice()).thenReturn(null);
        when(request.getCategoryId()).thenReturn(null);

        ItemGroup itemGroup1 = mock(ItemGroup.class);
        ItemGroup itemGroup2 = mock(ItemGroup.class);
        Group group1 = mock(Group.class);
        Group group2 = mock(Group.class);
        when(group1.getId()).thenReturn(GROUP_ID1);

        when(itemGroup1.getGroup()).thenReturn(group1);
        when(itemGroup2.getGroup()).thenReturn(group2);

        when(item.getItemGroups()).thenReturn(list);
        Iterator<ItemGroup> it = mock(Iterator.class);
        when(list.iterator()).thenReturn(it);
        when(it.hasNext())
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false);

        when(it.next())
            .thenReturn(itemGroup1)
            .thenReturn(itemGroup2);

        doNothing().when(it).remove();

        cmd.execute(request);

        verify(userDao).findByToken(USER_TOKEN);
        verify(groupDao).findOne(GROUP_ID1);
        verify(groupDao).findOne(GROUP_ID2);
        verify(itemDao).save(item);
        verifyAllRequestFields();
    }

	@Test
    public void givenInvalidGroupThenException() {
        when(request.getZipCode()).thenReturn(null);
        when(groupDao.findOne(GROUP_ID2)).thenReturn(null);

        try {
            cmd.execute(request);
            fail(MSG);
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.INVALID_GROUP.getCode(), e.getCode());
        }
    }

	@Test
    public void givenNullGroupsThenUpdate() {
        when(request.getGroups()).thenReturn(null);

        cmd.execute(request);

        verify(userDao).findByToken(USER_TOKEN);
        verify(categoryDao).findOne(CATEGORY_ID);
        verify(groupDao, never()).findOne(GROUP_ID1);
        verify(groupDao, never()).findOne(GROUP_ID2);
        verify(itemDao).save(item);
        verifyAllRequestFields();
    }

	@Test
	public void givenNullCategoryThenUpdate() {
        when(request.getCategoryId()).thenReturn(null);

        cmd.execute(request);

        verify(userDao).findByToken(USER_TOKEN);
        verify(categoryDao, never()).findOne(CATEGORY_ID);
        verify(groupDao).findOne(GROUP_ID1);
        verify(groupDao).findOne(GROUP_ID2);
        verify(itemDao).save(item);
        verifyAllRequestFields();
    }

	@Test
	public void givenInvalidCategoryThenUpdate() {
        when(itemDao.findOne(ITEM_ID)).thenReturn(item);
        when(categoryDao.findOne(CATEGORY_ID)).thenReturn(null);

        try {
            cmd.execute(request);
            fail(MSG);
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.CATEGORY_NOT_FOUND.getCode(), e.getCode());
        }
    }

	@Test
    public void whenSavingItemThenException() {
	    doThrow(new RuntimeException()).when(itemDao).save(any(Item.class));
	    when(request.getCategoryId()).thenReturn(null);

	    try {
            cmd.execute(request);
            fail(MSG);
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.ERROR_UPDATING_ITEM.getCode(), e.getCode());
        }
	}

    private void verifyAllRequestFields() {
        verify(request, atLeastOnce()).getDescription();
        verify(request, atLeastOnce()).getCategoryId();
        verify(request, atLeastOnce()).getGroups();
        verify(request, atLeastOnce()).getLatitude();
        verify(request, atLeastOnce()).getLocation();
        verify(request, atLeastOnce()).getLocationName();
        verify(request, atLeastOnce()).getLongitude();
        verify(request, atLeastOnce()).getName();
        verify(request, atLeastOnce()).getPrice();
        verify(request, atLeastOnce()).getToken();
        verify(request, atLeastOnce()).getZipCode();
    }
}


