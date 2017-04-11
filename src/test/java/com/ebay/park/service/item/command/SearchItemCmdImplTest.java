package com.ebay.park.service.item.command;

import com.ebay.park.db.entity.*;
import com.ebay.park.elasticsearch.document.ItemDocument;
import com.ebay.park.elasticsearch.document.converter.DocumentConverter;
import com.ebay.park.elasticsearch.repository.ItemRepository;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.group.dto.SearchGroupRequest;
import com.ebay.park.service.item.ItemUtils;
import com.ebay.park.service.item.dto.GetNewItemInformationRequest;
import com.ebay.park.service.item.dto.ItemSummary;
import com.ebay.park.service.item.dto.SearchItemRequest;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.util.InternationalizationUtil;
import com.ebay.park.util.ParkConstants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link SearchItemCmdImpl}.
 * @author Julieta Salvadó
 *
 */
public class SearchItemCmdImplTest {

    private static final int PAGE_SIZE                                        = 24;
    private static final int PAGE                                             = 0;

    private static final String USER_TOKEN                                    = "2234";
    private static final String INVALID_USER_TOKEN                            = "2235";

    private static final String LANG = ParkConstants.DEFAULT_LANGUAGE;
    private static final String MSG = "An exception was expected";
    private static final double LATITUDE = 10l;
    private static final double LONGITUDE = 10l;
    private static final long GROUP_ID = 1L;
    private static final int RADIUS = 20;
    private static final long CATEGORY_ID = 5L;
    private static final int INVALID_PAGE_SIZE = -1;
    private static final double TO = 1l;
    private static final double FROM = 2l;
    private static final String TIME = "1438444687";
    private static final String INVALID_TIME = "1438444687sss";
    private static final String PRICE_SORT = "price";
    private static final String PUBLISHED_SORT = "published";
    private static final String NEAREST_SORT = "nearest";
    private static final String CATEGORY_SORT = "category";
    private static final String OTHER_SORT = "other";
    private static final String INVERSE = "-";
    private static final int INVALID_RADIUS = -1;
    private static final long FOLLOWER_ID = 3l;
    private static final long ITEM_ID = 3l;

    @InjectMocks
    @Spy
    private SearchItemCmdImpl cmd;

    @Mock
    private User user;

    @Mock
    private UserServiceHelper userHelper;

    @Mock
    ItemRepository itemRepository;

    @Mock
    Page<ItemDocument> page;

    @Mock
    private DocumentConverter documentConverter;

    @Mock
    private List<ItemDocument> doclist;

    @Mock
    private List<Item> itemList;

    @Mock
    private Item item1;

    @Mock
    private ItemSummary itemSummary1;

    @Mock
    private Idiom idiom;

    @Mock
    private ItemUtils itemUtils;

    @Mock
    private InternationalizationUtil i18nUtil;

    @Mock
    private GetNewItemInformationCmd getNewItemInformationCmd;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        initMocks(this);
        when(userHelper.findAuthorizedUserByToken(USER_TOKEN)).thenReturn(user);
        when(user.getIdiom()).thenReturn(idiom);
        when(idiom.getCode()).thenReturn(ParkConstants.DEFAULT_LANGUAGE);
        ReflectionTestUtils.setField(cmd, "defaultUnloggedLatitude", LATITUDE);
        ReflectionTestUtils.setField(cmd, "defaultUnloggedLongitude", LONGITUDE);
        ReflectionTestUtils.setField(cmd, "defaultPageSizeMobile", PAGE_SIZE);
    }

    @Test
    public void givenUserUnauthorizedWhenExecutingThenException() {
        SearchItemRequest request = new SearchItemRequest(INVALID_USER_TOKEN, LANG, PAGE, PAGE_SIZE);
        when(userHelper.findAuthorizedUserByToken(INVALID_USER_TOKEN)).thenReturn(null);
        doThrow(ServiceException.class).when(userHelper).findAuthorizedUserByToken(INVALID_USER_TOKEN);
        thrown.expect(ServiceException.class);
        cmd.execute(request);
    }

