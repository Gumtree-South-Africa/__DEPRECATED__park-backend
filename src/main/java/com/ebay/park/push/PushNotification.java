package com.ebay.park.push;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Map;

/**
 * This is just a   simple pojo to represent a push notification. It holds:
 * <ul>
 *     <li>devideId</li>
 *     <li>deviceType</li>
 *     <li>templateMessage (to be filled with params</li>
 *     <li>params (to fill the template)</li>
 * </ul>
 */
public class PushNotification {

    private String deviceId;
	private String deviceType;
	private String templateMessage;
	private Map<String,String> params;
	private int badge;
	
	public String getDeviceId() {
		return deviceId;
	}
	
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
	public String getTemplateMessage() {
		return templateMessage;
	}

	public void setTemplateMessage(String templateMessage) {
		this.templateMessage = templateMessage;
	}

	public int getBadge() {
		return badge;
	}

	public void setBadge(int badge) {
		this.badge = badge;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PushNotification [deviceId=").append(deviceId).append(", deviceType=").append(deviceType)
				.append(", templateMessage=").append(templateMessage).append(", badge=").append(badge)
				.append(", params=").append(params).append("]");
		return builder.toString();
	}
	
}
