package com.ebay.park.service.group.dto;

import com.ebay.park.db.entity.Group;
import com.ebay.park.service.item.dto.SmallGroupDTO;
import com.ebay.park.service.item.dto.SmallUser;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

public class GroupDTO extends SmallGroupDTO{
	
	protected String description;
	protected List<SubscriberDTO> subscribers;
	
	@JsonInclude(Include.NON_EMPTY)
	private String noSubscribersMessage;

	public GroupDTO(Long id, String name, String pictureUrl) {
		super(id, name, pictureUrl);
	}
	
	public static GroupDTO fromGroup(Group group, List<SubscriberDTO> subscribers) {
		GroupDTO smallGroup = new GroupDTO(group.getId(), group.getName(), group.getPicture());
		setGroupFields(smallGroup,  group);
		smallGroup.setOwner(new SmallUser(group.getCreator()));
		smallGroup.setSubscribers(subscribers);
		smallGroup.setDescription(group.getDescription());
		return smallGroup;  
	}
	

	public List<SubscriberDTO> getSubscribers() {
		return subscribers;
	}


	public void setSubscribers(List<SubscriberDTO> subscribers) {
		this.subscribers = subscribers;
	}

	
	public String getNoSubscribersMessage() {
		return noSubscribersMessage;
	}

	public void setNoSubscribersMessage(String noSubscribersMessage) {
		this.noSubscribersMessage = noSubscribersMessage;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
