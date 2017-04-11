package com.ebay.park.sitemap.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Generation of the xml file name for locations + categories.
 * @author Julieta Salvadó
 *
 */
@Component
public class LocationAndCategoriesXMLFileNameBuilder implements
        XMLFileNameBuilder {

    @Value("${sitemap.locations.categories.file.name}")
    private String sitemapLocationsAndCategoriesFileName;

    @Value("${sitemap.file.extension}")
    private String sitemapFileExtension;

    @Override
    public String getXMLFileName() {
        return String.format("%s.%s", sitemapLocationsAndCategoriesFileName, sitemapFileExtension);
    }

}
