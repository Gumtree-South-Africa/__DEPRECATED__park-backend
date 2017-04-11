package com.ebay.park.service.blacklist.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ebay.park.db.dao.BlackListDao;
import com.ebay.park.db.entity.BlackList;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderation.dto.BlacklistedWordRequest;

public class UpdateBlacklistedWordImplTest {

	@InjectMocks
	private UpdateBlacklistedWordImpl updateBlacklistedWordImpl = new UpdateBlacklistedWordImpl();

	@Mock
	private BlackListDao blackListDao;

	@Mock
	private BlacklistedWordRequest request;

	@Before
	public void setUp() {
		initMocks(this);
	}

	@Test
	public void givenNullBlackListWordThenException() {
		when(blackListDao.findOne(request.getId())).thenReturn(null);
		try {
			updateBlacklistedWordImpl.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.BLACKLIST_WORD_NOT_FOUND.getCode());
		}

	}

	@Test
	public void givenBlackListWordAlreadyExistsThenException() {
		BlackList word = Mockito.mock(BlackList.class);
		when(blackListDao.findOne(request.getId())).thenReturn(word);
		when(blackListDao.findByDescription(request.getWord())).thenReturn(word);
		try {
			updateBlacklistedWordImpl.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.BLACKLIST_WORD_ALREADY_EXISTS.getCode());
		}

	}

	@Test
	public void givenBlackListWordThenUpdateWordSuccess() {
		BlackList word = Mockito.mock(BlackList.class);
		when(blackListDao.findOne(request.getId())).thenReturn(word);
		when(blackListDao.findByDescription(request.getWord())).thenReturn(null);
		ServiceResponse response = updateBlacklistedWordImpl.execute(request);
		assertEquals(ServiceResponse.SUCCESS.getStatusCode(), response.getStatusCode());
	}

}
