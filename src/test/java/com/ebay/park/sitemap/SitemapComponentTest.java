package com.ebay.park.sitemap;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import com.ebay.park.db.entity.ChangeFreqDescription;
import com.ebay.park.sitemap.model.SitemapRoot;
import com.ebay.park.sitemap.model.URLSet;

/**
 * Unit test for SitemapComponent.
 */
public class SitemapComponentTest {
    private static final String TEST3 = "test3";
    private static final String TEST2 = "test2";
    private static final String TEST1 = "test1";
    private static final double PRIORITY = 0.5;
    
    @Test
    public void givenEmptySitemapElementsWhenGeneratingSitemapThenCreateSitemap() {
        SitemapRoot sitemapFile = SitemapComponentBuilder.generateRootSitemap(new ArrayList<String>());
        assertNotNull(sitemapFile);
    }

    @Test
    public void givenNullSitemapElementsWhenGeneratingSitemapThenCreateEmptySitemap() {
        SitemapRoot sitemapFile = SitemapComponentBuilder.generateRootSitemap(null);
        assertNotNull(sitemapFile);
    }

    @Test
    public void givenNotEmptySitemapElementsWhenGeneratingSitemapThenCreateSitemap() {
        SitemapRoot sitemapFile = SitemapComponentBuilder.generateRootSitemap(Arrays.asList(TEST1, TEST2, TEST3));
        assertNotNull(sitemapFile);
    }

    @Test
    public void givenEmptyElementsWhenGeneratingURLSetFileThenReturnNull() {
        URLSet uRLSetFile = SitemapComponentBuilder.generateURLSet(
                new ArrayList<String>(), ChangeFreqDescription.NEVER, PRIORITY);
        assertNull(uRLSetFile);
    }

    @Test
    public void givenNullElementsWhenGeneratingURLSetFileThenReturnNull() {
        URLSet uRLSetFile = SitemapComponentBuilder.generateURLSet(
                new ArrayList<String>(), ChangeFreqDescription.NEVER, PRIORITY);
        assertNull(uRLSetFile);
    }

    @Test
    public void givenNotEmptyElementsWhenGeneratingURLSetFileThenCreateFile() {
        URLSet uRLSetFile = SitemapComponentBuilder.generateURLSet(
                Arrays.asList(TEST1, TEST2, TEST3), ChangeFreqDescription.NEVER, PRIORITY);
        assertNotNull(uRLSetFile);
    }

    @Test
    public void givenNullChangeFreqWhenGeneratingURLSetFileThenReturnNull() {
        URLSet uRLSetFile = SitemapComponentBuilder.generateURLSet(
                new ArrayList<String>(), null, PRIORITY);
        assertNull(uRLSetFile);
    }

    @Test
    public void givenNullPriorityWhenGeneratingURLSetFileThenReturnNull() {
        URLSet uRLSetFile = SitemapComponentBuilder.generateURLSet(
                new ArrayList<String>(), ChangeFreqDescription.NEVER, null);
        assertNull(uRLSetFile);
    }

    @Test
    public void givenNullPriorityAndNullChangeFreqWhenGeneratingURLSetFileThenReturnNull() {
        URLSet uRLSetFile = SitemapComponentBuilder.generateURLSet(
                new ArrayList<String>(), null, null);
        assertNull(uRLSetFile);
    }

    @Test
    public void givenNullPriorityAndNullChangeAndNullElementsFreqWhenGeneratingURLSetFileThenReturnNull() {
        URLSet uRLSetFile = SitemapComponentBuilder.generateURLSet(null, null, null);
        assertNull(uRLSetFile);
    }

    @Test
    public void givenNullChangeFreqAndNullElementsFreqWhenGeneratingURLSetFileThenReturnNull() {
        URLSet uRLSetFile = SitemapComponentBuilder.generateURLSet(null, ChangeFreqDescription.NEVER, null);
        assertNull(uRLSetFile);
    }

    @Test
    public void givenNullPriotityAndNullElementsFreqWhenGeneratingURLSetFileThenReturnNull() {
        URLSet uRLSetFile = SitemapComponentBuilder.generateURLSet(null, null, PRIORITY);
        assertNull(uRLSetFile);
    }
    
}
