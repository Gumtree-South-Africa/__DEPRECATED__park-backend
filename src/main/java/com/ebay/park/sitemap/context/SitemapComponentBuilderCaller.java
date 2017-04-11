package com.ebay.park.sitemap.context;

import java.util.List;

import com.ebay.park.sitemap.model.URLSet;

/**
 * Interface to call the sitemap component builder with the proper values.
 * @author Julieta Salvadó
 *
 */
@FunctionalInterface
public interface SitemapComponentBuilderCaller {
    URLSet buildSitemapComponent(List<String> uRLs);
}
