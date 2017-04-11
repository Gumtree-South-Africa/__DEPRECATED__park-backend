package com.ebay.park.service.social.dto;

import com.ebay.park.service.ListedResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * It includes the response with a list a Facebook friends.
 * @author Julieta Salvadó
 */
public class GetFacebookFriendsResponse extends ListedResponse {
    private List<FacebookFriend> friends = new ArrayList<>();

    public GetFacebookFriendsResponse(List<FacebookFriend> friends) {
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
