package com.ebay.park.elasticsearch.listeners;

import com.ebay.park.db.entity.*;
import com.ebay.park.elasticsearch.document.BlackListDocument;
import com.ebay.park.elasticsearch.document.GroupDocument;
import com.ebay.park.elasticsearch.document.ItemDocument;
import com.ebay.park.elasticsearch.document.UserDocument;
import com.ebay.park.elasticsearch.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @since 6/11/2015
 * @author  l.mariño & Julieta Salvadó
 */
public abstract class AbstractPostcommitEventListener {
	private static Logger LOGGER = LoggerFactory.getLogger(AbstractPostcommitEventListener.class);

	@Autowired
	protected ItemRepository itemRepository;
	@Autowired
	protected GroupRepository groupRepository;
	@Autowired
	protected BlackListRepository blackListRepository;
	@Autowired
	protected UserRepository userRepository;

	protected void indexAsItem(Item entity) {
		try {
			itemRepository.save(new ItemDocument(entity));
		} catch (Exception e) {
			LOGGER.error("Error when indexing the item {}", entity.toString(), e);
		}
	}

	protected void indexAsGroup(Group entity) {
		try {
			groupRepository.save(new GroupDocument(entity));
		} catch (Exception e) {
			LOGGER.error("Error when indexing the group {}", entity.toString(), e);
		}
	}

	protected void indexAsUser(User entity) {
		try {
			userRepository.save(new UserDocument(entity));
		} catch (Exception e) {
			LOGGER.error("Error when indexing the user {}", entity.toString(), e);
		}
	}

	protected void indexAsBlacklist(BlackList entity) {
		try {
			blackListRepository.save(new BlackListDocument(entity));
		} catch (Exception e) {
			LOGGER.error("Error when indexing the blacklisted word {}", entity.toString(), e);
		}
	}

	protected void indexUserFollowingGroup(UserFollowsGroup entity) {
		UserFollowsGroup userFollowGroup = entity;
		indexAsUser(userFollowGroup.getUser());
	}

	protected void indexItemInGroup(ItemGroup entity) {
		ItemGroup itemGroup = entity;
		indexAsItem(itemGroup.getItem());
	}

	protected void removeItemFromIndex(Item entity) {
		try {
			itemRepository.delete(new ItemDocument(entity));
		} catch (Exception e) {
			LOGGER.error("Error when removing from index the item {}", entity.toString(), e);
		}
	}

	protected void removeBlacklistFromIndex(BlackList blacklist) {
		try {
			blackListRepository.delete(new BlackListDocument(blacklist));
		} catch (Exception e) {
			LOGGER.error("Error when removing from index the blacklist {}", blacklist.toString(), e);
		}
	}

	protected void removeGroupFromIndex(Group group) {
		try {
			groupRepository.delete(new GroupDocument(group));
		} catch (Exception e) {
			LOGGER.error("Error when removing from index the group {}", group.toString(), e);
		}
	}

	protected void removeUserFromIndex(User user) {
		try {
			userRepository.delete(new UserDocument(user));
		} catch (Exception e) {
			LOGGER.error("Error when removing from index the user {}", user.toString(), e);
		}
	}

	protected void indexAsUserAndGroups(User user) {
		indexAsUser(user);
		//updates group index with the new user's information
		if (user.getCreatedGroups()!= null){
			for (Group createdGroup : user.getCreatedGroups()){
				indexAsGroup(createdGroup);
			}
		}
	}
}
