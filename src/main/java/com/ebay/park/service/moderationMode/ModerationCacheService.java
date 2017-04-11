package com.ebay.park.service.moderationMode;

import java.util.ArrayList;

/**
 * Access to user locked by moderation mode.
 * When a user is locked, any other moderator can take his/her items for moderation.
 *
 */
public interface ModerationCacheService {
	
	/**
	 * Returns the user who is locking the user associated to
	 * the given user ID. If it is not, it returns null. 
	 * @param userId
	 * @return the locked user stored in cache
	 */
	String getLockOwner(Long userId);
	
	/**
	 * Stores the user to lock.
	 * @param userId
	 * @param moderatorUserToken
	 * @return the moderator token 
	 */
	String lockUser(Long userId, String moderatorUserToken);
	
	/**
	 * Removes the user to unlock from cache
	 * @param userId
	 */
	void unlockUser(Long userId);
	
	/**
	 * Returns all the locked users.
	 * @return locked users 
	 */
	ArrayList<Long> getLockedUsers(String key);
	
	/**
	 * Stores a locked user list.
	 * @param key
	 * @param users It must be an ArrayList because it is Serializable
	 * to be stored in cache
	 * @return the stored list
	 */
	ArrayList<Long> saveUsers(String key, ArrayList<Long> users);

}
