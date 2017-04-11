package com.ebay.park.service.blacklist.dto;

import java.util.ArrayList;
import java.util.List;

public class SearchBlacklistedWordsResponse {

	private final List<BlacklistedWord> words;
	private int amountWordsFound;

	public SearchBlacklistedWordsResponse() {
		super();
		words = new ArrayList<BlacklistedWord>();
	}

	public List<BlacklistedWord> getWords() {
		return words;
	}

	public void addWord(Long id, String word) {
		this.words.add(new BlacklistedWord(id, word));
	}

	public int getAmountWordsFound() {
		return amountWordsFound;
	}

	public void setAmountWordsFound(int amountWordsFound) {
		this.amountWordsFound = amountWordsFound;
	}

}
