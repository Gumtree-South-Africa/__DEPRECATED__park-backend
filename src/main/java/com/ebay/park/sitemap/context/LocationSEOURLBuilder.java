package com.ebay.park.sitemap.context;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.ebay.park.sitemap.model.URLSet;

/**
 * It generates the SEO URLs for the {@link URLSet} that represents the
 * locations.
 * 
 * @author Julieta Salvadó
 *
 */
@Component
public class LocationSEOURLBuilder implements SEOURLBuilder {

    @Value("#{'${sitemap.locations.cities}'.split(',')}")
    private List<String> cities;

    @Value("${sitemap.url.scheme}")
    private String scheme;

    @Value("${sitemap.url.host}")
    private String host;

    @Value("${sitemap.url.location.component}")
    private String component;

    @Override
    public List<String> getSEOURLs() {
        List<String> list = new ArrayList<String>();
        for (String city : cities) {
            UriComponents uri = UriComponentsBuilder.newInstance()
                    .scheme(scheme)
                    .host(host)
                    .path(String.format("%s/%s", component, city)).build();

            list.add(uri.toString());

        }
        return list;
    }

}
