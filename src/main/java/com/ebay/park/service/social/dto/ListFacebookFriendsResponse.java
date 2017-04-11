package com.ebay.park.service.social.dto;

import java.util.List;

import com.ebay.park.service.ListedResponse;

/**
 * It represents the response when asking for the list of Facebook Friends of the logged user.
 * Response can include the friends list, empty list message or insufficient permission message.
 * @author Julieta Salvadó
 */
public class ListFacebookFriendsResponse extends ListedResponse {
    private List<FacebookFriend> friends;

    public ListFacebookFriendsResponse() {

    }
    public ListFacebookFriendsResponse(List<FacebookFriend> friends) {
        setFriends(friends);
    }

    @Override
    public boolean listIsEmpty() {
        return getFriends() == null || getFriends().isEmpty();
    }

    public List<FacebookFriend> getFriends() {
        return friends;
    }

    public void setFriends(List<FacebookFriend> friends) {
        this.friends = friends;
    }
}
