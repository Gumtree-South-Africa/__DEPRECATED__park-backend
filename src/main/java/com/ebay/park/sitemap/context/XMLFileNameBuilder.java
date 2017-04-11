package com.ebay.park.sitemap.context;

/**
 * Interface for the generation of the xml file name.
 * @author Julieta Salvadó
 *
 */
@FunctionalInterface
public interface XMLFileNameBuilder {
    String getXMLFileName();
}
