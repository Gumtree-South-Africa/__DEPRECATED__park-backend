/**
 * Copyright eBay, 2014
 */
package com.ebay.park.service.index.commmand;

import com.ebay.park.db.dao.*;
import com.ebay.park.db.entity.*;
import com.ebay.park.elasticsearch.document.*;
import com.ebay.park.elasticsearch.repository.*;
import com.ebay.park.elasticsearch.utils.ESUtils;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author diana.gazquez
 * 
 */
@Component
public class ReindexCmd implements ServiceCommand<Void, Boolean> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReindexCmd.class);

    @Autowired
    ItemDao itemDao;
    @Autowired
    BlackListDao blackListDao;
    @Autowired
    GroupDao groupDao;
    @Autowired
    UserDao userDao;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    BlackListRepository blackListRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    @Qualifier("elasticsearchTemplate")
    private ElasticsearchTemplate elasticsearchOperations;

	@Override
	public Boolean execute(Void param) throws ServiceException {

        LOGGER.info("Reindexing ITEM index");
        try {
            indexItems();
        } catch (Exception e) {
            LOGGER.error("Error indexing ITEM", e);
        }

        LOGGER.info("Reindexing BLACKLIST index");
        try {
            indexBlackLists();
        } catch (Exception e) {
            LOGGER.error("Error indexing BLACKLIST", e);
        }

        LOGGER.info("Reindexing GROUP index");
        try {
            indexGroups();
        } catch (Exception e) {
                LOGGER.error("Error indexing GROUP", e);
        }

        LOGGER.info("Reindexing USER index");
        try {
            indexUsers();
        } catch (Exception e) {
            LOGGER.error("Error indexing USER", e);
        }
        return true;
	}

    private void indexBlackLists(){
        elasticsearchOperations.deleteIndex(BlackListDocument.class);
        elasticsearchOperations.createIndex(BlackListDocument.class);
        elasticsearchOperations.putMapping(BlackListDocument.class);
        List<BlackList> blackLists = new ArrayList<BlackList>();
        List<BlackListDocument> blackListDocuments = new ArrayList<BlackListDocument>();
            blackLists = blackListDao.findAll();
            for (BlackList blackList : blackLists){
                blackListDocuments.add(new BlackListDocument(blackList));
            }
            blackListRepository.save(blackListDocuments);
    }

    private void indexGroups(){
        elasticsearchOperations.deleteIndex(GroupDocument.class);
        elasticsearchOperations.createIndex(GroupDocument.class);
        elasticsearchOperations.putMapping(GroupDocument.class);
        List<Group> groups = new ArrayList<Group>();
        List<GroupDocument> groupDocuments = new ArrayList<GroupDocument>();
            groups = groupDao.findAll();
            for (Group group : groups){
                groupDocuments.add(new GroupDocument(group));
            }
            groupRepository.save(groupDocuments);
    }

    private void indexUsers(){
        elasticsearchOperations.deleteIndex(UserDocument.class);
        elasticsearchOperations.createIndex(UserDocument.class);
        elasticsearchOperations.putMapping(UserDocument.class);
        List<User> users = new ArrayList<User>();
        List<UserDocument> userDocuments = new ArrayList<UserDocument>();
            users = userDao.findAll();
            for (User user : users){
                userDocuments.add(new UserDocument(user));
            }
            userRepository.save(userDocuments);
    }

    private void indexItems() {
        elasticsearchOperations.deleteIndex(ItemDocument.class);
        elasticsearchOperations.createIndex(ItemDocument.class);
        elasticsearchOperations.putMapping(ItemDocument.class);
        List<ItemDocument> itemDocuments = new ArrayList<ItemDocument>();
        List<Item> items = new ArrayList<Item>();
            items = itemDao.findAll();
            for (Item item : items){
                itemDocuments.add(new ItemDocument(item));
            }
            itemRepository.save(itemDocuments);
    }

}
