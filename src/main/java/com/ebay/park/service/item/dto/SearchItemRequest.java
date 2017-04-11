package com.ebay.park.service.item.dto;

import com.ebay.park.service.PaginatedRequest;

public class SearchItemRequest extends PaginatedRequest {

	private String criteria;
	private Double priceFrom;
	private Double priceTo;
	private String order;
	private Long categoryId;
	private Long groupId;
	private Double longitude;
	private Double latitude;
	private Integer radius;
	private String language;
	private boolean fromFollowedUsers;
	private boolean fromUserWishlist;
	private boolean fromFollowedGroups;
	private String publisherName;
	private Boolean tagNewItem;
	private String requestTime;

	public SearchItemRequest(String token, String language, Integer page, Integer pageSize) {
		super(token, language, page, pageSize);
	}
	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getCriteria() {
		return criteria;
	}

	public Double getPriceFrom() {
		return priceFrom;
	}

	public void setPriceFrom(Double priceFrom) {
		this.priceFrom = priceFrom;
	}

	public Double getPriceTo() {
		return priceTo;
	}

	public void setPriceTo(Double priceTo) {
		this.priceTo = priceTo;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}
	
	@Override
    public String getLanguage() {
		return language;
	}
	
	@Override
    public void setLanguage(String language) {
		this.language = language;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SearchItemRequest [criteria= ")
			.append(this.criteria).append(", priceFrom= ")
			.append(this.priceFrom).append(", priceTo= ")
			.append(this.priceTo).append(", order= ")
			.append(this.order).append(", categoryId= ")
			.append(this.categoryId).append(", groupId= ")
			.append(this.groupId).append(", longitude= ")
			.append(this.longitude).append(", latitude= ")
			.append(this.latitude).append(", radius= ")
			.append(this.radius).append(", fromFollowedUsers= ")
			.append(this.fromFollowedUsers).append(", fromUserWishlist= ")
			.append(this.fromUserWishlist).append(", fromFollowedGroups= ")
			.append(this.fromFollowedGroups).append(", publisherName= ")
			.append(this.publisherName).append(", tagNewItem= ")
			.append(this.tagNewItem).append(", requestTime= ")
			.append(this.requestTime).append("]");
			
	return builder.toString();
	}
	
	public static class SearchItemRequestBuilder {

		private final String nestedCriteria;
		private String nestedToken;
		private Integer nestedPage;
		private Integer nestedPageSize;
		private Double nestedPriceFrom;
		private Double nestedPriceTo;
		private String nestedOrder;
		private Long nestedCategoryId;
		private Long nestedGroupId;
		private Double nestedLongitude;
		private Double nestedLatitude;
		private Integer nestedRadius;
		private String nestedLanguage;
		private boolean nestedFromFollowedUsers;
		private boolean nestedFromUserWishlist;
		private boolean nestedFromFollowedGroups;
		private String nestedPublisherName;
		private boolean nestedTagNewItem;
		private String nestedRequestTime;

		public SearchItemRequestBuilder(String criteria) {
			this.nestedCriteria = criteria;
		}

		public SearchItemRequestBuilder setToken(String parkToken) {
			this.nestedToken = parkToken;
			return this;
		}

		public SearchItemRequestBuilder setPage(Integer page) {
			this.nestedPage = page;
			return this;
		}

		public SearchItemRequestBuilder setPageSize(Integer pageSize) {
			this.nestedPageSize = pageSize;
			return this;
		}

		public SearchItemRequestBuilder setPriceFrom(Double priceFrom) {
			this.nestedPriceFrom = priceFrom;
			return this;
		}

		public SearchItemRequestBuilder setPriceTo(Double priceTo) {
			this.nestedPriceTo = priceTo;
			return this;
		}

		public SearchItemRequestBuilder setOrder(String sort) {
			this.nestedOrder = sort;
			return this;
		}

		public SearchItemRequestBuilder setCategoryId(Long categoryId) {
			this.nestedCategoryId = categoryId;
			return this;
		}
		public SearchItemRequestBuilder setGroupId(Long groupId) {
			this.nestedGroupId = groupId;
			return this;
		}
		
		public SearchItemRequestBuilder setFromFollowedUsers(boolean nestedFromFollowedUsers) {
			this.nestedFromFollowedUsers = nestedFromFollowedUsers;
			return this;
		}
		
		public SearchItemRequestBuilder setFromUserWishlist(boolean nestedfromUserWishlist) {
			this.nestedFromUserWishlist = nestedfromUserWishlist;
			return this;
		}
		
		public SearchItemRequestBuilder setFromFollowedGroups(boolean nestedFromFollowedGroups) {
			this.nestedFromFollowedGroups = nestedFromFollowedGroups;
			return this;
		}

		public SearchItemRequestBuilder setLongitude(Double nestedLongitude) {
			this.nestedLongitude = nestedLongitude;
			return this;
		}

		public SearchItemRequestBuilder setLatitude(Double nestedLatitude) {
			this.nestedLatitude = nestedLatitude;
			return this;
		}

		public SearchItemRequestBuilder setRadius(Integer nestedRadius) {
			this.nestedRadius = nestedRadius;
			return this;
		}
		
		public SearchItemRequestBuilder setLanguage(String lang) {
			this.nestedLanguage = lang;
			return this;
		}
		
		public SearchItemRequestBuilder setPublisherName(String nestedPublisherName) {
			this.nestedPublisherName = nestedPublisherName;
			return this;
		}
		
		public SearchItemRequest build() {
			SearchItemRequest request = new SearchItemRequest(nestedToken, nestedLanguage, nestedPage, nestedPageSize);

			request.setCategoryId(nestedCategoryId);
			request.setCriteria(nestedCriteria);
			request.setPriceFrom(nestedPriceFrom);
			request.setPriceTo(nestedPriceTo);
			request.setOrder(nestedOrder);
			request.setLatitude(nestedLatitude);
			request.setLongitude(nestedLongitude);
			request.setRadius(nestedRadius);
			request.setGroupId(nestedGroupId);
			request.setLanguage(nestedLanguage);
			request.setFromFollowedUsers(nestedFromFollowedUsers);
			request.setFromUserWishlist(nestedFromUserWishlist);
	        request.setFromFollowedGroups(nestedFromFollowedGroups);
	        request.setPublisherName(nestedPublisherName);
	        request.setTagNewItem(nestedTagNewItem);
	        request.setRequestTime(nestedRequestTime);
			return request;
		}

		public SearchItemRequestBuilder setTagNewItem(Boolean tagNewItem) {
			this.nestedTagNewItem = tagNewItem;
			return this;
		}

		/**
		 * @param requestTime the requestTime to set
		 */
		public SearchItemRequestBuilder setRequestTime(String requestTime) {
			this.nestedRequestTime = requestTime;
			return this;
		}

		/**
		 * @return the nestedRequestTime
		 */
		public String getRequestTime() {
			return nestedRequestTime;
		}
	}

	/**
	 * @return the groupId
	 */
	public Long getGroupId() {
		return groupId;
	}

	@Override
    public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	@Override
    public String getRequestTime() {
		return requestTime;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
	public boolean isFromFollowedUsers() {
		return fromFollowedUsers;
	}
	
	public void setFromFollowedUsers(boolean fromFollowedUsers) {
		this.fromFollowedUsers = fromFollowedUsers;
	}
	
	public void setFromUserWishlist(boolean fromUserWishlist) {
		this.fromUserWishlist = fromUserWishlist;
	}
	
	public boolean isFromUserWishlist() {
		return fromUserWishlist;
	}
	
	
	public void setFromFollowedGroups(boolean fromFollowedGroups) {
		this.fromFollowedGroups = fromFollowedGroups;
	}
	
	public boolean isFromFollowedGroups() {
		return fromFollowedGroups;
	}
	
	public String getPublisherName() {
		return publisherName;
	}
	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}
	public Boolean getTagNewItem() {
		return tagNewItem;
	}
	public void setTagNewItem(Boolean tagNewItem) {
		this.tagNewItem = tagNewItem;
	}

}