//    @Test
//    public void givenLatAndLongWhenExecutingThenLatAndLongSearch() {
//        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
//        request.setLatitude(LATITUDE);
//        request.setLongitude(LONGITUDE);
//        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
//        when(page.getContent()).thenReturn(doclist);
//        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
//        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Arrays.asList(itemSummary1));
//
//        cmd.execute(request);
//
//        verify(userHelper).findAuthorizedUserByToken(USER_TOKEN);
//        verify(itemRepository).search(any(SearchQuery.class));
//        verify(page).getContent();
//        verify(documentConverter).fromItemDocument(doclist);
//        verify(itemUtils).convertToItemSummary(itemList, user, LANG);
//    }
//
//    @Test
//    public void givenNoLatAndLongWhenExecutingThenDefaultSearch() {
//        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
//        request.setTagNewItem(false);
//        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
//        when(page.getContent()).thenReturn(doclist);
//        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
//        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Arrays.asList(itemSummary1));
//
//        cmd.execute(request);
//
//        verify(userHelper).findAuthorizedUserByToken(USER_TOKEN);
//        verify(itemRepository).search(any(SearchQuery.class));
//        verify(page).getContent();
//        verify(documentConverter).fromItemDocument(doclist);
//        verify(itemUtils).convertToItemSummary(itemList, user, LANG);
//    }
//
    @Test
    public void givenLoggedAndNewTagAndGroupIdWhenExecutingThenAddTagInResults() {
        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
        request.setTagNewItem(true);
        request.setGroupId(GROUP_ID);
        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);
        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Collections.singletonList(itemSummary1));

        cmd.execute(request);

        verify(itemRepository).search(any(SearchQuery.class));
        verify(getNewItemInformationCmd).execute(any(GetNewItemInformationRequest.class));
    }

    @Test
    public void givenLoggedAndNewTagAndNotGroupIdWhenExecutingThenNoTagSearch() {
        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
        request.setTagNewItem(true);
        request.setGroupId(null);
        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);
        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Collections.singletonList(itemSummary1));

        cmd.execute(request);

        verify(itemRepository).search(any(SearchQuery.class));
        verify(getNewItemInformationCmd, never()).execute(any(GetNewItemInformationRequest.class));
    }

    @Test
    public void givenUnloggedAndNewTagWhenExecutingThenNotTagInResults() {
        SearchItemRequest request = new SearchItemRequest(null, LANG, PAGE, PAGE_SIZE);
        request.setTagNewItem(true);
        request.setGroupId(GROUP_ID);
        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);
        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
        when(itemUtils.convertToItemSummary(itemList, null, null)).thenReturn(Arrays.asList(itemSummary1));

        cmd.execute(request);

        verify(page).getContent();
        verify(getNewItemInformationCmd, Mockito.never()).execute(any(GetNewItemInformationRequest.class));
    }

