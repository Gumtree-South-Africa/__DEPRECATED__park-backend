package com.ebay.park.service.social.dto;

import com.ebay.park.db.entity.Follower;
import com.ebay.park.db.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * DTO for Facebook Friend information.
 * @author Julieta Salvadó
 */
public class FacebookFriend {
    private String username;
    private String profilePicture;
    private String location;
    private Boolean followedByUser;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String friendOf;

    public FacebookFriend(User loggedUser, User friendUser, String friendOf) {
        Assert.notNull(loggedUser, "User must not be null here");
        Assert.notNull(friendUser, "Friend user must not be null here");

        this.username = friendUser.getUsername();
        this.profilePicture = friendUser.getPicture();
        this.location = friendUser.getLocationName();

        this.followedByUser = isFollowedBy(loggedUser, friendUser);

        if (!StringUtils.isEmpty(friendOf)) {
            this.friendOf = friendOf;
        }
    }

    private boolean isFollowedBy(User loggedUser, User friendUser) {
        List<Follower> friends = loggedUser.getFollowed();
        return friends != null && friends.stream()
                .map(Follower::getUserFollowed)
                .map(User::getUsername)
                .anyMatch(followedUsername -> friendUser.getUsername().equals(followedUsername));
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getLocation() {
        return location;
    }

    public Boolean getFollowedByUser() {
        return followedByUser;
    }

    public String getFriendOf() {
        return friendOf;
    }
}
