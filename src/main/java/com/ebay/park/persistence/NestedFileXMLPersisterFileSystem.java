package com.ebay.park.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link XMLPersisterFileSystem} for the nested sitemaps files.
 * @author Julieta Salvadó
 */
@Component
public class NestedFileXMLPersisterFileSystem extends XMLPersisterFileSystem {

    @Autowired
    NestedFileXMLPersisterFileSystem(@Value("${sitemap.filesystem.nested.files.path}") String path) {
        super(path);
    }
}
