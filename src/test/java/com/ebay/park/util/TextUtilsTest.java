package com.ebay.park.util;

import com.ebay.park.db.entity.Category;
import com.ebay.park.db.entity.Item;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit Test for {@link TextUtils}
 * @author Julieta Salvadó
 */
public class TextUtilsTest {
    private static final String FILENAME = "filename";
    private static final String PATH = "path";
    private static final String LANG = "es";
    private static final String CATEGORY = "category";
    private static final String ITEM_NAME = "name";
    private static final long ITEM_ID = 1L;
    private static final String INTERNATIONALIZED = "internationalized";
    private static final String DOUBLE_QUOTES = "\"";
    @InjectMocks
    @Spy
    private TextUtils utils;

    @Mock
    private InternationalizationUtil i18nUtil;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        ReflectionTestUtils.setField(utils, "itemURL", "url");
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullPathWhenBuildingFullPathThenException() {
        utils.buildFullPath(null, FILENAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullFileNameWhenBuildingFullPathThenException() {
        utils.buildFullPath(PATH, null);
    }

    @Test
    public void givenValidEntriesWhenBuildingFullPathThenBuild() {
        String response = utils.buildFullPath(PATH, FILENAME);
        assertEquals(String.format("%s/%s", PATH, FILENAME), response);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullItemWhenCreatingItemSEOURLFromItemThenException() {
        utils.createItemSEOURL(null, LANG);
    }

    @Test
    public void givenValidEntriesWhenCreatingItemSEOURLFromItemThenReturnURL() {
        Item item = mock(Item.class);
        Category category = mock(Category.class);
        when(item.getCategory()).thenReturn(category);
        when(category.getKey()).thenReturn(CATEGORY);
        when(item.getName()).thenReturn(ITEM_NAME);
        when(item.getId()).thenReturn(ITEM_ID);
        when(i18nUtil.internationalizeCategoryName(CATEGORY, LANG)).thenReturn(INTERNATIONALIZED);

        utils.createItemSEOURL(item, LANG);

        verify(utils).createItemSEOURL(INTERNATIONALIZED, ITEM_NAME, ITEM_ID);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void givenANullStringWhenDoubleQuoteThenException() {
    	utils.doubleQuote(null);
    }
    
    @Test
    public void givenAStringWhenDoubleQuoteThenSuccess() {
    	//given
    	String toDoubleQuote = "string";
    	String expectedResult = DOUBLE_QUOTES + toDoubleQuote + DOUBLE_QUOTES;
    	
    	//when
    	String result = utils.doubleQuote(toDoubleQuote);
    	
    	//then
    	assertEquals(result, expectedResult);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void givenANullStringWhenCapitalizeThenException() {
    	utils.capitalize(null);
    }
    
    @Test
    public void givenAStringWhenCapitalizeThenSuccess() {
    	//given
    	String toCapitalize = "string";
    	String expectedResult = "String";
    	
    	//when
    	String result = utils.capitalize(toCapitalize);
    	
    	//then
    	assertEquals(result, expectedResult);
    }
    
}