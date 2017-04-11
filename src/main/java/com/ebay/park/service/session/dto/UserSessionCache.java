package com.ebay.park.service.session.dto;

import com.ebay.park.db.entity.*;
import com.ebay.park.service.device.dto.DeviceDTO;
import com.ebay.park.util.DataCommonUtil;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

public class UserSessionCache implements Serializable {

	private static final long serialVersionUID = 2262969058878276642L;

	private String username;
	private String email;
	private Long userId;
	private String lang;
	private DeviceDTO device;
	private Set<UserRole> roles;
	private Date lastActivityDay;
	private String swrveId;

	public UserSessionCache(UserSession userSession) {
		userSession.getUser().populateSession(this);
		if(userSession.getDevice() != null) {
			device = new DeviceDTO();
			this.device.setDeviceId(userSession.getDevice().getDeviceId());
			this.device.setDeviceType(userSession.getDevice().getPlatform().toString());
		}
		setLastActivityDay(DataCommonUtil.getCurrentTime());
	}

	public UserSessionCache(UserAdmin admin){
		admin.populateSession(this);
		setLastActivityDay(DataCommonUtil.getCurrentTime());
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public DeviceDTO getDevice() {
		return device;
	}

	public void setDevice(DeviceDTO device) {
		this.device = device;
	}

	public Set<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}

	public boolean is(UserRole role) {
		if (role != null && roles != null) {
			return roles.contains(role);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public Date getLastActivityDay() {
		return lastActivityDay;
	}

	public void setLastActivityDay(Date lastActivityDay) {
		this.lastActivityDay = lastActivityDay;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getSwrveId() {
		return swrveId;
	}

	public void setSwrveId(String swrveId) {
		this.swrveId = swrveId;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[ username:")
                .append(username)
                .append(", email:")
                .append(email)
                .append(", id:")
                .append(userId)
                .append(", roles:")
                .append(Arrays.toString(roles.toArray()))
                .append(" ]");
		return sb.toString();

	}
}