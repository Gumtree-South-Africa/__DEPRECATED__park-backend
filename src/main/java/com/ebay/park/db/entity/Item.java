/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.Validate;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.joda.time.DateTime;

import com.ebay.park.util.DataCommonUtil;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;


/**
 * The persistent class for the item database table.
 * 
 * @author juan.pizarro
 * 
 */
// @formatter:off

// @formatter:on

@Entity
@Table(name = "item")
public class Item extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ite_id")
	private Long id;

	@Column(name = "ite_brandpublish")
	private String brandPublish;

	@Column(name = "ite_description")
	private String description;

	@Column(name = "ite_location")
	private String location;

	@Column(name = "ite_name")
	private String name;

	@Column(name = "ite_location_name")
	private String locationName;

	@Column(name = "ite_price")
	private Double price;

	@Column(name = "ite_versionpublish")
	private String versionPublish;
	@Column(name = "ite_picture1Url")
	private String picture1Url;

	@Column(name = "ite_picture2Url")
	private String picture2Url;

	@Column(name = "ite_picture3Url")
	private String picture3Url;

	@Column(name = "ite_picture4Url")
	private String picture4Url;

	@Column(name = "ite_lat")
	private Double latitude;

	@Column(name = "ite_long")
	private Double longitude;
	
	@Column(name = "ite_zip_code")
	private String zipCode;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "ite_stu_id")
	private StatusDescription status;
	
	private transient String localizedStatus;

	@Column(name = "ite_deleted")
	private boolean deleted;
	
	@Column(name = "ite_pending_moderation")
	private boolean pendingModeration = true;

	// bi-directional many-to-one association to Chat
	@OneToMany(mappedBy = "item")
	private List<Chat> chats;

	// bi-directional many-to-one association to Category
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ite_cat_id")
	private Category category;

	// bi-directional many-to-one association to UserFollowsItem
	@OneToMany(mappedBy = "item", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<UserFollowsItem> followers;

	@OneToMany(mappedBy = "item")
	private List<Conversation> conversations;

	@OneToMany(mappedBy = "item")
	@Where(clause = "con_status = 'OPEN'" )
	private List<Conversation> openConversations;

	@OneToMany(mappedBy = "item")
	@Where(clause = "con_status = 'ACCEPTED'" )
	private List<Conversation> acceptedConversations;
	
	// bi-directional many-to-one association to ItemGroup
	@OneToMany(mappedBy = "item", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<ItemGroup> itemGroups;

	@ManyToOne
	@JoinColumn(name = "ite_publisher_id")
	private User publishedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ite_created")
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ite_last_update")
	@UpdateTimestamp
	private Date lastModificationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ite_published")
	private Date published;

	@Column(name = "ite_count_reports")
	private Integer countOfReports;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ite_sold_expired_date")
	private Date soldExpiredDate;

	//bi-directional many-to-one association to Feed
	@OneToMany(mappedBy="item")
	private List<Feed> feeds;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ite_face_share")
	private ShareStatus facebookShareStatus = ShareStatus.NON_REQUESTED;

	@Enumerated(EnumType.STRING)
	@Column(name = "ite_twitter_share")
	private ShareStatus twitterShareStatus = ShareStatus.NON_REQUESTED;

	Item() {
	}

	public Item(String name, Double price, String versionPublish, boolean shareFacebook, boolean shareTwitter) {
		super();
		this.name = name;
		this.price = price;
		this.versionPublish = versionPublish;
		this.created = DateTime.now().toDate();
		this.lastModificationDate = DateTime.now().toDate();
		this.published = DateTime.now().toDate();
		this.setCountOfReports(0);
		this.deleted = false;
		this.setStatus(StatusDescription.IMAGE_PENDING);
		if (shareFacebook){
			facebookShareStatus = ShareStatus.PENDING;
		}
		if (shareTwitter){
			twitterShareStatus = ShareStatus.PENDING;
		}
		this.pendingModeration = true;
	}

	public Item(Item oldItem, String versionPublish){
		this.setDescription(oldItem.getDescription());
		this.setLocation(oldItem.getLocation());
		this.setName(oldItem.getName());
		this.setPrice(oldItem.getPrice());
		this.setPicture1Url(oldItem.getPicture1Url());
		this.setPicture2Url(oldItem.getPicture2Url());
		this.setPicture3Url(oldItem.getPicture3Url());
		this.setPicture4Url(oldItem.getPicture4Url());

		this.setLatitude(oldItem.getLatitude());
		this.setLongitude(oldItem.getLongitude());
		this.setLocationName(oldItem.getLocationName());
		this.setCountOfReports(0);
		this.setStatus(StatusDescription.ACTIVE);
		this.setCategory(oldItem.getCategory());

		//FIXME what is the difference between created and published
		this.setPublished(DateTime.now().toDate());
		this.setCreated(this.getPublished());
		this.setPendingModeration(oldItem.isPendingModeration());
	}

	public String getBrandPublish() {
		return this.brandPublish;
	}

	public void setBrandPublish(String brandPublish) {
		this.brandPublish = brandPublish;
	}

	public String getDescription() {
		return this.description;
	}

	/**
	 * It sets the item description.
	 * <b>Important</b>: a verification over the string should be done to avoid
	 * blacklisted word in items.
	 * @param description the new item description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * It sets the item name or title.
	 * <b>Important</b>: a verification over the string should be done to avoid
	 * blacklisted word in items.
	 * @param name title to set to the item
	 */
	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getVersionPublish() {
		return this.versionPublish;
	}

	public List<Chat> getChats() {
		return this.chats;
	}

	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}

	public Chat addChat(Chat chat) {
		getChats().add(chat);
		chat.setItem(this);

		return chat;
	}

	public Chat removeChat(Chat chat) {
		getChats().remove(chat);
		chat.setItem(null);

		return chat;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<UserFollowsItem> getFollowers() {
		if (followers == null) {
			return new ArrayList<UserFollowsItem>();
		}
		return this.followers;
	}

	public void setFollowers(List<UserFollowsItem> followers) {
		this.followers = followers;
	}

	public Boolean isFollowedByUser(User user) {
		UserFollowsItemPK pk = new UserFollowsItemPK(user, this);
		return getFollowers().contains(new UserFollowsItem(pk, user, this));
	}

	public UserFollowsItem addFollower(UserFollowsItem userFollowsItem) {
		getFollowers().add(userFollowsItem);
		userFollowsItem.setItem(this);

		return userFollowsItem;
	}

	public UserFollowsItem removeFollower(UserFollowsItem userFollowsItem) {
		getFollowers().remove(userFollowsItem);
		userFollowsItem.setItem(null);

		return userFollowsItem;
	}
	
	/**
	 * Delete relations between the item and the groups.
	 */
	private void removeGroups() {
		Iterables.removeAll(itemGroups, getItemGroups());
	}

	public List<Long> removeGroups(final List<Long> groupIds) {
		final List<Long> groupsRemoved = new ArrayList<Long>();
		Iterables.removeIf(getItemGroups(), new Predicate<ItemGroup>() {
			@Override
			public boolean apply(ItemGroup group) {
				long groupId = group.getGroup().getId();
				if (groupIds.contains(groupId)) {
					groupsRemoved.add(groupId);
					return true;
				}
				return false;
			}
		});
		return groupsRemoved;
	}

	
	public String getPicture1Url() {
		return picture1Url;
	}

	public void setPicture1Url(String picture1Url) {
		this.picture1Url = picture1Url;
	}

	public String getPicture2Url() {
		return picture2Url;
	}

	public void setPicture2Url(String picture2Url) {
		this.picture2Url = picture2Url;
	}

	public String getPicture3Url() {
		return picture3Url;
	}

	public void setPicture3Url(String picture3Url) {
		this.picture3Url = picture3Url;
	}

	public String getPicture4Url() {
		return picture4Url;
	}

	public void setPicture4Url(String picture4Url) {
		this.picture4Url = picture4Url;
	}

	public StatusDescription getStatus() {
		return status;
	}

	protected void setStatus(StatusDescription status) {
		this.status = status;
	}

	public boolean isPendingModeration() {
		return pendingModeration;
	}
	
	public void setPendingModeration(boolean value) {
		this.pendingModeration = value;
	}
	
	public boolean isDeleted() {
		return deleted;
	}

	public void delete() {
		this.deleted = true;
		this.cancelOpenConversations();
		removeGroups();
		//item-follower relations are not deleted in order to allow sending push to the followers after deleting: EPA001-5940
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ItemGroup> getItemGroups() {
		if (itemGroups == null){
			return Collections.emptyList();
		}
		return itemGroups;
	}

	public void setItemGroups(List<ItemGroup> itemGroups) {
		this.itemGroups = itemGroups;
	}

	public User getPublishedBy() {
		return publishedBy;
	}


	public void setPublishedBy(User publishedBy) {
		this.publishedBy = publishedBy;
	}

	public Date getPublished() {
		return published;
	}

	protected void setCreated(Date created) {
		this.created = created;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double lattitude) {
		this.latitude = lattitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * @return the conversations in ANY status
	 */
	public List<Conversation> getConversations() {
		return conversations;
	}
	
	/**
	 * @return the conversations in OPEN status
	 */
	public List<Conversation> getOpenConversations() {
		return openConversations;
	}
	
	/**
	 * @return the conversations in Accepted status
	 */
	public List<Conversation> getAcceptedConversations() {
		return acceptedConversations;
	}

	public boolean is(StatusDescription statusDescription) {
		return getStatus().equals(statusDescription);
	}

	public boolean isSeller(Long userId) {
		return getPublishedBy().getId().equals(userId);
	}

	public Date getCreated() {
		return created;
	}

	public void setPublished(Date published) {
		this.published = published;
	}

	public Date getLastModificationDate() {
		return lastModificationDate;
	}

	public Integer getCountOfReports() {
		return countOfReports;
	}

	public void setCountOfReports(Integer countOfReports) {
		this.countOfReports = countOfReports;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Date getSoldExpiredDate() {
		return soldExpiredDate;
	}

	protected void setSoldExpiredDate(Date soldExpiredDate) {
		this.soldExpiredDate = soldExpiredDate;
	}

	public List<Feed> getFeeds() {
		return feeds;
	}

	public void setFeeds(List<Feed> feeds) {
		this.feeds = feeds;
	}

	public boolean addGroup(Group group){
		ItemGroup im = new ItemGroup(this,group, DataCommonUtil.getCurrentTime());
		if (this.getItemGroups().contains(im)){
			//Nothing added, already has group
			return false;
		}
		if (itemGroups == null){
			itemGroups = new ArrayList<ItemGroup>();
		}
		this.itemGroups.add(im);
		return true;
	}

	public void removeGroup(Group group){
		for (ItemGroup ig: this.getItemGroups()){
			if (ig.getGroup().equals(group)){
				itemGroups.remove(ig);
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Item [id=").append(id).append(", name=").append(name).append(", price=").append(price)
				.append(", status=").append(status).append(", deleted=").append(deleted)
				.append(", pendingModeration=").append(pendingModeration)
				.append(", facebookShareStatus=").append(facebookShareStatus).append(", twitterShareStatus=")
				.append(twitterShareStatus).append("]");
		return builder.toString();
	}
	
	public void activate(){
		this.setStatus(StatusDescription.ACTIVE);
	}
	
	public void ban(){
		this.cancelOpenConversations();
		this.setStatus(StatusDescription.PENDING);
	}
	
	public void sold(){
		this.setStatus(StatusDescription.SOLD);
		this.setSoldExpiredDate(DateTime.now().toDate());
	}
	
	public void expired(){
		this.setStatus(StatusDescription.EXPIRED);
		this.setSoldExpiredDate(DateTime.now().toDate());
		this.cancelOpenConversations();
	}

	public void republish() {
		this.setSoldExpiredDate(null);
		this.setPublished(DateTime.now().toDate());
		this.setStatus(StatusDescription.ACTIVE);
	}
	
	/**
	 * @return the localizedStatus
	 */
	public String getLocalizedStatus() {
		return localizedStatus;
	}

	/**
	 * @param localizedStatus the localizedStatus to set
	 */
	public void setLocalizedStatus(String localizedStatus) {
		this.localizedStatus = localizedStatus;
	}
	
	public boolean isActive(){
		return StatusDescription.ACTIVE.equals(this.getStatus()) && !this.deleted ;
	}
	
	public boolean pendingFacebookShare(){
		return ShareStatus.PENDING.equals(facebookShareStatus);
	}
	
	public boolean pendingTwitterShare(){
		return ShareStatus.PENDING.equals(twitterShareStatus);
	}
	
	public void facebookShareDone() {
		Validate.isTrue(pendingFacebookShare(), "Share is not pending");
		facebookShareStatus = ShareStatus.DONE;
	}
	
	public void facebookShareFailed() {
		Validate.isTrue(pendingFacebookShare(), "Share is not pending");
		facebookShareStatus = ShareStatus.FAILED;
	}
	
	public void twitterShareDone() {
		Validate.isTrue(pendingTwitterShare(), "Share is not pending");
		twitterShareStatus = ShareStatus.DONE;
	}
	
	public void twitterShareFailed() {
		Validate.isTrue(pendingTwitterShare(), "Share is not pending");
		twitterShareStatus = ShareStatus.FAILED;
	}

	/**
	 * It changes all open item conversations to the CANCELLED state.
	 */
	public void cancelOpenConversations(){
		Long sellerId = getPublishedBy().getId();
		for (Conversation c : this.getOpenConversations()) {
			c.setStatus(ConversationStatus.CANCELLED);
			Chat chat = c.buildCancelMilestone(sellerId, null);
			chat.setAutomaticGeneratedAction(true);
		}
	}

	/**
     * It changes all accepted item conversations to the CANCELLED state.
     */
	public void cancelAcceptedConversations(){
		Long sellerId = getPublishedBy().getId();
		for (Conversation c : this.getAcceptedConversations()) {
			c.setStatus(ConversationStatus.CANCELLED);
			Chat chat = c.buildCancelMilestone(sellerId, null);
			chat.setAutomaticGeneratedAction(true);
		}
	}

	public void setOpenConversations(List<Conversation> openConversations) {
		this.openConversations = openConversations;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipcode) {
		this.zipCode = zipcode;
	}

	public void updateLastModificationDate() {
		this.lastModificationDate = DateTime.now().toDate();
	}

	/**
	 * Indicates if a user has any conversation as item buyer.
	 * @param user
	 *     the user to search for
	 * @return
	 *     true if the user started a conversation as buyer; false, otherwise.
	 */
	public boolean hasConversationWithUser(User user) {
	        return (getConversations() == null ? false : getConversations().stream()
	            .map(Conversation::getBuyer)
	            .anyMatch(u -> u.equals(user)));
	}
}
