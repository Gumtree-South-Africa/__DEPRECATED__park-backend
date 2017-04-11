package com.ebay.park.sitemap.schedule;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ebay.park.sitemap.model.URLSetBuilder;

/**
 * Unit test for {@link GenerateSitemapLocationsAndCategoriesURLSetJob}
 * @author Julieta Salvadó
 *
 */
public class GenerateSitemapLocationsAndCategoriesURLSetJobTest {

    @InjectMocks
    private GenerateSitemapLocationsAndCategoriesURLSetJob job = new GenerateSitemapLocationsAndCategoriesURLSetJob();

    @Mock
    private URLSetBuilder builder;

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
