/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.item.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.DeletePhotoRequest;
import com.ebay.park.service.item.dto.UserItemRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;

public class DeletePhotoCmdTest {

	private static final String USERNAME = "my-username";
	private static final String FILENAME = "file-name";
	private static final String TOKEN = "token";
	private static final Long ITEM_ID = 1l;
	private static final Long USER_ID = 100l;
	private static final String RESOURCE_NAME = USERNAME + "/" + ITEM_ID + "/"
			+ FILENAME;
	private static final String PICTURE_URL1 = "http://site.com/"
			+ RESOURCE_NAME;
	private static final String PICTURE_URL2 = "url2";
	private static final String PICTURE_URL3 = "url3";
	private static final String PICTURE_URL4 = "url4";
    private static final String ITEM_NAME = null;
    private static final Double PRICE = null;
    private static final boolean FB = false;
    private static final boolean TW = false;
    private static final String VERSION = null;

	@Spy
	@InjectMocks
	private final DeletePhotoCmd cmd = new DeletePhotoCmd();

	@Mock
	private DeletePhotoRequest request;

	@Mock
	private UserSessionCache userSession;

	@Mock
	private SessionService sessionService;

	@Mock
	private Item item;

	@Mock
	private ItemDao itemDao;

	@Mock
	private ItemUtils itemUtils;

	@Mock
	private User user;

	@Before
	public void setUp() {
		initMocks(this);
		when(request.getItemId()).thenReturn(ITEM_ID);
		when(request.getToken()).thenReturn(TOKEN);
		when(request.getPictureIdList()).thenReturn(Arrays.asList(2l, 4l));

		when(userSession.getUsername()).thenReturn(USERNAME);
		when(userSession.getUserId()).thenReturn(USER_ID);

		when(sessionService.getUserSession(TOKEN)).thenReturn(userSession);

		when(user.getUserId()).thenReturn(USER_ID);
		when(user.getUsername()).thenReturn(USERNAME);
		UserItemRequest request = mock(UserItemRequest.class);
		when(request.getToken()).thenReturn(TOKEN);
		when(request.getItemId()).thenReturn(ITEM_ID);

		when(itemDao.findOne(ITEM_ID)).thenReturn(item);

		when(item.getPicture2Url()).thenReturn(PICTURE_URL1);
		when(item.getPublishedBy()).thenReturn(user);


		doNothing().when(itemUtils).rearrangeItemPictures(item);
	}

	@Test
	public void testDeletePictureItemNotPublishedByUserTest() {
		when(userSession.getUserId()).thenReturn(1000l);
		when(userSession.getUsername()).thenReturn("another-user");
		try {
			cmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.ITEM_DOESNT_BELONG_TO_USER.getCode(), e.getCode());
		}
	}

	@Test
	public void givenValidValuesWhenExecutingOnInvalidItemThenException() {
		when(itemDao.findOne(ITEM_ID)).thenReturn(null);
		try {
			cmd.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.ITEM_NOT_FOUND.getCode(), e.getCode());
		}
	}

	@Test
    public void givenValidValuesWhenExecutingOnDeletedItemThenException() {
	    Item item = mock(Item.class);
	    when(item.isDeleted()).thenReturn(true);

        when(itemDao.findOne(ITEM_ID)).thenReturn(item);
        try {
            cmd.execute(request);
            fail();
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.ITEM_NOT_FOUND.getCode(), e.getCode());
        }
    }

	@Test
    public void givenInvalidPictureValueItemWhenExecutingThenException() {
        try {
            cmd.execute(request);
            fail();
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.PICTURE_NOT_FOUND.getCode(), e.getCode());
        }
    }

	@Test
    public void givenValidValueItemWhenExecutingThenSuccess() {
	    Item item = new Item(ITEM_NAME, PRICE, VERSION, FB, TW);
	    item.setPublishedBy(user);
	    item.setPicture1Url(PICTURE_URL1);
	    item.setPicture2Url(PICTURE_URL2);
	    item.setPicture3Url(PICTURE_URL3);
	    item.setPicture4Url(PICTURE_URL4);
	    when(itemDao.findOne(ITEM_ID)).thenReturn(item);

	    assertEquals(ServiceResponse.SUCCESS, cmd.execute(request));
        verify(itemDao).save(item);
    }
}
