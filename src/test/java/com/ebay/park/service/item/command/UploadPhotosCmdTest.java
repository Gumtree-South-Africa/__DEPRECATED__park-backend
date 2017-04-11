package com.ebay.park.service.item.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Conversation;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.db.entity.User;
import com.ebay.park.eps.EPSClient;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.blacklist.BlacklistService;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.UploadPhotosRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.EPSUtils;

public class UploadPhotosCmdTest {

	private static final Long ITEM_ID = 1l;
	private static final String ITEM_NAME = "item_name";
	private static final String VERSION_PUBLISH = "versionPublish";
	private static final Double ITEM_PRICE = 20.3;
	private static final Long USER_ID = 2l;
	private static final String TOKEN = "user-token";
	private static final String USER_NAME = "name";
	private static final int MAX_AMOUNT_PICTURES = 4;
	private static final String MSG = "An exception was expected";

	@InjectMocks
	@Spy
	private final UploadPhotosCmd cmd = new UploadPhotosCmd();

	@Mock
	private ItemDao itemDao;

	@Mock
	private EPSUtils EPSUtils;

	@Mock
	private ItemUtils itemUtils;

	@Mock
	private UserDao userDao;

	@Mock
	private User user;

	private Item item;

	@Mock
	private MultipartFile photoFile;

	@Mock
	private UploadPhotosRequest request;

	@Mock
	private BlacklistService blackListService;

	@Mock
	private SessionService sessionService;

	@Mock
	private UserSessionCache userSession;

	@Mock
	EPSClient epsClient;

	@Before
	public void setUp() {
		initMocks(this);
		item = new Item(ITEM_NAME, ITEM_PRICE, VERSION_PUBLISH, Boolean.FALSE, Boolean.FALSE);
		item.setPublishedBy(user);
		request.setPhoto1(photoFile);
		request.setPhoto2(photoFile);
		ReflectionTestUtils.setField(cmd, "maxAmountPictures", MAX_AMOUNT_PICTURES);
		ReflectionTestUtils.setField(itemUtils, "itemDao", itemDao);
		when(request.getItemId()).thenReturn(ITEM_ID);
		when(request.getToken()).thenReturn(TOKEN);
		when(userDao.findByToken(TOKEN)).thenReturn(user);
		when(user.getUsername()).thenReturn(USER_NAME);
		when(itemDao.findOne(request.getItemId())).thenReturn(item);
		when(user.getUserId()).thenReturn(USER_ID);
		when(sessionService.getUserSession(TOKEN)).thenReturn(userSession);
		when(userSession.getUserId()).thenReturn(USER_ID);
	}

	/**
	 * Upload photo
	 * 
	 * @result The status of the item is IMAGE_PENDING, so if the photos are
	 *         uploaded correctly, it will change to ACTIVE status and it can be
	 *         seen by moderators.
	 */
	@Test
	public void givenItemWithImagePendingStatusWhenExecutesThenChangeStatusToActive() {
		when(itemUtils.activateItem(item)).thenCallRealMethod();
		when(itemDao.save(item)).thenReturn(item);
		ServiceResponse response = cmd.execute(request);
		assertEquals(ServiceResponse.SUCCESS.getStatusCode(), response.getStatusCode());
		assertEquals(item.is(StatusDescription.ACTIVE), Boolean.TRUE);
	}

}
