package com.ebay.park.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user_itemreport")
public class UserReportItem extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1986115345702503778L;

	@EmbeddedId
	private UserReportItemPK id;

	// bi-directional many-to-one association to User
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ur_usu_id", insertable = false, updatable = false)
	private User userReporter;

	// bi-directional many-to-one association to Item
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ur_ite_id", insertable = false, updatable = false)
	private Item itemReported;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ur_report_date")
	private Date reportDate;

	@Column(name = "ur_user_comment")
	private String userComment;

	public UserReportItem() {
		super();
	}

	/**
	 * @return the id
	 */
	public UserReportItemPK getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(UserReportItemPK id) {
		this.id = id;
	}

	public User getUserReporter() {
		return userReporter;
	}

	public void setUserReporter(User userReporter) {
		this.userReporter = userReporter;
	}

	public Item getItemReported() {
		return itemReported;
	}

	public void setItemReported(Item itemReported) {
		this.itemReported = itemReported;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public String getUserComment() {
		return userComment;
	}

	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}
}
