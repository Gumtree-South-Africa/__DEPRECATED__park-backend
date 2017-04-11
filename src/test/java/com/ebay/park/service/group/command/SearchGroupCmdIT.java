package com.ebay.park.service.group.command;

import com.ebay.park.config.ElasticsearchLocalConfig;
import com.ebay.park.config.MessagesConfig;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.User;
import com.ebay.park.elasticsearch.document.GroupDocument;
import com.ebay.park.service.group.dto.SearchGroupRequest;
import com.ebay.park.util.QueryUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Integration Test for {@link SearchGroupCmd}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ElasticsearchLocalConfig.class, MessagesConfig.class})
public class SearchGroupCmdIT  {
	private static final String GROUP_1_FOR_NORMAL                             = "Normal search";
	private static final String GROUP_2_FOR_FUZZY                              = "Zamzun Fone";
	private static final String GROUP_3_FOR_SHINGLES 						   = "Sue Ate The Alligator";
	private static final String GROUP_4_FOR_SHINGLES                           = "The alligator ate Sue";
	private static final String GROUP_5_FOR_SHINGLES                           = "Sue never goes anywhere without her alligator";
	private static final String GROUP_6_FOR_SHINGLES                           = "the test";
	private static final String GROUP_7_FOR_NGRAM                              = "old tin set can";
	private static final String GROUP_8_FOR_NGRAM                              = "Monedas de Pelicula";
	private static final String GROUP_9_FOR_NGRAM                              = "boti";
	private static final String GROUP_10_FOLDED_FOR_NORMAL                     = "Café";
	private static final String GROUP_11_FOR_NORMAL_AND_ADDITIONAL_REQ 		   = "Normal search with radius";

	private static final String PUBLISHER_1 = "Publisher";

	private static final String GROUP_SEARCH_CRITERIA					       = "Normal search";
	private static final String GROUP_SEARCH_CRITERIA_FUZZY_SUBSTITUTION       = "Zamzun Pone";
	private static final String GROUP_SEARCH_CRITERIA_FUZZY_INSERTION          = "Zamzun Fhone";
	private static final String GROUP_SEARCH_CRITERIA_FUZZY_DELETION           = "Zamzu Fone";
	private static final String GROUP_SEARCH_CRITERIA_FUZZY_TRANSPOSITION      = "Zamzun Foen";
	private static final String GROUP_SEARCH_CRITERIA_SHINGLES 				   = "The Hungry Alligator Ate Sue";
	private static final String GROUP_SEARCH_CRITERIA_NGRAM                    = "botin";
	private static final String GROUP_SEARCH_FOLDED_CRITERIA				   = "Cafe";

	private static final int PAGE = 0;
	private static final int PAGE_SIZE = 4;
	private static final String LANGUAGE = "es";
	private static final String USER_TOKEN = "1234";
	private static final double LATITUDE = 34.56;
	private static final double LONGITUDE = -74.56;
	private static final double LATITUDE_2 = 34.57;
	private static final double LONGITUDE_2 = -74.57;
	private static final double LATITUDE_3 = -34.57;
	private static final double LONGITUDE_3 = 74.57;
	private static final double RADIUS = 10;
	private static final String DATE = "1450728643"; /*"21/12/2015";*/
	private static final String USERNAME = "owner";

	@InjectMocks
	private SearchGroupCmd searchGroupCmd = new SearchGroupCmd();

	@Spy
	QueryUtils queryUtils;

	@Mock
	User requestUser;

	private HashMap<String, Group> groupDataSource;
	int id = 1;

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	private SearchGroupRequest request;

	@Before
	public void setUp() throws Exception{
		initMocks(this);

		elasticsearchTemplate.deleteIndex(GroupDocument.class);
		elasticsearchTemplate.createIndex(GroupDocument.class);
		elasticsearchTemplate.putMapping(GroupDocument.class);
		elasticsearchTemplate.refresh(GroupDocument.class);
		buildGroupDataSource();

		searchGroupCmd.fuzziness = "1";
		searchGroupCmd.prefixLength = 3;
		searchGroupCmd.maxExpansions = 500;
		searchGroupCmd.fuzzyBooster = 1f;
		searchGroupCmd.fuzzyTranspositions = true;
		searchGroupCmd.fuzzyMinimumMatch = "50%";
		searchGroupCmd.ngramMinimumMatch = "60%";
		searchGroupCmd.shinglesBooster = 4f;
		searchGroupCmd.ngramBooster = 3f;

		request = new SearchGroupRequest(USER_TOKEN, LANGUAGE, PAGE, PAGE_SIZE);

		request.setLatitude(LATITUDE);
		request.setLongitude(LONGITUDE);
		when(requestUser.getUsername()).thenReturn(USERNAME);
	}

