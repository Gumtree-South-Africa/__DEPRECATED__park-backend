package com.ebay.park.persistence;

/**
 * Exception thrown when deleting a directory is not successfully performed.
 * @author Julieta Salvadó
 *
 */
public class FileSystemDeleteException extends RuntimeException {
    private static final long serialVersionUID = 8070946984832345488L;

    public FileSystemDeleteException(Throwable t) {
        super(t);
    }
}
