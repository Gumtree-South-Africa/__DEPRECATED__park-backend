package com.ebay.park.sitemap.context;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.InjectMocks;

import com.ebay.park.sitemap.model.URLSet;

/**
 * Unit test for {@link LocationAndCategoriesSitemapComponentBuilderCaller}
 * @author Julieta Salvadó
 *
 */
public class LocationAndCategoriesXMLBuilderCallerTest {

    private static final String URL1 = "test1";
    private static final String URL2 = "test2";
    private static final String URL3 = "test3";

    @InjectMocks
    private LocationAndCategoriesSitemapComponentBuilderCaller caller = new LocationAndCategoriesSitemapComponentBuilderCaller();

    @Test
    public void givenEmptyURLsWhenBuildingXMLThenNull() {
        URLSet response = caller.buildSitemapComponent(new ArrayList<String>());
        assertNull(response);
    }

    @Test
    public void givenNullListWhenBuildingXMLThenNull() {
        URLSet response = caller.buildSitemapComponent(null);
        assertNull(response);
    }

    @Test
    public void givenValidURLListWhenBuildingXMLThenReturnXML() {
        URLSet response = caller.buildSitemapComponent(Arrays.asList(URL1, URL2, URL3));
        assertNotNull(response);
    }

}
