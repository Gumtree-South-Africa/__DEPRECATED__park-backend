/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.asset.dto;

import com.ebay.park.service.ParkRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author jpizarro
 * 
 */
public class AssetUploadRequest extends ParkRequest {

	private String name;

	private MultipartFile file;

	public AssetUploadRequest() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AssetUploadRequest [name= ")
			.append(this.name).append("]");
		
		return builder.toString();
	}

}
