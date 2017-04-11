package com.ebay.park.sitemap.context;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.test.util.ReflectionTestUtils;
/**
 * Unit test for {@link LocationAndCategoriesSEOURLBuilder}.
 * @author Julieta Salvadó
 *
 */
public class LocationAndCategoriesSEOURLBuilderTest {

    @InjectMocks
    private LocationAndCategoriesSEOURLBuilder builder = new LocationAndCategoriesSEOURLBuilder();

    private static final String CITY1 = "Tandil";
    private static final String CITY2 = "Paris";
    private static final String HOST = "www.vivanuncios.us";
    private static final String COMPONENT = "search";

    private static final String CAT1 = "moda";
    private static final String CAT2 = "autos";

    private static final String SCHEME = "www";

    private List<String> cities = Arrays.asList(CITY1, CITY2);
    private List<String> categories = Arrays.asList(CAT1, CAT2);

    @Before
    public void setUp() {
        initMocks(this);
        ReflectionTestUtils.setField(builder, "cities", cities);
        ReflectionTestUtils.setField(builder, "host", HOST);
        ReflectionTestUtils.setField(builder, "scheme", SCHEME);
        ReflectionTestUtils.setField(builder, "component", COMPONENT);
        ReflectionTestUtils.setField(builder, "categories", categories);
    }

    @Test
    public void whenAskingForSEOURLsThenReturnValidURLList() {
        //given
        List<String> response = builder.getSEOURLs();

        //then
        assertEquals(cities.size() * categories.size(), response.size());
        assertTrue(response.containsAll(Arrays.asList(
                SCHEME + "://" + HOST + "/" + COMPONENT + "/" + CITY1 + "/" + CAT1,
                SCHEME + "://" + HOST + "/" + COMPONENT + "/" + CITY1 + "/" + CAT2,
                SCHEME + "://" + HOST + "/" + COMPONENT + "/" + CITY2 + "/" + CAT1,
                SCHEME + "://" + HOST + "/" + COMPONENT + "/" + CITY2 + "/" + CAT2)));
    }

}
