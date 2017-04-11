package com.ebay.park.sitemap.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "urlset")
public class URLSet extends Sitemap<URL> {
    /**
     * @return the sitemapElements
     */
    public List<URL> getUrl() {
        return getElements();
    }

    /**
     * @param elements the sitemapElements to set
     */
    public void setUrl(List<URL> elements) {
        this.setElements(elements);
    }
}
