package com.ebay.park.persistence;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Objects;

/**
 * It represents a previous generation of files for the sitemap.
 * @author Julieta Salvadó
 */
@Component
public class OldSitemapFiles implements OldFiles {

    private Path rootPath;

    @Value("${sitemap.filesystem.old.files.path}")
    private String oldDirectoryPath;

    @Override
    public void init() {
        rootPath = Paths.get(oldDirectoryPath);
    }

    @Override
    public void deleteContent() {
        try {
            Files.walk(rootPath, FileVisitOption.FOLLOW_LINKS)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new FileSystemDeleteException(e);
        }
    }

    @Override
    public boolean isEmpty() throws IOException {
        Objects.requireNonNull(rootPath);
        try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(rootPath)) {
            return !dirStream.iterator().hasNext();
        }
    }

    @Override
    public boolean exists() {
        Assert.notNull(rootPath);
        return Files.exists(rootPath);
    }

    @Override
    public String toString() {
        return "OldSitemapFiles[" +
                "oldDirectoryPath='" + this.oldDirectoryPath + '\'' +
                ']';
    }
}
