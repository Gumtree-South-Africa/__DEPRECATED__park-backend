package com.ebay.park.service.blacklist.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.client.Client;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ebay.park.config.ElasticsearchLocalConfig;
import com.ebay.park.db.entity.BlackList;
import com.ebay.park.elasticsearch.document.BlackListDocument;
import com.ebay.park.service.blacklist.dto.SearchBlacklistedWordsRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ElasticsearchLocalConfig.class})
public class SearchBlacklistedWordsCmdIT {

    private static final int PAGE_SIZE                                      = 10;
    private static final int PAGE                                           = 0;
    private static final String BL_WORD_1_FOR_FUZZY                         = "citalopram";
    private static final String BL_WORD_2_FOR_NGRAM                         = "mephedrone";
    private static final String BL_WORD_3_FOR_NGRAM                         = "maduras";
    private static final String BL_WORD_4_FOR_NGRAM                         = "wholesale";

    private static final String GROUP_7_FOR_NGRAM                              = "old tin set can";
    private static final String GROUP_8_FOR_NGRAM                              = "Monedas de Pelicula";
    private static final String GROUP_9_FOR_NGRAM                              = "boti";

    private static final String BL_WORD_5_FOR_SHINGLES                      = "Sue Ate The Alligator";
    private static final String BL_WORD_6_FOR_SHINGLES                      = "The alligator ate Sue";
    private static final String BL_WORD_7_FOR_SHINGLES                      = "Sue never goes anywhere without her alligator";
    private static final String BL_WORD_8_FOR_SHINGLES                      = "the test";

    private static final String BL_WORD_SEARCH_CRITERIA_FUZZY_SUBSTITUTION  = "citalopran";
    private static final String BL_WORD_SEARCH_CRITERIA_FUZZY_INSERTION     = "citaloppram";
    private static final String BL_WORD_SEARCH_CRITERIA_FUZZY_DELETION      = "citaloprm";
    private static final String BL_WORD_SEARCH_CRITERIA_FUZZY_TRANSPOSITION = "citaloprma";
    private static final String BL_WORD_SEARCH_CRITERIA_SHINGLES            = "The Hungry Alligator Ate Sue";

    private static final String BL_WORD_SEARCH_CRITERIA_NGRAM               = "mephe";
    private static final String ORDER = "theOrder";

    @InjectMocks
    private SearchBlacklistedWordsCmd searchBlacklistedWordsCmd = new SearchBlacklistedWordsCmd();

    @Mock
    static
    Client localElasticClient;

    @Autowired
    private ElasticsearchOperations elasticsearchTemplate;

    private Map<String, BlackList> blackListDataSource = new HashMap<>();
    private int id = 1;

    @Before
    public void setUp(){
        initMocks(this);
        elasticsearchTemplate.deleteIndex(BlackListDocument.class);
        elasticsearchTemplate.createIndex(BlackListDocument.class);
        elasticsearchTemplate.putMapping(BlackListDocument.class);

        searchBlacklistedWordsCmd.fuzziness = "1";
        searchBlacklistedWordsCmd.prefixLength = 3;
        searchBlacklistedWordsCmd.maxExpansions = 500;
        searchBlacklistedWordsCmd.fuzzyBooster = 1f;
        searchBlacklistedWordsCmd.fuzzyTranspositions = true;
        searchBlacklistedWordsCmd.fuzzyMinimumMatch = "50%";
        searchBlacklistedWordsCmd.ngramMinimumMatch = "60%";
        searchBlacklistedWordsCmd.shinglesBooster = 4f;
        searchBlacklistedWordsCmd.ngramBooster = 3f;

        buildBlacklistDataSource();
    }

    /** Testing fuzzinnes
     * Typoes and Mispelings
     */
    @Test
    public void testFuzziness() {

        List<BlackListDocument> result;
        saveDocuments(new BlackListDocument(blackListDataSource.get(BL_WORD_1_FOR_FUZZY)),
                new BlackListDocument(blackListDataSource.get(BL_WORD_2_FOR_NGRAM)));

        SearchBlacklistedWordsRequest request = new SearchBlacklistedWordsRequest(PAGE, PAGE_SIZE, ORDER, BL_WORD_SEARCH_CRITERIA_FUZZY_SUBSTITUTION);
        SearchQuery searchQuery = searchBlacklistedWordsCmd.createRequestQuery(request);
        result = elasticsearchTemplate.queryForList(searchQuery, BlackListDocument.class);
        assertEquals(1, result.size());

        request = new SearchBlacklistedWordsRequest(PAGE, PAGE_SIZE, ORDER, BL_WORD_SEARCH_CRITERIA_FUZZY_INSERTION);
        searchQuery = searchBlacklistedWordsCmd.createRequestQuery(request);
        result = elasticsearchTemplate.queryForList(searchQuery, BlackListDocument.class);
        assertEquals(1, result.size());

        request = new SearchBlacklistedWordsRequest(PAGE, PAGE_SIZE, ORDER, BL_WORD_SEARCH_CRITERIA_FUZZY_DELETION);
        searchQuery = searchBlacklistedWordsCmd.createRequestQuery(request);
        result = elasticsearchTemplate.queryForList(searchQuery, BlackListDocument.class);
        assertEquals(1, result.size());

        request = new SearchBlacklistedWordsRequest(PAGE, PAGE_SIZE, ORDER, BL_WORD_SEARCH_CRITERIA_FUZZY_TRANSPOSITION);
        searchQuery = searchBlacklistedWordsCmd.createRequestQuery(request);
        result = elasticsearchTemplate.queryForList(searchQuery, BlackListDocument.class);
        assertEquals(1, result.size());

    }

