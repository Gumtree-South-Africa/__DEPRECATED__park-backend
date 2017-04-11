package com.ebay.park.service.social.dto;

import com.ebay.park.db.entity.Rating;
import com.ebay.park.db.entity.RatingStatus;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Date;

public class SmallRating {

	private String comment;
	private Date rateDate;
	private RatingStatus status;
	private String raterUsername;
	private Long raterId;
	private Long itemId;
	private String itemName;
	
	public SmallRating(){
		
	}
	
	public SmallRating(Rating rating){
		this.comment = rating.getComment();
		this.rateDate = rating.getRateDate();
		this.status = rating.getStatus();
		this.raterUsername = rating.getRater().getUsername();
		this.raterId = rating.getRater().getId();
		this.itemId = rating.getItem().getId();
		this.itemName = rating.getItem().getName();
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public Date getRateDate() {
		return rateDate;
	}
	
	public void setRateDate(Date rateDate) {
		this.rateDate = rateDate;
	}
	
	public RatingStatus getStatus() {
		return status;
	}
	
	public void setStatus(RatingStatus status) {
		this.status = status;
	}
	
	public String getRaterUsername() {
		return raterUsername;
	}
	
	public void setRaterUsername(String raterUsername) {
		this.raterUsername = raterUsername;
	}
	
	public Long getRaterId() {
		return raterId;
	}
	
	public void setRaterId(Long raterId) {
		this.raterId = raterId;
	}
	
	public Long getItemId() {
		return itemId;
	}
	
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	
	public String getItemName() {
		return itemName;
	}
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
