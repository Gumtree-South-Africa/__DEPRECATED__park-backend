package com.ebay.park.elasticsearch.document;

import com.ebay.park.db.entity.Group;
import com.ebay.park.db.entity.ItemGroup;
import com.ebay.park.db.entity.UserFollowsGroup;
import com.ebay.park.elasticsearch.document.nested.ESNItemGroup;
import com.ebay.park.elasticsearch.document.nested.ESNUser;
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
 * Elasticsearch entity for {@link Group}.
 * @author l.marino on 6/5/15.
 */
@Document(indexName = "group", type = "group")
@Setting(settingPath = "com/ebay/park/elasticsearch/document/settings/GroupDocumentSettings.json")
public class GroupDocument implements Serializable {

	private static Logger LOGGER = LoggerFactory.getLogger(GroupDocument.class);
	private static final long serialVersionUID = 1L;
	public static final String FIELD_GROUPNAME              = "groupName";
	public static final String FIELD_GROUPNAME_SORTABLE 	= "groupName.sortable";
	public static final String FIELD_GROUPNAME_FOLDED		= "groupName.folded";
	public static final String FIELD_GROUPS					= "groups";
	public static final String FIELD_GROUPS_USER_ID 		= "groups.userId";
	public static final String FIELD_CREATION               = "creation";

	@Id
	@Field(type = FieldType.Long)
	private Long groupId;
	@Field(type = FieldType.Date, format = DateFormat.date_time)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	private Date lastModDate;
	@MultiField(mainField = @Field(type = FieldType.String, index = FieldIndex.analyzed, analyzer = "default_analyzer", searchAnalyzer = "default_analyzer"),
			otherFields = {
					@InnerField(suffix = "ngrams", type = FieldType.String, indexAnalyzer = "ngram_analyzer", searchAnalyzer =	"ngram_analyzer"),
					@InnerField(suffix = "sortable", type = FieldType.String, indexAnalyzer = "sortable"),
					@InnerField(suffix = "folded", type = FieldType.String, indexAnalyzer = "folded",  searchAnalyzer = "folded")
			})
	private String groupName;
	@Field(type = FieldType.String, index = FieldIndex.analyzed)
	private String locationName;
	@GeoPointField
	private GeoPoint groupLocation;
	@Field(type = FieldType.Nested)
	private List<ESNUserFollowsGroup> groups;
	@Field(type = FieldType.Nested)
	private List<ESNItemGroup> items;
	@Field(type = FieldType.Object)
	private ESNUser creator;
	@Field(type = FieldType.Date, format = DateFormat.date_time)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	private Date creation;

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public List<ESNItemGroup> getItems() {
		return items;
	}

	public void setItems(List<ESNItemGroup> items) {
		this.items = items;
	}

	public ESNUser getCreator() {
		return creator;
	}

	public void setCreator(ESNUser creator) {
		this.creator = creator;
	}

	public GeoPoint getGroupLocation() {
		return groupLocation;
	}

	public void setGroupLocation(GeoPoint groupLocation) {
		this.groupLocation = groupLocation;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Date getLastModDate() {
		return lastModDate;
	}

	public void setLastModDate(Date lastModDate) {
		this.lastModDate = lastModDate;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public List<ESNUserFollowsGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<ESNUserFollowsGroup> groups) {
		this.groups = groups;
	}

	public GroupDocument() {
	}

	/**
	 * Generate a new Document form Entity
	 *
	 * @param group
	 */
	public GroupDocument(Group group) {
		Assert.notNull(group, "Group must not be null");
		try {
			this.groupId = group.getGroupId();
			this.groupName = group.getName();
			this.lastModDate = group.getLastModificationDate();
			this.locationName = group.getLocationName();
			this.creation = group.getCreation();
			this.creator = new ESNUser(group.getCreator().getId(),
					group.getCreator().getUsername(),
					group.getCreator().isEmailVerified(),
					group.getCreator().isMobileVerified());
			convertItems(group.getItems());
			convertGroups(group.getFollowers());
			convertLocation(group.getLatitude(), group.getLongitude(), group.getId());
		} catch (Exception e) {
			logException(e, group);
		}
	}

	private void convertLocation(Double latitude, Double longitude, Long id) {
		if (LocationUtil.validLatitude(latitude) && LocationUtil.validLongitude(longitude)) {
			this.groupLocation = new GeoPoint(latitude, longitude);
		} else {
			LOGGER.error("The group {} was indexed, but latitude and longitude are invalid",id);
		}
	}

	private void convertGroups(List<UserFollowsGroup> toCopy) {
		this.groups = new ArrayList<>();
		if (toCopy != null) {
			for (UserFollowsGroup userFollowsGroup : toCopy) {
				this.groups.add(new ESNUserFollowsGroup(userFollowsGroup
						.getId().getUserId(), userFollowsGroup.getGroup()
						.getId()));
			}
		}
	}

	private void convertItems(List<ItemGroup> toCopy) {
		this.items = new ArrayList<>();
		if (toCopy != null) {
			for (ItemGroup itemGroup : toCopy) {
				this.items.add(new ESNItemGroup(itemGroup.getItem()
						.getId(), itemGroup.getGroup().getGroupId()));
			}
		}
	}

	private void logException(Exception e, Group group) {
		StringBuilder builder = new StringBuilder();
		builder.append("GroupDocument from Group Conversion failed! ")
				.append(group == null ? "[Reason: null Group" : "[Group id " + group.getId())
				.append(" / ")
				.append("Name: ").append(group.getName())
				.append(" / ")
				.append("Last Modification Date: ").append(group.getLastModificationDate() == null ? "null" : group.getLastModificationDate().toString())
				.append(" / ")
				.append("Latitude: ").append(group.getLatitude())
				.append("& Longitud: ").append(group.getLongitude())
				.append(" / ")
				.append("Location Name: ").append(group.getLocationName())
				.append(" / ")
				.append("Creation: ").append(group.getCreation() == null ? "null" : group.getCreation().toString())
				.append(" / ")
				.append("Creator: ").append(group.getCreator() == null ? "null" : group.getCreator().toString())
				.append(" / ")
				.append("Items: ").append(group.getItems() == null ? "null" : group.getItems().toString())
				.append(" / ")
				.append("Followers: ").append(group.getFollowers() == null ? "null" : group.getFollowers().toString());
		builder.append("]");
		LOGGER.error(builder.toString(), e);
	}

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GroupDocument [id=").append(groupId).append(", name=").append(groupName).append("], ");
		return builder.toString();
	}
}