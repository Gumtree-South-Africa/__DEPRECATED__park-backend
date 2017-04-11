package com.ebay.park.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link XMLPersisterFileSystem} for the root sitemaps file.
 * @author Julieta Salvadó
 */
@Component
public class RootFileXMLPersisterFileSystem extends XMLPersisterFileSystem {

    private String path;

    @Autowired
    RootFileXMLPersisterFileSystem(@Value("${sitemap.filesystem.path}") String path) {
        super(path);
    }
}
