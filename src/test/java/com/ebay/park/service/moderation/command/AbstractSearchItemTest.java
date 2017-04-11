package com.ebay.park.service.moderation.command;

import com.ebay.park.db.entity.Category;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.elasticsearch.document.ItemDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * 
 * @author Julieta Salvadó
 *
 */
public abstract class AbstractSearchItemTest {
	
	@Autowired
    protected ElasticsearchOperations elasticsearchTemplate;
	
	protected Map<String, Item> itemDataSource = new HashMap<>();

	private int id = 1;

	public void setUp(){
		initMocks(this);
        elasticsearchTemplate.deleteIndex(ItemDocument.class);
        elasticsearchTemplate.createIndex(ItemDocument.class);
        elasticsearchTemplate.refresh(ItemDocument.class);
        elasticsearchTemplate.putMapping(ItemDocument.class);
        buildItemDataSource();
	}
	
	protected User createUser(String username){
        User user = new User();
        user.setUsername(username);
        return user;
    }

	protected Item createItem(Item item, Category category, User user, String description){
        item.setCategory(category);
        item.setPublishedBy(user);
        item.setDescription(description);
        item.activate();
        return item;
    }

	protected void saveDocuments(ItemDocument... documents){

        List<IndexQuery> indexQueries = new ArrayList<>();
        for(ItemDocument document : documents){
            indexQueries.add(new IndexQueryBuilder().withObject(document)
            .withIndexName("item").withId(String.valueOf(id)).withType("item").build());
            id++;
        }
        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh("item");
    }

	abstract protected void buildItemDataSource();
}
