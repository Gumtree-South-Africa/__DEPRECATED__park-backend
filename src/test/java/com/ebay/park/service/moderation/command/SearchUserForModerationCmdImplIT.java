package com.ebay.park.service.moderation.command;

import com.ebay.park.config.ElasticsearchLocalConfig;
import com.ebay.park.db.entity.User;
import com.ebay.park.elasticsearch.document.UserDocument;
import com.ebay.park.service.moderation.dto.SearchUserForModerationRequest;
import org.elasticsearch.client.Client;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Julieta Salvadó
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ElasticsearchLocalConfig.class})
public class SearchUserForModerationCmdImplIT {

	private static final int PAGE = 0;
	private static final int PAGE_SIZE =20;
	private static final String USER_1_FOR_PERFECT_MATCHING                   = "Tin";
	private static final String USER_2_FOR_FUZZY                              = "Zamzun-Fone";
	private static final String USER_3_FOR_NGRAM                              = "tin";
	private static final String USER_4_FOR_NGRAM                              = "Monedas";
	private static final String USER_5_FOR_NGRAM                              = "botin";
	
	private static final String USER_SEARCH_CRITERIA					       = "Tin";
	private static final String USER_SEARCH_CRITERIA_2					       = "tin";
	private static final String USER_SEARCH_CRITERIA_FUZZY_SUBSTITUTION       = "Zamzun-Pone";
	private static final String USER_SEARCH_CRITERIA_FUZZY_INSERTION          = "Zamzun-Fhone";
	private static final String USER_SEARCH_CRITERIA_FUZZY_DELETION           = "Zamzu-Fone";
	private static final String USER_SEARCH_CRITERIA_FUZZY_TRANSPOSITION      = "Zamzun-Foen";
	private static final String USER_SEARCH_CRITERIA_NGRAM                    = "botin";
	
	@Mock
    static
    Client localElasticClient;
	
	private HashMap<String, User> userDataSource;
	int id = 1;
	
	@Autowired
    private ElasticsearchOperations elasticsearchTemplate;

	@InjectMocks
	private SearchUserForModerationCmdImpl searchUserforModerationCmdImpl = new SearchUserForModerationCmdImpl();

	@Before
	public void setUp(){
		initMocks(this);
        elasticsearchTemplate.deleteIndex(UserDocument.class);
        elasticsearchTemplate.createIndex(UserDocument.class);
        elasticsearchTemplate.putMapping(UserDocument.class);
		buildUserDataSource();
				
		searchUserforModerationCmdImpl.fuzziness = "1";
		searchUserforModerationCmdImpl.prefixLength = 3;
		searchUserforModerationCmdImpl.maxExpansions = 500;
		searchUserforModerationCmdImpl.fuzzyBooster = 1f;
		searchUserforModerationCmdImpl.fuzzyTranspositions = true;
		searchUserforModerationCmdImpl.fuzzyMinimumMatch = "50%";
		searchUserforModerationCmdImpl.shinglesBooster = 4f;
		searchUserforModerationCmdImpl.ngramMinimumMatch = "70%";
		searchUserforModerationCmdImpl.ngramBooster = 3f;
	}
	
	private void buildUserDataSource() {
		userDataSource = new HashMap<>();

    	userDataSource.put(USER_1_FOR_PERFECT_MATCHING, createUser(USER_1_FOR_PERFECT_MATCHING));        		
    	userDataSource.put(USER_2_FOR_FUZZY, createUser(USER_2_FOR_FUZZY));
    	userDataSource.put(USER_3_FOR_NGRAM, createUser(USER_3_FOR_NGRAM));
    	userDataSource.put(USER_4_FOR_NGRAM, createUser(USER_4_FOR_NGRAM));
    	userDataSource.put(USER_5_FOR_NGRAM, createUser(USER_5_FOR_NGRAM));
    }
	
	private User createUser(String username) {
		User user = new User(); 
		user.setUsername(username);
		return user;
	}

