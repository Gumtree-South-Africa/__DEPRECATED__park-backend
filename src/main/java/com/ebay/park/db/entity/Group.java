/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import com.ebay.park.elasticsearch.document.UserDocument;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.geo.GeoPoint;
import org.hibernate.annotations.UpdateTimestamp;
import org.joda.time.DateTime;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the group database table.
 * 
 * @author juan.pizarro
 * 
 */
@Entity
@Table(name = "`group`")
public class Group extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String FIELD_NAME = "name";
	public static final String FIELD_NAME_SORT = "name_forSort";
	public static final String FIELD_CREATOR_NAME = "creator." + UserDocument.FIELD_USERNAME;
	public static final String FOLLOWERS_USER_ID = "followers.id.userId";
	public static final String FIELD_LOCATION = "groupLocation";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "grp_id")
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "grp_creation")
	private Date creation;

	@Column(name = "grp_lat")
	private Double latitude;

	@Column(name = "grp_long")
	private Double longitude;

	/**
	 * Name is NOT unique
	 */
	@Column(name = "grp_name")
	private String name;

	@Column(name = "grp_picture")
	private String picture;
	
	@Column(name = "grp_description")
	private String description;

	// bi-directional many-to-one association to UserGroup
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy = "group")
	private List<UserFollowsGroup> followers = new ArrayList <UserFollowsGroup>();

	// bi-directional many-to-one association to ItemGroup
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy = "group")
	private List<ItemGroup> items;

	@Column(name = "grp_location_name")
	private String locationName;
	
	@Column(name = "grp_zip_code")
	private String zipcode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "grp_last_update", nullable = false)
	@UpdateTimestamp
	private Date lastModificationDate;
	
	@ManyToOne
	@JoinColumn(name = "grp_creator_id", nullable = false)
	private User creator;

	Group() {
		
	}
	
	public Group(String name, User creator, String description) {
		this.creation  = DateTime.now().toDate();
		this.lastModificationDate = creation;
		this.creator = creator;
		this.name = name;
		this.description = description;
		
	}

	public Long getGroupId() {
		return this.id;
	}


	public Date getCreation() {
		return this.creation;
	}

	public Double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public void setLocation(Double longitude, Double latitude, String locationName, String zipCode) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.locationName = locationName;
		this.zipcode = zipCode;
	}

//TODO implement geolocalization
	public GeoPoint getLocation() {
		return new GeoPoint();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicture() {
		return (StringUtils.isBlank(this.picture) ? null : this.picture);
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public List<UserFollowsGroup> getFollowers() {
		return this.followers;
	}

	public UserFollowsGroup addFollower(UserFollowsGroup follower) {
		getFollowers().add(follower);;
		lastModificationDate = new Date();

		return follower;
	}

	public List<Long> removeFollowers(final List<Long> userIds) {
		final List<Long> followersRemoved = new ArrayList<Long>();
		Iterables.removeIf(getFollowers(), new Predicate<UserFollowsGroup>() {
			@Override
			public boolean apply(UserFollowsGroup follower) {
             		long userId = follower.getUser().getId();
					 // only removes if the follower is not the creator
					if ((userIds.contains(userId))
							&& (getCreator().getId() != userId)) {
						followersRemoved.add(userId);
						return true;
					}
				return false;
			}
		});
		return followersRemoved;
	}

	public boolean removeFollower(User follower) {
		UserFollowsGroup userGroupToRemove = null;
		for (UserFollowsGroup userGroup : getFollowers()) {
			if (userGroup.isUser(follower)) {
				userGroupToRemove = userGroup;
				continue;
			}
		}

		if (userGroupToRemove != null) {
			getFollowers().remove(userGroupToRemove);
			lastModificationDate = new Date();
			return true;
		}
		return false;
	}
	
	public List<ItemGroup> getItems() {
		if(items == null){
			items = new ArrayList<>();
		}
		return this.items;
	}

	public void setId(Long id){
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getLocationName() {
		return locationName;
	}

	/**
	 * @return the creator
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * @return the zipcode
	 */
	public String getZipcode() {
		return zipcode;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public Date getLastModificationDate() {
		return lastModificationDate;
	}

	/* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Group [id=").append(id).append(", name=").append(name).append(", locationName=")
				.append(locationName).append(", creator=").append(creator).append("]");
		return builder.toString();
	}

}