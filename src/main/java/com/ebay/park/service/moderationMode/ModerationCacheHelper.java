package com.ebay.park.service.moderationMode;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModerationCacheHelper {

    /**
     * Memcached key.
     */
    private static final String LOCKED_USERS_KEY = "LOCKED_USERS";

    /**
     * The service.
     */
    @Autowired
    private ModerationCacheService moderationCacheService;

    /**
     * It locks a user.
     *
     * @param userId
     *            user id to be locked
     * @param moderatorUserToken
     *            token from the moderator that is locking
     * @return token from the moderator that is locking
     */
    public String lockUser(Long userId, String moderatorUserToken) {
        ArrayList<Long> lockedUsers = moderationCacheService
                .getLockedUsers(LOCKED_USERS_KEY);
        if (lockedUsers == null) {
            lockedUsers = new ArrayList<>();
        }
        lockedUsers.add(userId);
        moderationCacheService.saveUsers(LOCKED_USERS_KEY, lockedUsers);
        return moderationCacheService.lockUser(userId, moderatorUserToken);
    }

    /**
     * It unlocks a user from setting its locking moderator.
     *
     * @param userId
     *            user to be unlocked
     * @param moderatorUserToken
     *            token from the locking moderator
     */
    public void unlockUser(Long userId, String moderatorUserToken) {
        String ownerToken = moderationCacheService.getLockOwner(userId);
        if (ownerToken != null && moderatorUserToken.equals(ownerToken)) {
            ArrayList<Long> lockedUsers = moderationCacheService
                    .getLockedUsers(LOCKED_USERS_KEY);
            if (lockedUsers != null && !lockedUsers.isEmpty()) {
                lockedUsers.remove(userId);
            }
            moderationCacheService.saveUsers(LOCKED_USERS_KEY, lockedUsers);
            moderationCacheService.unlockUser(userId);
        }
    }

    /**
     * Unlocks any user locked by this moderator.
     *
     * @param token
     *            moderator token
     */
    public void unlockUser(String token) {
        List<Long> lockedUserList = getLockedUsers();
        if (lockedUserList != null && !lockedUserList.isEmpty()) {
        lockedUserList
                .stream()
                .filter(userId -> token.equals(moderationCacheService.getLockOwner(userId)))
                .forEach(userId -> unlockUser(userId, token));
        }
    }

    /**
     * It checks if the item is not locked by another moderator.
     *
     * @param publisherId
     *            id of the owner of the item
     * @param token
     *            token of the moderator that wants to know if the item is
     *            available to him
     * @return true if the item is not locked by another user; false, otherwise
     */
    public boolean isItemAvailableToModerate(Long publisherId, String token) {
        String ownerToken = moderationCacheService.getLockOwner(publisherId);
        if (ownerToken == null) {
            return true;
        } else {
            return token.equals(ownerToken);
        }
    }

    /**
     * It returns the list of locked users.
     *
     * @return list of locked users
     */
    public ArrayList<Long> getLockedUsers() {
        return moderationCacheService.getLockedUsers(LOCKED_USERS_KEY);
    }

    /**
     * Stores a locked user list.
     *
     * @param key
     *            the key
     * @param users
     *            user list
     * @return the stored lis
     */
    public ArrayList<Long> saveUsers(String key, ArrayList<Long> users) {
        return moderationCacheService.saveUsers(key, users);
    }
}
