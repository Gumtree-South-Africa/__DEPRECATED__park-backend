package com.ebay.park.service.profile.command;

import com.ebay.park.db.dao.FollowerDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.profile.dto.UserInfoRequest;
import com.ebay.park.service.profile.dto.UserInfoResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static com.ebay.park.service.ServiceException.createServiceException;
import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class UpdateUserInfoCmdTest {

	private static final String USERNAME = "mike_w";
	private static final Long KEY_USER = 12345l;
	private static final Double POS_LAT = -33.234d;
	private static final Double POS_LONG = 10.4321d;
	private static final String UPDATED_POSITION = "37.3160648,-59.1356282";
	private static final String MOBILE = "555-5555";
	private static final String UPDATED_MOBILE = "888-8888";
	private static final String ZIP_CODE = "90210";
	private static final String UPDATED_ZIP_CODE = "33333";
	private static final String EMAIL = "m.wazowski@monster.inc";
	private static final String PICTURE = "no picture";
	private static final String UPDATED = "UPDATED_";

	@InjectMocks
	private UpdateUserInfoCmd updateUserInfoCmd;

	@Mock
	private UserDao userDao;

	@Mock
	private FollowerDao followerDao;

	@Before
	public void setUp() {
		updateUserInfoCmd = new UpdateUserInfoCmd();
		initMocks(this);
	}

	protected String getUpdatedValue(String value) {
		return UPDATED + value;
	}

	@Test
	public void executeTest() {
		User user = createUser();
		Mockito.when(userDao.findByUsername(USERNAME)).thenReturn(user);
		Mockito.when(userDao.save(user)).thenReturn(user);
		UserInfoRequest request = createValidInfoUpdateRequest();
		UserInfoResponse response = updateUserInfoCmd.execute(request);
		
		assertNotNull(response);
		assertEquals(response.getLocation(), UPDATED_POSITION);
		assertEquals(response.getMobile(), UPDATED_MOBILE);
		assertEquals(response.getPicture(), getUpdatedValue(PICTURE));
		assertEquals(response.getZipCode(), UPDATED_ZIP_CODE);

		Mockito.verify(userDao, Mockito.times(1)).findByUsername(USERNAME);
		Mockito.verify(userDao, Mockito.times(1)).save(user);
	}

	@Test
	public void executeInvalidUserInfoUpdateReq() {
		User user = createUser();
		Mockito.when(userDao.findByUsername(USERNAME)).thenReturn(user);
		ServiceException exception = createServiceException(ServiceExceptionCode.INVALID_USERINFO_UPDATE_REQ);
		Mockito.when(userDao.save(user)).thenThrow(exception);
		UserInfoRequest request = createValidInfoUpdateRequest();
		UserInfoResponse response = null;
		try{
			response = updateUserInfoCmd.execute(request);
		} catch(ServiceException se){
			assertNull(response);
			assertEquals(ServiceExceptionCode.INVALID_USERINFO_UPDATE_REQ.getCode(), se.getCode());
		}

	}

	@Test
	public void executeNoUserTest() {
		String invalidUser = "INVALID" + USERNAME;
		Mockito.when(userDao.findByUsername(invalidUser)).thenReturn(null);
		UserInfoResponse response = null;
		UserInfoRequest request = new UserInfoRequest();
		request.setUsername(invalidUser);
		try {
			response = updateUserInfoCmd.execute(request);
		} catch (ServiceException se) {
			assertNull(response);
			assertEquals(ServiceExceptionCode.USER_NOT_FOUND.getCode(), se.getCode());
			Mockito.verify(userDao, Mockito.times(1)).findByUsername(invalidUser);
		}
	}

	private UserInfoRequest createValidInfoUpdateRequest() {
		UserInfoRequest request = new UserInfoRequest();
		request.setUsername(USERNAME);
		request.setPicture(getUpdatedValue(PICTURE));
		request.setLocation(UPDATED_POSITION);
		request.setMobile(UPDATED_MOBILE);
		request.setZipCode(UPDATED_ZIP_CODE);
		return request;
	}

	private User createUser() {
		User user = new User();
		user.setUserId(KEY_USER);
		user.setUsername(USERNAME);
		user.setEmail(EMAIL);
		user.setLatitude(POS_LAT);
		user.setLatitude(POS_LONG);
		user.setPicture(PICTURE);
		user.setMobile(MOBILE);
		user.setZipCode(ZIP_CODE);
		return user;
	}
}
