package com.ebay.park.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the sitemap_file database table.
 * @author Julieta Salvadó
 *
 */
@Entity
@Table(name = "sitemap_file")
public class SitemapFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sit_id")
    private Long id;

    @Column(name = "sit_name")
    private String name;

    public SitemapFile() {}

    public SitemapFile(String fileName) {
        setName(fileName);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
}
