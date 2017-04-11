package com.ebay.park.sitemap.context;

import java.util.List;

@FunctionalInterface
public interface SEOURLBuilder {
    List<String> getSEOURLs();
}
