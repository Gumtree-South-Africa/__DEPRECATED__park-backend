package com.ebay.park.sitemap.context;

public class URLSetBuilderContext <T extends XMLFileNameBuilder, X extends SitemapComponentBuilderCaller, W extends SEOURLBuilder>{
    private T xMLFileNameBuilder;
    private X xMLBuilderCaller;
    private W sEOURLBuilder;

    public URLSetBuilderContext(T xMLFileNameBuilder, X xMLBuilderCaller, W sEOURLBuilder) {
        setXMLFileNameBuilder(xMLFileNameBuilder);
        setXMLBuilderCaller(xMLBuilderCaller);
        setsEOURLBuilder(sEOURLBuilder);
    }

    /**
     * @return the xMLFileNameBuilder
     */
    public T getXMLFileNameBuilder() {
        return xMLFileNameBuilder;
    }

    /**
     * @param xMLFileNameBuilder the xMLFileNameBuilder to set
     */
    public void setXMLFileNameBuilder(T xMLFileNameBuilder) {
        this.xMLFileNameBuilder = xMLFileNameBuilder;
    }

    /**
     * @return the xMLBuilderCaller
     */
    public X getXMLBuilderCaller() {
        return xMLBuilderCaller;
    }

    /**
     * @param xMLBuilderCaller the xMLBuilderCaller to set
     */
    public void setXMLBuilderCaller(X xMLBuilderCaller) {
        this.xMLBuilderCaller = xMLBuilderCaller;
    }

    /**
     * @return the sEOURLBuilder
     */
    public W getsEOURLBuilder() {
        return sEOURLBuilder;
    }

    /**
     * @param sEOURLBuilder the sEOURLBuilder to set
     */
    public void setsEOURLBuilder(W sEOURLBuilder) {
        this.sEOURLBuilder = sEOURLBuilder;
    }
}
