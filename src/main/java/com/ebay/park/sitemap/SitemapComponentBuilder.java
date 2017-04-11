package com.ebay.park.sitemap;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import com.ebay.park.db.entity.ChangeFreqDescription;
import com.ebay.park.sitemap.model.SitemapElement;
import com.ebay.park.sitemap.model.SitemapRoot;
import com.ebay.park.sitemap.model.URL;
import com.ebay.park.sitemap.model.URLSet;
import com.ebay.park.util.DataCommonUtil;

/**
 * It generates sitemap components objects from URL lists.
 * @author Julieta Salvadó
 *
 */
public class SitemapComponentBuilder {

    private static final String XMLNS = "http://www.sitemaps.org/schemas/sitemap/0.9";

    /**
     * It generates a {@link SitemapRoot}.
     * @param uRLs
     *      list of URLs to fill the response.
     * @return
     *      object created from the incoming parameters
     */
    public static SitemapRoot generateRootSitemap(List<String> uRLs) {
        SitemapRoot sitemap = new SitemapRoot();
        sitemap.setXmlns(XMLNS);

        if (CollectionUtils.isNotEmpty(uRLs)) {
            String lastMod = DataCommonUtil.getCurrentDate();
            List<SitemapElement> sitemapElements = uRLs.stream().map(url -> {
                SitemapElement elem = new SitemapElement();
                elem.setLastmod(lastMod);
                elem.setLoc(url);
                return elem;
            }).collect(Collectors.toList());

            sitemap.setSitemap(sitemapElements);
        }

        return sitemap;
    }

    /**
     * It generates a {@link URLSet}.
     * @param uRLs
     *      list of URLs to fill the response.
     * @param changeFreq
     *      frequency of update of the elements in the response.
     * @param priority
     *      priority of the elements in the response
     * @return
     *      object created from the incoming parameters
     */
    public static URLSet generateURLSet(List<String> uRLs, ChangeFreqDescription changeFreq, Double priority) {
        if (CollectionUtils.isNotEmpty(uRLs) && changeFreq != null && priority != null) {
            URLSet sitemap = new URLSet();
            sitemap.setXmlns(XMLNS);

            String lastMod = DataCommonUtil.getCurrentDate();
            List<URL> sitemapElements = uRLs.stream().map(url -> {
                URL elem = new URL();
                elem.setLastmod(lastMod);
                elem.setLoc(url);
                elem.setPriority(priority);
                elem.setChangefreq(changeFreq);
                return elem;
            }).collect(Collectors.toList());
            sitemap.setUrl(sitemapElements);

            return sitemap;
        }
        return null;
    }
}
