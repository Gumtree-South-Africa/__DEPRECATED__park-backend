package com.ebay.park.util;

import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Utility class for files.
 * @author Julieta Salvadó
 */
public class FileUtils {
    public static final String SEPARATOR = "/";

    /**
     * If the directory does not exists, this method creates it.
     * @param path
     *      path to the directory
     * @throws IOException
     *      if the creation fails.
     * @throws IllegalArgumentException
     *      if path parameter is null
     */
    public static void prepareDirectory(String path) throws IOException {
        Assert.notNull(path);
        if (!Files.exists(Paths.get(path))) {
            Files.createDirectories(Paths.get(path));
        }
    }

    /**
     * It creates a file name from a prefix, a base file name and a username
     * @param prefix
     * @param originalFileName
     * @param username
     * @return
     */
    public static String createFileName(String prefix, String originalFileName, String username) {
        return new StringBuilder()
                .append(prefix)
                .append(username)
                .append(SEPARATOR)
                .append(originalFileName)
                .toString();
    }

}
