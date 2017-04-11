package com.ebay.park.service.group.dto;

import com.ebay.park.service.PaginatedResponse;
import com.ebay.park.service.item.dto.SmallGroupDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author marcos.lambolay
 */
public class ListGroupsResponse extends PaginatedResponse {

	private List<SmallGroupDTO> groups = new ArrayList<SmallGroupDTO>();
	
	//FIXME coordinate with FE use totalElements instead
	private Integer totalGroupsFound;

	public ListGroupsResponse(List<SmallGroupDTO> groups, Integer totalGroupsFound) {
		super(totalGroupsFound.longValue(), groups.size());
		this.groups = groups;
		this.totalGroupsFound = totalGroupsFound;
	}

	public void add(SmallGroupDTO element) {
		groups.add(element);
	}

	public List<SmallGroupDTO> getGroups() {
		return groups;
	}

	public void setGroups(List<SmallGroupDTO> groups) {
		this.groups = groups;
	}

	@Override
	public boolean listIsEmpty() {
		return groups == null || groups.isEmpty();
	}

	/**
	 * @return the totalGroupsFound
	 */
	public Integer getTotalGroupsFound() {
		return totalGroupsFound;
	}

}
