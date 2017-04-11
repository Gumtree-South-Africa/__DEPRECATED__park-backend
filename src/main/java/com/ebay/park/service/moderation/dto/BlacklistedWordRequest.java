package com.ebay.park.service.moderation.dto;

import com.ebay.park.service.ParkRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class BlacklistedWordRequest extends ParkRequest {

	@JsonIgnore
	private Long id;

	private String word;

	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @param word the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BlacklistedWordRequest [id= ")
			.append(this.id).append(", word= ")
			.append(this.word).append("]");
			
	return builder.toString();
	}

}
