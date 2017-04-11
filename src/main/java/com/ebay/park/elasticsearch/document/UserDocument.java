package com.ebay.park.elasticsearch.document;

import com.ebay.park.db.entity.Follower;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserFollowsGroup;
import com.ebay.park.elasticsearch.document.nested.ESNFollower;
import com.ebay.park.elasticsearch.document.nested.ESNUserFollowsGroup;
import com.ebay.park.util.LocationUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Elasticsearch entity for {@link User}.
 * @author l.marino on 6/5/15.
 */
@Document(indexName = "user", type = "user")
@Setting(settingPath = "com/ebay/park/elasticsearch/document/settings/UserDocumentSettings.json")
public class UserDocument implements Serializable {

	private static Logger LOGGER = LoggerFactory.getLogger(UserDocument.class);
	private static final long serialVersionUID         = 1L;

	public static final String FIELD_USERNAME          = "username";
	public static final String FIELD_USERNAME_SORTABLE = "sortable";
	public static final String FIELD_EMAIL             = "email";
	public static final String FIELD_STATUS            = "status";
	public static final String FIELD_CREATION_DATE     = "creation";
	public static final String FIELD_LOCATION          = "location";
	public static final String FIELD_SUFFIX_NGRAMS     = "ngrams";
	public static final String FIELD_USER_ID    	   = "userId";
	public static final String FIELD_FOLLOWERS     	   = "followers";
	public static final String FIELD_FOLLOWER_ID       = "followers.id";
	public static final String FIELD_GROUPS            = "groups";
	public static final String FIELD_GROUP_ID          = "groups.groupId";

	@Id
	private Long userId;

	@Field(type = FieldType.Date, format = DateFormat.date_time)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	//FIXME, delete this when elasticsearch spring data implements it https://jira.spring.io/browse/DATAES-287
	private Date creation;

	@Field(type = FieldType.String, index = FieldIndex.analyzed, analyzer = "email_analyzer", searchAnalyzer = "email_analyzer")
	private String email;

	@Field(type = FieldType.Boolean)
	private Boolean emailVerified;

	@Field(type = FieldType.Boolean)
	private Boolean mobileVerified;

	@MultiField(mainField = @Field(type = FieldType.String, index = FieldIndex.analyzed, analyzer = "default_analyzer", searchAnalyzer = "default_analyzer"),
			otherFields = {
					@InnerField(suffix = FIELD_SUFFIX_NGRAMS, type = FieldType.String, indexAnalyzer = "ngram_analyzer", searchAnalyzer = "ngram_analyzer"),
					@InnerField(suffix = "sortable", type = FieldType.String, indexAnalyzer = "sortable")})
	private String username;

	@Field(type = FieldType.String)
	private String status;

	@GeoPointField
	private GeoPoint location;

	@Field(type = FieldType.Nested)
	private List<ESNFollower> followers;

	@Field(type = FieldType.Nested)
	private List<ESNUserFollowsGroup> groups;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(Boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public Boolean getMobileVerified() {
		return mobileVerified;
	}

	public void setMobileVerified(Boolean mobileVerified) {
		this.mobileVerified = mobileVerified;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public GeoPoint getLocation() {
		return location;
	}

	public void setLocation(GeoPoint location) {
		this.location = location;
	}

	public UserDocument() {
	}

	public List<ESNUserFollowsGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<ESNUserFollowsGroup> groups) {
		this.groups = groups;
	}

	/**
	 * Generate a new Document from Entity.
	 * @param user the source
	 */
	public UserDocument(User user) {
		Assert.notNull(user, "user must not be null");
		try {
			this.userId = user.getUserId();
			this.creation = user.getCreation();
			this.email = user.getEmail();
			this.emailVerified = user.isEmailVerified();
			this.mobileVerified = user.isMobileVerified();
			this.username = user.getUsername();
			this.status = user.getStatus().toString();
			convertLocation(user.getLatitude(), user.getLongitude());
			convertFollowers(user.getFollowers());
			convertFollowsGroup(user.getGroups());
		} catch (Exception e) {
			logException(e, user);
		}
	}

	private void convertLocation(Double latitude, Double longitude) {
		if (LocationUtil.validLatitude(latitude) && LocationUtil.validLongitude(longitude)) {
			this.location = new GeoPoint(latitude, longitude);
		}
	}

	private void convertFollowsGroup(List<UserFollowsGroup> followsGroups) {
		this.groups = new ArrayList<>();
		if (followsGroups != null) {
			for(UserFollowsGroup userFollowsGroup : followsGroups){
				this.groups.add(new ESNUserFollowsGroup(userFollowsGroup.getId().getUserId(),userFollowsGroup.getGroup().getId()));
			}
		}
	}

	private void convertFollowers(List<Follower> followers) {
		this.followers = new ArrayList<>();
		if (followers != null) {
			for (Follower follower : followers) {
				this.followers.add(new ESNFollower(follower.getId().getFollowerId()));
			}
		}
	}

	private void logException(Exception e, User user) {
		StringBuilder builder = new StringBuilder();
		builder.append("UserDocument from User Conversion failed!")
				.append(user == null ? " [Reason: null User" : "[User id " + user.getId());
		if (user != null) {
			builder.append(" / ")
					.append("Creation: ").append(user.getCreation())
					.append(" / ")
					.append("Email: ").append(user.getEmail())
					.append(" / ")
					.append("Email Verified: ").append(user.isEmailVerified())
					.append(" / ")
					.append("Mobile Verified: ").append(user.isMobileVerified())
					.append(" / ")
					.append("Username: ").append(user.getUsername())
					.append(" / ")
					.append(user.getStatus() == null ? "null status" : "Status: " + user.getStatus().toString())
					.append(" / ")
					.append(user.getLatitude() == null ? "null latitude" : "Latitude: " + user.getLatitude().toString())
					.append(" / ")
					.append(user.getLongitude() == null ? "null longitude" : "Longitude: " + user.getLatitude().toString())
					.append(" / ")
					.append(user.getFollowers() == null ? "null followers" : "Followers: " + user.getFollowers().toString())
					.append(" / ")
					.append(user.getGroups() == null ? "null groups" : "Groups: " + user.getGroups().toString());
		}
		builder.append("]");
		LOGGER.error(builder.toString(), e);
	}
}