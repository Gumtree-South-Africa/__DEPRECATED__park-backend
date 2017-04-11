package com.ebay.park.sitemap.model;

import com.ebay.park.persistence.XMLPersisterFileSystem;
import com.ebay.park.sitemap.SitemapFileHelper;
import com.ebay.park.sitemap.context.*;
import com.ebay.park.sitemap.marshalling.JAXBMarshaller;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for {@link URLSetBuilder}.
 * @author Julieta Salvadó
 *
 */
public class URLSetBuilderTest {

    private static final String URL = "url";
    private static final String FILENAME = "name";
    private static final int LENGHT = 5;
    private static final String XML = "xml";

    @InjectMocks
    private URLSetBuilder builder = new URLSetBuilder();

    @SuppressWarnings("rawtypes")
    @Mock
    private URLSetBuilderContext context;

    @Mock
    private XMLPersisterFileSystem persister;

    @Mock
    private JAXBMarshaller marshaller;

    @Mock
    private LocationSEOURLBuilder locationSEOBuilder;

    @Mock
    private LocationAndCategoriesSEOURLBuilder locationAndCategorySEOBuilder;
    
    @Mock
    private LocationSitemapComponentBuilderCaller locationXMLBuilderCaller;

    @Mock
    private LocationAndCategoriesSitemapComponentBuilderCaller locationAndCategoriesXMLBuilderCaller;

    @Mock
    private LocationAndCategoriesXMLFileNameBuilder locationAndCategoriesXMLFileNameBuilder;

    @Mock
    private LocationXMLFileNameBuilder locationXMLFileNameBuilder;

    @Mock
    private SitemapFileHelper sitemapFileHelper;

    @Before
    public void setUp() {
        initMocks(this);
        ReflectionTestUtils.setField(builder, "sitemapLength", LENGHT);
        ReflectionTestUtils.setField(builder, "sitemapFileExtension", XML);
    }

    @Test
    public void givenNullURLsToIndexAndLocationSEOURLBuilderWhenGeneratingThenDoNotPersist() throws IOException {
        when(context.getsEOURLBuilder()).thenReturn(locationSEOBuilder);
        when(context.getXMLFileNameBuilder()).thenReturn(locationXMLFileNameBuilder);
        when(locationSEOBuilder.getSEOURLs()).thenReturn(null);

        builder.generate();

        verify(persister, never()).publishXML(anyString(), anyString());
    }

    @Test
    public void givenNullURLsToIndexAndLocationCategorySEOURLBuilderWhenGeneratingThenDoNotPersist() throws IOException {
        when(context.getsEOURLBuilder()).thenReturn(locationAndCategorySEOBuilder);
        when(context.getXMLFileNameBuilder()).thenReturn(locationAndCategoriesXMLFileNameBuilder);
        when(locationAndCategorySEOBuilder.getSEOURLs()).thenReturn(new ArrayList<String>());

        builder.generate();

        verify(persister, never()).publishXML(anyString(), anyString());
    }

    @Test
    public void givenEmptyURLsToIndexAndLocationSEOURLBuilderWhenGeneratingThenDoNotPersist() throws IOException {
        when(context.getsEOURLBuilder()).thenReturn(locationSEOBuilder);
        when(context.getXMLFileNameBuilder()).thenReturn(locationXMLFileNameBuilder);
        when(locationSEOBuilder.getSEOURLs()).thenReturn(new ArrayList<String>());

        builder.generate();

        verify(persister, never()).publishXML(anyString(), anyString());

    }

    @Test
    public void givenEmptyURLsToIndexAndLocationCategorySEOURLBuilderWhenGeneratingThenDoNotPersist() throws IOException {
        when(context.getsEOURLBuilder()).thenReturn(locationAndCategorySEOBuilder);
        when(context.getXMLFileNameBuilder()).thenReturn(locationAndCategoriesXMLFileNameBuilder);
        when(locationAndCategorySEOBuilder.getSEOURLs()).thenReturn(null);

        builder.generate();

        verify(persister, never()).publishXML(anyString(), anyString());
    }

    @Test
    public void givenLocationComponentsWhenGeneratingThenPersist() throws IOException {
        when(context.getsEOURLBuilder()).thenReturn(locationSEOBuilder);
        when(context.getXMLBuilderCaller()).thenReturn(locationXMLBuilderCaller);
        when(context.getXMLFileNameBuilder()).thenReturn(locationXMLFileNameBuilder);

        List<String> uRLs = Collections.singletonList(URL);
        when(locationSEOBuilder.getSEOURLs()).thenReturn(uRLs);
        URLSet uRLSet = mock(URLSet.class);
        when(locationXMLBuilderCaller.buildSitemapComponent(uRLs)).thenReturn(uRLSet);
        when(locationXMLFileNameBuilder.getXMLFileName()).thenReturn(FILENAME);

        builder.generate();

        verify(persister).publishXML(anyString(), anyString());
    }

    @Test
    public void givenLocationAndCategoryComponentsWhenGeneratingThenPersist() throws IOException {
        when(context.getsEOURLBuilder()).thenReturn(locationAndCategorySEOBuilder);
        when(context.getXMLBuilderCaller()).thenReturn(locationAndCategoriesXMLBuilderCaller);
        when(context.getXMLFileNameBuilder()).thenReturn(locationAndCategoriesXMLFileNameBuilder);

        List<String> uRLs = Collections.singletonList(URL);
        when(locationAndCategorySEOBuilder.getSEOURLs()).thenReturn(uRLs);
        URLSet uRLSet = mock(URLSet.class);
        when(locationAndCategoriesXMLBuilderCaller.buildSitemapComponent(uRLs)).thenReturn(uRLSet);
        when(locationAndCategoriesXMLFileNameBuilder.getXMLFileName()).thenReturn(FILENAME);

        builder.generate();

        verify(persister).publishXML(anyString(), anyString());
    }
}
