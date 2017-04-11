package com.ebay.park.sitemap.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.ebay.park.db.entity.ChangeFreqDescription;

@XmlType(propOrder = {"loc", "lastmod", "changefreq", "priority"})
public class URL extends SitemapElement {
    private static final double DEFAULT = 0.5;

    private String loc;
    private String lastmod;
    private ChangeFreqDescription changefreq;
    private double priority = DEFAULT;

    /**
     * @return the lastMod
     */
    @Override
    @XmlElement
    public String getLastmod() {
        return lastmod;
    }
    /**
     * @param lastmod the lastMod to set
     */
    @Override
    public void setLastmod(String lastmod) {
        this.lastmod = lastmod;
    }
    /**
     * @return the loc
     */
    @Override
    @XmlElement
    public String getLoc() {
        return loc;
    }
    /**
     * @param loc the loc to set
     */
    @Override
    public void setLoc(String loc) {
        this.loc = loc;
    }

    @Override
    public String toString() {
        return "InnerSitemap [loc=" + loc + ", lastmod=" + lastmod + ", changefren= " + changefreq + "]";
    }
    /**
     * @return the changefreq
     */
    @XmlElement
    public String getChangefreq() {
        return changefreq.toString().toLowerCase();
    }
    /**
     * @param changefreq the changefreq to set
     */
    public void setChangefreq(ChangeFreqDescription changefreq) {
        this.changefreq = changefreq;
    }
    /**
     * @return the priority
     */
    @XmlElement
    public double getPriority() {
        return priority;
    }
    /**
     * @param priority the priority to set
     */
    public void setPriority(double priority) {
        this.priority = priority;
    }
}