	@Test
	public void testPerfectMatching() {
		List<UserDocument> result;
		saveDocuments(new UserDocument(userDataSource.get(USER_1_FOR_PERFECT_MATCHING)),
    			new UserDocument(userDataSource.get(USER_2_FOR_FUZZY)));
		
		SearchUserForModerationRequest request = new SearchUserForModerationRequest(PAGE, PAGE_SIZE) ;
    	
    	//user name
    	request.setUsername(USER_SEARCH_CRITERIA);
    	request.setExactMatch(true);
    	SearchQuery searchQuery = searchUserforModerationCmdImpl.createRequestQuery(request); 
    	result = elasticsearchTemplate.queryForList(searchQuery, UserDocument.class);
    	assertEquals(1, result.size());
    	
    	request.setUsername(USER_SEARCH_CRITERIA_2);
    	request.setExactMatch(true);
    	searchQuery = searchUserforModerationCmdImpl.createRequestQuery(request); 
    	result = elasticsearchTemplate.queryForList(searchQuery, UserDocument.class);
    	assertEquals(1, result.size());
	}

	/** 
	 * Testing fuzziness
     * Typos and Misspellings
     */
    @Test
    public void testFuzziness() {
    	List<UserDocument> result;
    	
    	saveDocuments(new UserDocument(userDataSource.get(USER_1_FOR_PERFECT_MATCHING)),
    			new UserDocument(userDataSource.get(USER_2_FOR_FUZZY)));
    	
    	SearchUserForModerationRequest request = new SearchUserForModerationRequest(PAGE, PAGE_SIZE) ;
    	
    	//user name
    	request.setUsername(USER_SEARCH_CRITERIA_FUZZY_DELETION);
    	request.setExactMatch(true);
    	SearchQuery searchQuery = searchUserforModerationCmdImpl.createRequestQuery(request); 
    	result = elasticsearchTemplate.queryForList(searchQuery, UserDocument.class);
    	assertEquals(1, result.size());
    	
    	request.setUsername(USER_SEARCH_CRITERIA_FUZZY_INSERTION);
    	request.setExactMatch(true);
    	searchQuery = searchUserforModerationCmdImpl.createRequestQuery(request); 
    	result = elasticsearchTemplate.queryForList(searchQuery, UserDocument.class);
    	assertEquals(1, result.size());
    	
    	request.setUsername(USER_SEARCH_CRITERIA_FUZZY_SUBSTITUTION);
    	request.setExactMatch(true);
    	searchQuery = searchUserforModerationCmdImpl.createRequestQuery(request); 
    	result = elasticsearchTemplate.queryForList(searchQuery, UserDocument.class);
    	assertEquals(1, result.size());
    	
    	request.setUsername(USER_SEARCH_CRITERIA_FUZZY_TRANSPOSITION);
    	request.setExactMatch(true);
    	searchQuery = searchUserforModerationCmdImpl.createRequestQuery(request); 
    	result = elasticsearchTemplate.queryForList(searchQuery, UserDocument.class);
    	assertEquals(1, result.size());
    }
    
    @Test
    public void testPartialMatching() {
        List<UserDocument> result;

        saveDocuments(new UserDocument(userDataSource.get(USER_3_FOR_NGRAM)),
        		new UserDocument(userDataSource.get(USER_4_FOR_NGRAM)),
        		new UserDocument(userDataSource.get(USER_5_FOR_NGRAM)));

        SearchUserForModerationRequest request = new SearchUserForModerationRequest(PAGE, PAGE_SIZE) ;
        request.setUsername(USER_SEARCH_CRITERIA_NGRAM);
        request.setExactMatch(true);
        SearchQuery searchQuery = searchUserforModerationCmdImpl.createRequestQuery(request);        
        result = elasticsearchTemplate.queryForList(searchQuery, UserDocument.class);
        assertEquals(1, result.size());
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
