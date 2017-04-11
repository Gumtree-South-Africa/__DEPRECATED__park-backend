package com.ebay.park.service.group.dto;

import com.ebay.park.service.PaginatedResponse;
import com.ebay.park.service.item.dto.SmallGroupDTO;

import java.util.List;

/**
 * Response of searching groups.
 * @author marcos.lambolay
 */
public class SearchGroupResponse extends PaginatedResponse {

    /**
     * List of response groups.
     */
	private List<SmallGroupDTO> groups;
	/**
	 * Response time.
	 */
	private String serverTime;

    /**
     * Constructor.
     *
     * @param groups
     *            list of groups
     * @param totalGroupsFound
     *            number of groups
     * @param serverTime
     *            response time
     */
	public SearchGroupResponse(List<SmallGroupDTO> groups, Integer totalGroupsFound, String serverTime) {
		super(totalGroupsFound.longValue(), groups.size());
		this.setGroups(groups);
		this.setServerTime(serverTime);
	}

	/**
     * Constructor.
     *
     * @param groups
     *            list of groups
     * @param totalGroupsFound
     *            number of groups
     */
    public SearchGroupResponse(List<SmallGroupDTO> groups, Integer totalGroupsFound) {
        super(totalGroupsFound.longValue(), groups.size());
        this.setGroups(groups);
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
     * @return the serverTime
     */
    public String getServerTime() {
        return serverTime;
    }

    /**
     * @param serverTime the serverTime to set
     */
    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }
}
