package com.ebay.park.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link InternationalizationUtil}.
 * @author Julieta Salvadó
 */
public class InternationalizationUtilTest {

    private static final String LANG_PARAM = "es";
    @InjectMocks
    private InternationalizationUtil util;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNullNameWhenInternationalizingCategoryNameThenException() {
        util.internationalizeCategoryName(null, LANG_PARAM);
    }
}