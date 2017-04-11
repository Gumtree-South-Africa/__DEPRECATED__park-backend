package com.ebay.park.elasticsearch.document.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.lambdaj.Lambda;

import com.ebay.park.db.dao.BlackListDao;
import com.ebay.park.db.dao.GroupDao;
import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.BlackList;
import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.User;
import com.ebay.park.elasticsearch.document.BlackListDocument;
import com.ebay.park.elasticsearch.document.GroupDocument;
import com.ebay.park.elasticsearch.document.ItemDocument;
import com.ebay.park.elasticsearch.document.UserDocument;
import com.ebay.park.service.moderation.dto.GroupForModerationDTO;

/**
 * @author l.marino on 6/19/15.
 */
@Component
public class DocumentConverter {
    @Autowired
    ItemDao itemDao;

    @Autowired
    UserDao userDao;

    @Autowired
    GroupDao groupDao;
    
    @Autowired
    BlackListDao blackListDao;
   
    public Item fromItemDocument(ItemDocument itemDocument){
        return itemDao.findOne(itemDocument.getItemId());
    }

    public List<Item> fromItemDocument(List<ItemDocument> itemDocuments){
        List<Long> itemDocumentsIds = Lambda.extract(itemDocuments,Lambda.on(ItemDocument.class).getItemId());
        List<Item> results = new ArrayList<Item>();
        for (Long id : itemDocumentsIds) {
        	results.add(itemDao.findOne(id));
        }
        return results;
    }
    
    public User fromUserDocument(UserDocument userDocument){
        return userDao.findOne(userDocument.getUserId());
    }

    public List<User> fromUserDocument(List<UserDocument> userDocuments){
        List<Long> userDocumentsIds = Lambda.extract(userDocuments, Lambda.on(UserDocument.class).getUserId());
        List<User> users = new ArrayList<>();
        for (Long id : userDocumentsIds){
        	User user = userDao.findOne(id);
        	if (user != null){
        		users.add(user);
        	}
        }
        return users;
    }

    public Group fromGroupDocument(GroupDocument groupDocument) {
    	return groupDao.findOne(groupDocument.getGroupId());
    }

    public List<Group> fromGroupDocument(List<GroupDocument> groupDocuments) {
    	List<Long> groupDocumentsId = Lambda.extract(groupDocuments, Lambda.on(GroupDocument.class).getGroupId());
    	List<Group> results = parseGroupDocumentsToGroups(groupDocumentsId);
        return results;
    }

    public Group fromGroupModeration(GroupForModerationDTO groupModeration) {
    	return groupDao.findOne(groupModeration.getId());
    }

    public List<Group> fromGroupModeration(List<GroupForModerationDTO> groupsModeration) {
    	List<Long> groupDocumentsId = Lambda.extract(groupsModeration, Lambda.on(GroupForModerationDTO.class).getId());
    	return this.parseGroupDocumentsToGroups(groupDocumentsId);
    }
    
    public BlackList fromBlackListDocument(BlackList blackList) { 
    	return blackListDao.findOne(blackList.getId());
    }
    
    public List<BlackList> fromBlackListDocument(List<BlackListDocument> blackLists) {
    	List<Long> blackListIds = Lambda.extract(blackLists, Lambda.on(BlackListDocument.class).getBlackListId());
    	return parseBlackListIdsToBlackLists(blackListIds);
    }

	private List<BlackList> parseBlackListIdsToBlackLists(
			List<Long> blackListIds) {
		List<BlackList> blacklists = new ArrayList<>();
		for (Long id : blackListIds){
			BlackList blacklist = blackListDao.findOne(id);
			if (blacklist != null) {
				blacklists.add(blacklist);
			}
		}
		return blacklists;
	}
    
    private List<Group> parseGroupDocumentsToGroups(List<Long> groupDocumentsId) {
    	List<Group> results = new ArrayList<>();
    	for (Long id : groupDocumentsId) {
    		results.add(groupDao.findOne(id));
    	}
    	return results;
    }
    
    /**
     * Returns the item ids from item documents
     * @param itemDocuments
     * @return item ids
     */
    public List<Long> itemIdsfromItemDocument(List<ItemDocument> itemDocuments){
        return Lambda.extract(itemDocuments,Lambda.on(ItemDocument.class).getItemId());
    }
}
