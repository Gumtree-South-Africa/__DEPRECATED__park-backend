package com.ebay.park.sitemap.marshalling;

import com.ebay.park.sitemap.model.SitemapRoot;
import com.ebay.park.sitemap.model.URLSet;

/**
 * @author Julieta Salvadó
 */
public interface Marshaller {
    /**
     * It converts an {@link URLSet} object into a string representing the xml.
     * @param urlSet
     *      object to convert
     * @return
     *      string containing the object content
     */
    String marshall(URLSet urlSet);
    /**
     * It converts an {@link SitemapRoot} object into a string representing the xml.
     * @param sitemapRoot
     *      object to convert
     * @return
     *      string containing the object content
     */
    String marshall(SitemapRoot sitemapRoot);
}
