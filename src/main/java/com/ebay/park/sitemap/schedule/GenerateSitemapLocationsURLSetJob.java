package com.ebay.park.sitemap.schedule;

import com.ebay.park.util.ParkConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ebay.park.sitemap.context.LocationSEOURLBuilder;
import com.ebay.park.sitemap.context.LocationSitemapComponentBuilderCaller;
import com.ebay.park.sitemap.context.LocationXMLFileNameBuilder;
import com.ebay.park.sitemap.context.URLSetBuilderContext;
import com.ebay.park.sitemap.model.URLSetBuilder;

/**
 * Scheduled Job that generates the xml file for location + urls.
 * <p>NOTE: This bean is defined in <code>scheduled</code> profile since it will be ony needed in the culster's
 * instance selected to execute scheduled jobs</p>
 *
 * @author Julieta Salvadó
 */
@Component
@Profile(ParkConstants.SCHEDULED_PROFILE)
public class GenerateSitemapLocationsURLSetJob {

    private static Logger logger = LoggerFactory.getLogger(GenerateSitemapLocationsURLSetJob.class);

    @Autowired
    private URLSetBuilder builder;

    @Autowired
    private LocationXMLFileNameBuilder xMLFileBuilder;

    @Autowired
    private LocationSitemapComponentBuilderCaller xMLBuilderCaller;

    @Autowired
    private LocationSEOURLBuilder sEOURLBuilder;

    @Transactional
    @Scheduled(cron = "${scheduler.sitemap.cron.locations}")
    public void execute() {
        logger.info("sitemap locations + urls xml file generation is about to start...");
        builder.setContext(new URLSetBuilderContext
                <LocationXMLFileNameBuilder, LocationSitemapComponentBuilderCaller, LocationSEOURLBuilder>
                (xMLFileBuilder, xMLBuilderCaller, sEOURLBuilder));
        builder.generate();
        logger.info("sitemap locations + urls xml file generation is done!");
    }
}
