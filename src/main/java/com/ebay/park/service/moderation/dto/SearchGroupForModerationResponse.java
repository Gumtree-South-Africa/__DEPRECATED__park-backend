package com.ebay.park.service.moderation.dto;

import com.ebay.park.service.PaginatedResponse;

import java.util.List;

/**
 * 
 * @author diana.gazquez
 */
public class SearchGroupForModerationResponse extends PaginatedResponse {

	private List<GroupForModerationDTO> groups;
	//FIXME coordinate with FE use totalElements instead
	private Integer totalGroupsFound;


	public SearchGroupForModerationResponse(List<GroupForModerationDTO> groups, Integer totalGroupsFound) {
		super(totalGroupsFound.longValue(), groups.size());
		this.groups = groups;
		this.totalGroupsFound = totalGroupsFound;
	}

	public void add(GroupForModerationDTO element) {
		groups.add(element);
	}

	public List<GroupForModerationDTO> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupForModerationDTO> groups) {
		this.groups = groups;
	}

	public Integer getTotalGroupsFound() {
		return totalGroupsFound;
	}

}
