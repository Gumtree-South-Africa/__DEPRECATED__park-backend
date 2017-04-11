package com.ebay.park.sitemap;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;

import com.ebay.park.db.entity.ChangeFreqDescription;
import com.ebay.park.sitemap.model.SitemapRoot;
import com.ebay.park.sitemap.model.URLSet;
import com.ebay.park.util.DataCommonUtil;

public class SitemapComponentTestIT {

    private static final String LAST_LINE = "</sitemapindex>";
    private static final String LINE1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
    private static final String LINE2_NULL_ELEMENTS = "<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\"/>";
    private static final String LINE2_NOT_NULL_ELEMENTS = "<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">";
    private static final String LINE2_URLSET = "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">";

    private static final String FILENAME = "sitemap.xml";
    private static final String TEST3 = "test3";
    private static final String TEST2 = "test2";
    private static final String TEST1 = "test1";
    private static final String FILE_NAME = "sitemap-location-1";
    private static final double PRIORITY = 0.5;

    @Test
    public void givenEmptySitemapElementsWhenGeneratingSitemapThenCreateSitemap() throws JAXBException, IOException {
        SitemapRoot sitemapFile = SitemapComponentBuilder.generateRootSitemap(new ArrayList<String>());
        JAXBContext jaxbContext = JAXBContext.newInstance(SitemapRoot.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(sitemapFile, new File(FILENAME));
        assertEquals(removeWhiteSpaces(getExpectedResultNullSitemapElements().toString()),
                    removeWhiteSpaces(new String(Files.readAllBytes(Paths.get(FILENAME)))));
    }

    private StringBuilder getExpectedResultNullSitemapElements() {
        StringBuilder file = new StringBuilder();
        file.append(LINE1);
        file.append(LINE2_NULL_ELEMENTS);

        return file;
    }

    @Test
    public void givenNotEmptySitemapElementsWhenGeneratingSitemapThenCreateSitemap() throws JAXBException, IOException {
        List<String> URLs = Arrays.asList(TEST1, TEST2, TEST3);
        SitemapRoot sitemapFile = SitemapComponentBuilder.generateRootSitemap(URLs);
        JAXBContext jaxbContext = JAXBContext.newInstance(SitemapRoot.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        File output = new File(FILENAME);
        jaxbMarshaller.marshal(sitemapFile, output);

        assertEquals(removeWhiteSpaces(getExpectedResult().toString()),
                    removeWhiteSpaces(new String(Files.readAllBytes(Paths.get(FILENAME)))));
    }

    private String removeWhiteSpaces(String input) {
        return input.replaceAll("[\\t\\n\\r\\s]","");
    }

    private StringBuilder getExpectedResult() {
        StringBuilder file = new StringBuilder();
        file.append(LINE1);
        file.append(LINE2_NOT_NULL_ELEMENTS);

        file.append("<sitemap>");
        file.append("<loc>");
        file.append(TEST1);
        file.append("</loc>");
        file.append("<lastmod>");
        file.append(DataCommonUtil.getCurrentDate());
        file.append("</lastmod>");
        file.append("</sitemap>");

        file.append("<sitemap>");
        file.append("<loc>");
        file.append(TEST2);
        file.append("</loc>");
        file.append("<lastmod>");
        file.append(DataCommonUtil.getCurrentDate());
        file.append("</lastmod>");
        file.append("</sitemap>");

        file.append("<sitemap>");
        file.append("<loc>");
        file.append(TEST3);
        file.append("</loc>");
        file.append("<lastmod>");
        file.append(DataCommonUtil.getCurrentDate());
        file.append("</lastmod>");
        file.append("</sitemap>");

        file.append(LAST_LINE);

        return file;
    }

    @Test
    public void givenNotEmptyElementsWhenGeneratingURLSetFileThenCreateFile() throws JAXBException, IOException {
        List<String> URLs = Arrays.asList(TEST1, TEST2, TEST3);
        URLSet innerSitemapFile = SitemapComponentBuilder.generateURLSet(URLs,
                ChangeFreqDescription.MONTHLY, PRIORITY);
        JAXBContext jaxbContext = JAXBContext.newInstance(URLSet.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        File output = new File(FILE_NAME + ".xml");
        jaxbMarshaller.marshal(innerSitemapFile, output);

        assertEquals(removeWhiteSpaces(getExpectedURLSetResult().toString()),
                    removeWhiteSpaces(new String(Files.readAllBytes(Paths.get(FILE_NAME + ".xml")))));
    }

    private StringBuilder getExpectedURLSetResult() {
        StringBuilder file = new StringBuilder();
        String currentDate = DataCommonUtil.getCurrentDate();

        file.append(LINE1);
        file.append(LINE2_URLSET);

        file.append("<url>");
        file.append("<loc>");
        file.append(TEST1);
        file.append("</loc>");
        file.append("<lastmod>");
        file.append(currentDate);
        file.append("</lastmod>");
        file.append("<changefreq>");
        file.append("monthly");
        file.append("</changefreq>");
        file.append("<priority>");
        file.append(PRIORITY);
        file.append("</priority>");
        file.append("</url>");

        file.append("<url>");
        file.append("<loc>");
        file.append(TEST2);
        file.append("</loc>");
        file.append("<lastmod>");
        file.append(currentDate);
        file.append("</lastmod>");
        file.append("<changefreq>");
        file.append("monthly");
        file.append("</changefreq>");
        file.append("<priority>");
        file.append(PRIORITY);
        file.append("</priority>");
        file.append("</url>");

        file.append("<url>");
        file.append("<loc>");
        file.append(TEST3);
        file.append("</loc>");
        file.append("<lastmod>");
        file.append(currentDate);
        file.append("</lastmod>");
        file.append("<changefreq>");
        file.append("monthly");
        file.append("</changefreq>");
        file.append("<priority>");
        file.append(PRIORITY);
        file.append("</priority>");
        file.append("</url>");

        file.append("</urlset>");

        return file;
    }
}
