package com.ebay.park.service.blacklist.dto;

public class BlacklistedWord {

	private Long id;

	private String word;

	public BlacklistedWord() {
		super();
	}

	public BlacklistedWord(Long id, String word) {
		super();
		this.id = id;
		this.word = word;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
}
