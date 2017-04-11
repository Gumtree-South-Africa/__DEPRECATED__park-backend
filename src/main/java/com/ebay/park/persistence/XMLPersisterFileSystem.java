package com.ebay.park.persistence;

import com.ebay.park.util.FileUtils;
import com.ebay.park.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * It persist the xml file into the defined filesystem path.
 *
 * @author Julieta Salvadó
 */
@Component
public abstract class XMLPersisterFileSystem implements XMLPersister {

    private static final Logger LOGGER = LoggerFactory.getLogger(XMLPersisterFileSystem.class);

    private final String path;

    @Autowired
    private TextUtils textUtils;

    XMLPersisterFileSystem(String path) {
        super();
        this.path = path;
    }

    @Override
    public boolean publishXML(String fileContent, String fileName) throws IOException {
        Assert.notNull(fileContent, "File content must not be null here");
        Assert.notNull(fileName, "Filename must not be null here");

        FileUtils.prepareDirectory(path);
        String fullPath = textUtils.buildFullPath(path, fileName);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fullPath)))) {
            bw.write(fileContent);
        } catch (IOException e) {
            throw new FileSystemPublishException(e);
        }

        LOGGER.info("XML file saved at {}!", fullPath);
        return true;
    }
}
