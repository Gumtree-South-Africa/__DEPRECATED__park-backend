package com.ebay.park.service.group.dto;

import com.ebay.park.service.ParkRequest;
import org.springframework.web.multipart.MultipartFile;

public class UploadGroupPhotoRequest extends ParkRequest {

	private Long groupId;
	private MultipartFile photo;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public MultipartFile getPhoto() {
		return photo;
	}

	public void setPhoto(MultipartFile photo) {
		this.photo = photo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UploadGroupPhotoRequest [groupId= ")
			.append(this.groupId).append("]");
			
	return builder.toString();
	}
}
