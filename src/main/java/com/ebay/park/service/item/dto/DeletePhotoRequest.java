package com.ebay.park.service.item.dto;

import java.util.Arrays;
import java.util.List;

public class DeletePhotoRequest extends UserItemRequest {

	public DeletePhotoRequest(String token) {
		super(token);
	}

	private List<Long> pictureIdList;

	public List<Long> getPictureIdList() {
		return pictureIdList;
	}

	public void setPictureId(Long[] photoIdList) {
		this.pictureIdList = Arrays.asList(photoIdList);
	}

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DeletePhotoRequest [pictureIdList=")
        .append(this.pictureIdList.toString())
        .append(", token=")
        .append(this.token)
        .append(", itemId=")
        .append(super.getItemId())
        .append("]");
        return builder.toString();
    }
}
