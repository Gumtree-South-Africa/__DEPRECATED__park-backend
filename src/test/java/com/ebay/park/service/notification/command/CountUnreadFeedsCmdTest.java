package com.ebay.park.service.notification.command;

import com.ebay.park.db.dao.FeedDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CountUnreadFeedsCmdTest {

	private static final String TOKEN = "token";

	@InjectMocks
	private CountUnreadFeedsCmd cmd;

	@Mock
	private UserDao userDao;

	@Mock
	private FeedDao feedDao;

	@Mock
	private User user;

	@Before
	public void setUp() {
		cmd = new CountUnreadFeedsCmd();
		initMocks(this);
	}

	@Test
	public void test() {
		when(userDao.findByToken(TOKEN)).thenReturn(user);
		when(feedDao.countByOwnerAndRead(user, false)).thenReturn(2l);
		Long execute = cmd.execute(TOKEN);
		assertEquals(new Long(2l), execute);
	}

	@Test
	public void shouldFailOnMissingUser() {
		try {
			cmd.execute("invaliduser");
			fail();
		} catch (ServiceException e) {
			assertEquals(ServiceExceptionCode.USER_NOT_FOUND.getCode(),
					e.getCode());
		}
	}

}
