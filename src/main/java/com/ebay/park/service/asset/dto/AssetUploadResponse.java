/*
 * Copyright eBay, 2014
 */
package com.ebay.park.service.asset.dto;

/**
 * @author jpizarro
 * 
 */
public class AssetUploadResponse {

	private String url;
	private String name;

	public AssetUploadResponse() {
	}

	public AssetUploadResponse(String name, String url) {
		this.url = url;
		this.name = name;
	}

	public String getURL() {
		return this.url;
	}

	public String getName() {
		return name;
	}

}
