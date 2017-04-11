package com.ebay.park.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ebay.park.db.entity.SitemapFile;

/**
 * Data access interface for {@link SitemapFile} entity.
 * @author Julieta Salvadó
 *
 */
public interface SitemapFileDao extends JpaRepository<SitemapFile, Long> {

    SitemapFile findByName(String fileName);

    @Query(value = "select sit from SitemapFile sit where sit.name like CONCAT(:fileName, '%')")
    List<SitemapFile> findSimilarFileName(@Param("fileName") String fileName);
}
