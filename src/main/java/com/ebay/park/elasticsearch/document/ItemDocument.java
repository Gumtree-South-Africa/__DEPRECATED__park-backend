package com.ebay.park.elasticsearch.document;


import com.ebay.park.db.entity.Category;
import com.ebay.park.db.entity.Item;
import com.ebay.park.db.entity.ItemGroup;
import com.ebay.park.elasticsearch.document.nested.ESNCategory;
import com.ebay.park.elasticsearch.document.nested.ESNItemGroup;
import com.ebay.park.elasticsearch.document.nested.ESNUser;
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
 * Elasticsearch Entity for {@link Item}.
 * @author l.marino on 6/5/15.
 */
@Document(indexName = "item", type = "item")
@Setting(settingPath = "com/ebay/park/elasticsearch/document/settings/ItemDocumentSettings.json")
public class ItemDocument implements Serializable {

	private static final long serialVersionUID = 1L;
    private static Logger LOGGER = LoggerFactory.getLogger(ItemDocument.class);
	public static final String FIELD_ID                   = "itemId";
    public static final String FIELD_COUNT_OF_REPORTS     = "countOfReports";
    public static final String FIELD_NAME                 = "name";
    public static final String FIELD_DESCRIPTION          = "description";
    public static final String FIELD_PUBLISHED            = "published";
    public static final String FIELD_PRICE                = "price";
    public static final String FIELD_LOCATION_NAME        = "locationName";
    public static final String FIELD_LOCATION             = "location";
    public static final String FIELD_PUBLISHER_ID         = "publishedBy.id";
    public static final String FIELD_PUBLISHER_NAME       = "publishedBy.username";
    public static final String FIELD_PUBLISHER_LOCATION   = "publishedBy.userLocation";
    public static final String FIELD_ITEM_GROUPS          = "itemGroups";
    public static final String FIELD_ITEM_GROUPS_GROUP_ID = "itemGroups.groupId";
    public static final String FIELD_STATUS               = "status";
    public static final String FIELD_DELETED              = "deleted";
    public static final String FIELD_CATEGORY_ID = "category.id";
    public static final String FIELD_CAREGORY_ORDER       = "category.order";
    public static final String FIELD_SUFFIX_SHINGLES      = "shingles";
    public static final String FIELD_SUFFIX_NGRAMS        = "ngrams";
    public static final String FIELD_LAST_MODIFICATION    = "lastModificationDate";
    public static final String FIELD_PENDING_MODERATION   = "pendingModeration";
    public static final String FIELD_ZIPCODE              = "zipcode";

    @Id
    private Long itemId;

    @MultiField(mainField = @Field(type = FieldType.String, index = FieldIndex.analyzed, analyzer = "default_analyzer", searchAnalyzer = "default_analyzer"),
            otherFields = {
                    @InnerField(suffix = FIELD_SUFFIX_SHINGLES, type = FieldType.String, indexAnalyzer = "shingle_analyzer", searchAnalyzer = "shingle_analyzer"),
                    @InnerField(suffix = FIELD_SUFFIX_NGRAMS, type = FieldType.String, indexAnalyzer = "ngram_analyzer", searchAnalyzer = "ngram_analyzer")})
    private String description;

    @Field(type = FieldType.String)
    private String locationName;
    @Field(type = FieldType.Integer)
    private Integer countOfReports;

    @Field(type = FieldType.Object)
    private ESNCategory category;

    @MultiField(mainField = @Field(type = FieldType.String, index = FieldIndex.analyzed, analyzer = "default_analyzer", searchAnalyzer = "default_analyzer"),
            otherFields = {
                    @InnerField(suffix = FIELD_SUFFIX_SHINGLES, type = FieldType.String, indexAnalyzer = "shingle_analyzer", searchAnalyzer = "shingle_analyzer"),
                    @InnerField(suffix = FIELD_SUFFIX_NGRAMS, type = FieldType.String, indexAnalyzer = "ngram_analyzer", searchAnalyzer = "ngram_analyzer")})
    private String name;
    @Field(type = FieldType.Double)
    private Double price;
    @Field(type = FieldType.Date, format = DateFormat.date_time)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZZ") //FIXME, delete this when elasticsearch spring data implements it https://jira.spring.io/browse/DATAES-287
    private Date published;
    @Field(type = FieldType.String)
    private String status;
    @Field(type = FieldType.Nested)
    private List<ESNItemGroup> itemGroups;
    @Field(type = FieldType.Object)
    private ESNUser publishedBy;
    @Field(type = FieldType.Boolean)
    private Boolean deleted;
    @GeoPointField
    private GeoPoint location;
    @Field(type = FieldType.Date, format = DateFormat.date_time)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    //FIXME, delete this when elasticsearch spring data implements it https://jira.spring.io/browse/DATAES-287
    private Date lastModificationDate;
    @Field(type = FieldType.Boolean)
    private Boolean pendingModeration;
    @Field(type = FieldType.String)
    private String zipcode;
    
    public String getZipcode() {
        return zipcode;
    }
    
    public void setZipCode(String zipcode) {
        this.zipcode = zipcode;
    }
    
    public Integer getCountOfReports() {
        return countOfReports;
    }

