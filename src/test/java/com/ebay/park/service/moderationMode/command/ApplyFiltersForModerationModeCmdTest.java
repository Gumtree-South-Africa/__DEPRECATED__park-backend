package com.ebay.park.service.moderationMode.command;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.test.util.ReflectionTestUtils;

import com.ebay.park.elasticsearch.document.converter.DocumentConverter;
import com.ebay.park.service.moderationMode.ModerationCacheHelper;
import com.ebay.park.service.moderationMode.dto.ApplyFiltersForModerationModeRequest;
import com.ebay.park.util.DataCommonUtil;

/**
 * 
 * @author Julieta Salvadó
 *
 */
public class ApplyFiltersForModerationModeCmdTest {

    private static final String USER_TOKEN = "abcdefg";
    
    private static final String USER_NAME = "userName";
    
    private static final String DESCRIPTION = "description";
    
    private static final String NAME = "name";

    private static final int PAGE_SIZE = 20;

    private static final long CATEGORY_ID = 1;

    private static final String VALID_DATE = "1410881711525";

    private static final List<Integer> ZIPCODE_LIST = Arrays.asList(7000, 90210);

    @InjectMocks
    @Spy
    private ApplyFiltersForModerationModeCmdImpl cmd;

    @Mock
    private ModerationCacheHelper moderationCacheHelper;

    @Mock
    private ElasticsearchOperations elasticsearchOperations;

    @Mock
    private DocumentConverter documentConverter;

    @Before
    public void setUp() {
        initMocks(this);
        doNothing().when(moderationCacheHelper).unlockUser(USER_TOKEN);
        ReflectionTestUtils.setField(cmd, "defaultPageSize", PAGE_SIZE);
    }

    @Test
    public void givenCategoryWhenExecutingThenFilter() {
        ApplyFiltersForModerationModeRequest request = mock(ApplyFiltersForModerationModeRequest.class);
        when(request.getToken()).thenReturn(USER_TOKEN);
        when(request.getCategoryId()).thenReturn(CATEGORY_ID);

        cmd.execute(request);
        verify(request, atLeastOnce()).getCategoryId();
    }

    @Test
    public void givenNullDateFromWhenExecutingThenFilterWithDefaultDateFrom() {
        ApplyFiltersForModerationModeRequest request = mock(ApplyFiltersForModerationModeRequest.class);
        when(request.getItemLastUpdatedFrom()).thenReturn(null);
        cmd.execute(request);
        verify(request, atLeastOnce()).getItemLastUpdatedFrom();
    }

    @Test
    public void givenValidDateFromWhenExecutingThenFilter() {
        ApplyFiltersForModerationModeRequest request = mock(ApplyFiltersForModerationModeRequest.class);
        when(request.getItemLastUpdatedFrom()).thenReturn(DataCommonUtil.parseUnixTime(VALID_DATE));
        cmd.execute(request);
        verify(request, atLeastOnce()).getItemLastUpdatedFrom();
    }

    @Test
    public void givenNullDateToWhenExecutingThenFilterWithDefaultDateTo() {
        ApplyFiltersForModerationModeRequest request = mock(ApplyFiltersForModerationModeRequest.class);
        when(request.getItemLastUpdatedTo()).thenReturn(null);
        cmd.execute(request);
        verify(request, atLeastOnce()).getItemLastUpdatedTo();
    }

    @Test
    public void givenValidDateToWhenExecutingThenFilter() {
        ApplyFiltersForModerationModeRequest request = mock(ApplyFiltersForModerationModeRequest.class);
        when(request.getItemLastUpdatedTo()).thenReturn(DataCommonUtil.parseUnixTime(VALID_DATE));
        cmd.execute(request);
        verify(request, atLeastOnce()).getItemLastUpdatedTo();
    }

    @Test
    public void givenValidZipCodeListWhenExecutingThenFilter() {
        ApplyFiltersForModerationModeRequest request = mock(ApplyFiltersForModerationModeRequest.class);
        when(request.getZipCodes()).thenReturn(ZIPCODE_LIST);

        cmd.execute(request);
        verify(request, atLeastOnce()).getZipCodes();
    }
    
    @Test
    public void givenUserNameWhenExecutingThenFilter() {
        ApplyFiltersForModerationModeRequest request = mock(ApplyFiltersForModerationModeRequest.class);
        when(request.getToken()).thenReturn(USER_TOKEN);
        when(request.getUserName()).thenReturn(USER_NAME);

        cmd.execute(request);
        verify(request, atLeastOnce()).getUserName();
    }
    
    @Test
    public void givenDescriptionWhenExecutingThenFilter() {
        ApplyFiltersForModerationModeRequest request = mock(ApplyFiltersForModerationModeRequest.class);
        when(request.getToken()).thenReturn(USER_TOKEN);
        when(request.getDescription()).thenReturn(DESCRIPTION);

        cmd.execute(request);
        verify(request, atLeastOnce()).getDescription();
    }
    
    @Test
    public void givenNameWhenExecutingThenFilter() {
        ApplyFiltersForModerationModeRequest request = mock(ApplyFiltersForModerationModeRequest.class);
        when(request.getToken()).thenReturn(USER_TOKEN);
        when(request.getName()).thenReturn(NAME);

        cmd.execute(request);
        verify(request, atLeastOnce()).getDescription();
    }
}
