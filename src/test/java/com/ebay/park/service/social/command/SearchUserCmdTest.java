package com.ebay.park.service.social.command;

import com.ebay.park.config.ElasticsearchLocalConfig;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.elasticsearch.document.UserDocument;
import com.ebay.park.elasticsearch.utils.ESUtils;
import com.ebay.park.service.social.dto.SearchUserRequest;
import org.junit.Before;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by mauricio on 7/31/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ElasticsearchLocalConfig.class})
class SearchUserCmdIT {
    private static final int PAGE_SIZE                                        = 10;
    private static final int PAGE                                             = 0;
    private static final String USER_1_FOR_FUZZY                              = "jbenton";
    private static final String USER_2_FOR_FUZZY                              = "lpaprocki";
    private static final String EMAIL_JSMITH                                  = "jsmith@gmail.com";
    private static final String EMAIL_1_FOR_FUZZY                             = "jbutt@gmail.com";
    private static final String EMAIL_2_FOR_FUZZY                             = "kiley.caldarera@aol.com";
    private static final String EMAIL_3_FOR_NGRAM                             = "josephine_darakjy@darakjy.org";
    private static final String EMAIL_4_FOR_NGRAM                             = "marguerita.hiatt@gmail.com";
    private static final String USER_3_FOR_NGRAM                              = "rfrancescon";
    private static final String USER_4_FOR_NGRAM                              = "rspickerman";
    private static final String USER_5_FOR_NGRAM                              = "astpickerwood";

    private static final String USER_TOKEN                                    = "2234";

    private static final String USER_SEARCH_CRITERIA_FUZZY_SUBSTITUTION       = "jbentom";
    private static final String USER_SEARCH_CRITERIA_FUZZY_INSERTION          = "jbenthon";
    private static final String USER_SEARCH_CRITERIA_FUZZY_DELETION           = "jbento";
    private static final String USER_SEARCH_CRITERIA_FUZZY_TRANSPOSITION      = "jbentno";
    private static final String USER_SEARCH_CRITERIA_NGRAM                    = "picker";
    private static final String EMAIL_SEARCH_CRITERIA_NGRAM                   = "smit";
    private static final String EMAIL_SEARCH_CRITERIA_FUZZY_SUBSTITUTION      = "kiley.caldarara@aol.com";
    private static final String EMAIL_SEARCH_CRITERIA_FUZZY_INSERTION         = "kiley.caldarrera@aol.com";
    private static final String EMAIL_SEARCH_CRITERIA_FUZZY_DELETION          = "kiley.caldarera@ao.com";
    private static final String EMAIL_SEARCH_CRITERIA_FUZZY_TRANSPOSITION     = "kiley.caldarear@aol.com";
    private static final String LANG = "en";

    @InjectMocks
    private SearchUserCmd searchUserCmd;

    ESUtils esUtils; //TODO is it needed?

    @Autowired
    private ElasticsearchOperations elasticsearchTemplate;

    @Mock
    private UserDao userDao;

    Map<String, User> userDataSource = new HashMap<>();
    int id = 1;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        esUtils = new ESUtils();
        buildUserDataSource();
        elasticsearchTemplate.deleteIndex(UserDocument.class);
        elasticsearchTemplate.createIndex(UserDocument.class);
        elasticsearchTemplate.putMapping(UserDocument.class, esUtils.esPropertiesMapper(UserDocument.class));
        User requestUser = new User();
        requestUser.setUserId(222L);
        requestUser.setLatitude(12D);
        requestUser.setLongitude(44D);
        when(userDao.findByToken(USER_TOKEN)).thenReturn(requestUser);

