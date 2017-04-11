package com.ebay.park.sitemap.schedule;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ebay.park.sitemap.context.LocationXMLFileNameBuilder;
import com.ebay.park.sitemap.model.URLSetBuilder;

/**
 * Unit test for {@link GenerateSitemapLocationsAndCategoriesURLSetJob}.
 * @author Julieta Salvadó
 *
 */
public class GenerateSitemapLocationsURLSetJobTest {

    @InjectMocks
    private GenerateSitemapLocationsURLSetJob job = new GenerateSitemapLocationsURLSetJob();

    @Mock
    private URLSetBuilder builder;

    @Mock
    private LocationXMLFileNameBuilder xMLFileBuilder;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void whenExecutingThenGenerate() {
        job.execute();
        verify(builder).generate();
    }
}
