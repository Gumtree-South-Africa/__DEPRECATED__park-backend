package com.ebay.park.elasticsearch.repository;

import com.ebay.park.elasticsearch.document.ItemDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author l.marino on 6/3/15.
 */

public interface ItemRepository extends ElasticsearchRepository <ItemDocument,Long> {

    public List<ItemDocument> findByDescription(String description);
    
}
