package com.ebay.park.sitemap.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Generation of the xml file name for locations.
 * @author Julieta Salvadó
 *
 */
@Component
public class LocationXMLFileNameBuilder implements XMLFileNameBuilder {

    @Value("${sitemap.locations.file.name}")
    private String sitemapLocationsFileName;

    @Value("${sitemap.file.extension}")
    private String sitemapFileExtension;

    @Override
    public String getXMLFileName() {
        return String.format("%s.%s", sitemapLocationsFileName, sitemapFileExtension);
    }

}