//    @Test
//    public void givenUnloggedAndNotLanguageWhenExecutingThenDefaultLanguageSearch() {
//        SearchItemRequest request = new SearchItemRequest(null, null, PAGE, PAGE_SIZE);
//        request.setTagNewItem(false);
//        request.setRadius(RADIUS);
//        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
//        when(page.getContent()).thenReturn(doclist);
//        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
//        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Arrays.asList(itemSummary1));
//
//        cmd.execute(request);
//
//        verify(itemRepository).search(any(SearchQuery.class));
//        verify(page).getContent();
//        verify(documentConverter).fromItemDocument(doclist);
//        verify(itemUtils).convertToItemSummary(itemList, null, null);
//    }
//
//    @Test
//    public void givenRadiusWhenExecutingThenRadiusSearch() {
//        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
//        request.setTagNewItem(false);
//        request.setRadius(RADIUS);
//        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
//        when(page.getContent()).thenReturn(doclist);
//        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
//        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Arrays.asList(itemSummary1));
//
//        cmd.execute(request);
//
//        verify(userHelper).findAuthorizedUserByToken(USER_TOKEN);
//        verify(itemRepository).search(any(SearchQuery.class));
//        verify(page).getContent();
//        verify(documentConverter).fromItemDocument(doclist);
//        verify(itemUtils).convertToItemSummary(itemList, user, LANG);
//    }
//
//    @Test
//    public void givenInvalidRadiusWhenExecutingThenNoRadiusSearch() {
//        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
//        request.setTagNewItem(false);
//        request.setRadius(INVALID_RADIUS);
//        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
//        when(page.getContent()).thenReturn(doclist);
//        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
//        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Arrays.asList(itemSummary1));
//
//        cmd.execute(request);
//
//        verify(userHelper).findAuthorizedUserByToken(USER_TOKEN);
//        verify(itemRepository).search(any(SearchQuery.class));
//        verify(page).getContent();
//        verify(documentConverter).fromItemDocument(doclist);
//        verify(itemUtils).convertToItemSummary(itemList, user, LANG);
//    }
//
    @Test
    public void givenCategoryWhenExecutingThenCategorySearch() {
        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
        request.setTagNewItem(false);
        request.setCategoryId(CATEGORY_ID);
        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);
        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Collections.singletonList(itemSummary1));

        cmd.execute(request);
        verify(itemRepository).search(any(SearchQuery.class));
        verify(cmd).buildCategoryFilter(CATEGORY_ID);
    }

    @Test
    public void givenPriceRangeWhenExecutingThenPriceSearch() {
        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
        request.setTagNewItem(false);
        request.setPriceFrom(FROM);
        request.setPriceTo(TO);
        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);
        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Collections.singletonList(itemSummary1));

        cmd.execute(request);

        verify(itemRepository).search(any(SearchQuery.class));
        verify(cmd).buildPriceFilter(FROM, TO);
    }

    @Test
    public void givenPriceToWhenExecutingThenPriceSearch() {
        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
        request.setTagNewItem(false);
        request.setPriceTo(TO);
        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);
        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Collections.singletonList(itemSummary1));

        cmd.execute(request);

        verify(itemRepository).search(any(SearchQuery.class));
        verify(cmd).buildPriceFilter(null, TO);
    }

    @Test
    public void givenPriceFromWhenExecutingThenPriceSearch() {
        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
        request.setTagNewItem(false);
        request.setPriceFrom(FROM);
        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);
        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Collections.singletonList(itemSummary1));

        cmd.execute(request);

        verify(itemRepository).search(any(SearchQuery.class));
        verify(cmd).buildPriceFilter(FROM, null);
    }

    @Test
    public void givenValidRequestTimeWhenExecutingThenSearchWithTimeFilter() {
        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
        request.setTagNewItem(false);
        request.setRequestTime(TIME);
        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
        when(page.getContent()).thenReturn(doclist);
        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Arrays.asList(itemSummary1));

        cmd.execute(request);

        verify(itemRepository).search(any(SearchQuery.class));
        verify(cmd).addTimeFilter(TIME);
    }