	/**
	 * Saving "Café" and "Zamzun Fone", and searching "Cafe".
	 */
//	@Test
//	public void givenFoldedAndZeroMatchingTermsOnGroupNameWhenRequestingThenReturnFoldedMatchTerm() {
//		saveDocuments(new GroupDocument(groupDataSource.get(GROUP_10_FOLDED_FOR_NORMAL)),
//				new GroupDocument(groupDataSource.get(GROUP_2_FOR_FUZZY)));
//		request.setCriteria(GROUP_SEARCH_FOLDED_CRITERIA);
//		SearchQuery searchQuery = searchGroupCmd.createRequestQuery(request, mock(User.class));
//		List<GroupDocument> result = elasticsearchTemplate.queryForList(searchQuery, GroupDocument.class);
//
//		assertThat("One match is expected", result.size(), is(1));
//		assertThat("Search should return the exact match", result.get(0).getGroupName(), is(GROUP_10_FOLDED_FOR_NORMAL));
//	}

	/**
	 * Saving "Cafe" and "Zamzun Fone", and searching "Café".
	 */
//	@Test
//	public void givenAnotherFoldedAndZeroMatchingTermsOnGroupNameWhenRequestingThenReturnFoldedMatchTerm() {
//		saveDocuments(new GroupDocument(groupDataSource.get(GROUP_SEARCH_FOLDED_CRITERIA)),
//				new GroupDocument(groupDataSource.get(GROUP_2_FOR_FUZZY)));
//		request.setCriteria(GROUP_10_FOLDED_FOR_NORMAL);
//		SearchQuery searchQuery = searchGroupCmd.createRequestQuery(request, mock(User.class));
//		List<GroupDocument> result = elasticsearchTemplate.queryForList(searchQuery, GroupDocument.class);
//
//		assertThat("One match is expected", result.size(), is(1));
//		assertThat("Search should return the exact match", result.get(0).getGroupName(), is(GROUP_SEARCH_FOLDED_CRITERIA));
//	}

	/**
	 * Saving "Normal search" and "Zamzun Fone", and searching "Normal search".
	 */
	@Test
	public void givenPerfectAndZeroMatchingTermsOnGroupNameWhenRequestingThenReturnTotalMatchTerm() {
		saveDocuments(new GroupDocument(groupDataSource.get(GROUP_1_FOR_NORMAL)),
				new GroupDocument(groupDataSource.get(GROUP_2_FOR_FUZZY)));
		request.setCriteria(GROUP_SEARCH_CRITERIA);
		SearchQuery searchQuery = searchGroupCmd.createRequestQuery(request, mock(User.class));
		List<GroupDocument> result = elasticsearchTemplate.queryForList(searchQuery, GroupDocument.class);

		assertThat("One match is expected", result.size(), is(1));
		assertThat("Search should return the exact match", result.get(0).getGroupName(), is(GROUP_1_FOR_NORMAL));
	}

	/**
	 * Saving "Normal search" and "Zamzun Fone", and searching "Zamzun Pone".
	 */
	@Test
	public void givenFuzzinessSubstitutionAndZeroMatchingTermsOnGroupNameWhenRequestingThenReturnFuzzinessSubstitutionTerm() {
		saveDocuments(new GroupDocument(groupDataSource.get(GROUP_1_FOR_NORMAL)),
				new GroupDocument(groupDataSource.get(GROUP_2_FOR_FUZZY)));
		request.setCriteria(GROUP_SEARCH_CRITERIA_FUZZY_SUBSTITUTION);
		SearchQuery searchQuery = searchGroupCmd.createRequestQuery(request, mock(User.class));
		List<GroupDocument> result = elasticsearchTemplate.queryForList(searchQuery, GroupDocument.class);

		assertThat("One match is expected", result.size(), is(1));
		assertThat("Search should return the fuzziness substitution match", result.get(0).getGroupName(), is(GROUP_2_FOR_FUZZY));
	}

