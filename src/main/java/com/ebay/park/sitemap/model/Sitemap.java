package com.ebay.park.sitemap.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

public class Sitemap <T extends SitemapElement> {

    private String xmlns;
    private List<T> elements;

    /**
     * @return the xmlns
     */
    @XmlAttribute
    public String getXmlns() {
        return xmlns;
    }

    /**
     * @param xmlns the xmlns to set
     */
    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    /**
     * @return the elements
     */
    protected List<T> getElements() {
        return elements;
    }

    /**
     * @param elements the elements to set
     */
    protected void setElements(List<T> elements) {
        this.elements = elements;
    }
}
