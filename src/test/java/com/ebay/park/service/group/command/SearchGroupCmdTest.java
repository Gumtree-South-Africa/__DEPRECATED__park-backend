package com.ebay.park.service.group.command;

import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.Idiom;
import com.ebay.park.db.entity.User;
import com.ebay.park.elasticsearch.document.GroupDocument;
import com.ebay.park.elasticsearch.document.converter.DocumentConverter;
import com.ebay.park.elasticsearch.repository.GroupRepository;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.group.dto.GetGroupCounterRequest;
import com.ebay.park.service.group.dto.SearchGroupRequest;
import com.ebay.park.service.group.dto.SearchGroupResponse;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.util.InternationalizationUtil;
import com.ebay.park.util.ParkConstants;
import com.ebay.park.util.QueryUtils;
import com.ebay.park.util.TextUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link SearchGroupCmd}.
 *
 * @author Julieta Salvadó
 */
public class SearchGroupCmdTest {
    private static final int PAGE = 0;
    private static final int PAGE_SIZE = 2;
    private static final String LANG = "es";
    private static final String INVALID_USER_TOKEN = "invalid";
    private static final String MSG = "An exception was expected";
    private static final String VALID_USER_TOKEN = "valid";
    private static final double LATITUDE = 10;
    private static final double LONGITUDE = 10;
    private static final double INVALID_RADIUS = -2;
    private static final double VALID_RADIUS = 2;
    private static final String USERNAME = "Julieta";
    private static final long USER_ID = 1L;
    private static final int INVALID_PAGE_SIZE = -10;
    private static final String VALID_REQUEST_TIME = "1457032851";
    private static final String INVALID_REQUEST_TIME = "20 de abril";
    private static final String NAME_ORDER = "name";
    private static final String OTHER_ORDER = "other";
    private static final String CRITERIA = "search criteria";
    private static final String FUZZINESS = "1";
    private static final boolean FUZZY_TRANSPOSITIONS = true;
    private static final int MAX_EXPANSIONS = 500;
    private static final int PREFIX_LENGTH = 3;
    private static final String FUZZY_MINIMUM_MATCH = "50%";
    private static final int INVALID_PAGE = -1;

    @InjectMocks
    @Spy
    private SearchGroupCmd cmd = new SearchGroupCmd();

    @Mock
    private UserServiceHelper userHelper;

    @Mock
    private User user;

    @Mock
    GroupRepository groupRepository;

    @Mock
    private DocumentConverter documentConverter;

    @Mock
    Page<GroupDocument> page;

    @Mock
    private List<GroupDocument> doclist;

    @Mock
    private InternationalizationUtil i18nUtil;

    @Mock
    private GetGroupCounterCmd getGroupCounterCmd;

    @Mock
    private TextUtils textUtils;

    @Mock
    private Idiom idiom;