	/**
	 * Saving "Normal search" and "Zamzun Fone", and searching "Zamzun Fhone".
	 */
	@Test
	public void givenFuzzinessInsertionAndZeroMatchingTermsOnGroupNameWhenRequestingThenReturnFuzzinessInsertionTerm() {
		saveDocuments(new GroupDocument(groupDataSource.get(GROUP_1_FOR_NORMAL)),
				new GroupDocument(groupDataSource.get(GROUP_2_FOR_FUZZY)));
		request.setCriteria(GROUP_SEARCH_CRITERIA_FUZZY_INSERTION);
		SearchQuery searchQuery = searchGroupCmd.createRequestQuery(request, mock(User.class));
		List<GroupDocument> result = elasticsearchTemplate.queryForList(searchQuery, GroupDocument.class);

		assertThat("One match is expected", result.size(), is(1));
		assertThat("Search should return the fuzziness insertion match", result.get(0).getGroupName(), is(GROUP_2_FOR_FUZZY));
	}

	/**
	 * Saving "Normal search" and "Zamzun Fone", and searching "Zamzu Fone".
	 */
	@Test
	public void givenFuzzinessDeletionAndZeroMatchingTermsOnGroupNameWhenRequestingThenReturnFuzzinessDeletionTerm() {
		saveDocuments(new GroupDocument(groupDataSource.get(GROUP_1_FOR_NORMAL)),
				new GroupDocument(groupDataSource.get(GROUP_2_FOR_FUZZY)));
		request.setCriteria(GROUP_SEARCH_CRITERIA_FUZZY_DELETION);
		SearchQuery searchQuery = searchGroupCmd.createRequestQuery(request, mock(User.class));
		List<GroupDocument> result = elasticsearchTemplate.queryForList(searchQuery, GroupDocument.class);

		assertThat("One match is expected", result.size(), is(1));
		assertThat("Search should return the fuzziness deletion match", result.get(0).getGroupName(), is(GROUP_2_FOR_FUZZY));
	}

	/**
	 * Saving "Normal search" and "Zamzun Fone", and searching "Zamzu Fone".
	 */
	@Test
	public void givenFuzzinessTranspositionAndZeroMatchingTermsOnGroupNameWhenRequestingThenReturnFuzzinessTranspositionTerm() {
		saveDocuments(new GroupDocument(groupDataSource.get(GROUP_1_FOR_NORMAL)),
				new GroupDocument(groupDataSource.get(GROUP_2_FOR_FUZZY)));
		request.setCriteria(GROUP_SEARCH_CRITERIA_FUZZY_TRANSPOSITION);
		SearchQuery searchQuery = searchGroupCmd.createRequestQuery(request, mock(User.class));
		List<GroupDocument> result = elasticsearchTemplate.queryForList(searchQuery, GroupDocument.class);

		assertThat("One match is expected", result.size(), is(1));
		assertThat("Search should return the fuzziness deletion match", result.get(0).getGroupName(), is(GROUP_2_FOR_FUZZY));
	}

	/**
	 * Testing Shingles.
	 * Find Associated Words in Phrase.
	 */
	@Test
	public void givenShingleAndZeroMatchingTermOnItemNameWhenQueringThenReturnShingleMatchingTerms() {
		saveDocuments(
				new GroupDocument(groupDataSource.get(GROUP_3_FOR_SHINGLES)),
				new GroupDocument(groupDataSource.get(GROUP_4_FOR_SHINGLES)),
				new GroupDocument(groupDataSource.get(GROUP_5_FOR_SHINGLES)),
				new GroupDocument(groupDataSource.get(GROUP_6_FOR_SHINGLES)));

		request.setCriteria(GROUP_SEARCH_CRITERIA_SHINGLES);

		SearchQuery searchQuery = searchGroupCmd.createRequestQuery(request, requestUser);
		List<GroupDocument> result = elasticsearchTemplate.queryForList(searchQuery, GroupDocument.class);
		assertThat("Results : " + result.toString() + ". Three shingle matches are expected.", result.size(), is(3));
	}

