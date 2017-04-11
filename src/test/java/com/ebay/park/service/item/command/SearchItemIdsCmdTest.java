package com.ebay.park.service.item.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.elasticsearch.document.ItemDocument;
import com.ebay.park.elasticsearch.document.converter.DocumentConverter;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.item.dto.SearchItemIdsResponse;
import com.ebay.park.service.item.dto.SearchItemRequest;
import com.ebay.park.util.InternationalizationUtil;

/**
 * Unit test for {@link SearchItemIdsCmd}
 * @author scalderon
 *
 */
public class SearchItemIdsCmdTest {
	
	private static final int EMPTY_SIZE_RESULT = 0;
	private static final Long ITEM_ID = 1L;
	private static final String TOKEN = "token";
	private static final String LANG = "es";
	
	@InjectMocks
	private SearchItemIdsCmd cmd = new SearchItemIdsCmd();
	
	@Mock
	private DocumentConverter documentConverter;
	
	@Mock
	private SearchItemCmdHelper searchCmdHelper;
	
	@Mock
	private InternationalizationUtil i18nUtil;
	
	@Mock
	private SearchItemRequest request;
	
	@Mock
    private Page<ItemDocument> page;
	
	@Mock
	private UserDao userDao;
	
	@Mock
	private User user;

	@Before
	public void setUp() {
		initMocks(this);
	}

	@Test(expected=IllegalArgumentException.class)
	public void givenANullRequestWhenExecuteThenIllegalArgumentException() {
		cmd.execute(null);
	}
	
	@Test(expected=ServiceException.class)
	public void givenANotValidTokenWhenExecuteThenServiceExceptionCode() {
		when(request.getToken()).thenReturn(TOKEN);
		when(userDao.findByToken(TOKEN)).thenReturn(null);
		cmd.execute(request);
	}
	
	@Test
	public void givenANullResultWhenExecuteThenEmptyResponse() {
		//given
		when(request.getToken()).thenReturn(TOKEN);
		when(userDao.findByToken(TOKEN)).thenReturn(user);
		when(searchCmdHelper.search(request)).thenReturn(null);
		when(request.getLanguage()).thenReturn(LANG);
		
		//when
		SearchItemIdsResponse response = cmd.execute(request);
		
		//then
		assertNotNull(response);
		assertEquals(response.getItemIds().size(), EMPTY_SIZE_RESULT);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void givenAnExceptionWhenExecuteThenEmptyResponse() {
		//given
		when(request.getToken()).thenReturn(TOKEN);
		when(userDao.findByToken(TOKEN)).thenReturn(user);
		when(searchCmdHelper.search(request)).thenThrow(Exception.class);
		when(request.getLanguage()).thenReturn(LANG);
		
		//when
		SearchItemIdsResponse response = cmd.execute(request);
		
		//then
		assertNotNull(response);
		assertEquals(response.getItemIds().size(), EMPTY_SIZE_RESULT);
	}
	
	@Test
	public void givenANullContentWhenExecuteThenSuccess() {
		//given
		when(request.getToken()).thenReturn(TOKEN);
		when(userDao.findByToken(TOKEN)).thenReturn(user);
		when(searchCmdHelper.search(request)).thenReturn(page);
		when(page.getContent()).thenReturn(null);
		when(request.getLanguage()).thenReturn(LANG);
		
		//when
		SearchItemIdsResponse response = cmd.execute(request);
		
		//then
		assertNotNull(response);
		assertEquals(response.getItemIds().size(), EMPTY_SIZE_RESULT);
	}
	
	@Test
	public void givenAValidSearchWhenExecuteThenSuccess() {
		//given
		ItemDocument itemDocument = new ItemDocument();
		itemDocument.setItemId(ITEM_ID);
		
		List<ItemDocument> result = new ArrayList<>();
		result.add(itemDocument);
		
		List<Long> expectedItemIds = new ArrayList<>();
		expectedItemIds.add(ITEM_ID);

		when(request.getToken()).thenReturn(TOKEN);
		when(userDao.findByToken(TOKEN)).thenReturn(user);
		when(searchCmdHelper.search(request)).thenReturn(page);
		when(page.getContent()).thenReturn(result);
		when(documentConverter.itemIdsfromItemDocument(result)).thenReturn(expectedItemIds);
		
		//when
		SearchItemIdsResponse response = cmd.execute(request);
				
		//then
		assertNotNull(response);
		assertEquals(response.getItemIds().size(), expectedItemIds.size());
		assertEquals(ITEM_ID, expectedItemIds.get(0));
	}
	
}
