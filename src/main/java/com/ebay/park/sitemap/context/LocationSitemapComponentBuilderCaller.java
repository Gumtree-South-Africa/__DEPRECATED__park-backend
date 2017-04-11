package com.ebay.park.sitemap.context;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ebay.park.db.entity.ChangeFreqDescription;
import com.ebay.park.sitemap.SitemapComponentBuilder;
import com.ebay.park.sitemap.model.URLSet;

/**
 * It call the sitemap component builder with the proper values for location.
 * @author Julieta Salvadó
 *
 */
@Component
public class LocationSitemapComponentBuilderCaller implements SitemapComponentBuilderCaller {

    @Value("${sitemap.locations.priority}")
    private double priority;

    @Override
    public URLSet buildSitemapComponent(List<String> uRLs) {
        return CollectionUtils.isEmpty(uRLs) ? null :
            SitemapComponentBuilder.generateURLSet(uRLs, ChangeFreqDescription.MONTHLY, priority);
    }

}
