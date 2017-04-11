package com.ebay.park.db.entity;

import com.ebay.park.service.banner.dto.BannerPriority;
import com.ebay.park.service.banner.dto.BannerType;

import javax.persistence.*;

@Entity
@Table(name="banner")
public class Banner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="banner_id")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name="banner_priority")
	private BannerPriority priority;
	
	@Enumerated(EnumType.STRING)
	@Column(name="banner_type")
	private BannerType type;
	
	@Column(name="banner_msg")
	private String message;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "banner_idi_id")
	private Idiom idiom;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BannerPriority getPriority() {
		return priority;
	}

	public void setPriority(BannerPriority priority) {
		this.priority = priority;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Idiom getIdiom() {
		return idiom;
	}

	public void setIdiom(Idiom idiom) {
		this.idiom = idiom;
	}

	public BannerType getType() {
		return type;
	}

	public void setType(BannerType type) {
		this.type = type;
	}
	
}
