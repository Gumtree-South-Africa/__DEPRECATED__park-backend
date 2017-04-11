package com.ebay.park.db.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the session_device database table.
 * 
 */
@Entity
@Table(name = "session_device")
public class Device implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dev_id")
	private Long id;

	@OneToOne
	@JoinColumn(name = "dev_session_id")
	private UserSession userSession;

	@Column(name="dev_device_id")
	private String deviceId;

	@Enumerated(EnumType.STRING)
	@Column(name="dev_platform")
	private DeviceType platform;

	public Device() {

	}

	public Device(UserSession userSession, String deviceId, DeviceType platform) {
		this.userSession = userSession;
		this.deviceId = deviceId;
		this.platform = platform;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public UserSession getUserSession() {
		return userSession;
	}

	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}

	public DeviceType getPlatform() {
		return platform;
	}

	public void setPlatform(DeviceType platform) {
		this.platform = platform;
	}
}