//
//    @Test
//    public void givenNoPageSizeWhenExecutingThenDefaultSearch() {
//        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, null, null);
//        request.setTagNewItem(false);
//        request.setRadius(RADIUS);
//        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
//        when(page.getContent()).thenReturn(doclist);
//        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
//        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Arrays.asList(itemSummary1));
//
//        cmd.execute(request);
//
//        verify(userHelper).findAuthorizedUserByToken(USER_TOKEN);
//        verify(itemRepository).search(any(SearchQuery.class));
//        verify(page).getContent();
//        verify(documentConverter).fromItemDocument(doclist);
//        verify(itemUtils).convertToItemSummary(itemList, user, LANG);
//    }
//
//    @Test
//    public void givenInvalidPageSizeWhenExecutingThenDefaultSearch() {
//        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE,
//                INVALID_PAGE_SIZE);
//        request.setTagNewItem(false);
//        request.setRadius(RADIUS);
//        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
//        when(page.getContent()).thenReturn(doclist);
//        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
//        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Arrays.asList(itemSummary1));
//
//        cmd.execute(request);
//
//        verify(userHelper).findAuthorizedUserByToken(USER_TOKEN);
//        verify(itemRepository).search(any(SearchQuery.class));
//        verify(page).getContent();
//        verify(documentConverter).fromItemDocument(doclist);
//        verify(itemUtils).convertToItemSummary(itemList, user, LANG);
//    }
//
//    /**
//     * Sort options:
//       String ORDER_PUBLISHED = "published";
//       String ORDER_PRICE = "price";
//       String ORDER_NEAREST = "nearest";
//       String ORDER_CATEGORY = "category";
//     */
//    @Test
//    public void givenPublishedSortingWhenExecutingThenPublishedSortAndSearch() {
//        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
//        request.setTagNewItem(false);
//        request.setOrder(PUBLISHED_SORT);
//        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
//        when(page.getContent()).thenReturn(doclist);
//        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
//        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Arrays.asList(itemSummary1));
//
//        cmd.execute(request);
//
//        verify(userHelper).findAuthorizedUserByToken(USER_TOKEN);
//        verify(itemRepository).search(any(SearchQuery.class));
//        verify(page).getContent();
//        verify(documentConverter).fromItemDocument(doclist);
//        verify(itemUtils).convertToItemSummary(itemList, user, LANG);
//    }
//
//    @Test
//    public void givenNearestSortingWhenExecutingThenNearestSortAndSearch() {
//        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
//        request.setTagNewItem(false);
//        request.setOrder(NEAREST_SORT);
//        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
//        when(page.getContent()).thenReturn(doclist);
//        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
//        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Arrays.asList(itemSummary1));
//
//        cmd.execute(request);
//
//        verify(userHelper).findAuthorizedUserByToken(USER_TOKEN);
//        verify(itemRepository).search(any(SearchQuery.class));
//        verify(page).getContent();
//        verify(documentConverter).fromItemDocument(doclist);
//        verify(itemUtils).convertToItemSummary(itemList, user, LANG);
//    }
//
//    @Test
//    public void givenPriceSortingWhenExecutingThenPriceSortAndSearch() {
//        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
//        request.setTagNewItem(false);
//        request.setOrder(PRICE_SORT);
//        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
//        when(page.getContent()).thenReturn(doclist);
//        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
//        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Arrays.asList(itemSummary1));
//
//        cmd.execute(request);
//
//        verify(userHelper).findAuthorizedUserByToken(USER_TOKEN);
//        verify(itemRepository).search(any(SearchQuery.class));
//        verify(page).getContent();
//        verify(documentConverter).fromItemDocument(doclist);
//        verify(itemUtils).convertToItemSummary(itemList, user, LANG);
//    }
//
//    @Test
//    public void givenCategorySortingWhenExecutingThenCategorySortAndSearch() {
//        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
//        request.setTagNewItem(false);
//        request.setOrder(CATEGORY_SORT);
//        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
//        when(page.getContent()).thenReturn(doclist);
//        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
//        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Arrays.asList(itemSummary1));
//
//        cmd.execute(request);
//
//        verify(userHelper).findAuthorizedUserByToken(USER_TOKEN);
//        verify(itemRepository).search(any(SearchQuery.class));
//        verify(page).getContent();
//        verify(documentConverter).fromItemDocument(doclist);
//        verify(itemUtils).convertToItemSummary(itemList, user, LANG);
//    }
//
//    @Test
//    public void givenOtherSortingWhenExecutingThenDefaultSortAndSearch() {
//        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
//        request.setTagNewItem(false);
//        request.setOrder(OTHER_SORT);
//        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
//        when(page.getContent()).thenReturn(doclist);
//        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
//        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Arrays.asList(itemSummary1));
//
//        cmd.execute(request);
//
//        verify(userHelper).findAuthorizedUserByToken(USER_TOKEN);
//        verify(itemRepository).search(any(SearchQuery.class));
//        verify(page).getContent();
//        verify(documentConverter).fromItemDocument(doclist);
//        verify(itemUtils).convertToItemSummary(itemList, user, LANG);
//    }
//
//    @Test
//    public void givenInverseSortingWhenExecutingThenInverseSortAndSearch() {
//        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
//        request.setTagNewItem(false);
//        request.setOrder(INVERSE + PUBLISHED_SORT);
//        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
//        when(page.getContent()).thenReturn(doclist);
//        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
//        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Arrays.asList(itemSummary1));
//
//        cmd.execute(request);
//
//        verify(userHelper).findAuthorizedUserByToken(USER_TOKEN);
//        verify(itemRepository).search(any(SearchQuery.class));
//        verify(page).getContent();
//        verify(documentConverter).fromItemDocument(doclist);
//        verify(itemUtils).convertToItemSummary(itemList, user, LANG);
//    }
//
//    @Test
//    public void givenFromFollowersWhenExecutingThenFromFollowersSearch() {
//        Follower follower = mock(Follower.class);
//        FollowerPK followerPK = mock(FollowerPK.class);
//
//        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
//        request.setFromFollowedUsers(true);
//        request.setTagNewItem(true);
//        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
//        when(page.getContent()).thenReturn(doclist);
//        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
//        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Arrays.asList(itemSummary1));
//        when(user.getFollowed()).thenReturn(Arrays.asList(follower));
//        when(follower.getId()).thenReturn(followerPK);
//        when(followerPK.getUserId()).thenReturn(FOLLOWER_ID);
//
//        cmd.execute(request);
//
//        verify(userHelper).findAuthorizedUserByToken(USER_TOKEN);
//        verify(itemRepository).search(any(SearchQuery.class));
//        verify(page).getContent();
//        verify(documentConverter).fromItemDocument(doclist);
//        verify(itemUtils).convertToItemSummary(itemList, user, LANG);
//        verify(user).getFollowed();
//        verify(follower).getId();
//        verify(followerPK).getUserId();
//    }
//
//    @Test
//    public void givenFromUserWishlistWhenExecutingThenFromUserWishlistSearch() {
//        UserFollowsItem userFollowsItem = mock(UserFollowsItem.class);
//        Item item = mock(Item.class);
//        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
//        request.setTagNewItem(true);
//        request.setFromUserWishlist(true);
//        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
//        when(page.getContent()).thenReturn(doclist);
//        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
//        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Arrays.asList(itemSummary1));
//        when(user.getItemLikeds()).thenReturn(Arrays.asList(userFollowsItem));
//        when(userFollowsItem.getItem()).thenReturn(item);
//        when(item.getId()).thenReturn(ITEM_ID);
//
//        cmd.execute(request);
//
//        verify(userHelper).findAuthorizedUserByToken(USER_TOKEN);
//        verify(itemRepository).search(any(SearchQuery.class));
//        verify(page).getContent();
//        verify(documentConverter).fromItemDocument(doclist);
//        verify(itemUtils).convertToItemSummary(itemList, user, LANG);
//        verify(user).getItemLikeds();
//        verify(userFollowsItem, atLeastOnce()).getItem();
//    }
//
//    @Test
//    public void givenFromFollowedGroupsWhenExecutingThenFromFollowedGroupsSearch() {
//        UserFollowsGroup userFollowsGroup = mock(UserFollowsGroup.class);
//        UserFollowsGroupPK userFollowsGroupPK = mock(UserFollowsGroupPK.class);
//        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
//        request.setFromFollowedGroups(true);
//        request.setTagNewItem(true);
//        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
//        when(page.getContent()).thenReturn(doclist);
//        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
//        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Arrays.asList(itemSummary1));
//        when(user.getGroups()).thenReturn(Arrays.asList(userFollowsGroup));
//        when(userFollowsGroup.getId()).thenReturn(userFollowsGroupPK);
//        when(userFollowsGroupPK.getGroupId()).thenReturn(GROUP_ID);
//
//        cmd.execute(request);
//
//        verify(userHelper).findAuthorizedUserByToken(USER_TOKEN);
//        verify(itemRepository).search(any(SearchQuery.class));
//        verify(page).getContent();
//        verify(documentConverter).fromItemDocument(doclist);
//        verify(itemUtils).convertToItemSummary(itemList, user, LANG);
//        verify(user).getGroups();
//        verify(userFollowsGroup).getId();
//        verify(userFollowsGroupPK).getGroupId();
//    }
//
//    @Test
//    public void givenTimeConversionExceptionWhenExecutingThenNormalSearch() {
//        SearchItemRequest request = new SearchItemRequest(USER_TOKEN, LANG, PAGE, PAGE_SIZE);
//        request.setRequestTime(INVALID_TIME);
//        when(itemRepository.search(any(SearchQuery.class))).thenReturn(page);
//        when(page.getContent()).thenReturn(doclist);
//        when(documentConverter.fromItemDocument(doclist)).thenReturn(itemList);
//        when(itemUtils.convertToItemSummary(itemList, user, LANG)).thenReturn(Arrays.asList(itemSummary1));
//
//        cmd.execute(request);
//        verify(userHelper).findAuthorizedUserByToken(USER_TOKEN);
//        verify(itemRepository).search(any(SearchQuery.class));
//        verify(page).getContent();
//        verify(documentConverter).fromItemDocument(doclist);
//        verify(itemUtils).convertToItemSummary(itemList, user, LANG);
//    }
}