package com.ebay.park.sitemap.context;

/**
 * Exception thrown when the URL could not be build.
 * @author Julieta Salvadó
 *
 */
public class SEOURLSyntaxException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SEOURLSyntaxException(String message, Throwable t) {
        super(message, t);
    }
}
