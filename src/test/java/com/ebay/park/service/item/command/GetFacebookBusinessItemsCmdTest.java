package com.ebay.park.service.item.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.entity.Category;
import com.ebay.park.db.entity.StatusDescription;
import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.item.dto.FacebookBusinessItem;
import com.ebay.park.util.TextUtils;

/**
 * Unit tests for {@link GetFacebookBusinessItemsCmd}
 * @author scalderon
 *
 */
public class GetFacebookBusinessItemsCmdTest {
	
	private static final Long ITEM_ID = 1L;
	private static final String ITEM_DESCRIPTION = "Description";
	private static final String ITEM_PICTURE_URL = "url";
	private static final String ITEM_NAME = "Name";
	private static final Double ITEM_PRICE = 20.0;
	private static final String CATEGORY_KEY = "category";
	private static final String ITEM_AVAILABILITY = "in stock";
	private static final String ITEM_CONDITION = "used";
	private static final String ITEM_BRAND = "N/A";
	private static final String ITEM_WEB_URL = "web-url";
	
	@Spy
	@InjectMocks
	private final GetFacebookBusinessItemsCmd cmd = new GetFacebookBusinessItemsCmd();
	
	@Mock
	private ItemDao itemDao;
	
	@Mock
	private TextUtils textUtils;
	
	@Before
	public void setUp() {
		initMocks(this);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void givenANullRequestWhenExecuteThenException() {
		cmd.execute(null);
	}
	
	@Test
	public void givenANullFacebookBusinessItemListWhenExecuteThenReturnAnEmptyList() {
		//given
		when(itemDao.findAllForFacebookBusinessByStatus(StatusDescription.ACTIVE)).thenReturn(null);
		ParkRequest request = new ParkRequest();
		
		//when
		List<FacebookBusinessItem> result = cmd.execute(request);
		
		//then
		assertNotNull(result);
		verify(itemDao).findAllForFacebookBusinessByStatus(StatusDescription.ACTIVE);
	}
	
	@Test
	public void givenAFacebookBusinessListWhenExecuteThenReturnAList() {
		//given
		ParkRequest request = new ParkRequest();
		Category category = new Category();
		category.setKey(CATEGORY_KEY);
			
		Object[] row = new Object[] {ITEM_ID, ITEM_DESCRIPTION, ITEM_PICTURE_URL, 
				category, ITEM_NAME, ITEM_PRICE};
		List<Object[]> facebookBusinessItems = new ArrayList<>();
		facebookBusinessItems.add(row);
		
		when(itemDao.findAllForFacebookBusinessByStatus(StatusDescription.ACTIVE)).thenReturn(facebookBusinessItems);
		when(textUtils.createItemSEOURL(CATEGORY_KEY, ITEM_NAME, ITEM_ID)).thenReturn(ITEM_WEB_URL);
		when(textUtils.doubleQuote(ITEM_WEB_URL)).thenReturn("\""+ITEM_WEB_URL+"\"");
		when(textUtils.doubleQuote(ITEM_PICTURE_URL)).thenReturn("\""+ITEM_PICTURE_URL+"\"");
		when(textUtils.capitalize(ITEM_DESCRIPTION)).thenReturn(ITEM_DESCRIPTION);
		when(textUtils.doubleQuote(ITEM_DESCRIPTION)).thenReturn("\""+ITEM_DESCRIPTION+"\"");
		when(textUtils.capitalize(ITEM_NAME)).thenReturn(ITEM_NAME);
		when(textUtils.doubleQuote(ITEM_NAME)).thenReturn("\""+ITEM_NAME+"\"");
		
		//when
		List<FacebookBusinessItem> result = cmd.execute(request);
		
		//then
		assertNotNull(result);
		assertEquals(result.get(0).getId(), ITEM_ID);
		assertEquals(result.get(0).getAvailability(), ITEM_AVAILABILITY);
		assertEquals(result.get(0).getCondition(), ITEM_CONDITION);
		assertEquals(result.get(0).getDescription(), "\""+ITEM_DESCRIPTION+"\"");
		assertEquals(result.get(0).getImageLink(), "\""+ITEM_PICTURE_URL+"\"");
		assertNotNull(result.get(0).getLink(), "\""+ITEM_WEB_URL+"\"");
		assertEquals(result.get(0).getTitle(), "\""+ITEM_NAME+"\"");
		assertEquals(result.get(0).getPrice(), ITEM_PRICE.toString() + " " + Currency.getInstance(Locale.US));
		assertEquals(result.get(0).getBrand(), ITEM_BRAND);
		verify(itemDao).findAllForFacebookBusinessByStatus(StatusDescription.ACTIVE);
		verify(textUtils).createItemSEOURL(CATEGORY_KEY, ITEM_NAME, ITEM_ID);
	}


}
