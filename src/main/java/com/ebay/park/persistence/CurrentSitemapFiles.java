package com.ebay.park.persistence;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * It represents the current files for the sitemap.
 * @author Julieta Salvadó
 */
@Component
public class CurrentSitemapFiles implements CurrentFiles {
    private Path rootPath;

    @Value("${sitemap.filesystem.path}")
    private String currentDirectoryPath;

    @Value("${sitemap.filesystem.nested.files.path}")
    private String currentNestedDirectoryPath;

    @Value("${sitemap.filesystem.old.files.path}")
    private String oldDirectoryPath;

    @Override
    public void init() {
        rootPath = Paths.get(currentDirectoryPath);
    }

    @Override
    public boolean isEmpty() throws IOException {
        Objects.requireNonNull(rootPath);
        try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(rootPath)) {
            return !dirStream.iterator().hasNext();
        }
    }

    @Override
    public boolean backup() {
        File currentNameFile = new File(currentDirectoryPath);
        File futureNameFile = new File(oldDirectoryPath);
        return currentNameFile.renameTo(futureNameFile);
    }

    @Override
    public void createEmptyFolders() throws IOException {
        Files.createDirectories(Paths.get(currentDirectoryPath));
        Files.createDirectories(Paths.get(currentNestedDirectoryPath));
    }

    @Override
    public boolean exists() {
        Assert.notNull(rootPath);
        return Files.exists(rootPath);
    }
}
