package com.ebay.park.service.moderation.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.ebay.park.config.ElasticsearchLocalConfig;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Category;
import com.ebay.park.db.entity.Item;
import com.ebay.park.elasticsearch.document.ItemDocument;
import com.ebay.park.service.moderation.dto.SearchItemForModerationRequest;

/**
 * Integration test for {@link SearchItemForModerationCmdImpl}.
 * @author Julieta Salvadó
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ElasticsearchLocalConfig.class})
public class SearchItemForModerationCmdImplIT extends AbstractSearchItemTest {

    private static final int PAGE_SIZE = 10;
    private static final int PAGE = 0;

    private static final String ITEM_1_FOR_PERFECT_MATCH = "perfect matching";
    private static final String ITEM_2 = "Zamzun Fone";
    private static final String ITEM_1_DESCRIPTION_PERFECT_MATCH = "perfect description";
    private static final String ITEM_2_DESCRIPTION = "another description";
    private static final String ITEM_3_FOR_FUZZY = "Zamzun Fone";
    private static final String ITEM_4_FOR_FUZZY = "Pley Esteision";
    private static final String PUBLISHER_1_FOR_FUZZY = "Publisher for fuzzy";
    private static final String PUBLISHER_2_FOR_FUZZY = "John Smith";
    private static final String PUBLISHER_3_FOR_NGRAM = "desmond";
    private static final String PUBLISHER_JOHN_SMITH = "John Smith";
    private static final String ITEM_5_FOR_SHINGLES = "Sue Ate The Alligator";
    private static final String ITEM_6_FOR_SHINGLES = "The alligator ate Sue";
    private static final String ITEM_7_FOR_SHINGLES = "Sue never goes anywhere without her alligator";
    private static final String ITEM_8_FOR_SHINGLES = "the test";
    private static final String ITEM_9_FOR_NGRAM = "old tin set can";
    private static final String ITEM_10_FOR_NGRAM = "Monedas de Pelicula";
    private static final String ITEM_11_FOR_NGRAM = "botines";

    private static final String PUBLISHER_PERFECT_MATCH = "Anthony";
    private static final String PUBLISHER = "Johny";

    private static final String ITEM_SEARCH_CRITERIA = "perfect matching";
    private static final String ITEM_DESCRIPTION_CRITERIA_1 = "perfect description";
    private static final String PUBLISHER_SEARCH_CRITERIA_1 = "Anthony";
    private static final String PUBLISHER_SEARCH_CRITERIA_2 = "anthony";
    private static final String ITEM_SEARCH_CRITERIA_FUZZY_SUBSTITUTION = "Zamzun Pone";
    private static final String ITEM_SEARCH_CRITERIA_FUZZY_INSERTION = "Zamzun Fhone";
    private static final String ITEM_SEARCH_CRITERIA_FUZZY_DELETION = "Zamzu Fone";
    private static final String ITEM_SEARCH_CRITERIA_FUZZY_TRANSPOSITION = "Zamzun Foen";
    private static final String ITEM_SEARCH_CRITERIA_SHINGLES = "The Hungry Alligator Ate Sue";
    private static final String ITEM_SEARCH_CRITERIA_NGRAM = "botin";
    private static final String PUBLISHER_SEARCH_CRITERIA_NGRAM = "desmo";
    private static final String PUBLISHER_SEARCH_CRITERIA_FUZZY_SUBSTITUTION = "Publisher for fuzzi";
    private static final String PUBLISHER_SEARCH_CRITERIA_FUZZY_INSERTION = "Publisher for futzzy";
    private static final String PUBLISHER_SEARCH_CRITERIA_FUZZY_DELETION = "Publisher for fuzy";
    private static final String PUBLISHER_SEARCH_CRITERIA_FUZZY_TRANSPOSITION = "Publisher for fuzyz";
    @InjectMocks
    SearchItemForModerationCmdImpl cmd = new SearchItemForModerationCmdImpl();

    @Mock
    private UserDao userDao;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        cmd.fuzziness = "1";
        cmd.prefixLength = 3;
        cmd.maxExpansions = 500;
        cmd.fuzzyBooster = 1f;
        cmd.fuzzyTranspositions = true;
        cmd.fuzzyMinimumMatch = "50%";
        cmd.shinglesBooster = 4f;
        cmd.ngramMinimumMatch = "70%";
        cmd.ngramBooster = 3f;
    }

    /**
     * Testing perfect match
     */
    @Test
    public void testPerfectMatch() {
        List<ItemDocument> result;

        saveDocuments(new ItemDocument(itemDataSource.get(ITEM_1_FOR_PERFECT_MATCH)),
                new ItemDocument(itemDataSource.get(ITEM_2)));

        //group name
        SearchItemForModerationRequest request1 = new SearchItemForModerationRequest(PAGE, PAGE_SIZE);
        request1.setName(ITEM_SEARCH_CRITERIA);
        request1.setExactMatch(true);
        SearchQuery searchQuery1 = cmd.createRequestQuery(request1);
        result = elasticsearchTemplate.queryForList(searchQuery1, ItemDocument.class);
        assertEquals(1, result.size());

        //creator name
        SearchItemForModerationRequest request2 = new SearchItemForModerationRequest(PAGE, PAGE_SIZE);
        request2.setUsername(PUBLISHER_SEARCH_CRITERIA_1);
        request2.setExactMatch(true);
        SearchQuery searchQuery2 = cmd.createRequestQuery(request2);
        result = elasticsearchTemplate.queryForList(searchQuery2, ItemDocument.class);
        assertEquals(1, result.size());

        SearchItemForModerationRequest request3 = new SearchItemForModerationRequest(PAGE, PAGE_SIZE);
        request3.setUsername(PUBLISHER_SEARCH_CRITERIA_2);
        request3.setExactMatch(true);
        SearchQuery searchQuery3 = cmd.createRequestQuery(request3);
        result = elasticsearchTemplate.queryForList(searchQuery3, ItemDocument.class);
        assertEquals(1, result.size());

        //description
        SearchItemForModerationRequest request4 = new SearchItemForModerationRequest(PAGE, PAGE_SIZE);
        request4.setDescription(ITEM_DESCRIPTION_CRITERIA_1);
        request4.setExactMatch(true);
        SearchQuery searchQuery4 = cmd.createRequestQuery(request4);
        result = elasticsearchTemplate.queryForList(searchQuery4, ItemDocument.class);
        assertEquals(1, result.size());
    }

    /**
     * Testing fuzziness
     * Typos and Misspelings
     */
    @Test
    public void testFuzziness() {
        /**
         * Saving "Zamzun Fone" and "Pley Esteision".
         */
        List<ItemDocument> result;
        saveDocuments(new ItemDocument(itemDataSource.get(ITEM_3_FOR_FUZZY)),
                new ItemDocument(itemDataSource.get(ITEM_4_FOR_FUZZY)));

        //Searching for item name "Zamzun Pone"
        SearchItemForModerationRequest request1 = new SearchItemForModerationRequest(PAGE, PAGE_SIZE);
        request1.setName(ITEM_SEARCH_CRITERIA_FUZZY_SUBSTITUTION);
        request1.setExactMatch(true);
        SearchQuery searchQuery = cmd.createRequestQuery(request1);
        result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);
        assertEquals(1, result.size());

        //Searching for item name "Zamzun Fhone"
        SearchItemForModerationRequest request2 = new SearchItemForModerationRequest(PAGE, PAGE_SIZE);
        request2.setName(ITEM_SEARCH_CRITERIA_FUZZY_INSERTION);
        request2.setExactMatch(true);
        searchQuery = cmd.createRequestQuery(request2);
        result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);
        assertEquals(1, result.size());

        //Searching for item name "Zamzu Fone"
        SearchItemForModerationRequest request3 = new SearchItemForModerationRequest(PAGE, PAGE_SIZE);
        request3.setName(ITEM_SEARCH_CRITERIA_FUZZY_DELETION);
        request3.setExactMatch(true);
        searchQuery = cmd.createRequestQuery(request3);
        result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);
        assertEquals(1, result.size());

        //Searching for item name "Zamzun Foen"
        SearchItemForModerationRequest request4 = new SearchItemForModerationRequest(PAGE, PAGE_SIZE);
        request4.setName(ITEM_SEARCH_CRITERIA_FUZZY_TRANSPOSITION);
        request4.setExactMatch(true);
        searchQuery = cmd.createRequestQuery(request4);
        result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);
        assertEquals(1, result.size());

        //Publisher Username
        SearchItemForModerationRequest request5 = new SearchItemForModerationRequest(PAGE, PAGE_SIZE);
        request5.setUsername(PUBLISHER_SEARCH_CRITERIA_FUZZY_SUBSTITUTION);
        request5.setExactMatch(true);
        searchQuery = cmd.createRequestQuery(request5);
        result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);
        assertEquals(1, result.size());

        SearchItemForModerationRequest request6 = new SearchItemForModerationRequest(PAGE, PAGE_SIZE);
        request6.setUsername(PUBLISHER_SEARCH_CRITERIA_FUZZY_INSERTION);
        request6.setExactMatch(true);
        searchQuery = cmd.createRequestQuery(request6);
        result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);
        assertEquals(1, result.size());

        SearchItemForModerationRequest request7 = new SearchItemForModerationRequest(PAGE, PAGE_SIZE);
        request7.setUsername(PUBLISHER_SEARCH_CRITERIA_FUZZY_DELETION);
        request7.setExactMatch(true);
        searchQuery = cmd.createRequestQuery(request7);
        result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);
        assertEquals(1, result.size());

        SearchItemForModerationRequest request8 = new SearchItemForModerationRequest(PAGE, PAGE_SIZE);
        request8.setUsername(PUBLISHER_SEARCH_CRITERIA_FUZZY_TRANSPOSITION);
        request8.setExactMatch(true);
        searchQuery = cmd.createRequestQuery(request8);
        result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);
        assertEquals(1, result.size());
    }

    /**
     * Testing Shingles. Find Associated Words in Phrase
     * Saving "Sue Ate The Alligator", "The alligator ate Sue", "Sue never goes anywhere without her alligator" and "the test".
     * Searching for "The Hungry Alligator Ate Sue"
     */
    @Test
    public void givenShinglesMatchingAndZeroMatchingTermsOnItemNameWhenRequestingThenReturnPartiallyMatchingTerm() {
        List<ItemDocument> result;
        saveDocuments(new ItemDocument(itemDataSource.get(ITEM_5_FOR_SHINGLES)),
                new ItemDocument(itemDataSource.get(ITEM_6_FOR_SHINGLES)),
                new ItemDocument(itemDataSource.get(ITEM_7_FOR_SHINGLES)),
                new ItemDocument(itemDataSource.get(ITEM_8_FOR_SHINGLES)));

        SearchItemForModerationRequest request = new SearchItemForModerationRequest(PAGE, PAGE_SIZE);
        request.setName(ITEM_SEARCH_CRITERIA_SHINGLES);
        request.setExactMatch(true);
        SearchQuery searchQuery = cmd.createRequestQuery(request);
        result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);
        assertThat("Results: " + result.toString() + " expecting three shingles matching on name", result.size(), is(3));
    }

    /**
     * Saving item names "old tin set can", "Monedas de Pelicula" and "botines", and searching for "botin" on item name.
     */
    @Test
    public void givenPartiallyMatchingAndZeroMatchingTermsOnItemNameWhenRequestingThenReturnPartiallyMatchingTerm() {
        List<ItemDocument> result;

        saveDocuments(new ItemDocument(itemDataSource.get(ITEM_9_FOR_NGRAM)),
                new ItemDocument(itemDataSource.get(ITEM_10_FOR_NGRAM)),
                new ItemDocument(itemDataSource.get(ITEM_11_FOR_NGRAM)));

        SearchItemForModerationRequest request1 = new SearchItemForModerationRequest(PAGE, PAGE_SIZE);
        request1.setName(ITEM_SEARCH_CRITERIA_NGRAM);
        request1.setExactMatch(true);
        SearchQuery searchQuery = cmd.createRequestQuery(request1);
        result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);
        assertThat("Results: " + result.toString() + " expecting two partial matching on name", result.size(), is(2));
    }

    /**
     * Saving item names "old tin set can", "Monedas de Pelicula" and "botines" with publishers
     * "John Smith" and "desmond". Searching for "desmo" on publisher
     */
    @Test
    @Ignore
    public void givenPartiallyMatchingAndZeroMatchingTermsOnPublisherNameWhenRequestingThenReturnPartiallyMatchingTerm() {
        //Partial matching is not implemented on publisher name, only perfect matching is allow
        saveDocuments(new ItemDocument(itemDataSource.get(ITEM_9_FOR_NGRAM)),
                new ItemDocument(itemDataSource.get(ITEM_10_FOR_NGRAM)),
                new ItemDocument(itemDataSource.get(ITEM_11_FOR_NGRAM)));

        SearchItemForModerationRequest request2 = new SearchItemForModerationRequest(PAGE, PAGE_SIZE);
        request2.setUsername(PUBLISHER_SEARCH_CRITERIA_NGRAM);
        request2.setExactMatch(true);
        SearchQuery searchQuery = cmd.createRequestQuery(request2);
        List<ItemDocument> result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);
        assertThat("Results: " + result.toString() + " expecting one partial matching on publisher", result.size(), is(2));
    }

    @Override
    protected void buildItemDataSource() {
        itemDataSource = new HashMap<>();

        Category category = new Category();
        category.setWebColor("webColor");
        category.setCategoryId(2222L);
        category.setCatOrder(1);
        category.setName("catName");
        category.setSelectable(true);

        itemDataSource.put(ITEM_1_FOR_PERFECT_MATCH, createItem(new Item(ITEM_1_FOR_PERFECT_MATCH, Double.valueOf("12.15"), "version", false, false), category, createUser(PUBLISHER_PERFECT_MATCH), ITEM_1_DESCRIPTION_PERFECT_MATCH));
        itemDataSource.put(ITEM_2, createItem(new Item(ITEM_2, Double.valueOf("12.15"), "version", false, false), category, createUser(PUBLISHER), ITEM_2_DESCRIPTION));
        itemDataSource.put(ITEM_3_FOR_FUZZY, createItem(new Item(ITEM_3_FOR_FUZZY, Double.valueOf("12.15"), "version", false, false), category, createUser(PUBLISHER_1_FOR_FUZZY), null));
        itemDataSource.put(ITEM_4_FOR_FUZZY, createItem(new Item(ITEM_4_FOR_FUZZY, Double.valueOf("12.15"), "version", false, false), category, createUser(PUBLISHER_2_FOR_FUZZY), null));
        itemDataSource.put(ITEM_5_FOR_SHINGLES, createItem(new Item(ITEM_5_FOR_SHINGLES, Double.valueOf("12.15"), "version", false, false), category, createUser(PUBLISHER_JOHN_SMITH), null));
        itemDataSource.put(ITEM_6_FOR_SHINGLES, createItem(new Item(ITEM_6_FOR_SHINGLES, Double.valueOf("12.15"), "version", false, false), category, createUser(PUBLISHER_JOHN_SMITH), null));
        itemDataSource.put(ITEM_7_FOR_SHINGLES, createItem(new Item(ITEM_7_FOR_SHINGLES, Double.valueOf("12.15"), "version", false, false), category, createUser(PUBLISHER_JOHN_SMITH), null));
        itemDataSource.put(ITEM_8_FOR_SHINGLES, createItem(new Item(ITEM_8_FOR_SHINGLES, Double.valueOf("12.15"), "version", false, false), category, createUser(PUBLISHER_JOHN_SMITH), null));
        itemDataSource.put(ITEM_9_FOR_NGRAM, createItem(new Item(ITEM_9_FOR_NGRAM, Double.valueOf("12.15"), "version", false, false), category, createUser(PUBLISHER_JOHN_SMITH), null));
        itemDataSource.put(ITEM_10_FOR_NGRAM, createItem(new Item(ITEM_10_FOR_NGRAM, Double.valueOf("12.15"), "version", false, false), category, createUser(PUBLISHER_JOHN_SMITH), null));
        itemDataSource.put(ITEM_11_FOR_NGRAM, createItem(new Item(ITEM_11_FOR_NGRAM, Double.valueOf("12.15"), "version", false, false), category, createUser(PUBLISHER_3_FOR_NGRAM), null));
    }
}
