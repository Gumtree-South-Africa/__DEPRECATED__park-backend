package com.ebay.park.sitemap.context;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Unit test for {@link LocationSEOURLBuilder}.
 * @author Julieta Salvadó
 *
 */
public class LocationSEOURLBuilderTest {

    private static final String CITY1 = "Tandil";
    private static final String CITY2 = "Paris";
    private static final String CITY3 = "New York";
    private static final String HOST = "www.vivanuncios.us";
    private static final String COMPONENT = "home";
    private static final Object SCHEME = "https";

    private List<String> cities = Arrays.asList(CITY1, CITY2, CITY3);

    @InjectMocks
    private LocationSEOURLBuilder builder = new LocationSEOURLBuilder();

    @Before
    public void setUp() {
        initMocks(this);
        ReflectionTestUtils.setField(builder, "cities", cities);
        ReflectionTestUtils.setField(builder, "scheme", SCHEME);
        ReflectionTestUtils.setField(builder, "host", HOST);
        ReflectionTestUtils.setField(builder, "component", COMPONENT);
    }

    @Test
    public void whenAskingForListThenReturnValidList() {
        //when
        List<String> response = builder.getSEOURLs();

        //then
        assertEquals(cities.size(), response.size());
        assertEquals(SCHEME + "://" + HOST + "/" + COMPONENT + "/" + CITY1, response.get(0));
        assertEquals(SCHEME + "://" + HOST + "/" + COMPONENT + "/" + CITY2, response.get(1));
        assertEquals(SCHEME + "://" + HOST + "/" + COMPONENT + "/" + CITY3, response.get(2));

    }

}