	/**
	 * Saving "old tin set can", "Monedas de Pelicula" and "boti", and searching for "botin".
	 */
	@Test
	public void givenPartiallyAndZeroMatchingOnGroupNameWhenQueringThenReturnPartiallyMatchingTerm() {
		saveDocuments(
				new GroupDocument(groupDataSource.get(GROUP_7_FOR_NGRAM)),
				new GroupDocument(groupDataSource.get(GROUP_8_FOR_NGRAM)),
				new GroupDocument(groupDataSource.get(GROUP_9_FOR_NGRAM)));

		request.setCriteria(GROUP_SEARCH_CRITERIA_NGRAM);

		SearchQuery searchQuery = searchGroupCmd.createRequestQuery(request, requestUser);
		List<GroupDocument> result = elasticsearchTemplate.queryForList(searchQuery, GroupDocument.class);

		assertThat("One partial match is expected.", result.size(), is(1));
		assertThat("Results : " + result.toString() + ". One partial match is expected.",
				result.get(0).getGroupName(), is(GROUP_9_FOR_NGRAM));
	}

	/**
	 * Saving "Normal search" x 2 and "Zamzun Fone", and searching "Normal search".
	 * Including Radius criteria.
	 */
	@Test
	public void givenPerfectMatchingTermAndRadiusOnGroupNameWhenRequestingThenReturnTotalMatchTermWithRadiusMatch() {
		saveDocuments(
				new GroupDocument(groupDataSource.get(GROUP_1_FOR_NORMAL)),
				new GroupDocument(groupDataSource.get(GROUP_11_FOR_NORMAL_AND_ADDITIONAL_REQ)),
				new GroupDocument(groupDataSource.get(GROUP_2_FOR_FUZZY)));
		request.setCriteria(GROUP_SEARCH_CRITERIA);
		request.setRadius(RADIUS);
		SearchQuery searchQuery = searchGroupCmd.createRequestQuery(request, requestUser);
		List<GroupDocument> result = elasticsearchTemplate.queryForList(searchQuery, GroupDocument.class);

		assertThat("Results : " + result.toString() + ". One match is expected", result.size(), is(1));
		assertThat("Search should return the exact match", result.get(0).getGroupName(), is(GROUP_1_FOR_NORMAL));
	}

	/**
	 * Saving "Normal search" x 2 and "Zamzun Fone", and searching "Normal search".
	 * Including Time criteria.
	 */
	@Test
	public void givenPerfectMatchingTermAndTimeOnGroupNameWhenRequestingThenReturnTotalMatchTermWithTimeMatch() throws ParseException {
		GroupDocument validGroup = new GroupDocument(groupDataSource.get(GROUP_1_FOR_NORMAL));
		GroupDocument tooNewGroup = new GroupDocument(groupDataSource.get(GROUP_2_FOR_FUZZY));
		saveDocuments(validGroup, tooNewGroup);
		request.setCriteria(GROUP_SEARCH_CRITERIA);
		request.setRequestTime(DATE);

		SearchQuery searchQuery = searchGroupCmd.createRequestQuery(request, mock(User.class));
		List<GroupDocument> result = elasticsearchTemplate.queryForList(searchQuery, GroupDocument.class);

		assertThat("Results : " + result.toString() +
						". Zero matches are expected, all elements were created after the request time",
				result.size(), is(0));
	}

	/**
	 * Saving "Normal search" x 2 and "Zamzun Fone", and searching "Normal search".
	 * Including Time criteria.
	 */
	@Test
	public void givenNoCriteriaWhenRequestingThenReturnAll() throws ParseException {
		saveDocuments(
				new GroupDocument(groupDataSource.get(GROUP_3_FOR_SHINGLES)),
				new GroupDocument(groupDataSource.get(GROUP_4_FOR_SHINGLES)),
				new GroupDocument(groupDataSource.get(GROUP_5_FOR_SHINGLES)),
				new GroupDocument(groupDataSource.get(GROUP_6_FOR_SHINGLES)));

		SearchQuery searchQuery = searchGroupCmd.createRequestQuery(request, mock(User.class));
		List<GroupDocument> result = elasticsearchTemplate.queryForList(searchQuery, GroupDocument.class);

		assertThat("Results : " + result.toString() + ". All elements must be retrieve", result.size(), is(4));
	}

	@Test
	public void givenNoCriteriaAndOnlyOwnedRequestWhenRequestingThenReturnOnlyOwned() throws ParseException {
		saveDocuments(
				new GroupDocument(groupDataSource.get(GROUP_1_FOR_NORMAL)),
				new GroupDocument(groupDataSource.get(GROUP_2_FOR_FUZZY)),
				new GroupDocument(groupDataSource.get(GROUP_3_FOR_SHINGLES)),
				new GroupDocument(groupDataSource.get(GROUP_4_FOR_SHINGLES)));
		request.setOnlyOwned(true);
		SearchQuery searchQuery = searchGroupCmd.createRequestQuery(request, requestUser);
		List<GroupDocument> result = elasticsearchTemplate.queryForList(searchQuery, GroupDocument.class);

		assertThat("Results : " + result.toString() + ". Two elements must be retrieved", result.size(), is(2));
		assertTrue(result.stream().anyMatch(group -> GROUP_3_FOR_SHINGLES.equals(group.getGroupName())));
		assertTrue(result.stream().anyMatch(group -> GROUP_2_FOR_FUZZY.equals(group.getGroupName())));

	}

