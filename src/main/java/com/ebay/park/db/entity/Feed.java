package com.ebay.park.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;


/**
 * The persistent class for the feed database table.
 * 
 */
@Entity
@Table(name="feed")
public class Feed implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="feed_id")
	private Long id;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="feed_usr_id")
	private User user;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="feed_owner_id")
	private User owner;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "feed_creation_date")
	private Date creationDate;

	@Column(name = "feed_read")
	private boolean read;
	
	//bi-directional many-to-one association to FeedProperty
    @ElementCollection
    @MapKeyColumn(name="fd_prop_key")
    @Column(name="fd_prop_value")
    @CollectionTable(name="feed_properties", joinColumns=@JoinColumn(name="fd_prop_feed_id"))
	private Map<String,String> feedProperties;

	//bi-directional many-to-one association to NtfConfigTemp
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="feed_not_conf_id")
	private NotificationConfig notificationConfig;
	
	//bi-directional many-to-one association to Item
	@ManyToOne
	@JoinColumn(name="feed_item_id")
	private Item item;

	public Feed() {
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Map<String, String> getFeedProperties() {
		return feedProperties;
	}

	public void setFeedProperties(Map<String, String> feedProperties) {
		this.feedProperties = feedProperties;
	}

	/**
	 * @return the notificationConfig
	 */
	public NotificationConfig getNotificationConfig() {
		return notificationConfig;
	}

	/**
	 * @param notificationConfig the notificationConfig to set
	 */
	public void setNotificationConfig(NotificationConfig notificationConfig) {
		this.notificationConfig = notificationConfig;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

}