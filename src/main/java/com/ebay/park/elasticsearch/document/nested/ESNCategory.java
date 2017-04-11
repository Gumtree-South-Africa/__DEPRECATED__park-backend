package com.ebay.park.elasticsearch.document.nested;

/**
 * @author l.marino on 6/25/15.
 */
public class ESNCategory {
    private Long id;
    private Integer order;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ESNCategory() {
    }

    public ESNCategory(Long id, Integer order, String name) {
        this.id = id;
        this.order = order;
        this.name = name;
    }
}
