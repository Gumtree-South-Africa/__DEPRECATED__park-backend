package com.ebay.park.service.item.command;

import com.ebay.park.config.ElasticsearchLocalConfig;
import com.ebay.park.db.entity.Category;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.elasticsearch.document.ItemDocument;
import com.ebay.park.elasticsearch.document.converter.DocumentConverter;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.SearchItemRequest;
import com.ebay.park.service.item.dto.SearchItemRequest.SearchItemRequestBuilder;
import com.ebay.park.service.moderation.command.AbstractSearchItemTest;
import com.ebay.park.util.InternationalizationUtil;
import com.ebay.park.util.ParkConstants;
import com.ebay.park.util.TextUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Integration test for {@link SearchItemCmdImpl}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ElasticsearchLocalConfig.class})
public class SearchItemHelperIT extends AbstractSearchItemTest {

    private static final int PAGE_SIZE                                        = 10;
    private static final int PAGE                                             = 0;
    private static final String ITEM_1_FOR_FUZZY                              = "Zamzun Fone";
    private static final String ITEM_2_FOR_FUZZY                              = "Pley Esteision";
    private static final String PUBLISHER_1_FOR_FUZZY                         = "Publisher for fuzzy";
    private static final String PUBLISHER_2_FOR_FUZZY                         = "John Smith";
    private static final String PUBLISHER_3_FOR_NGRAM                         = "desmond";
    private static final String PUBLISHER_JOHN_SMITH                          = "John Smith";
    private static final String ITEM_3_FOR_SHINGLES                           = "Sue Ate The Alligator";
    private static final String ITEM_4_FOR_SHINGLES                           = "The alligator ate Sue";
    private static final String ITEM_5_FOR_SHINGLES                           = "Sue never goes anywhere without her alligator";
    private static final String ITEM_6_FOR_SHINGLES                           = "the test";
    private static final String ITEM_7_FOR_NGRAM                              = "old tin set can";
    private static final String ITEM_8_FOR_NGRAM                              = "Monedas de Pelicula";
    private static final String ITEM_9_FOR_NGRAM                              = "botines";
    private static final String ITEM_10_FOR_NORMAL                            = "Normal search";
    private static final String ITEM_11_FOR_NORMAL                            = "Normal search 2";
    private static final String ITEM_12_FOR_NORMAL                            = "Normal search 3";

    private static final String USER_TOKEN                                    = "2234";

    private static final String ITEM_SEARCH_CRITERIA_NORMAL_SEARCH            = "Normal search";
    private static final String ITEM_SEARCH_CRITERIA_FUZZY_SUBSTITUTION       = "Zamzun Pone";
    private static final String ITEM_SEARCH_CRITERIA_FUZZY_INSERTION          = "Zamzun Fhone";
    private static final String ITEM_SEARCH_CRITERIA_FUZZY_DELETION           = "Zamzu Fone";
    private static final String ITEM_SEARCH_CRITERIA_FUZZY_TRANSPOSITION      = "Zamzun Foen";
    private static final String ITEM_SEARCH_CRITERIA_SHINGLES                 = "The Hungry Alligator Ate Sue";
    private static final String ITEM_SEARCH_CRITERIA_NGRAM                    = "botin";
    private static final String LANG = ParkConstants.DEFAULT_LANGUAGE;
    private static final double PRICE_1                                       = 1;
    private static final double PRICE_2                                       = 2;
    private static final double PRICE_3                                       = 3;
    private static final double PRICE_4                                       = 4;


    private static final double TO_2 = 3;
    private static final long CATEGORY_1 = 1L;
    private static final long CATEGORY_2 = 2L;

    @InjectMocks
    private SearchItemCmdImpl searchItemCmdImpl = new SearchItemCmdImpl();

    private User user;

    @Mock
    private InternationalizationUtil i18nUtil;

    @Mock
    private DocumentConverter documentConverter;

    @Mock
    private TextUtils textUtils;

    @Mock
    private ItemUtils itemUtils;

