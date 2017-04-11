package com.ebay.park.service.moderation.command;

import com.ebay.park.config.ElasticsearchLocalConfig;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.User;
import com.ebay.park.elasticsearch.document.GroupDocument;
import com.ebay.park.service.moderation.dto.SearchGroupForModerationRequest;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Integration test for {@link SearchGroupForModerationCmd}.
 * @author Julieta Salvadó
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ElasticsearchLocalConfig.class})
public class SearchGroupForModerationCmdIT {
	private static final String GROUP_1_FOR_PERFECT_MATCH                      = "perfect matching";
	private static final String GROUP_2                      			       = "Zamzun Fone";
	
	private static final String PUBLISHER_PERFECT_MATCH			               = "Publisher";
	private static final String PUBLISHER						               = "Johny";
		
	private static final String GROUP_SEARCH_CRITERIA					       = "perfect matching";
	private static final String PUBLISHER_SEARCH_CRITERIA_1		               = "Publisher";
	private static final String PUBLISHER_SEARCH_CRITERIA_2		               = "publisher";
		
	private static final int PAGE = 0;
	private static final int PAGE_SIZE = 20;

	@Mock
	private UserDao userDao;

	@InjectMocks
	private SearchGroupForModerationCmd searchGroupForModerationCmd = new SearchGroupForModerationCmd();

	@Autowired
    private ElasticsearchOperations elasticsearchTemplate;

    private HashMap<String, Group> groupDataSource;
	int id = 1;
	
	@Before
	public void setUp() throws Exception {
		initMocks(this);
        elasticsearchTemplate.deleteIndex(GroupDocument.class);
        elasticsearchTemplate.createIndex(GroupDocument.class);
        elasticsearchTemplate.putMapping(GroupDocument.class);
		buildGroupDataSource();
	}

	/**
	 * Testing perfect match on group name.
	 */
	@Test
	public void givenPerfectMatchTermOnGroupNameWhenQueryingThenReturnTerm() {
		saveDocuments(
				new GroupDocument(groupDataSource.get(GROUP_1_FOR_PERFECT_MATCH)),
				new GroupDocument(groupDataSource.get(GROUP_2)));

		SearchGroupForModerationRequest request = new SearchGroupForModerationRequest(GROUP_SEARCH_CRITERIA, null /*creatorName*/, PAGE, PAGE_SIZE);
		SearchQuery query = searchGroupForModerationCmd.createRequestQuery(request);
		List<GroupDocument>  result = elasticsearchTemplate.queryForList(query, GroupDocument.class);

		assertThat("Group name perfect match is retrieved",
				result.get(0).getGroupName(),
				is(GROUP_1_FOR_PERFECT_MATCH));
	}

	/**
	 * Testing perfect match on creator name.
	 */
	@Test
	public void givenPerfectMatchingTermOnCreatorNameWhenQueryingThenReturnTerm() {
		List<GroupDocument> result;

		saveDocuments(new GroupDocument(groupDataSource.get(GROUP_1_FOR_PERFECT_MATCH)),
				new GroupDocument(groupDataSource.get(GROUP_2)));

		SearchGroupForModerationRequest request = new SearchGroupForModerationRequest(null, PUBLISHER_SEARCH_CRITERIA_1, PAGE, PAGE_SIZE);
		SearchQuery query = searchGroupForModerationCmd.createRequestQuery(request);
		result = elasticsearchTemplate.queryForList(query, GroupDocument.class);

		assertThat("Owner name perfect match is retrieved",
				result.get(0).getCreator().getUsername(),
				is(PUBLISHER_PERFECT_MATCH));
	}

	
	/** 
	 * Testing lower case creator name.
     */
    @Test
    public void givenLowerCaseMatchingTermOnCreatorNameWhenQueryingThenReturnTerm() {
    	saveDocuments(new GroupDocument(groupDataSource.get(GROUP_1_FOR_PERFECT_MATCH)),
    			new GroupDocument(groupDataSource.get(GROUP_2)));
    	SearchGroupForModerationRequest request = new SearchGroupForModerationRequest(null, PUBLISHER_SEARCH_CRITERIA_2,  PAGE, PAGE_SIZE);
    	SearchQuery query = searchGroupForModerationCmd.createRequestQuery(request);
		List<GroupDocument> result = elasticsearchTemplate.queryForList(query, GroupDocument.class);

		assertThat("Owner name perfect match is retrieved",
				result.get(0).getCreator().getUsername(),
				is(PUBLISHER_PERFECT_MATCH));
    }
	
	private void buildGroupDataSource() {
    	groupDataSource = new HashMap<>();
    	
    	groupDataSource.put(GROUP_1_FOR_PERFECT_MATCH,
        		new Group(GROUP_1_FOR_PERFECT_MATCH, createUser(PUBLISHER), ""));
    	groupDataSource.put(GROUP_2,
    		new Group(GROUP_2, createUser(PUBLISHER_PERFECT_MATCH), ""));
    }

	private User createUser(String username){
        User user = new User();
        user.setUsername(username);
        user.setEmailVerified(true);
        user.setMobileVerified(true);
        return user;
    }
	
	private void saveDocuments(GroupDocument... documents){

        List<IndexQuery> indexQueries = new ArrayList<>();
        for(GroupDocument document : documents){
            indexQueries.add(new IndexQueryBuilder().withObject(document)
            .withIndexName("group").withId(String.valueOf(id)).withType("group").build());
            id++;
        }
        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh("group");
    }
}