    /**
     * Saving mephedrone", "maduras" and "wholesale" and searching for "mephe".
     */
    @Test
    public void givenPartiallyAndZeroMatchingTermsWhenQueryingThenReturnPartiallyMatchingTerm() {

        List<BlackListDocument> result;
        saveDocuments(new BlackListDocument(blackListDataSource.get(BL_WORD_2_FOR_NGRAM)),
                new BlackListDocument(blackListDataSource.get(BL_WORD_3_FOR_NGRAM)),
                new BlackListDocument(blackListDataSource.get(BL_WORD_4_FOR_NGRAM)));

        SearchBlacklistedWordsRequest request = new SearchBlacklistedWordsRequest(PAGE, PAGE_SIZE, ORDER, BL_WORD_SEARCH_CRITERIA_NGRAM);
        SearchQuery searchQuery = searchBlacklistedWordsCmd.createRequestQuery(request);
        result = elasticsearchTemplate.queryForList(searchQuery, BlackListDocument.class);
        assertThat("Result: " + result.toString() + ", two partial matchings expected. Query: "
                + searchQuery.getQuery()+  searchQuery.getHighlightFields(), result.size(), is(2));
    }

    /**
     * Saving "Sue Ate The Alligator", "The alligator ate Sue", "Sue never goes anywhere without her alligator"
     * and "the test", and searching for "The Hungry Alligator Ate Sue".
     */
    @Test
    public void givenShingleAndZeroMatchingTermsWhenQueryingThenReturnShingleMatchingTerms() {

        List<BlackListDocument> result;
        saveDocuments(new BlackListDocument(blackListDataSource.get(BL_WORD_5_FOR_SHINGLES)),
                new BlackListDocument(blackListDataSource.get(BL_WORD_6_FOR_SHINGLES)),
                new BlackListDocument(blackListDataSource.get(BL_WORD_7_FOR_SHINGLES)),
                new BlackListDocument(blackListDataSource.get(BL_WORD_8_FOR_SHINGLES)));

        SearchBlacklistedWordsRequest request = new SearchBlacklistedWordsRequest(PAGE, PAGE_SIZE, ORDER, BL_WORD_SEARCH_CRITERIA_SHINGLES);
        SearchQuery searchQuery = searchBlacklistedWordsCmd.createRequestQuery(request);
        result = elasticsearchTemplate.queryForList(searchQuery, BlackListDocument.class);
        assertThat("Result: " + result.toString() + ", three shingle matching expected", result.size(), is(3));

    }

    private void buildBlacklistDataSource() {

        BlackList blackList1ForFuzzy = new BlackList();
        blackList1ForFuzzy.setDescription(BL_WORD_1_FOR_FUZZY);

        BlackList blackList2ForNgram = new BlackList();
        blackList2ForNgram.setDescription(BL_WORD_2_FOR_NGRAM);

        BlackList blackList3ForNgram = new BlackList();
        blackList3ForNgram.setDescription(BL_WORD_3_FOR_NGRAM);

        BlackList blackList4ForNgram = new BlackList();
        blackList4ForNgram.setDescription(BL_WORD_4_FOR_NGRAM);

        BlackList blackList5ForShingles = new BlackList();
        blackList5ForShingles.setDescription(BL_WORD_5_FOR_SHINGLES);

        BlackList blackList6ForShingles = new BlackList();
        blackList6ForShingles.setDescription(BL_WORD_6_FOR_SHINGLES);

        BlackList blackList7ForShingles = new BlackList();
        blackList7ForShingles.setDescription(BL_WORD_7_FOR_SHINGLES);

        BlackList blackList8ForShingles = new BlackList();
        blackList8ForShingles.setDescription(BL_WORD_8_FOR_SHINGLES);

        blackListDataSource.put(BL_WORD_1_FOR_FUZZY, blackList1ForFuzzy);
        blackListDataSource.put(BL_WORD_2_FOR_NGRAM, blackList2ForNgram);
        blackListDataSource.put(BL_WORD_3_FOR_NGRAM, blackList3ForNgram);
        blackListDataSource.put(BL_WORD_4_FOR_NGRAM, blackList4ForNgram);
        blackListDataSource.put(BL_WORD_5_FOR_SHINGLES, blackList5ForShingles);
        blackListDataSource.put(BL_WORD_6_FOR_SHINGLES, blackList6ForShingles);
        blackListDataSource.put(BL_WORD_7_FOR_SHINGLES, blackList7ForShingles);
        blackListDataSource.put(BL_WORD_8_FOR_SHINGLES, blackList8ForShingles);
    }

    private void saveDocuments(BlackListDocument... documents){
        List<IndexQuery> indexQueries = new ArrayList<>();
        for(BlackListDocument document : documents){
            indexQueries.add(new IndexQueryBuilder().withObject(document)
                    .withIndexName("black_list").withId(String.valueOf(id)).withType("black_list").build());
            id++;
        }
        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh("black_list");
    }

}