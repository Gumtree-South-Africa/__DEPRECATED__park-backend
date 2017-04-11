package com.ebay.park.elasticsearch.repository;

import com.ebay.park.elasticsearch.document.BlackListDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author l.marino on 6/3/15.
 */

public interface BlackListRepository extends ElasticsearchRepository<BlackListDocument,Long>{
}
