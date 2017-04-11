package com.ebay.park.service.blacklist.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import com.ebay.park.service.blacklist.dto.BlacklistedWord;
import com.ebay.park.service.moderation.dto.BlacklistedWordRequest;

public class AddBlackListedWordImplTest {

	private static final String WORD = "black_listed_word";

	@InjectMocks
	private AddBlacklistedWordImpl addBlacklistedWordImpl = new AddBlacklistedWordImpl();

	@Mock
	private BlackListDao blackListDao;

	@Mock
	private BlacklistedWordRequest request;

	@Before
	public void setUp() {
		initMocks(this);
	}

	@Test
	public void givenBlackListWordAlreadyExistsThenException() {
		BlackList word = Mockito.mock(BlackList.class);
		when(blackListDao.findByDescription(request.getWord())).thenReturn(word);
		try {
			addBlacklistedWordImpl.execute(request);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getCode(), ServiceExceptionCode.BLACKLIST_WORD_ALREADY_EXISTS.getCode());
		}
	}

	@Test
	public void givenNewBlackListWordThenAddBlackListedWordSuccess() {
		when(blackListDao.findByDescription(request.getWord())).thenReturn(null);
		when(request.getWord()).thenReturn(WORD);
		BlacklistedWord response = addBlacklistedWordImpl.execute(request);
		assertNotNull(response);
		assertEquals(WORD, response.getWord());
	}

}
