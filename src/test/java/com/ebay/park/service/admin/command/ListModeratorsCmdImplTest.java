package com.ebay.park.service.admin.command;

import com.ebay.park.db.dao.UserAdminDao;
import com.ebay.park.db.entity.UserAdmin;
import com.ebay.park.service.PaginatedRequest;
import com.ebay.park.service.admin.dto.ListModeratorsResponse;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.InternationalizationUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link ListModeratorsCmdImpl}.
 */
public class ListModeratorsCmdImplTest {

    private static final String USR_TOKEN = "usrTok";
	private static final int DEFAULT_PAGE_SIZE = 3;
	private static final String USERNAME = "username";
    private static final String LANGUAGE = "en";

    @InjectMocks
	private ListModeratorsCmdImpl cmd = new ListModeratorsCmdImpl();

	@Mock
	private UserAdminDao userAdminDao;

	@Mock
	private SessionService sessionService;

	@Mock
	private InternationalizationUtil i18nUtil;
	
	@Before
	public void setUp(){
		initMocks(this);
        ReflectionTestUtils.setField(cmd, "defaultPageSize", DEFAULT_PAGE_SIZE);
		UserAdmin adminPage1 = new UserAdmin();
        UserAdmin adminPage2 = new UserAdmin();
        UserAdmin adminPage3 = new UserAdmin();
        UserAdmin adminPage4 = new UserAdmin();

		List<UserAdmin> adminContentList = new ArrayList<UserAdmin>();
		adminContentList.add(adminPage1);
        adminContentList.add(adminPage2);
        adminContentList.add(adminPage3);

        List<UserAdmin> adminContentListAll = new ArrayList<UserAdmin>();
        adminContentListAll.add(adminPage1);
        adminContentListAll.add(adminPage2);
        adminContentListAll.add(adminPage3);
        adminContentListAll.add(adminPage4);

		Page<UserAdmin> page = new PageImpl<UserAdmin>(adminContentList);
		
		Pageable pageable = new PageRequest(0, DEFAULT_PAGE_SIZE, new Sort(Sort.Direction.DESC, USERNAME));
		when(userAdminDao.findAll(pageable)).thenReturn(page);

        when(userAdminDao.findAll()).thenReturn(adminContentListAll);
	}

	@Test
	public void givenPageNumberAndPageSizeValuesWhenExecutingThenGetModeratorListPage() {
		PaginatedRequest request = new PaginatedRequest(USR_TOKEN, LANGUAGE, 0, DEFAULT_PAGE_SIZE);
		ListModeratorsResponse response = cmd.execute(request);
		assertThat(3L, is(response.getTotalElements()));
	}

    @Test
    public void givenNoPageNumberValuesWhenExecutingThenGetModeratorListPageNumber0() {
        PaginatedRequest request = new PaginatedRequest(USR_TOKEN, LANGUAGE, null, DEFAULT_PAGE_SIZE);
        ListModeratorsResponse response = cmd.execute(request);
        assertThat(3L, is(response.getTotalElements()));
    }

    @Test
    public void givenNoPageSizeAndPageNumberValuesWhenExecutingThenGetModeratorListDefaultPageSize() {
        PaginatedRequest request = new PaginatedRequest(USR_TOKEN, LANGUAGE, 0, null);
        ListModeratorsResponse response = cmd.execute(request);
        assertThat(3L, is(response.getTotalElements()));
    }

    @Test
    public void givenNoPageSizeAndPageNumberValuesWhenExecutingThenGetModeratorListAll() {
        PaginatedRequest request = new PaginatedRequest(USR_TOKEN, LANGUAGE, null, null);
        ListModeratorsResponse response = cmd.execute(request);
        assertThat(4L, is(response.getTotalElements()));
    }

    @Test
    public void givenEmptyPageWhenExecutingThenEmptyResponse() {
        Pageable pageable = new PageRequest(1, DEFAULT_PAGE_SIZE, new Sort(Sort.Direction.DESC, USERNAME));
        Page<UserAdmin> page = new PageImpl<UserAdmin>(new ArrayList<UserAdmin>());
        when(userAdminDao.findAll(pageable)).thenReturn(page);
        when(sessionService.getUserSession(USR_TOKEN)).thenReturn(mock(UserSessionCache.class));
        PaginatedRequest request = new PaginatedRequest(USR_TOKEN, LANGUAGE, 1, DEFAULT_PAGE_SIZE);
        ListModeratorsResponse response = cmd.execute(request);
        assertThat(0L, is(response.getTotalElements()));
    }
}