        searchUserCmd.fuzziness = "1";
        searchUserCmd.prefixLength = 3;
        searchUserCmd.maxExpansions = 500;
        searchUserCmd.fuzzyBooster = 1f;
        searchUserCmd.fuzzyTranspositions = true;
        searchUserCmd.fuzzyMinimumMatch = "50%";
        searchUserCmd.shinglesBooster = 4f;
        searchUserCmd.ngramMinimumMatch = "60%";
        searchUserCmd.ngramBooster = 3f;

    }
    /** Testing fuzzinnes
     * Typoes and Mispelings
     */
    @Test
    public void testFuzziness() {
        List<UserDocument> result;
        saveDocuments(new UserDocument(userDataSource.get(USER_1_FOR_FUZZY)),
                new UserDocument(userDataSource.get(USER_2_FOR_FUZZY)));

        SearchUserRequest request = new SearchUserRequest(USER_TOKEN,LANG,PAGE,PAGE_SIZE);
        request.setCriteria(USER_SEARCH_CRITERIA_FUZZY_SUBSTITUTION);
        SearchQuery searchQuery = searchUserCmd.createRequestQuery(request,userDao.findByToken(USER_TOKEN));
        result = elasticsearchTemplate.queryForList(searchQuery,UserDocument.class);
        assertEquals(result.size(),1);

        request.setCriteria(USER_SEARCH_CRITERIA_FUZZY_INSERTION);
        searchQuery = searchUserCmd.createRequestQuery(request,userDao.findByToken(USER_TOKEN));
        result = elasticsearchTemplate.queryForList(searchQuery,UserDocument.class);
        assertEquals(result.size(),1);

        request.setCriteria(USER_SEARCH_CRITERIA_FUZZY_DELETION);
        searchQuery = searchUserCmd.createRequestQuery(request,userDao.findByToken(USER_TOKEN));
        result = elasticsearchTemplate.queryForList(searchQuery,UserDocument.class);
        assertEquals(result.size(),1);

        request.setCriteria(USER_SEARCH_CRITERIA_FUZZY_TRANSPOSITION);
        searchQuery = searchUserCmd.createRequestQuery(request,userDao.findByToken(USER_TOKEN));
        result = elasticsearchTemplate.queryForList(searchQuery,UserDocument.class);
        assertEquals(result.size(),1);

        request.setCriteria(EMAIL_SEARCH_CRITERIA_FUZZY_SUBSTITUTION);
        searchQuery = searchUserCmd.createRequestQuery(request,userDao.findByToken(USER_TOKEN));
        result = elasticsearchTemplate.queryForList(searchQuery,UserDocument.class);
        assertEquals(result.size(),1);

        request.setCriteria(EMAIL_SEARCH_CRITERIA_FUZZY_INSERTION);
        searchQuery = searchUserCmd.createRequestQuery(request,userDao.findByToken(USER_TOKEN));
        result = elasticsearchTemplate.queryForList(searchQuery,UserDocument.class);
        assertEquals(result.size(),1);

        request.setCriteria(EMAIL_SEARCH_CRITERIA_FUZZY_DELETION);
        searchQuery = searchUserCmd.createRequestQuery(request,userDao.findByToken(USER_TOKEN));
        result = elasticsearchTemplate.queryForList(searchQuery,UserDocument.class);
        assertEquals(result.size(),1);

        request.setCriteria(EMAIL_SEARCH_CRITERIA_FUZZY_TRANSPOSITION);
        searchQuery = searchUserCmd.createRequestQuery(request,userDao.findByToken(USER_TOKEN));
        result = elasticsearchTemplate.queryForList(searchQuery,UserDocument.class);
        assertEquals(result.size(),1);

    }

    @Test
    public void testPartialMatching() {
        List<UserDocument> result;
        saveDocuments(new UserDocument(userDataSource.get(USER_3_FOR_NGRAM)),
                new UserDocument(userDataSource.get(USER_4_FOR_NGRAM)),
                new UserDocument(userDataSource.get(USER_5_FOR_NGRAM)));

        SearchUserRequest request = new SearchUserRequest(USER_TOKEN,LANG,PAGE,PAGE_SIZE);
        request.setCriteria(USER_SEARCH_CRITERIA_NGRAM);
        SearchQuery searchQuery = searchUserCmd.createRequestQuery(request,userDao.findByToken(USER_TOKEN));
        result = elasticsearchTemplate.queryForList(searchQuery,UserDocument.class);
        assertEquals(result.size(),2);

        request.setCriteria(EMAIL_SEARCH_CRITERIA_NGRAM);
        searchQuery = searchUserCmd.createRequestQuery(request,userDao.findByToken(USER_TOKEN));
        result = elasticsearchTemplate.queryForList(searchQuery,UserDocument.class);
        assertEquals(result.size(),1);
    }


    private void buildUserDataSource() {
        userDataSource = new HashMap<>();

        User user1forFuzzy = new User();
        user1forFuzzy.setUsername(USER_1_FOR_FUZZY);
        user1forFuzzy.setEmail(EMAIL_1_FOR_FUZZY);

        User user2forFuzzy = new User();
        user2forFuzzy.setUsername(USER_2_FOR_FUZZY);
        user2forFuzzy.setEmail(EMAIL_2_FOR_FUZZY);

        User user3ForNgram = new User();
        user3ForNgram.setUsername(USER_3_FOR_NGRAM);
        user3ForNgram.setEmail(EMAIL_3_FOR_NGRAM);

        User user4ForNgram = new User();
        user4ForNgram.setUsername(USER_4_FOR_NGRAM);
        user4ForNgram.setEmail(EMAIL_4_FOR_NGRAM);

        User user5ForNgram = new User();
        user5ForNgram.setUsername(USER_5_FOR_NGRAM);
        user5ForNgram.setEmail(EMAIL_JSMITH);

        userDataSource.put(USER_1_FOR_FUZZY, user1forFuzzy);
        userDataSource.put(USER_2_FOR_FUZZY, user2forFuzzy);
        userDataSource.put(USER_3_FOR_NGRAM,user3ForNgram);
        userDataSource.put(USER_4_FOR_NGRAM,user4ForNgram);
        userDataSource.put(USER_5_FOR_NGRAM,user5ForNgram);
    }

    private void saveDocuments(UserDocument... documents){

        List<IndexQuery> indexQueries = new ArrayList<>();
        for(UserDocument document : documents){
            indexQueries.add(new IndexQueryBuilder().withObject(document)
                    .withIndexName("user").withId(String.valueOf(id)).withType("user").build());
            id++;
        }
        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh("user");
    }
}