    @Before
    public void setUp() {
        super.setUp();
        user = new User();
        user.setUserId(222L);
        user.setLatitude(12D);
        user.setLongitude(44D);

        searchItemCmdImpl.fuzziness = "1";
        searchItemCmdImpl.prefixLength = 3;
        searchItemCmdImpl.maxExpansions = 500;
        searchItemCmdImpl.fuzzyBooster = 1f;
        searchItemCmdImpl.fuzzyTranspositions = true;
        searchItemCmdImpl.fuzzyMinimumMatch = "50%";
        searchItemCmdImpl.shinglesBooster = 4f;
        searchItemCmdImpl.ngramMinimumMatch = "70%";
        searchItemCmdImpl.ngramBooster = 3f;

    }

    public void saveDocumentsForFuzziness() {
        /*Items: 1- item name: Zamzun Fone, item publisher: Publisher for fuzzy
         *       2- item name: Pley Esteision, item publisher: John Smith */
        saveDocuments(new ItemDocument(itemDataSource.get(ITEM_1_FOR_FUZZY)),
                new ItemDocument(itemDataSource.get(ITEM_2_FOR_FUZZY)));
    }

    @Test
    public void givenNotCriteriaWhenQueryingThenReturnAll() {
        saveDocuments(
                new ItemDocument(itemDataSource.get(ITEM_1_FOR_FUZZY)),
                new ItemDocument(itemDataSource.get(ITEM_2_FOR_FUZZY)),
                new ItemDocument(itemDataSource.get(ITEM_3_FOR_SHINGLES)));

        SearchItemRequest searchRequest = new SearchItemRequestBuilder(null).setPage(PAGE).setPageSize(PAGE_SIZE).build();
        SearchQuery searchQuery = searchItemCmdImpl.createRequestQuery(
                searchRequest, user);
        List<ItemDocument> result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);

