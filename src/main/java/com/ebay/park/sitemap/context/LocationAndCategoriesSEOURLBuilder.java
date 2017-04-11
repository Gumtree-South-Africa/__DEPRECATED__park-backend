package com.ebay.park.sitemap.context;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.ebay.park.sitemap.model.URLSet;

/**
 * It generates the SEO URLs for the {@link URLSet} that represents the locations+categories.
 * @author Julieta Salvadó
 *
 */
@Component
public class LocationAndCategoriesSEOURLBuilder implements SEOURLBuilder {

    @Value("#{'${sitemap.locations.cities}'.split(',')}")
    private List<String> cities;

    @Value("${sitemap.url.scheme}")
    private String scheme;

    @Value("${sitemap.url.host}")
    private String host;

    @Value("${sitemap.url.location.categories.component}")
    private String component;

    @Value("#{'${sitemap.categories}'.split(',')}")
    private List<String> categories;

    @Override
    public List<String> getSEOURLs() {
        List<String> list = new ArrayList<String>();
        for (String category : categories) {
            for (String city : cities) {
                UriComponents uri = UriComponentsBuilder.newInstance()
                        .scheme(scheme)
                        .host(host)
                        .path(String.format("%s/%s/%s", component, city, category)).build();
    
                list.add(uri.toString());
    
            }
        }
        return list;
    }

}
