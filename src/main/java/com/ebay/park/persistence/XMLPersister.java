package com.ebay.park.persistence;

import java.io.IOException;

/**
 * Interface for xml file persistence.
 * @author Julieta Salvadó
 *
 */
@FunctionalInterface
public interface XMLPersister {
    boolean publishXML(String fileContent, String fileName) throws IOException;
}
