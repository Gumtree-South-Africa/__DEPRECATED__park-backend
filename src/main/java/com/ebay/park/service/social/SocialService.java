package com.ebay.park.service.social;

import com.ebay.park.db.entity.User;
import com.ebay.park.service.social.dto.*;

import java.util.List;

/**
 * Social Service with methods common in social services such as follow someone,
 * get information about followings and followers, etc.
 * 
 * @author lucia.masola
 * 
 */
public interface SocialService {

	public ListUsersFollowedResponse getFollowers(GetFollowersRequest request);

	public List<User> getFollowers(User user);

	public ListUsersFollowedResponse getFollowings(GetFollowingsRequest request);

	public FollowUserResponse addFollowerToUser(FollowUserRequest request);

	public List<SmallRating> getUserRates(UserRatesRequest request);

	public Boolean removeFollowToUser(UnfollowUserRequest request);

	public Boolean shareProfileOnFacebook(ShareProfileRequest request);

	public Boolean shareProfileOnTwitter(ShareProfileRequest request);

	public Boolean connectToSocialNetwork(SocialNetworkConnectRequest request);

	public Boolean disconnectSocialNetwork(SocialNetworkDisconnectRequest request);

	public ListFacebookFriendsResponse listFacebookFriends(ListFacebookFriendsRequest request);

	public ListUsersFollowedResponse discoverUsers(DiscoverUsersRequest request);
	
	public ListUsersFollowedResponse discoverPublicUsers(DiscoverUsersRequest request);

	public SearchUserResponse searchUsers(SearchUserRequest request);

	public SearchUserResponse searchPublicUsers(SearchUserRequest request);

	public UnreadCountResponse unreadCount(UnreadCountRequest request);
}
