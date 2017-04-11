/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the rating database table.
 * 
 * @author juan.pizarro
 * 
 */
@Entity
@Table(name = "rating")
public class Rating extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rat_id")
	private Long id;

	@Column(name = "rat_comment")
	private String comment;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "rat_ratedate")
	private Date rateDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "rat_status")
	private RatingStatus status;

	// bi-directional many-to-one association to User
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rat_use_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rat_ite_id")
	private Item item;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rat_rater_use_id", updatable = false)
	private User rater;

	public Rating(User userToRate, User rater, Item item) {
		this.user = userToRate;
		this.rater = rater;
		this.item = item;
	}

	public Rating() {

	}

	public Long getRateId() {
		return this.id;
	}

	public void setRateId(Long rateId) {
		this.id = rateId;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String ratComment) {
		this.comment = ratComment;
	}

	public Date getRateDate() {
		return this.rateDate;
	}

	public void setRateDate(Date rateDate) {
		this.rateDate = rateDate;
	}

	public RatingStatus getStatus() {
		return this.status;
	}

	public void setStatus(RatingStatus status) {
		this.status = status;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getRater() {
		return rater;
	}

	public void setRater(User rater) {
		this.rater = rater;
	}
	
	public Item getItem() {
		return item;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
}