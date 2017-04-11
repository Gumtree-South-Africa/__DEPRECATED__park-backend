package com.ebay.park.sitemap.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"loc", "lastmod"})
public class SitemapElement {
    private String loc;

    private String lastmod;

    /**
     * @return the lastMod
     */
    @XmlElement
    public String getLastmod() {
        return lastmod;
    }
    /**
     * @param lastmod the lastMod to set
     */
    public void setLastmod(String lastmod) {
        this.lastmod = lastmod;
    }
    /**
     * @return the loc
     */
    @XmlElement
    public String getLoc() {
        return loc;
    }
    /**
     * @param loc the loc to set
     */
    public void setLoc(String loc) {
        this.loc = loc;
    }

    @Override
    public String toString() {
        return "Sitemap [loc=" + loc + ", lastmod=" + lastmod + "]";
    }
}
