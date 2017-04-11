package com.ebay.park.service.profile.dto;

import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSocial;
import com.ebay.park.util.DataCommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

public class UserProfile {

	private String username;

	@JsonInclude(Include.NON_NULL)
	private String email;

	private String profilePicture;

	private String creationDate;

	private String locationName;
	
	private String zipCode;

	private Integer positiveRatings;

	private Integer negativeRatings;

	private Integer neutralRatings;

	private Integer followers;

	private Integer following;

	private List<String> userSocials;
	
	private Integer offersMade;
	
	private Integer offersReceived;

	@JsonInclude(Include.NON_NULL)
	private Boolean followedByUser;

	private boolean isEmailVerified;

	private boolean isMobileVerified;

    private String status;
	
	private Integer itemsPublishedCount;
	
	private String url;

	@JsonInclude(Include.NON_NULL)
	private String phoneNumber;

	public UserProfile() {
	}

	public UserProfile(User user, boolean privateProfile) {
		this.username = user.getUsername();
		if (privateProfile) {
			this.email = user.getEmail();
			this.itemsPublishedCount = user.getPublishedItems().size();
		} else {
			this.itemsPublishedCount = user.publicPublishedItems().size();
		}
		this.profilePicture = user.getPicture();
		this.creationDate = (DataCommonUtil.getDateTimeAsUnixFormat(user.getCreation()));
		this.locationName = user.getLocationName();
		this.isEmailVerified = user.isEmailVerified();

		userSocials = new ArrayList<String>();
		if (user.getUserSocials() != null) {
			for (UserSocial userSocial : user.getUserSocials()) {
				userSocials.add(userSocial.getSocial().getDescription());
			}
		}

		this.status = user.getStatus().toString();
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getFollowers() {
		return followers;
	}

	public void setFollowers(Integer followers) {
		this.followers = followers;
	}

	public Integer getFollowing() {
		return following;
	}

	public void setFollowing(Integer following) {
		this.following = following;
	}

	public Integer getPositiveRatings() {
		return positiveRatings;
	}

	public void setPositiveRatings(Integer positiveRatings) {
		this.positiveRatings = positiveRatings;
	}

	public Integer getNegativeRatings() {
		return negativeRatings;
	}

	public void setNegativeRatings(Integer negativeRatings) {
		this.negativeRatings = negativeRatings;
	}

	public Integer getNeutralRatings() {
		return neutralRatings;
	}

	public void setNeutralRatings(Integer neutralRatings) {
		this.neutralRatings = neutralRatings;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public List<String> getUserSocials() {
		return userSocials;
	}

	public void setUserSocials(List<String> userSocials) {
		this.userSocials = userSocials;
	}

	public Boolean getFollowedByUser() {
		return followedByUser;
	}

	public void setFollowedByUser(Boolean followedByUser) {
		this.followedByUser = followedByUser;
	}
	
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/**
	 * Set the email verification value.
	 * @param emailVerified
	 */
	public void setVerified(boolean emailVerified) {
		this.isEmailVerified = emailVerified;
	}

	/**
	 * Check if the user is email verified.
	 * @return
	 */
	public boolean isVerified(){
		return this.isEmailVerified;
	}

	public boolean isMobileVerified() {
	    return isMobileVerified;
	}

	public void setMobileVerified(boolean isMobileVerified) {
	    this.isMobileVerified = isMobileVerified;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	public Integer getOffersMade() {
		return offersMade;
	}

	public void setOffersMade(Integer offersMade) {
		this.offersMade = offersMade;
	}

	public Integer getOffersReceived() {
		return offersReceived;
	}

	public void setOffersReceived(Integer offersReceived) {
		this.offersReceived = offersReceived;
	}

	public Integer getItemsPublishedCount() {
		return itemsPublishedCount;
	}

	public void setItemsPublishedCount(Integer itemsPublishedCount) {
		this.itemsPublishedCount = itemsPublishedCount;
	}

	//TODO: Name & LastName Must be deleted in new version 0.9.6
	public String getName() {
		return "none";
	}
	public String getLastname() {
		return "none";
	}
	//

	public void setURL(String url) {
		this.url = url;		
	}
	
	public String getURL() {
		return url;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
