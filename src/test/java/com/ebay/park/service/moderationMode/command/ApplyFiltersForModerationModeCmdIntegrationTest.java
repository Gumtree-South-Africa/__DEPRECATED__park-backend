package com.ebay.park.service.moderationMode.command;

import com.ebay.park.config.ElasticsearchLocalConfig;
import com.ebay.park.db.entity.Category;
import com.ebay.park.db.entity.Item;
import com.ebay.park.elasticsearch.document.ItemDocument;
import com.ebay.park.service.moderation.command.AbstractSearchItemTest;
import com.ebay.park.service.moderationMode.ModerationCacheHelper;
import com.ebay.park.service.moderationMode.dto.ApplyFiltersForModerationModeRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import com.ebay.park.config.ElasticsearchLocalConfig;
import com.ebay.park.db.entity.Category;
import com.ebay.park.db.entity.Item;
import com.ebay.park.elasticsearch.document.ItemDocument;
import com.ebay.park.service.moderation.command.AbstractSearchItemTest;
import com.ebay.park.service.moderationMode.ModerationCacheHelper;
import com.ebay.park.service.moderationMode.dto.ApplyFiltersForModerationModeRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ElasticsearchLocalConfig.class})
public class
ApplyFiltersForModerationModeCmdIntegrationTest  extends AbstractSearchItemTest {

	private static final String TOKEN = "12345";
	private static final String LANG = "es";
	private static final long ITEM_ID1 = 1L;
	private static final long ITEM_ID2 = 2L;
	private static final long ITEM_ID3 = 3L;
	private static final long ITEM_ID4 = 4L;
	private static final String PUBLISHER = "John Doe";
	private static final String ITEM_DESCRIPTION = "description";
	private static final String ITEM_1 = "item1";
	private static final String ITEM_2 =  "item2";
	private static final String ITEM_3 =  "item3";
	private static final String ITEM_4 = "item4";
	private static final String ITEM_NAME = "name";

	private static final int PAGE_SIZE = 20;
	private Item item1;
	private Item item2;
	private Item item3;
	private Item item4;
	
	@Mock
	private ModerationCacheHelper moderationCacheHelper;
	
	@InjectMocks
	@Spy
	private  ApplyFiltersForModerationModeCmdImpl cmd;

	private Category category1;

	@Override
    @Before
	public void setUp(){
		super.setUp();
		ReflectionTestUtils.setField(cmd, "defaultPageSize", PAGE_SIZE);
	}
	
	@Test
	public void givenSkippedItemsWhenFilteringThenFilter() {
		List<Long> skippedItems = Arrays.asList(ITEM_ID1, ITEM_ID3);
		ApplyFiltersForModerationModeRequest request = new ApplyFiltersForModerationModeRequest(
				TOKEN, LANG, skippedItems);
		
		saveDocuments(new ItemDocument(itemDataSource.get(ITEM_1)),
    			new ItemDocument(itemDataSource.get(ITEM_2)),
    			new ItemDocument(itemDataSource.get(ITEM_3)),
    			new ItemDocument(itemDataSource.get(ITEM_4)));

	    SearchQuery searchQuery1 = cmd.createRequestQuery(request); 
	    List<ItemDocument> result = elasticsearchTemplate.queryForList(searchQuery1, ItemDocument.class);
	    assertEquals(1, result.size());
	}
	
	@Test
	public void givenCategoryWhenFilteringThenFilter() {
	    ApplyFiltersForModerationModeRequest request = new ApplyFiltersForModerationModeRequest(
                TOKEN, LANG, null);
	    request.setCategoryId(category1.getCategoryId());
        
        saveDocuments(new ItemDocument(itemDataSource.get(ITEM_1)),
                new ItemDocument(itemDataSource.get(ITEM_2)),
                new ItemDocument(itemDataSource.get(ITEM_3)),
                new ItemDocument(itemDataSource.get(ITEM_4)));

        SearchQuery searchQuery1 = cmd.createRequestQuery(request); 
        List<ItemDocument> result = elasticsearchTemplate.queryForList(searchQuery1, ItemDocument.class);
        assertEquals(3, result.size());
	}
	
	@Test
	public void givenUserNameWhenFilteringThenFilter() {
	    ApplyFiltersForModerationModeRequest request = new ApplyFiltersForModerationModeRequest(
                TOKEN, LANG, null);
	    request.setUserName(PUBLISHER);
        
        saveDocuments(new ItemDocument(itemDataSource.get(ITEM_1)),
                new ItemDocument(itemDataSource.get(ITEM_2)),
                new ItemDocument(itemDataSource.get(ITEM_3)),
                new ItemDocument(itemDataSource.get(ITEM_4)));

        SearchQuery searchQuery1 = cmd.createRequestQuery(request); 
        List<ItemDocument> result = elasticsearchTemplate.queryForList(searchQuery1, ItemDocument.class);
        assertEquals(3, result.size());
	}
	
	@Test
	public void givenDescriptionWhenFilteringThenFilter() {
	    ApplyFiltersForModerationModeRequest request = new ApplyFiltersForModerationModeRequest(
                TOKEN, LANG, null);
	    request.setDescription(ITEM_DESCRIPTION);
        
        saveDocuments(new ItemDocument(itemDataSource.get(ITEM_1)),
                new ItemDocument(itemDataSource.get(ITEM_2)),
                new ItemDocument(itemDataSource.get(ITEM_3)),
                new ItemDocument(itemDataSource.get(ITEM_4)));

        SearchQuery searchQuery1 = cmd.createRequestQuery(request); 
        List<ItemDocument> result = elasticsearchTemplate.queryForList(searchQuery1, ItemDocument.class);
        assertEquals(1, result.size());
	}
	
	@Test
	public void givenNameWhenFilteringThenFilter() {
	    ApplyFiltersForModerationModeRequest request = new ApplyFiltersForModerationModeRequest(
                TOKEN, LANG, null);
	    request.setName((ITEM_NAME));
        
        saveDocuments(new ItemDocument(itemDataSource.get(ITEM_1)),
                new ItemDocument(itemDataSource.get(ITEM_2)),
                new ItemDocument(itemDataSource.get(ITEM_3)),
                new ItemDocument(itemDataSource.get(ITEM_4)));

        SearchQuery searchQuery1 = cmd.createRequestQuery(request); 
        List<ItemDocument> result = elasticsearchTemplate.queryForList(searchQuery1, ItemDocument.class);
        assertEquals(3, result.size());
	}
	
	@Override
	protected void buildItemDataSource() {
		itemDataSource = new HashMap<>();
    	
    	category1 = new Category();
        category1.setWebColor("webColor");
        category1.setCategoryId(2222L);
        category1.setCatOrder(1);
        category1.setName("catName");
        category1.setSelectable(true);
        
        Category category2 = new Category();
        category2.setWebColor("webColor");
        category2.setCategoryId(2221L);
        category2.setCatOrder(2);
        category2.setName("catName2");
        category2.setSelectable(true);
   	
        item1 = createItem(new Item(ITEM_1, Double.valueOf("12.15"), "version", false, false), category1, createUser(PUBLISHER), ITEM_DESCRIPTION);
        item2 = createItem(new Item(ITEM_2, Double.valueOf("12.15"), "version", false, false), category2, createUser(PUBLISHER), ITEM_DESCRIPTION);
        item3 = createItem(new Item(ITEM_3, Double.valueOf("12.15"), "version", false, false), category1, createUser(PUBLISHER), null);
        item4 = createItem(new Item(ITEM_4, Double.valueOf("12.15"), "version", false, false), category1, createUser(PUBLISHER), null);
        
        item1.setPendingModeration(true);
        item2.setPendingModeration(false);
        item3.setPendingModeration(true);
        item4.setPendingModeration(true);
        
        item1.setId(ITEM_ID1);
        item2.setId(ITEM_ID2);
        item3.setId(ITEM_ID3);
        item4.setId(ITEM_ID4);
        
        item1.setName(ITEM_NAME);
        item2.setName(ITEM_NAME);
        item3.setName(ITEM_NAME);
        item4.setName(ITEM_NAME);
        
    	itemDataSource.put(ITEM_1, item1);
    	itemDataSource.put(ITEM_2, item2);
    	itemDataSource.put(ITEM_3, item3);
        itemDataSource.put(ITEM_4, item4);
	}
}
