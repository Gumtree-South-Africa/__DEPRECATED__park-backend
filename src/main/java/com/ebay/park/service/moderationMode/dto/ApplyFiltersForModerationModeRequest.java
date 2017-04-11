package com.ebay.park.service.moderationMode.dto;

import static com.ebay.park.service.ServiceException.createServiceException;

import java.util.Date;
import java.util.List;

import org.springframework.util.StringUtils;

import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.util.DataCommonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Request for applying filters and search items of pending moderation.
 * 
 * @author Julieta Salvadó
 *
 */
@JsonInclude(Include.NON_NULL)
public class ApplyFiltersForModerationModeRequest extends ParkRequest {
	/**
	 * Item to be excluded in the result list.
	 */
	private List<Long> skippedItems;
	/**
	 * Category id when filtering by category is selected.
	 */
	private Long categoryId;

	/**
	 * Description when filtering by description is selected.
	 */
	private String description;

	/**
	 * Name when filtering by title is selected. 
	 */
	private String name;
	
	/**
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;

	}

	/**
	 * Date to filter items updated after it.
	 */
	private Date itemLastUpdatedFrom;

	/**
	 * Date to filter items updated before it.
	 */
	private Date itemLastUpdatedTo;
	/**
	 * User name when filtering by userName is selected.
	 */
	private String userName;
	
	/**
	 * List of zipcode to search for item in this area.
	 */
	private List<Integer> zipCodes;

	/**
	 * 
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;

	}

	public ApplyFiltersForModerationModeRequest() {

	}

	public ApplyFiltersForModerationModeRequest(String parkToken, String language, List<Long> skippedItems) {
		super(parkToken, language);
		this.setSkippedItems(skippedItems);
	}

	/**
	 * @return the skippedItems
	 */
	public List<Long> getSkippedItems() {
		return skippedItems;
	}

	/**
	 * @param skippedItems
	 *            the skippedItems to set
	 */
	public void setSkippedItems(List<Long> skippedItems) {
		this.skippedItems = skippedItems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UpdateItemRequest [skippedItems=").append(skippedItems == null ? null : skippedItems.toString())
				.append(" categoryId=").append(categoryId).append(" description=")
				.append(description == null ? null : description).append(" itemLastUpdatedFrom=")
				.append(itemLastUpdatedFrom == null ? null : itemLastUpdatedFrom.toString())
				.append(" itemLastUpdatedTo=").append(itemLastUpdatedTo == null ? null : itemLastUpdatedTo.toString())
				.append("userName=").append(userName == null ? null : userName)
				.append("name=").append(name == null ? null : name).append("]");
		return builder.toString();
	}

	/**
	 * @return the categoryId
	 */
	public Long getCategoryId() {
		return categoryId;
	}

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the itemLastUpdatedFrom
     */
    public Date getItemLastUpdatedFrom() {
        return itemLastUpdatedFrom;
    }

    /**
     * @param itemLastUpdatedFrom the itemLastUpdatedFrom to set
     */
    public void setItemLastUpdatedFrom(String itemLastUpdatedFrom) {
        if (!StringUtils.isEmpty(itemLastUpdatedFrom)) {
            try {
                this.itemLastUpdatedFrom = DataCommonUtil.parseUnixTime(itemLastUpdatedFrom);
            } catch (IllegalArgumentException e) {
                throw createServiceException(ServiceExceptionCode.WRONG_DATE_FORMAT);
            }
        }
    }

    /**
     * @return the zipCodes
     */
    public List<Integer> getZipCodes() {
        return zipCodes;
    }

    /**
     * @param zipCodes the zipCodes to set
     */
    public void setZipCodes(List<Integer> zipCodes) {
        this.zipCodes = zipCodes;
    }

	/**
	 * @return the itemLastUpdatedTo
	 */
	public Date getItemLastUpdatedTo() {
		return itemLastUpdatedTo;
	}

	/**
	 * @param itemLastUpdatedTo
	 *            the itemLastUpdatedTo to set
	 */
	public void setItemLastUpdatedTo(String itemLastUpdatedTo) {
		if (!StringUtils.isEmpty(itemLastUpdatedTo)) {
			try {
				this.itemLastUpdatedTo = DataCommonUtil.parseUnixTime(itemLastUpdatedTo);
			} catch (IllegalArgumentException e) {
				throw createServiceException(ServiceExceptionCode.WRONG_DATE_FORMAT);
			}
		}
	}
}
