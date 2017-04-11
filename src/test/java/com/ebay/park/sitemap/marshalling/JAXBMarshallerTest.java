package com.ebay.park.sitemap.marshalling;

import com.ebay.park.sitemap.model.SitemapRoot;
import com.ebay.park.sitemap.model.URLSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link JAXBMarshaller}
 * @author Julieta Salvadó
 */
public class JAXBMarshallerTest {
    @InjectMocks
    @Spy
    private JAXBMarshaller marshaller;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test (expected = IllegalArgumentException.class)
    public void givenNullURLSetWhenMarshallingThenException() {
        URLSet urlSet = null;
        marshaller.marshall(urlSet);
    }

    @Test
    public void givenValidURLSetWhenMashallingThenMarshall() {
        String response = marshaller.marshall(mock(URLSet.class));
        assertNotNull(response);
    }

    @Test (expected = IllegalArgumentException.class)
    public void givenNullSitemapWhenMarshallingThenException() {
        SitemapRoot sitemapRoot = null;
        marshaller.marshall(sitemapRoot);
    }

    @Test
    public void givenValidSitemapWhenMashallingThenMarshall() {
        String response = marshaller.marshall(mock(SitemapRoot.class));
        assertNotNull(response);
    }
}