package com.ebay.park.sitemap.context;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Unit test for {@link LocationAndCategoriesXMLFileNameBuilder}
 * @author Julieta Salvadó
 *
 */
public class LocationAndCategoriesXMLFileNameBuilderTest {

    private static final String FILE_NAME = "filename";
    private static final String EXTENSION = "xml";
    @InjectMocks
    private LocationAndCategoriesXMLFileNameBuilder builder = new LocationAndCategoriesXMLFileNameBuilder();

    @Before
    public void setUp() {
        initMocks(this);
        ReflectionTestUtils.setField(builder, "sitemapLocationsAndCategoriesFileName", FILE_NAME);
        ReflectionTestUtils.setField(builder, "sitemapFileExtension", EXTENSION);
    }

    @Test
    public void whenGeneratingFileNameThenReturnFileName() {
        String response = builder.getXMLFileName();
        assertEquals(FILE_NAME + "." + EXTENSION, response);
    }

}
