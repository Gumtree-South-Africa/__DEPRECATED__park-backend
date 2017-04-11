package com.ebay.park.db.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tutorial")
public class Tutorial extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tut_id")
	private Long id;

	@Column(name = "tut_picture")
	private String picture;

	@Column(name = "tut_step")
	private Integer step;

	// bi-directional many-to-one association to Idiom
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tut_idi_id")
	private Idiom idiom;

	public Tutorial() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public Idiom getIdiom() {
		return idiom;
	}

	public void setIdiom(Idiom idiom) {
		this.idiom = idiom;
	}

}