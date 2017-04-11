package com.ebay.park.service.moderation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SendNotificationsForModerationResponse {

	private Integer receivers;

	private Integer pushConfirmed;

	public Integer getReceivers() {
		return receivers;
	}

	public void setReceivers(Integer receivers) {
		this.receivers = receivers;
	}

	@JsonInclude(Include.NON_NULL)
	public Integer getPushConfirmed() {
		return pushConfirmed;
	}

	public void setPushConfirmed(Integer pushConfirmed) {
		this.pushConfirmed = pushConfirmed;
	}

}
