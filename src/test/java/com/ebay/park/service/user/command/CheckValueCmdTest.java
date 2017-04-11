package com.ebay.park.service.user.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.user.dto.CheckValueRequest;
import com.ebay.park.service.user.dto.CheckValueResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * 
 * @author lucia.masola
 * 
 */
public class CheckValueCmdTest {

	@InjectMocks
	private CheckValueCmd checkValueCmd;

	@Mock
	private UserDao userDao;

	private static final String VALID_VALUE = "validEmail@gmail.com";
	private static final String EMAIL_FIELD = "email";
	private static final String NAME_FIELD = "username";

	@Before
	public void setUp() {
		checkValueCmd = new CheckValueCmd();
		initMocks(this);
	}

	@Test
	public void testExecuteCheckValueEmailSuccessfully() throws Exception {

		CheckValueRequest checkValueRequest = new CheckValueRequest(EMAIL_FIELD, VALID_VALUE);

		when(userDao.findByEmail(EMAIL_FIELD)).thenReturn(new User());

		CheckValueResponse checkValueResponse = checkValueCmd.execute(checkValueRequest);

		assertNotNull("The response was not expected to be null. ", checkValueResponse);
		assertTrue(checkValueResponse.isAvailable());

		verify(userDao, times(1)).findByEmail(VALID_VALUE);

	}

	@Test
	public void testExecuteCheckValueUsernameSuccessfully() throws Exception {

		CheckValueRequest checkValueRequest = new CheckValueRequest(NAME_FIELD, VALID_VALUE);

		when(userDao.findByUsername(NAME_FIELD)).thenReturn(new User());

		CheckValueResponse checkValueResponse = checkValueCmd.execute(checkValueRequest);

		assertNotNull("The response was not expected to be null. ", checkValueResponse);
		assertTrue(checkValueResponse.isAvailable());

		verify(userDao, times(1)).findByUsername(VALID_VALUE);

	}

}
