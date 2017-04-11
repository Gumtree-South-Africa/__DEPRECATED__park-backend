package com.ebay.park.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the orphaned_device database table.
 * 
 * @author Julieta Salvadó
 * 
 */
@Entity
@Table(name = "orphaned_device")
public class OrphanedDevice extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "orp_dev_id")
	private Long id;

	@Column(name = "orp_dev_unique_device_id")
	private String uniqueDeviceId;

	@Column(name = "orp_dev_device_id")
	private String deviceId;

	@Enumerated(EnumType.STRING)
	@Column(name = "orp_dev_device_platform")
	private DeviceType platform;
	
	public OrphanedDevice() {
	}

	public OrphanedDevice(String uniqueDeviceId, String deviceId,
			DeviceType platform) {
		this.setUniqueDeviceId(uniqueDeviceId);
		this.setDeviceId(deviceId);
		this.setPlatform(platform);
	}

	/**
	 * @return the uniqueDeviceId
	 */
	public String getUniqueDeviceId() {
		return uniqueDeviceId;
	}

	/**
	 * @param uniqueDeviceId the uniqueDeviceId to set
	 */
	public void setUniqueDeviceId(String uniqueDeviceId) {
		this.uniqueDeviceId = uniqueDeviceId;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the platform
	 */
	public DeviceType getPlatform() {
		return platform;
	}

	/**
	 * @param platform the platform to set
	 */
	public void setPlatform(DeviceType platform) {
		this.platform = platform;
	}
}