    public void setCountOfReports(Integer countOfReports) {
        this.countOfReports = countOfReports;
    }

    public ESNCategory getCategory() {
        return category;
    }

    public void setCategory(ESNCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public List<ESNItemGroup> getItemGroups() {
        return itemGroups;
    }

    public void setItemGroups(List<ESNItemGroup> itemGroups) {
        this.itemGroups = itemGroups;
    }

    public ESNUser getPublishedBy() {
        return publishedBy;
    }

    public void setPublishedBy(ESNUser publishedBy) {
        this.publishedBy = publishedBy;
    }
    
    public Boolean getPendingModeration() {
        return pendingModeration;
    }

    public void setPendingModeration(Boolean pendingModeration) {
        this.pendingModeration = pendingModeration;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
    
    public Date getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
	}

	public ItemDocument() {
    }

    /**
     * Generate a new Document form Entity from an {@link Item}.
     * @param item the source item
     */
    public ItemDocument(Item item) {
        Assert.notNull(item, "Item must not be null");
        try {
            this.itemId = item.getId();
            this.countOfReports = item.getCountOfReports();
            if (item.getCategory() != null) {
                this.category = new ESNCategory(item.getCategory().getCategoryId(),
                        item.getCategory().getCatOrder(),
                        item.getCategory().getName());
            }
            this.description = item.getDescription();
            this.locationName = item.getLocationName();
            this.name = item.getName();
            this.price = item.getPrice();
            this.status = item.getStatus().toString();
            this.deleted = item.isDeleted();
            this.zipcode = item.getZipCode();
            this.itemGroups = new ArrayList<>();
            if (item.getItemGroups() != null) {
                for (ItemGroup itemGroup : item.getItemGroups()) {
                    this.itemGroups.add(new ESNItemGroup(
                            itemGroup.getItem().getId(),
                            itemGroup.getGroup().getGroupId()));
                }
            }

            if (item.getPublishedBy() != null) {
                this.publishedBy = new ESNUser(item.getPublishedBy().getId(),
                        item.getPublishedBy().getUsername(),
                        item.getPublishedBy().isEmailVerified(),
                        item.getPublishedBy().isMobileVerified());
            }
            this.pendingModeration = item.isPendingModeration();
            convertLocation(item.getLatitude(), item.getLongitude(), item.getId());
            this.lastModificationDate = item.getLastModificationDate();
            this.published = item.getPublished();
        } catch (Exception e) {
            logException(e, item);
        }
    }

    private void convertLocation(Double latitude, Double longitude, Long id) {
        if (LocationUtil.validLatitude(latitude) && LocationUtil.validLongitude(longitude)) {
            this.location = new GeoPoint(latitude, longitude);
        } else {
            LOGGER.error("The item {} was indexed, but latitude and longitude are invalid", id);
        }
    }

    private void logException(Exception e, Item item) {
        StringBuilder builder = new StringBuilder();
        builder.append("ItemDocument from Item Conversion failed! ")
                .append(item == null ? "[Reason: null Item" : "[Item id " + item.getId());
        if (item != null) {
            builder.append(" / ")
                    .append("Count of reports: ").append(item.getCountOfReports())
                    .append(" / ");
            Category category = item.getCategory();
            if (category == null) {
                builder.append("Null category")
                        .append(" / ");
            } else {
                builder.append("Category Id: ").append(category.getCategoryId())
                        .append(" / ")
                        .append("Category Order: ").append(category.getCatOrder())
                        .append(" / ")
                        .append("Category Name: ").append(category.getName());
            }
            builder.append(" / ")
                    .append("Description: ")
                    .append(item.getDescription() == null ? "null" : item.getDescription().toString())
                    .append(" / ")
                    .append("Location Name: ").append(item.getLocationName())
                    .append(" / ")
                    .append("Name: ").append(item.getName())
                    .append(" / ")
                    .append("Price: ").append(item.getPrice())
                    .append(" / ")
                    .append("Status: ").append(item.getStatus())
                    .append(" / ")
                    .append("Is deleted: ").append(item.isDeleted())
                    .append(" / ")
                    .append("Zipcode: ").append(item.getZipCode())
                    .append(" / ")
                    .append(item.getItemGroups() == null ? "Null groups" : "Groups: " + item.getItemGroups().toString())
                    .append(" / ")
                    .append(item.getPublishedBy() == null ? "Null PublishedBy" : "Published by: " + item.getPublishedBy().toString())
                    .append(" / ")
                    .append("Pending moderation: ").append(item.isPendingModeration())
                    .append(" / ")
                    .append("Latitude: ").append(item.getLatitude())
                    .append(" & Longitud: ").append(item.getLongitude())
                    .append(" / ")
                    .append("Last Modification Date: ").append(item.getLastModificationDate() == null ? "null" : item.getLastModificationDate().toString())
                    .append("Published Date: ").append(item.getPublished() == null ? "null" : item.getPublished().toString());
        }
        builder.append("]");
        LOGGER.error(builder.toString(), e);
    }

    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ItemDocument [id=").append(itemId)
                .append(", name=").append(name)
                .append(", user=").append(publishedBy == null ? "null" : publishedBy.getUsername()).append("], ");
        return builder.toString();
    }

}
