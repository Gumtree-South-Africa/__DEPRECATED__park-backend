package com.ebay.park.persistence;

import java.io.IOException;

/**
 * Interface for the last generated files.
 * @author Julieta Salvadó
 *
 */
public interface CurrentFiles {
    void init();

    /**
     * Tells if the directory is empty or not.
     * @return true if empty; false, otherwise
     * @throws IOException
     *      if there is an error when trying to verify its emptiness
     * @throws NullPointerException
     *      if the path could not be set during construction
     */
    boolean isEmpty() throws IOException;

    boolean backup();

    void createEmptyFolders() throws IOException;

    boolean exists();
}
