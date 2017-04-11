package com.ebay.park.service.group.dto;

import com.ebay.park.service.ParkRequest;

public class GetGroupRequest extends ParkRequest {

    private String id;
    private boolean includeAdminUser = false;

    public Boolean getIncludeAdminUser() {
        return includeAdminUser;
    }

    public void setIncludeAdminUser(Boolean includeAdminUser) {
        if (includeAdminUser != null) {
            this.includeAdminUser = includeAdminUser;
        }
    }

	public GetGroupRequest(String id, Boolean includeAdminUser, String parkToken, String lang) {
		setId(id);
		setIncludeAdminUser(includeAdminUser);
		super.setToken(parkToken);
		super.setLanguage(lang);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetGroupRequest [id= ")
			.append(this.id).append(", includeAdmionUser= ")
			.append(this.includeAdminUser).append("]");
			
	return builder.toString();
	}

}
