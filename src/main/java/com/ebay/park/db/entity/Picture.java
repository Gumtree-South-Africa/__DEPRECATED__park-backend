/*
 * Copyright eBay, 2014
 */
package com.ebay.park.db.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the picture database table.
 * 
 * @author juan.pizarro
 * 
 */
@Entity
@Table(name = "picture")
public class Picture extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pic_id")
	private Long id;

	@Column(name = "pic_automadjustment")
	private int automAdjustment;

	@Column(name = "pic_brightness")
	private int brightness;

	@Column(name = "pic_contrast")
	private int contrast;

	@Column(name = "pic_picture")
	private String picture;

	@Column(name = "pic_rotation")
	private int rotation;

	@Column(name = "pic_saturation")
	private int saturation;

	@Column(name = "pic_sharpening")
	private int sharpening;

	@Column(name = "pic_vignetting")
	private int vignetting;

	public Picture() {
	}

	public Long getPictureId() {
		return this.id;
	}

	public void setPictureId(Long pictureId) {
		this.id = pictureId;
	}

	public int getAutomAdjustment() {
		return this.automAdjustment;
	}

	public void setAutomAdjustment(int automadjustment) {
		this.automAdjustment = automadjustment;
	}

	public int getBrightness() {
		return this.brightness;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	public int getContrast() {
		return this.contrast;
	}

	public void setContrast(int contrast) {
		this.contrast = contrast;
	}

	public String getPicture() {
		return this.picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public int getRotation() {
		return this.rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public int getSaturation() {
		return this.saturation;
	}

	public void setSaturation(int saturation) {
		this.saturation = saturation;
	}

	public int getSharpening() {
		return this.sharpening;
	}

	public void setSharpening(int sharpening) {
		this.sharpening = sharpening;
	}

	public int getVignetting() {
		return this.vignetting;
	}

	public void setVignetting(int vignetting) {
		this.vignetting = vignetting;
	}
}