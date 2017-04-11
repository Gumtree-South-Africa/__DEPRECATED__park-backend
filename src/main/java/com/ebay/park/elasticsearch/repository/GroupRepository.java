package com.ebay.park.elasticsearch.repository;

import com.ebay.park.elasticsearch.document.GroupDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author l.marino on 6/3/15.
 */
public interface GroupRepository extends ElasticsearchRepository <GroupDocument,Long>{
}
