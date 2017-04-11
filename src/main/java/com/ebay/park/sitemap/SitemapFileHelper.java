package com.ebay.park.sitemap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.SitemapFileDao;
import com.ebay.park.db.entity.SitemapFile;

@Component
public class SitemapFileHelper {

    @Autowired
    private SitemapFileDao sitemapFileDao;

    public void deletePreviousFileNames(String fileName) {
        List<SitemapFile> list = sitemapFileDao.findSimilarFileName(fileName);
        if (list != null) {
            list.forEach(sitemapFileDao::delete);
        }

    }

    public void updateFileNames(String fileName) {
        if (sitemapFileDao.findByName(fileName) == null) {
            sitemapFileDao.save(new SitemapFile(fileName));
        }
    }

}