        assertThat("All elements are retrieved", result.size(), is(3));
        assertThat(result.stream().anyMatch(item -> ITEM_1_FOR_FUZZY.equals(item.getName())), is(true));
        assertThat(result.stream().anyMatch(item -> ITEM_2_FOR_FUZZY.equals(item.getName())), is(true));
        assertThat(result.stream().anyMatch(item -> ITEM_3_FOR_SHINGLES.equals(item.getName())), is(true));
    }

    @Test
    public void givenPerfectAndZeroMatchingTermWhenQueryingThenReturnPerfectMatchingTerm() {
        saveDocuments(
                new ItemDocument(itemDataSource.get(ITEM_1_FOR_FUZZY)),
                new ItemDocument(itemDataSource.get(ITEM_2_FOR_FUZZY)),
                new ItemDocument(itemDataSource.get(ITEM_10_FOR_NORMAL)));

        SearchItemRequest searchRequest = new SearchItemRequestBuilder(ITEM_SEARCH_CRITERIA_NORMAL_SEARCH).setPage(PAGE).setPageSize(PAGE_SIZE).build();
        searchRequest.setToken(USER_TOKEN);
        searchRequest.setLanguage(LANG);
        SearchQuery searchQuery = searchItemCmdImpl.createRequestQuery(searchRequest, user);
        List<ItemDocument> result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);

        assertThat("One element is retrieved", result.size(), is(1));
        assertThat("Perfect match element is retrieved", result.get(0).getName(), is(ITEM_10_FOR_NORMAL));
    }

    /**
     * Saving "Zamzun Fone" and "Pley Esteision", and searching for "Zamzun Pone" on item name
     */
    @Test
    public void givenFuzzinessSubstitutionAndZeroMatchingOnItemNameWhenQueryingThenReturnFuzzinessSubstitutionTerm() {
        saveDocumentsForFuzziness();
        SearchItemRequest searchRequest = new SearchItemRequestBuilder(
                ITEM_SEARCH_CRITERIA_FUZZY_SUBSTITUTION).setPage(PAGE).setPageSize(PAGE_SIZE).build();
        searchRequest.setToken(USER_TOKEN);
        searchRequest.setLanguage(LANG);

        SearchQuery searchQuery = searchItemCmdImpl.createRequestQuery(searchRequest, user);
        List<ItemDocument> result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);

        assertThat("One element should be retrieved", result.size(), is(1));
        assertThat("Element with substitution fuzziness must be retrieved", result.get(0).getName(), is(ITEM_1_FOR_FUZZY));
    }

    /**
     * Saving "Zamzun Fone" and "Pley Esteision", and searching for "Zamzun Fhone" on item name
     */
    @Test
    public void givenFuzzinessInsertionAndZeroMatchingOnItemNameWhenQueryingThenReturnFuzzinessInsertionTerm() {
        saveDocumentsForFuzziness();
        SearchItemRequest searchRequest = new SearchItemRequestBuilder(
                ITEM_SEARCH_CRITERIA_FUZZY_INSERTION).setPage(PAGE).setPageSize(PAGE_SIZE).build();
        searchRequest.setToken(USER_TOKEN);
        searchRequest.setLanguage(LANG);

        SearchQuery searchQuery = searchItemCmdImpl.createRequestQuery(searchRequest, user);
        List<ItemDocument> result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);

        assertThat("One element is retrieved", result.size(), is(1));
        assertThat("Element with insertion fuzziness must be retrieved", result.get(0).getName(), is(ITEM_1_FOR_FUZZY));
    }

    /**
     * Saving "Zamzun Fone" and "Pley Esteision", and searching for "Zamzu Fone" on item name
     */
    @Test
    public void givenFuzzinessDeletionAndZeroMatchingOnItemNameWhenQueryingThenReturnFuzzinessDeletionTerm() {
        saveDocumentsForFuzziness();
        SearchItemRequest searchRequest = new SearchItemRequestBuilder(
                ITEM_SEARCH_CRITERIA_FUZZY_DELETION).setPage(PAGE).setPageSize(PAGE_SIZE).build();
        searchRequest.setToken(USER_TOKEN);
        searchRequest.setLanguage(LANG);

        SearchQuery searchQuery = searchItemCmdImpl.createRequestQuery(searchRequest, user);
        List<ItemDocument> result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);

        assertThat("One element is retrieved", result.size(), is(1));
        assertThat("Element with deletion fuzziness must be retrieved", result.get(0).getName(), is(ITEM_1_FOR_FUZZY));
    }


    /**
     * Saving "Zamzun Fone" and "Pley Esteision", and searching for "Zamzun Foen" on item name
     */
    @Test
    public void givenFuzzinessTranspositionAndZeroMatchingOnItemNameWhenQueryingThenReturnFuzzinessTranspositionTerm() {
        saveDocumentsForFuzziness();
        SearchItemRequest searchRequest = new SearchItemRequestBuilder(
                ITEM_SEARCH_CRITERIA_FUZZY_TRANSPOSITION).setPage(PAGE).setPageSize(PAGE_SIZE).build();
        searchRequest.setToken(USER_TOKEN);
        searchRequest.setLanguage(LANG);

        SearchQuery searchQuery = searchItemCmdImpl.createRequestQuery(searchRequest, user);
        List<ItemDocument> result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);

        assertThat("One element is retrieved", result.size(), is(1));
        assertThat("Element with fuzziness transposition must be retrieved", result.get(0).getName(), is(ITEM_1_FOR_FUZZY));
    }

    /**
     * Shingles. Find Associated Words in Phrase.
     * Saving "Sue ate the Aligator", "the alligator ate Sue", "She never goes anywhere without her alligator" and "the test",
     * and searching "The Angry Alligator Ate Sue".
     */
    @Test
    public void givenShinglesAndZeroMatchingWhenQueryingThenReturnShingleMatchingTerms() {
        saveDocuments(new ItemDocument(itemDataSource.get(ITEM_3_FOR_SHINGLES)),
                new ItemDocument(itemDataSource.get(ITEM_4_FOR_SHINGLES)),
                new ItemDocument(itemDataSource.get(ITEM_5_FOR_SHINGLES)),
                new ItemDocument(itemDataSource.get(ITEM_6_FOR_SHINGLES)));

        SearchItemRequest searchRequest = new SearchItemRequestBuilder(ITEM_SEARCH_CRITERIA_SHINGLES).
                setPage(PAGE).setPageSize(PAGE_SIZE).build();
        searchRequest.setToken(USER_TOKEN);
        searchRequest.setLanguage(LANG);
        SearchQuery searchQuery = searchItemCmdImpl.createRequestQuery(searchRequest, user);
        List<ItemDocument> result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);

        assertThat("Three elements must be retrieved", result.size(), is(3));
        assertTrue(result.stream().anyMatch(item -> ITEM_3_FOR_SHINGLES.equals(item.getName())));
        assertTrue(result.stream().anyMatch(item -> ITEM_4_FOR_SHINGLES.equals(item.getName())));
        assertTrue(result.stream().anyMatch(item -> ITEM_5_FOR_SHINGLES.equals(item.getName())));
    }

    /**
     * N-Gram.
     * Saving "old tin set can", "Monedas de Pelicula" and "botines", and searching for "botin".
     */
    @Test
    public void givenPartiallyMatchingAndZeroMatchingTermsOnItemNameWhenRequestingThenReturnPartiallyMatchingTerms() {
        List<ItemDocument> result;

        saveDocuments(new ItemDocument(itemDataSource.get(ITEM_7_FOR_NGRAM)),
                new ItemDocument(itemDataSource.get(ITEM_8_FOR_NGRAM)),
                new ItemDocument(itemDataSource.get(ITEM_9_FOR_NGRAM)));

        SearchItemRequest searchRequest = new SearchItemRequestBuilder(
                ITEM_SEARCH_CRITERIA_NGRAM).setPage(PAGE).setPageSize(PAGE_SIZE).build();
        searchRequest.setToken(USER_TOKEN);
        searchRequest.setLanguage(LANG);
        SearchQuery searchQuery = searchItemCmdImpl.createRequestQuery(searchRequest, user);

        result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);
        assertThat("Two elements must be retrieved", result.size(), is(2));
        assertThat("More similar element must come first", result.get(0).getName(), is(ITEM_9_FOR_NGRAM));
        assertThat("Lesss similar element must come last", result.get(1).getName(), is(ITEM_7_FOR_NGRAM));
    }

    @Test
    public void givenNoCriteriaAndPriceFromFilterWhenQueryingThenReturnPriceFiltered() {
        saveDocuments(
                new ItemDocument(itemDataSource.get(ITEM_1_FOR_FUZZY)),
                new ItemDocument(itemDataSource.get(ITEM_2_FOR_FUZZY)),
                new ItemDocument(itemDataSource.get(ITEM_3_FOR_SHINGLES)));

        SearchItemRequest searchRequest = new SearchItemRequestBuilder(null).setPage(PAGE).setPageSize(PAGE_SIZE).build();
        searchRequest.setToken(USER_TOKEN);
        searchRequest.setLanguage(LANG);
        searchRequest.setPriceFrom(PRICE_2);
        searchRequest.setPage(PAGE);
        searchRequest.setPageSize(PAGE_SIZE);
        SearchQuery searchQuery = searchItemCmdImpl.createRequestQuery(searchRequest, user);
        List<ItemDocument> result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);

        assertThat("Two elements are retrieved", result.size(), is(2));
        assertTrue(result.stream().anyMatch(item -> ITEM_2_FOR_FUZZY.equals(item.getName())));
        assertTrue(result.stream().anyMatch(item -> ITEM_3_FOR_SHINGLES.equals(item.getName())));
    }

    @Test
    public void givenNoCriteriaAndPriceToFilterWhenQueryingThenReturnPriceFiltered() {
        saveDocuments(
                new ItemDocument(itemDataSource.get(ITEM_1_FOR_FUZZY)),
                new ItemDocument(itemDataSource.get(ITEM_2_FOR_FUZZY)),
                new ItemDocument(itemDataSource.get(ITEM_3_FOR_SHINGLES)));

        SearchItemRequest searchRequest = new SearchItemRequestBuilder(null).setPage(PAGE).setPageSize(PAGE_SIZE).build();
        searchRequest.setToken(USER_TOKEN);
        searchRequest.setLanguage(LANG);
        searchRequest.setPriceTo(PRICE_2);
        SearchQuery searchQuery = searchItemCmdImpl.createRequestQuery(searchRequest, user);
        List<ItemDocument> result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);

        assertThat("Two elements are retrieved", result.size(), is(2));
        assertTrue(result.stream().anyMatch(item -> ITEM_1_FOR_FUZZY.equals(item.getName())));
        assertTrue(result.stream().anyMatch(item -> ITEM_2_FOR_FUZZY.equals(item.getName())));
    }

    @Test
    public void givenNoCriteriaAndPriceRangeFilterWhenQueryingThenReturnPriceFiltered() {
        saveDocuments(
                new ItemDocument(itemDataSource.get(ITEM_1_FOR_FUZZY)),
                new ItemDocument(itemDataSource.get(ITEM_2_FOR_FUZZY)),
                new ItemDocument(itemDataSource.get(ITEM_3_FOR_SHINGLES)),
                new ItemDocument(itemDataSource.get(ITEM_4_FOR_SHINGLES)));

        SearchItemRequest searchRequest = new SearchItemRequestBuilder(null).setPage(PAGE).setPageSize(PAGE_SIZE).build();
        searchRequest.setToken(USER_TOKEN);
        searchRequest.setLanguage(LANG);
        searchRequest.setPriceFrom(PRICE_2);
        searchRequest.setPriceTo(TO_2);
        SearchQuery searchQuery = searchItemCmdImpl.createRequestQuery(searchRequest, user);
        List<ItemDocument> result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);

        assertThat("Two elements are retrieved", result.size(), is(2));
        assertTrue(result.stream().anyMatch(item -> ITEM_3_FOR_SHINGLES.equals(item.getName())));
        assertTrue(result.stream().anyMatch(item -> ITEM_2_FOR_FUZZY.equals(item.getName())));
    }

    @Test
    public void givenMatchTermAndPriceRangeFilterWhenQueryingThenReturnPriceAndNameFiltered() {
        saveDocuments(
                new ItemDocument(itemDataSource.get(ITEM_1_FOR_FUZZY)),
                new ItemDocument(itemDataSource.get(ITEM_2_FOR_FUZZY)),
                new ItemDocument(itemDataSource.get(ITEM_10_FOR_NORMAL)),
                new ItemDocument(itemDataSource.get(ITEM_11_FOR_NORMAL)));

        SearchItemRequest searchRequest = new SearchItemRequestBuilder(null).setPage(PAGE).setPageSize(PAGE_SIZE).build();
        searchRequest.setToken(USER_TOKEN);
        searchRequest.setLanguage(LANG);
        searchRequest.setPriceFrom(PRICE_3);
        searchRequest.setCriteria(ITEM_SEARCH_CRITERIA_NORMAL_SEARCH);
        SearchQuery searchQuery = searchItemCmdImpl.createRequestQuery(searchRequest, user);
        List<ItemDocument> result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);

        assertThat("One element should be retrieved", result.size(), is(1));
        assertThat("The retrieved element must be the one matching price and item name",
                result.get(0).getName(), is(ITEM_11_FOR_NORMAL));
    }

    @Test
    public void givenNoCriteriaAndCategoryFromFilterWhenQueryingThenReturnPriceFiltered() {
        saveDocuments(
                new ItemDocument(itemDataSource.get(ITEM_1_FOR_FUZZY)),
                new ItemDocument(itemDataSource.get(ITEM_2_FOR_FUZZY)),
                new ItemDocument(itemDataSource.get(ITEM_3_FOR_SHINGLES)));

        SearchItemRequest searchRequest = new SearchItemRequestBuilder(null).setPage(PAGE).setPageSize(PAGE_SIZE).build();
        searchRequest.setToken(USER_TOKEN);
        searchRequest.setLanguage(LANG);
        searchRequest.setCategoryId(CATEGORY_2);
        SearchQuery searchQuery = searchItemCmdImpl.createRequestQuery(searchRequest, user);
        List<ItemDocument> result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);

        assertThat("Two elements should be retrieved", result.size(), is(2));
        assertTrue(result.stream().anyMatch(item -> ITEM_2_FOR_FUZZY.equals(item.getName())));
        assertTrue(result.stream().anyMatch(item -> ITEM_3_FOR_SHINGLES.equals(item.getName())));
    }

    @Test
    public void givenMatchTermAndPriceRangeAndCategoryFilterWhenQueryingThenReturnPriceAndNameFiltered() {
        saveDocuments(
                new ItemDocument(itemDataSource.get(ITEM_1_FOR_FUZZY)),
                new ItemDocument(itemDataSource.get(ITEM_2_FOR_FUZZY)),
                new ItemDocument(itemDataSource.get(ITEM_10_FOR_NORMAL)),
                new ItemDocument(itemDataSource.get(ITEM_11_FOR_NORMAL)),
                new ItemDocument(itemDataSource.get(ITEM_12_FOR_NORMAL)));

        SearchItemRequest searchRequest = new SearchItemRequestBuilder(null).setPage(PAGE).setPageSize(PAGE_SIZE).build();
        searchRequest.setToken(USER_TOKEN);
        searchRequest.setLanguage(LANG);
        searchRequest.setPriceFrom(PRICE_3);
        searchRequest.setCategoryId(CATEGORY_2);
        searchRequest.setCriteria(ITEM_SEARCH_CRITERIA_NORMAL_SEARCH);
        SearchQuery searchQuery = searchItemCmdImpl.createRequestQuery(searchRequest, user);
        List<ItemDocument> result = elasticsearchTemplate.queryForList(searchQuery, ItemDocument.class);

        assertThat("One element should be retrieved", result.size(), is(1));
        assertThat("The retrieved element must be the one matching price, item name and category",
                result.get(0).getName(), is(ITEM_11_FOR_NORMAL));
    }

    @Override
    protected void buildItemDataSource() {
        itemDataSource = new HashMap<>();

        Category category_1 = createCategory(CATEGORY_1);
        Category category_2 = createCategory(CATEGORY_2);
        itemDataSource.put(ITEM_1_FOR_FUZZY, createItem(new Item(ITEM_1_FOR_FUZZY, PRICE_1, "version", false, false), category_1, createUser(PUBLISHER_1_FOR_FUZZY), null));
        itemDataSource.put(ITEM_2_FOR_FUZZY, createItem(new Item(ITEM_2_FOR_FUZZY, PRICE_2, "version", false, false), category_2, createUser(PUBLISHER_2_FOR_FUZZY), null));
        itemDataSource.put(ITEM_3_FOR_SHINGLES, createItem(new Item(ITEM_3_FOR_SHINGLES, PRICE_3, "version", false, false), category_2, createUser(PUBLISHER_JOHN_SMITH), null));
        itemDataSource.put(ITEM_4_FOR_SHINGLES, createItem(new Item(ITEM_4_FOR_SHINGLES,  PRICE_4, "version", false, false),category_1,createUser(PUBLISHER_JOHN_SMITH), null));
        itemDataSource.put(ITEM_5_FOR_SHINGLES, createItem(new Item(ITEM_5_FOR_SHINGLES,  PRICE_1, "version", false, false),category_1,createUser(PUBLISHER_JOHN_SMITH), null));
        itemDataSource.put(ITEM_6_FOR_SHINGLES, createItem(new Item(ITEM_6_FOR_SHINGLES, PRICE_1, "version", false, false),category_1,createUser(PUBLISHER_JOHN_SMITH), null));
        itemDataSource.put(ITEM_7_FOR_NGRAM, createItem(new Item(ITEM_7_FOR_NGRAM, PRICE_1, "version", false, false),category_1,createUser(PUBLISHER_JOHN_SMITH), null));
        itemDataSource.put(ITEM_8_FOR_NGRAM, createItem(new Item(ITEM_8_FOR_NGRAM, PRICE_1, "version", false, false),category_1,createUser(PUBLISHER_JOHN_SMITH), null));
        itemDataSource.put(ITEM_9_FOR_NGRAM, createItem(new Item(ITEM_9_FOR_NGRAM, PRICE_1, "version", false, false),category_1,createUser(PUBLISHER_3_FOR_NGRAM), null));
        itemDataSource.put(ITEM_10_FOR_NORMAL, createItem(new Item(ITEM_10_FOR_NORMAL, PRICE_1, "version", false, false),category_1,createUser(PUBLISHER_3_FOR_NGRAM), null));
        itemDataSource.put(ITEM_11_FOR_NORMAL, createItem(new Item(ITEM_11_FOR_NORMAL, PRICE_4, "version", false, false),category_2,createUser(PUBLISHER_3_FOR_NGRAM), null));
        itemDataSource.put(ITEM_12_FOR_NORMAL, createItem(new Item(ITEM_12_FOR_NORMAL, PRICE_4, "version", false, false),category_1,createUser(PUBLISHER_3_FOR_NGRAM), null));
    }

    private Category createCategory(long categoryId) {
        Category cat = new Category();
        cat.setWebColor("webColor");
        cat.setCategoryId(categoryId);
        cat.setCatOrder(1);
        cat.setName("catName");
        cat.setSelectable(true);
        return cat;
    }
}