package com.ebay.park.elasticsearch.repository;

import com.ebay.park.elasticsearch.document.UserDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author l.marino on 6/3/15.
 */
public interface UserRepository extends ElasticsearchRepository<UserDocument,Long> {
	
	List<UserDocument> findByUsername(String username);
}
