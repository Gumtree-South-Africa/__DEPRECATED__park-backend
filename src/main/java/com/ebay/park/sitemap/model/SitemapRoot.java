package com.ebay.park.sitemap.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sitemapindex")
public class SitemapRoot extends Sitemap<SitemapElement>{
    /**
     * @return the sitemapElements
     */
    public List<SitemapElement> getSitemap() {
        return getElements();
    }

    /**
     * @param elements the sitemapElements to set
     */
    public void setSitemap(List<SitemapElement> elements) {
        this.setElements(elements);
    }
}