	@Test
	public void givenCriteriaAndOnlyOwnedRequestWhenRequestingThenReturnOnlyOwnedAndCriteriaMatchedTerm() throws ParseException {
		saveDocuments(
				new GroupDocument(groupDataSource.get(GROUP_1_FOR_NORMAL)),
				new GroupDocument(groupDataSource.get(GROUP_2_FOR_FUZZY)),
				new GroupDocument(groupDataSource.get(GROUP_3_FOR_SHINGLES)),
				new GroupDocument(groupDataSource.get(GROUP_4_FOR_SHINGLES)));
		request.setOnlyOwned(true);
		request.setCriteria(GROUP_SEARCH_CRITERIA_FUZZY_SUBSTITUTION);
		SearchQuery searchQuery = searchGroupCmd.createRequestQuery(request, requestUser);
		List<GroupDocument> result = elasticsearchTemplate.queryForList(searchQuery, GroupDocument.class);

		assertThat("Results : " + result.toString() + ". One element must be retrieved", result.size(), is(1));
		assertThat("Perfect match and owned element should be retrieved", result.get(0).getGroupName(), is(GROUP_2_FOR_FUZZY));
	}

	private void buildGroupDataSource() throws ParseException {
		groupDataSource = new HashMap<>();
		Group group1_radius = new Group(GROUP_1_FOR_NORMAL, createUser(PUBLISHER_1), "");
		group1_radius.setLatitude(LATITUDE_2);
		group1_radius.setLongitude(LONGITUDE_2);

		Group group11_radius = new Group(GROUP_1_FOR_NORMAL, createUser(PUBLISHER_1), "");
		group11_radius.setLatitude(LATITUDE_3);
		group11_radius.setLongitude(LONGITUDE_3);

		groupDataSource.put(GROUP_1_FOR_NORMAL, group1_radius);
		groupDataSource.put(GROUP_2_FOR_FUZZY,
				new Group(GROUP_2_FOR_FUZZY, createUser(USERNAME), ""));
		groupDataSource.put(GROUP_3_FOR_SHINGLES,
				new Group(GROUP_3_FOR_SHINGLES, createUser(USERNAME), ""));
		groupDataSource.put(GROUP_4_FOR_SHINGLES,
				new Group(GROUP_4_FOR_SHINGLES, createUser(PUBLISHER_1), ""));
		groupDataSource.put(GROUP_5_FOR_SHINGLES,
				new Group(GROUP_5_FOR_SHINGLES, createUser(PUBLISHER_1), ""));
		groupDataSource.put(GROUP_6_FOR_SHINGLES,
				new Group(GROUP_6_FOR_SHINGLES, createUser(PUBLISHER_1), ""));
		groupDataSource.put(GROUP_7_FOR_NGRAM,
				new Group(GROUP_7_FOR_NGRAM, createUser(PUBLISHER_1), ""));
		groupDataSource.put(GROUP_8_FOR_NGRAM,
				new Group(GROUP_8_FOR_NGRAM, createUser(PUBLISHER_1), ""));
		groupDataSource.put(GROUP_9_FOR_NGRAM,
				new Group(GROUP_9_FOR_NGRAM, createUser(PUBLISHER_1), ""));
		groupDataSource.put(GROUP_10_FOLDED_FOR_NORMAL,
				new Group(GROUP_10_FOLDED_FOR_NORMAL, createUser(PUBLISHER_1), ""));
		groupDataSource.put(GROUP_SEARCH_FOLDED_CRITERIA,
				new Group(GROUP_SEARCH_FOLDED_CRITERIA, createUser(PUBLISHER_1), ""));
		groupDataSource.put(GROUP_11_FOR_NORMAL_AND_ADDITIONAL_REQ,
				new Group(GROUP_1_FOR_NORMAL, createUser(PUBLISHER_1), ""));
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

	@After
	public void after() {
		elasticsearchTemplate.getClient().close();
	}
}
