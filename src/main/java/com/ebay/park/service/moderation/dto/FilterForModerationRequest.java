package com.ebay.park.service.moderation.dto;

import java.util.List;

import com.ebay.park.db.entity.SessionStatusDescription;
import com.ebay.park.service.ParkRequest;

/**
 * Request from ModTool to express a request for filtering the users regarding
 * some criterias.
 * 
 * @author Julieta Salvadó
 *
 */
public class FilterForModerationRequest extends ParkRequest {

	private static final String DEFAULT_PLATFORM = "all";
    private static final String DEFAULT_SESSION_STATUS = "all";

    private String platform;
	private String accountCreationFrom;
	private List<Integer> zipCode;
	private String accountCreationTo;
	private Boolean activeItems;
	private Boolean hasFacebook;
	private Boolean hasTwitter;
	private Boolean isGroupOwner;
	private Boolean isVerified;
	private Long categoryActiveItems;
	private Boolean isGroupFollower;
	private SessionStatusDescription sessionStatus;
	private Long isMemberOfGroup;

	/**
	 * @return the isMemberOfGroup.
	 */
	public Long getIsMemberOfGroup() {
		return isMemberOfGroup;
	}

	/**
	 * 
	 * @param isMemberOfGroup
	 *            the isMemberOfGroup to set.
	 */
	public void setIsMemberOfGroup(Long isMemberOfGroup) {
		this.isMemberOfGroup = isMemberOfGroup;
	}

	public FilterForModerationRequest() {
	    this.setPlatform(DEFAULT_PLATFORM);
	    this.setSessionStatus(DEFAULT_SESSION_STATUS);
	}

	public FilterForModerationRequest(String parkToken, String platform, Boolean isGroupFollower, Boolean isGroupOwner,
			List<Integer> zipCodeList, String accountCreationDateFrom, String accountCreationDateTo,
			Boolean hasActiveItem, Long categoryActiveItems, Boolean hasFacebook, Boolean hasTwitter,
			Boolean isVerified, String sessionStatus) {

		super(parkToken);
		if (platform == null) {
			this.setPlatform(DEFAULT_PLATFORM);
		}
		if (sessionStatus == null) {
			this.setSessionStatus(DEFAULT_SESSION_STATUS);
		}
		this.setPlatform(platform);
		this.setIsGroupFollower(isGroupFollower);
		this.setIsGroupFollower(isGroupOwner);
		this.setZipCode(zipCodeList);
		this.setAccountCreationFrom(accountCreationDateFrom);
		this.setAccountCreationTo(accountCreationDateTo);
		this.setIsVerified(isVerified);
		this.setHasFacebook(hasFacebook);
		this.setHasTwitter(hasTwitter);
		this.setHasActiveItems(hasActiveItem);
		this.setCategoryActiveItems(categoryActiveItems);
	}

	public void setIsGroupFollower(Boolean isGroupFollower) {
		this.isGroupFollower = isGroupFollower;
	}

	public void setAccountCreationFrom(String accountCreationFrom) {
		this.accountCreationFrom = accountCreationFrom;
	}

	public void setZipCode(List<Integer> zipCodeList) {
		this.zipCode = zipCodeList;
	}

	public List<Integer> getZipCode() {
		return zipCode;
	}

	public void setAccountCreationTo(String accountCreationTo) {
		this.accountCreationTo = accountCreationTo;
	}

	public String getAccountCreationTo() {
		return accountCreationTo;
	}

	public String getAccountCreationFrom() {
		return accountCreationFrom;
	}

	public void setHasActiveItems(Boolean activeItems) {
		this.activeItems = activeItems;
	}

	public Boolean getHasActiveItems() {
		return activeItems;
	}

	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	/**
	 * @return the fb
	 */
	public Boolean getHasFacebook() {
		return hasFacebook;
	}

	/**
	 * @param fb
	 *            the fb to set
	 */
	public void setHasFacebook(Boolean fb) {
		this.hasFacebook = fb;
	}

	/**
	 * @return the tw
	 */
	public Boolean getHasTwitter() {
		return hasTwitter;
	}

	/**
	 * @param tw
	 *            the tw to set
	 */
	public void setHasTwitter(Boolean tw) {
		this.hasTwitter = tw;
	}

	/**
	 * @return the activeItemCategory
	 */
	public Long getCategoryActiveItems() {
		return categoryActiveItems;
	}

	/**
	 * @param categoryActiveItems
	 *            the categoryActiveItems to set
	 */
	public void setCategoryActiveItems(Long categoryActiveItems) {
		this.categoryActiveItems = categoryActiveItems;
	}

	/**
	 * @param groupOwner
	 *            the groupOwner to set
	 */
	public void setIsGroupOwner(Boolean groupOwner) {
		this.isGroupOwner = groupOwner;
	}

	/**
	 * @return the verified
	 */
	public Boolean getIsVerified() {
		return isVerified;
	}

	/**
	 * @param verified
	 *            the verified to set
	 */
	public void setIsVerified(Boolean verified) {
		this.isVerified = verified;
	}

	public Boolean getIsGroupFollower() {
		return isGroupFollower;
	}

	public Boolean getIsGroupOwner() {
		return isGroupOwner;
	}

	/**
	 * @return the sessionStatus
	 */
	public SessionStatusDescription getSessionStatus() {
		return sessionStatus;
	}

	/**
	 * @param sessionStatus
	 *            the sessionStatus to set
	 */
	public void setSessionStatus(String sessionStatus) {
		this.sessionStatus = SessionStatusDescription.getSessionStatusByValue(sessionStatus);
	}

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FilterForModerationRequest [")
                .append(toStringFields())
                .append("]");
        return builder.toString();
    }

    protected String toStringFields() {
        StringBuilder builder = new StringBuilder();
        builder.append("platform=").append(platform)
                .append(", accountCreationFrom=").append(accountCreationFrom)
                .append(", accountCreationTo=").append(accountCreationTo)
                .append(", zipCode=").append(zipCode)
                .append(", activeItems=").append(activeItems)
                .append(", hasFacebook=").append(hasFacebook)
                .append(", hasTwitter=").append(hasTwitter)
                .append(", isGroupOwner=").append(isGroupOwner)
                .append(", isVerified=").append(isVerified)
                .append(", categoryActiveItems=").append(categoryActiveItems)
                .append(", isGroupFollower=").append(isGroupFollower)
                .append(", sessionStatus=").append(sessionStatus)
                .append(", isMemberOfGroup=").append(isMemberOfGroup);
        return builder.toString();
    }
}
