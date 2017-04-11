package com.ebay.park.service.moderationMode.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import com.ebay.park.db.dao.CategoryDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Category;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.moderationMode.dto.ApplyFiltersForModerationModeRequest;
import com.ebay.park.util.DataCommonUtil;

public class ApplyFiltersForModerationModeValidatorTest {

    private static final long CATEGORY_ID = 5;

    private static final String VALID_DATE = "1410881711525";

    private static final String MSG = "An exception was expected";

    @InjectMocks
    @Spy
    private ApplyFiltersForModerationModeValidator validator;
    
    @Mock
    private ApplyFiltersForModerationModeRequest request;
    
    @Mock
    private CategoryDao categoryDao;
    
    @Mock
    private UserDao userDao;
    
    @Mock
    private Category category;
    
    @Mock
    private User user;

    @Before
    public void setUp() {
        initMocks(this);
    }
    
    @Test
    public void givenNullCategoryIdWhenValidatingThenSuccess() {
        when(request.getCategoryId()).thenReturn(null);
        validator.validate(request);
        verify(request).getCategoryId();
    }
    
    @Test
    public void givenInvalidCategoryIdWhenValidatingThenException() {
        when(request.getCategoryId()).thenReturn(CATEGORY_ID);
        when(categoryDao.findOne(CATEGORY_ID)).thenReturn(null);

        try {
            validator.validate(request);
            fail(MSG);
        } catch (ServiceException e) {
            assertEquals(ServiceExceptionCode.CATEGORY_NOT_FOUND.getCode(), e.getCode());
        }
    }
    
    @Test
    public void givenValidRequestWhenValidatingThenSuccess() {
        when(category.getCategoryId()).thenReturn(CATEGORY_ID);
        when(request.getCategoryId()).thenReturn(CATEGORY_ID);
        when(categoryDao.findOne(CATEGORY_ID)).thenReturn(category);
        when(request.getItemLastUpdatedFrom()).thenReturn(DataCommonUtil.parseUnixTime(VALID_DATE));

        validator.validate(request);

        verify(request, atLeastOnce()).getCategoryId();
    }
}
