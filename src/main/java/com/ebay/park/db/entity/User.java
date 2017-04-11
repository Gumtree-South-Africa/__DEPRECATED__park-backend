/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.Validate;
import org.hibernate.annotations.Where;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.HibernateProxyHelper;

import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.Mappable;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the user database table.
 * 
 * @author juan.pizarro
 * 
 */
@Entity
@Table(name = "user")
public class User extends AbstractEntity implements BasicUser, Serializable, Mappable<String, String> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "use_id")
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "use_creation")
	private Date creation;

	@Column(name = "use_email")
	private String email;

	@Column(name = "use_lat")
	private Double latitude;

	@Column(name = "use_long")
	private Double longitude;

	@Column(name = "use_mobile")
	private String mobile;

	@Column(name = "use_location_name")
	private String locationName;

	@JsonIgnore
	@Column(name = "use_password")
	private byte[] password;

	@Column(name = "use_picture")
	private String picture;

	@Column(name = "use_token")
	private String token;

	@Column(name = "use_url")
	private String url;

	@Column(name = "use_username")
	private String username;

	@Column(name = "use_zip_code")
	private String zipcode;

	@Embedded
	private Access access;

	// bi-directional many-to-one association to Follower
	@OneToMany(mappedBy = "userFollowed")
	private List<Follower> followers;
	
	// bi-directional many-to-one association to Follower
	@OneToMany(mappedBy = "userFollower")
	private List<Follower> followed;

	// bi-directional many-to-one association to Rating
	@OneToMany(mappedBy = "user")
	private List<Rating> ratings;

	// bi-directional many-to-one association to City
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "use_cit_id")
	private City city;

	// bi-directional many-to-one association to Idiom
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "use_idi_id")
	private Idiom idiom;

	// bi-directional many-to-one association to Notmobile
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "use_nmo_id")
	private Notmobile notmobile;

	// bi-directional many-to-one association to UserItemliked
	@OneToMany(mappedBy = "user")
	private List<UserFollowsItem> itemLikeds;
	
	@OneToMany(mappedBy = "creator")
	private List<Group> createdGroups; 

	// bi-directional many-to-one association to UserGroup
	@OneToMany(mappedBy = "user", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<UserFollowsGroup> groups;

	// bi-directional many-to-many association to NotificationConfig
	@ManyToMany
	@JoinTable(name = "user_notification", joinColumns = { @JoinColumn(name = "us_use_id") }, inverseJoinColumns = { @JoinColumn(name = "us_not_id") })
	private List<NotificationConfig> notificationConfigs;

	// bi-directional many-to-one association to UserSocial
	@OneToMany(mappedBy = "user")
	private List<UserSocial> userSocials;

	@OneToMany(mappedBy = "publishedBy")
	@Where(clause = "ite_deleted=0 and ite_stu_id > 0 and ite_stu_id < 5" )
	private List<Item> publishedItems;
	
	@OneToMany(mappedBy = "publishedBy")
	@Where(clause = "ite_deleted=0 and (ite_stu_id = 1 or ite_stu_id = 2)" )
	private List<Item> publicPublishedItems;

	@Column(name = "use_email_verified")
	private boolean emailVerified;

	@Column(name = "use_mobile_verified")
    private boolean mobileVerified;

    // bi-directional many-to-one association to Feed
	@OneToMany(mappedBy = "owner")
	private List<Feed> ownFeeds;

	// bi-directional many-to-one association to Feed
	@OneToMany(mappedBy = "user")
	private List<Feed> othersFeeds;

	@Enumerated(EnumType.STRING)
	@Column(name = "use_status")
	private UserStatusDescription status = UserStatusDescription.ACTIVE;

	// bi-directional many-to-one association to Feed
	@OneToMany(cascade={CascadeType.MERGE, CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval=true, mappedBy = "user")
	private List<UserSession> userSessions;

	public User() {
	}

	public Long getUserId() {
		return this.id;
	}

	public void setUserId(Long userId) {
		this.id = userId;
	}

	public Date getCreation() {
		return this.creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public byte[] getPassword() {
		return this.password;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

	public String getPicture() {
		return this.picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	@Override
	public String getToken() {
		return this.token;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getZipCode() {
		return this.zipcode;
	}

	public void setZipCode(String zipCode) {
		this.zipcode = zipCode;
	}

	public List<Follower> getFollowers() {
		if (this.followers == null) {
			followers = new ArrayList<Follower>();
		}
		return this.followers;
	}

	public void setFollowers(List<Follower> followers) {
		this.followers = followers;
	}

	public boolean isFollowedByUser(User user) {
		FollowerPK pk = new FollowerPK(user.getId(), id);
		return getFollowers().contains(new Follower(pk, user));
	}

	public Follower addFollower(Follower follower) {
		getFollowers().add(follower);
		follower.setUserFollower(this);

		return follower;
	}

	public Follower removeFollower(Follower follower) {
		getFollowers().remove(follower);
		follower.setUserFollower(null);

		return follower;
	}

	public UserSession removeUserSession(UserSession userSession){
		getUserSessions().remove(userSession);
		return userSession;
	}

	public List<Rating> getRatings() {
		return this.ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}

	public Rating addRating(Rating rating) {
		getRatings().add(rating);
		rating.setUser(this);

		return rating;
	}

	public Rating removeRating(Rating rating) {
		getRatings().remove(rating);
		rating.setUser(null);

		return rating;
	}

	public City getCity() {
		return this.city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Idiom getIdiom() {
		return this.idiom;
	}

	public void setIdiom(Idiom idiom) {
		this.idiom = idiom;
	}

	public Notmobile getNotmobile() {
		return this.notmobile;
	}

	public void setNotmobile(Notmobile notmobile) {
		this.notmobile = notmobile;
	}

	public List<UserFollowsItem> getItemLikeds() {
		return this.itemLikeds;
	}

	public void setItemLikeds(List<UserFollowsItem> ItemLikeds) {
		this.itemLikeds = ItemLikeds;
	}

	public UserFollowsItem addItemLiked(UserFollowsItem itemLiked) {
		getItemLikeds().add(itemLiked);
		itemLiked.setUser(this);

		return itemLiked;
	}

	public UserFollowsItem removeUserItemliked(UserFollowsItem itemLiked) {
		getItemLikeds().remove(itemLiked);
		itemLiked.setUser(null);

		return itemLiked;
	}

	public List<UserFollowsGroup> getGroups() {
	    if (this.followers == null) {
            groups = new ArrayList<UserFollowsGroup>();
        }
		return this.groups;
	}

	public void setGroups(List<UserFollowsGroup> groups) {
		this.groups = groups;
	}
	
	public List<UserSocial> getUserSocials() {
		return this.userSocials;
	}

	public void setUserSocials(List<UserSocial> userSocials) {
		this.userSocials = userSocials;
	}

	public UserSocial addUserSocial(UserSocial userSocial) {
		if (getUserSocials() == null) {
			userSocials = new ArrayList<UserSocial>();
		}
		getUserSocials().add(userSocial);
		userSocial.setUser(this);
		return userSocial;
	}

	public UserSocial removeUserSocial(UserSocial userSocial) {
		getUserSocials().remove(userSocial);
		userSocial.setUser(null);

		return userSocial;
	}

	@Override
	public Long getId() {
		return id;
	}

	public Access getAccess() {
		if (access == null) {
			access = new Access();
		}
		return access;
	}

	protected void setAccess(Access access) {
		this.access = access;
	}

	public List<Item> getPublishedItems() {
		return publishedItems;
	}

	public void setPublishedItems(List<Item> publishedItems) {
		this.publishedItems = publishedItems;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public boolean isMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(boolean phoneVerified) {
        this.mobileVerified = phoneVerified;
    }

	public List<NotificationConfig> getNotificationConfigs() {
		return this.notificationConfigs;
	}

	public void setNotificationConfigs(
			List<NotificationConfig> notificationConfigs) {
		this.notificationConfigs = notificationConfigs;
	}

	public List<Feed> getOwnFeeds() {
		return ownFeeds;
	}

	public void setOwnFeeds(List<Feed> ownFeeds) {
		this.ownFeeds = ownFeeds;
	}

	public List<Feed> getOthersFeeds() {
		return othersFeeds;
	}

	public void setOthersFeeds(List<Feed> othersFeeds) {
		this.othersFeeds = othersFeeds;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserStatusDescription getStatus() {
		return status;
	}

	public void setStatus(UserStatusDescription status) {
		Validate.notNull(status, "Status is requiered for the user");
		this.status = status;
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("user.email", this.email);
		map.put("user.username", this.username);
		map.put("user.picture", this.picture);
		map.put("user.url", this.url);
		if (this.getAccess() != null) {
			map.put("user.tempToken", this.getAccess().getTemporaryToken());
		}

		return map;
	}


	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public boolean isSubscribedToGroup(Group group) {
		for(UserFollowsGroup userGroup : getGroups()){
			if (userGroup.isGroup(group)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void populateSession(UserSessionCache userSessionCache) {
		userSessionCache.setUsername(this.getUsername());
		userSessionCache.setEmail(this.getEmail());
		userSessionCache.setUserId(this.getId());
		userSessionCache.setLang(this.getIdiom().getCode());
	}

	public NotificationConfig getNotificationConfig(NotificationAction action, NotificationType type) {
		for (NotificationConfig not : getNotificationConfigs()) {
			if (not.getNotificationType().equals(type) & not.getNotificationAction().equals(action)) {
				return not;
			}
		}
		return null;
	}

	public boolean isNotificationEnabled(NotificationAction action, NotificationType type) {
		return getNotificationConfig(action, type) != null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [id=").append(id).append(", email=").append(email).append(", username=").append(username)
		.append(", status=").append(status).append("]");
		return builder.toString();
	}

	/**
	 * @return the publicPublishedItems
	 */
	public List<Item> publicPublishedItems() {
		return publicPublishedItems;
	}

	/**
	 * @param publicPublishedItems the publicPublishedItems to set
	 */
	public void setPublicPublishedItems(List<Item> publicPublishedItems) {
		this.publicPublishedItems = publicPublishedItems;
	}

	public List<Follower> getFollowed() {
		return followed;
	}

	public void setFollowed(List<Follower> followed) {
		this.followed = followed;
	}

	/**
	 * Indicates if the user has his/her Park account linked to his/her
	 * Facebook account.
	 * @return true if Park account is linked to Facebook; otherwise, false.
	 */
	public boolean hasFacebookAccountLinked() {
	    List<UserSocial> userSocialList = getUserSocials();
	    if (userSocialList != null) {
	        return userSocialList.stream()
    	        .map(UserSocial::getSocial)
    	        .map(Social::getDescription)
    	        .anyMatch(description -> description.equals(Social.FACEBOOK));
	    }
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (HibernateProxyHelper.getClassWithoutInitializingProxy(this) != HibernateProxyHelper.getClassWithoutInitializingProxy(obj))
			return false;
		User other;
		if (obj instanceof HibernateProxy) {
	        other = (User) ((HibernateProxy) obj).getHibernateLazyInitializer()
	                .getImplementation();
	    } else {
	    	other = (User) obj;
	    }
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public List<Group> getCreatedGroups() {
		return createdGroups;
	}

	public void setCreatedGroups(List<Group> createdGroups) {
		this.createdGroups = createdGroups;
	}

	public List<UserSession> getUserSessions() {
		return userSessions;
	}

	public void setUserSessions(List<UserSession> userSessions) {
		this.userSessions = userSessions;
	}

	public UserSession addUserSession(UserSession userSession) {
		if(this.getUserSessions() == null){
			this.setUserSessions(new ArrayList<UserSession>());
		}
		getUserSessions().add(userSession);
		userSession.setUser(this);
		return userSession;
	}

	public long getUnreadFeeds() {
		return ownFeeds.stream().filter(feed -> !feed.isRead()).count();
	}

}
