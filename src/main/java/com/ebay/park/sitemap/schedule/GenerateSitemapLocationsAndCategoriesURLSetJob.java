package com.ebay.park.sitemap.schedule;

import com.ebay.park.util.ParkConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ebay.park.sitemap.context.LocationAndCategoriesSEOURLBuilder;
import com.ebay.park.sitemap.context.LocationAndCategoriesSitemapComponentBuilderCaller;
import com.ebay.park.sitemap.context.LocationAndCategoriesXMLFileNameBuilder;
import com.ebay.park.sitemap.context.URLSetBuilderContext;
import com.ebay.park.sitemap.model.URLSetBuilder;

/**
 * <p>Scheduled Job that generates the xml file for location + categories.</p>
 * <p>NOTE: This bean is defined in <code>scheduled</code> profile since it will be ony needed in the culster's
 * instance selected to execute scheduled jobs</p>
 * @author Julieta Salvadó
 */
@Component
@Profile(ParkConstants.SCHEDULED_PROFILE)
public class GenerateSitemapLocationsAndCategoriesURLSetJob {

    private static Logger logger = LoggerFactory.getLogger(GenerateSitemapLocationsAndCategoriesURLSetJob.class);

    @Autowired
    private URLSetBuilder builder;

    @Autowired
    private LocationAndCategoriesXMLFileNameBuilder xMLFileBuilder;

    @Autowired
    private LocationAndCategoriesSitemapComponentBuilderCaller xMLBuilderCaller;

    @Autowired
    private LocationAndCategoriesSEOURLBuilder sEOURLBuilder;

    @Transactional
    @Scheduled(cron = "${scheduler.sitemap.cron.locations.categories}")
    public void execute() {
        logger.info("sitemap locations + categories xml file generation is about to start...");
        builder.setContext(new URLSetBuilderContext
                <LocationAndCategoriesXMLFileNameBuilder, LocationAndCategoriesSitemapComponentBuilderCaller, LocationAndCategoriesSEOURLBuilder>
                (xMLFileBuilder, xMLBuilderCaller, sEOURLBuilder));
        builder.generate();
        logger.info("sitemap locations + categories xml file generation is done!");
    }
}
