package com.ebay.park.sitemap.model;

import com.ebay.park.persistence.FileSystemPublishException;
import com.ebay.park.persistence.XMLPersister;
import com.ebay.park.sitemap.SitemapFileHelper;
import com.ebay.park.sitemap.context.URLSetBuilderContext;
import com.ebay.park.sitemap.marshalling.Marshaller;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This generates an xml file (or multiple files, if apply) and store it/them.
 * @author Julieta Salvadó
 *
 */
@Component
public class URLSetBuilder {

    @Value("${sitemap.length}")
    private int sitemapLength;

    @Value("${sitemap.file.extension}")
    protected String sitemapFileExtension;

    @Autowired
    @Qualifier("nestedFileXMLPersisterFileSystem")
    private XMLPersister persister;

    @Autowired
    @Qualifier("JAXBMarshaller")
    private Marshaller marshaller;

    @Autowired
    private SitemapFileHelper sitemapFileHelper;

    private URLSetBuilderContext context;

    public void setContext(URLSetBuilderContext context) {
        this.context = context;
    }

    public void generate() {
        sitemapFileHelper.deletePreviousFileNames(context.getXMLFileNameBuilder().getXMLFileName());

        List<String> indexingUrls = context.getsEOURLBuilder().getSEOURLs();
        if (CollectionUtils.isNotEmpty(indexingUrls)) {
            List<URLSet> files = IntStream
                    .iterate(0, i -> i + sitemapLength)
                    .limit(getLimit(indexingUrls.size()))
                    .boxed()
                    .map(i -> context.getXMLBuilderCaller().buildSitemapComponent(getSublist(indexingUrls, i)))
                    .collect(Collectors.toList());
    
            files.stream()
                    .map(this::marshall)
                    .forEach(this::persist);
        }
    }

    private String marshall(URLSet urlSet) {
        return marshaller.marshall(urlSet);
    }

    private long getLimit(int size) {
        return (size + sitemapLength - 1) / sitemapLength;
    }

    public void updateSitemapFiles(String fileName) {
       sitemapFileHelper.updateFileNames(fileName);
    }

    private void persist(String fileContent) {
        try {
            String fileName = context.getXMLFileNameBuilder().getXMLFileName();
            persister.publishXML(fileContent, fileName);
            this.updateSitemapFiles(fileName);
        } catch (IOException e) {
            throw new FileSystemPublishException(e);
        }
    }

    private List<String> getSublist(List<String> list, Integer pageNumber) {
        return list.subList(pageNumber, Math.min(pageNumber + sitemapLength, list.size()));
    }
}
