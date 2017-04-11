package com.ebay.park.persistence;

import java.io.IOException;

/**
 * Interface for a previous generation of files.
 * @author Julieta Salvadó
 *
 */
public interface OldFiles {
    /**
     * This method deletes the files in the path and the parent directory.
     */
    void deleteContent();
    boolean isEmpty() throws IOException;
    void init();
    boolean exists();
}