    @Mock
    private QueryUtils queryUtils;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        initMocks(this);
        ReflectionTestUtils.setField(cmd, "defaultUnloggedLatitude", LATITUDE);
        ReflectionTestUtils.setField(cmd, "defaultUnloggedLongitude", LONGITUDE);
    }

    @Test
    public void givenInvalidUserTokenWhenExecutingThenException() {
        SearchGroupRequest request = new SearchGroupRequest(INVALID_USER_TOKEN, LANG, PAGE, PAGE_SIZE);
        doThrow(ServiceException.class).when(userHelper).findAuthorizedUserByToken(INVALID_USER_TOKEN);
        thrown.expect(ServiceException.class);
        cmd.execute(request);
    }

    @Test
    public void givenValidUserTokenWhenNoResultsThenSearch() {
        SearchGroupRequest request = new SearchGroupRequest(VALID_USER_TOKEN, LANG, PAGE, PAGE_SIZE);
        when(userHelper.findAuthorizedUserByToken(VALID_USER_TOKEN)).thenReturn(user);
        when(groupRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);

        SearchGroupResponse response = cmd.execute(request);
        assertThat("No results must be found", response.getGroups().size(), is(0));
        verify(groupRepository).search(any(SearchQuery.class));
    }

    @Test
    public void givenValidUserTokenWhenRuntimeExceptionThenException() {
        doThrow(new RuntimeException()).when(groupRepository).search(any(SearchQuery.class));
        SearchGroupRequest request = new SearchGroupRequest(VALID_USER_TOKEN, LANG, PAGE, PAGE_SIZE);
        when(userHelper.findAuthorizedUserByToken(VALID_USER_TOKEN)).thenReturn(user);

        try {
            cmd.execute(request);
            fail(MSG);
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.SEARCH_TERM_IS_NOT_SPECIFIC_ENOUGH.getCode(),
                    e.getCode());
        }
    }

    @Test
    public void givenValidUserTokenWhenResultsThenSearch() {
        SearchGroupRequest request = new SearchGroupRequest(VALID_USER_TOKEN, LANG, PAGE, PAGE_SIZE);
        when(userHelper.findAuthorizedUserByToken(VALID_USER_TOKEN)).thenReturn(user);
        when(groupRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);

        List<Group> groupList = Arrays.asList(mock(Group.class), mock(Group.class), mock(Group.class));
        when(documentConverter.fromGroupDocument(doclist)).thenReturn(groupList);

        SearchGroupResponse response = cmd.execute(request);

        assertThat("This valid search must return three results", response.getNumberOfElements(), is(groupList.size()));
    }

    @Test
    public void givenNoUserWhenResultsThenSearch() {
        SearchGroupRequest request = new SearchGroupRequest(null, LANG, PAGE, PAGE_SIZE);
        when(groupRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);

        List<Group> groupList = Arrays.asList(mock(Group.class), mock(Group.class), mock(Group.class));
        when(documentConverter.fromGroupDocument(doclist)).thenReturn(groupList);

        SearchGroupResponse response = cmd.execute(request);

        assertThat("This valid search must return three results", response.getNumberOfElements(), is(groupList.size()));
    }

    @Test
    public void givenNoUserAndNoLangWhenNoResultsThenSearch() {
        SearchGroupRequest request = new SearchGroupRequest(null, null, PAGE, PAGE_SIZE);
        when(groupRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);

        SearchGroupResponse response = cmd.execute(request);

        assertThat("No results must be found", response.getGroups().size(), is(0));
        verify(groupRepository).search(any(SearchQuery.class));
    }

    @Test
    public void givenUserAndNoLangAndNoResultsWhenExecutingThenSearch() {
        SearchGroupRequest request = new SearchGroupRequest(VALID_USER_TOKEN, null, PAGE, PAGE_SIZE);
        when(userHelper.findAuthorizedUserByToken(VALID_USER_TOKEN)).thenReturn(user);
        when(user.getIdiom()).thenReturn(idiom);
        when(idiom.getCode()).thenReturn(ParkConstants.DEFAULT_LANGUAGE);
        when(groupRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);

        SearchGroupResponse response = cmd.execute(request);

        assertThat("No results must be found", response.getGroups().size(), is(0));
        verify(groupRepository).search(any(SearchQuery.class));
    }

    @Test
    public void givenInvalidRadiusWhenNoResultsThenSearchWithoutRadius() {
        SearchGroupRequest request = new SearchGroupRequest(VALID_USER_TOKEN, LANG, PAGE, PAGE_SIZE);
        request.setRadius(INVALID_RADIUS);
        when(userHelper.findAuthorizedUserByToken(VALID_USER_TOKEN)).thenReturn(user);
        when(groupRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);

        SearchGroupResponse response = cmd.execute(request);
        assertThat("No results must be found", response.getGroups().size(), is(0));
        verify(queryUtils, never()).buildDistanceFilter(request);
    }

    @Test
    public void givenValidRadiusAndDefaultLatAndLonWhenNoResultsThenSearchWithRadius() {
        SearchGroupRequest request = mock(SearchGroupRequest.class);
        when(request.getLanguage()).thenReturn(LANG);
        when(request.getPage()).thenReturn(PAGE);
        when(request.getPageSize()).thenReturn(PAGE_SIZE);
        when(request.getToken()).thenReturn(VALID_USER_TOKEN);
        when(request.getRadius()).thenReturn(VALID_RADIUS);

        when(userHelper.findAuthorizedUserByToken(VALID_USER_TOKEN)).thenReturn(user);
        when(groupRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);

        cmd.execute(request);
        verify(queryUtils).buildDistanceFilter(request);
    }

    @Test
    public void givenIsOnlyOwnedWhenExecutingThenSearch() {
        SearchGroupRequest request = mock(SearchGroupRequest.class);
        when(request.getLanguage()).thenReturn(LANG);
        when(request.getToken()).thenReturn(VALID_USER_TOKEN);
        when(request.getPage()).thenReturn(PAGE);
        when(request.getPageSize()).thenReturn(PAGE_SIZE);
        when(request.isOnlyOwned()).thenReturn(true);

        when(userHelper.findAuthorizedUserByToken(VALID_USER_TOKEN)).thenReturn(user);
        when(user.getUsername()).thenReturn(USERNAME);
        when(groupRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);

        cmd.execute(request);

        verify(groupRepository).search(any(SearchQuery.class));
        verify(getGroupCounterCmd).execute(any(GetGroupCounterRequest.class));
        verify(cmd).addOnlyOwnedItemsFilter(USERNAME);
    }

    @Test
    public void givenFindOnlyUserFollowsGroupWhenNoResultsThenSearch() {
        SearchGroupRequest request = mock(SearchGroupRequest.class);
        when(request.getLanguage()).thenReturn(LANG);
        when(request.getToken()).thenReturn(VALID_USER_TOKEN);
        when(request.getPage()).thenReturn(PAGE);
        when(request.getPageSize()).thenReturn(PAGE_SIZE);
        when(request.findOnlyUserFollowsGroup()).thenReturn(true);
        when(user.getId()).thenReturn(USER_ID);

        when(userHelper.findAuthorizedUserByToken(VALID_USER_TOKEN)).thenReturn(user);
        when(groupRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);

        cmd.execute(request);
        verify(groupRepository).search(any(SearchQuery.class));
        verify(getGroupCounterCmd).execute(any(GetGroupCounterRequest.class));
        verify(cmd).addOnlyUserFollowsGroup(USER_ID);
    }

    @Test
    public void givenNoPageValuesWhenExecutingThenSearchWithDefaultPageValues() {
        ReflectionTestUtils.setField(cmd, "defaultPageSize", PAGE_SIZE);

        SearchGroupRequest request = mock(SearchGroupRequest.class);
        when(request.getLanguage()).thenReturn(LANG);
        when(request.getToken()).thenReturn(VALID_USER_TOKEN);
        when(request.getPage()).thenReturn(null);
        when(request.getPageSize()).thenReturn(null);

        when(userHelper.findAuthorizedUserByToken(VALID_USER_TOKEN)).thenReturn(user);
        when(groupRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);

        cmd.execute(request);
        verify(groupRepository).search(any(SearchQuery.class));
    }

    @Test
    public void givenInvalidPageIndexWhenENoResultsThenSearchWithDefaultValue() {
        ReflectionTestUtils.setField(cmd, "defaultPageSize", PAGE_SIZE);

        SearchGroupRequest request = mock(SearchGroupRequest.class);
        when(request.getLanguage()).thenReturn(LANG);
        when(request.getToken()).thenReturn(VALID_USER_TOKEN);
        when(request.getPage()).thenReturn(INVALID_PAGE);
        when(userHelper.findAuthorizedUserByToken(INVALID_USER_TOKEN)).thenReturn(null);
        when(groupRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);

        cmd.execute(request);
        verify(groupRepository).search(any(SearchQuery.class));
    }

    @Test
    public void givenNullPageIndexWhenENoResultsThenSearchWithDefaultValue() {
        ReflectionTestUtils.setField(cmd, "defaultPageSize", PAGE_SIZE);

        SearchGroupRequest request = mock(SearchGroupRequest.class);
        when(request.getLanguage()).thenReturn(LANG);
        when(request.getToken()).thenReturn(VALID_USER_TOKEN);
        when(request.getPage()).thenReturn(null);


        when(userHelper.findAuthorizedUserByToken(VALID_USER_TOKEN)).thenReturn(user);
        when(groupRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);

        SearchGroupResponse response = cmd.execute(request);
        verify(groupRepository).search(any(SearchQuery.class));
    }

    @Test
    public void givenInvalidPageSizeWhenENoResultsThenSearchWithDefaultValue() {
        ReflectionTestUtils.setField(cmd, "defaultPageSize", PAGE_SIZE);

        SearchGroupRequest request = mock(SearchGroupRequest.class);
        when(request.getLanguage()).thenReturn(LANG);
        when(request.getToken()).thenReturn(VALID_USER_TOKEN);
        when(request.getPageSize()).thenReturn(INVALID_PAGE_SIZE);

        when(userHelper.findAuthorizedUserByToken(VALID_USER_TOKEN)).thenReturn(user);
        when(groupRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);

        SearchGroupResponse response = cmd.execute(request);
        verify(groupRepository).search(any(SearchQuery.class));
    }

    @Test
    public void givenValidRequestTimeAndNoResultsWhenExecutingThenSearchWithTimeFilter() {
        SearchGroupRequest request = mock(SearchGroupRequest.class);
        when(request.getLanguage()).thenReturn(LANG);
        when(request.getToken()).thenReturn(VALID_USER_TOKEN);
        when(request.getPage()).thenReturn(PAGE);
        when(request.getPageSize()).thenReturn(PAGE_SIZE);
        when(request.getRequestTime()).thenReturn(VALID_REQUEST_TIME);

        when(userHelper.findAuthorizedUserByToken(VALID_USER_TOKEN)).thenReturn(user);
        when(groupRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);

        cmd.execute(request);
        verify(request, atLeastOnce()).getRequestTime();
        verify(cmd).buildTimeFilter(VALID_REQUEST_TIME);
    }

    @Test
    @Ignore
    public void givenInvalidRequestTimeWhenNoResultsThenSearchWithoutTime() {
        SearchGroupRequest request = mock(SearchGroupRequest.class);
        when(request.getLanguage()).thenReturn(LANG);
        when(request.getToken()).thenReturn(VALID_USER_TOKEN);
        when(request.getPage()).thenReturn(PAGE);
        when(request.getPageSize()).thenReturn(PAGE_SIZE);
        when(request.getRequestTime()).thenReturn(INVALID_REQUEST_TIME);

        when(userHelper.findAuthorizedUserByToken(VALID_USER_TOKEN)).thenReturn(user);
        when(groupRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);

        cmd.execute(request);
        verify(request, atLeastOnce()).getRequestTime();
        verify(cmd, never()).buildTimeFilter(anyString());
    }

    @Test
    public void givenNameOrderTokenWhenExecutingThenSearchByName() {
        SearchGroupRequest request = new SearchGroupRequest(VALID_USER_TOKEN, LANG, PAGE, PAGE_SIZE);
        request.setOrder(NAME_ORDER);
        when(userHelper.findAuthorizedUserByToken(VALID_USER_TOKEN)).thenReturn(user);
        when(groupRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);

        SearchGroupResponse response = cmd.execute(request);
        verify(groupRepository).search(any(SearchQuery.class));
        verify(cmd).getOrderByGroupName();
    }

  @Test
  public void givenOtherOrderTokenWhenNoResultsThenSearchByNearest() {
      SearchGroupRequest request = new SearchGroupRequest(VALID_USER_TOKEN, LANG, PAGE, PAGE_SIZE);
      request.setLongitude(LONGITUDE);
      request.setLatitude(LATITUDE);
      request.setOrder(OTHER_ORDER);
      when(userHelper.findAuthorizedUserByToken(VALID_USER_TOKEN)).thenReturn(user);
      when(groupRepository.search(any(SearchQuery.class))).thenReturn(page);
      when(page.getContent()).thenReturn(doclist);

      cmd.execute(request);
      verify(groupRepository).search(any(SearchQuery.class));
      verify(cmd).getOrderByNearest(LATITUDE, LONGITUDE);
  }

    @Test
    public void givenNullOrderTokenWhenNoResultsThenSearchByNearest() {
        SearchGroupRequest request = new SearchGroupRequest(VALID_USER_TOKEN, LANG, PAGE, PAGE_SIZE);
        request.setLongitude(LONGITUDE);
        request.setLatitude(LATITUDE);
        request.setOrder(OTHER_ORDER);
        when(userHelper.findAuthorizedUserByToken(VALID_USER_TOKEN)).thenReturn(user);
        when(groupRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);

       cmd.execute(request);
        verify(groupRepository).search(any(SearchQuery.class));
        verify(cmd).getOrderByNearest(LATITUDE, LONGITUDE);
    }

    @Test
    public void givenCriteriaWhenNoResultsThenSearchWithCriteria() {
        ReflectionTestUtils.setField(cmd, "fuzziness", FUZZINESS);
        ReflectionTestUtils.setField(cmd, "fuzzyTranspositions", FUZZY_TRANSPOSITIONS);
        ReflectionTestUtils.setField(cmd, "maxExpansions", MAX_EXPANSIONS);
        ReflectionTestUtils.setField(cmd, "prefixLength", PREFIX_LENGTH);
        ReflectionTestUtils.setField(cmd, "fuzzyMinimumMatch", FUZZY_MINIMUM_MATCH);

        SearchGroupRequest request = mock(SearchGroupRequest.class);
        when(request.getLanguage()).thenReturn(LANG);
        when(request.getToken()).thenReturn(VALID_USER_TOKEN);
        when(request.getPage()).thenReturn(PAGE);
        when(request.getPageSize()).thenReturn(PAGE_SIZE);
        when(request.getCriteria()).thenReturn(CRITERIA);

        when(userHelper.findAuthorizedUserByToken(VALID_USER_TOKEN)).thenReturn(user);
        when(groupRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);

        cmd.execute(request);
        verify(request, atLeastOnce()).getCriteria();
        verify(cmd).buildCriteriaQuery(anyString());
    }

    @Test
    public void givenNoCriteriaWhenNoResultsThenSearchWithoutCriteria() {
        SearchGroupRequest request = mock(SearchGroupRequest.class);
        when(request.getLanguage()).thenReturn(LANG);
        when(request.getToken()).thenReturn(VALID_USER_TOKEN);
        when(request.getPage()).thenReturn(PAGE);
        when(request.getPageSize()).thenReturn(PAGE_SIZE);

        when(userHelper.findAuthorizedUserByToken(VALID_USER_TOKEN)).thenReturn(user);
        when(groupRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);

        cmd.execute(request);
        verify(request, atLeastOnce()).getCriteria();
        verify(cmd, never()).buildCriteriaQuery(anyString());
    }
}
