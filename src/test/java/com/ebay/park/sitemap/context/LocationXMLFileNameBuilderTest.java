package com.ebay.park.sitemap.context;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Unit test for {@link LocationXMLFileNameBuilder}
 * @author Julieta Salvadó
 *
 */
public class LocationXMLFileNameBuilderTest {

    private static final String FILE_NAME = "filename";
    private static final String EXTENSION = "xml";

    @InjectMocks
    private LocationXMLFileNameBuilder builder = new LocationXMLFileNameBuilder();

    @Before
    public void setUp() {
        initMocks(this);
        ReflectionTestUtils.setField(builder, "sitemapLocationsFileName", FILE_NAME);
        ReflectionTestUtils.setField(builder, "sitemapFileExtension", EXTENSION);
    }

    @Test
    public void whenGeneratingFileNameThenReturnFileName() {
        String response = builder.getXMLFileName();
        assertEquals(FILE_NAME + "." + EXTENSION, response);
    }

}
