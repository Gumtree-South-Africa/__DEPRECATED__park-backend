package com.ebay.park.db.entity;

import com.ebay.park.notification.NotificationAction;
import com.ebay.park.notification.NotificationType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the notification_config database table.
 * 
 */
@Entity
@Table(name="notification_config")
public class NotificationConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="not_id")
	private Long id;
	

	@Enumerated(EnumType.STRING)
	@Column(name="not_action")
	private NotificationAction notificationAction;
	
	@Enumerated(EnumType.STRING)
	@Column(name="not_type")
	private NotificationType notificationType;
	
	@Column(name="not_template_name")
	private String templetaName;

	@Column(name="not_version")
	private Long version;
	
	
	//bi-directional many-to-many association to User
	@ManyToMany(mappedBy="notificationConfigs")
	private List<User> users;
	
	//bi-directional many-to-one association to Feed
	@OneToMany(mappedBy="notificationConfig")
	private List<Feed> feeds;


	NotificationConfig() {
	}

	public NotificationConfig(NotificationAction notificationAction, NotificationType notificationType) {
		this.notificationAction = notificationAction;
		this.notificationType = notificationType;
	}
	
	public Long getNotId() {
		return this.id;
	}

	public void setNotId(Long notId) {
		this.id = notId;
	}

	public NotificationAction getNotificationAction() {
		return this.notificationAction;
	}


	public NotificationType getNotificationType() {
		return this.notificationType;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * @return the templeteName
	 */
	public String getTemplateName() {
		return templetaName;
	}

	/**
	 * @param templetaName the templeteName to set
	 */
	public void setTemplateName(String templetaName) {
		this.templetaName = templetaName;
	}
	
	public List<Feed> getFeeds() {
		return this.feeds;
	}

	public void setFeeds(List<Feed> feeds) {
		this.feeds = feeds;
	}

	public Feed addFeed(Feed feed) {
		getFeeds().add(feed);
		feed.setNotificationConfig(this);

		return feed;
	}

	public Feed removeFeed(Feed feed) {
		getFeeds().remove(feed);
		feed.setNotificationConfig(null);

		return feed;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NotificationConfig [id=")
        		.append(id).append(", notificationAction=")
        		.append(notificationAction)
				.append(", notificationType=")
				.append(notificationType)
				.append(", templeteName=")
				.append(templetaName)
				.append(", users=")
				.append(users)
				.append(", feeds=")
				.append(feeds)
				.append(", version=")
				.append(version)
				.append("]");
		return builder.toString();
	}

    /**
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Long version) {
        this.version = version;
    }
}