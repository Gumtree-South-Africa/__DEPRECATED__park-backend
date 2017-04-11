package com.ebay.park.service.blacklist.command;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.BlackListDao;
import com.ebay.park.db.entity.Item;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;

@Component
public class ValidateTextOnBlackListCmd implements ServiceCommand<Item, Boolean> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidateTextOnBlackListCmd.class);
	
	private LinkedList<LinkedList<String>> index;
	
	@Autowired
	private BlackListDao blackListDao;
	
	@PostConstruct
	public void initialize() throws InterruptedException, IOException {
		generateBlacklistIndex();
	}

	@Override
	public Boolean execute(Item item) throws ServiceException {
		if (!isBlacklisted(item.getName())) {
			return isBlacklisted(item.getDescription()); 
		}
		return true;
	}
	
    /**
     * Indicates whether the text is allowed or contains a blacklisted word or
     * expression.
     *
     * @param text
     *            text to be analyzed
     * @return true if the text is not allowed; false, otherwise.
     */
	private boolean isBlacklisted(String text) {
		//lowercase the text
		text = text.toLowerCase();
		
		//create a list with the current text
		List<String> textAsList = new LinkedList<String>(Arrays.asList(text.split("\\s")));

		//search the phrase in the index structure
		Iterator<LinkedList<String>> indexIterator = index.iterator();
	    while (indexIterator.hasNext()) {
	    	if (isFound(textAsList, indexIterator.next())) {
	    		return true;
	    	}
	    }
	    return false;
	}

	private boolean isFound(List<String> originalPhrase, List<String> blacklistedPhrase) {
		Iterator<String> blacklistedIterator = blacklistedPhrase.iterator();
		Iterator<String> originalTextIterator = originalPhrase.iterator();
		String nextBlacklistedWord;
		String nextOriginalWord;
		boolean stop;
		while (blacklistedIterator.hasNext()) {
			stop = false;
			nextBlacklistedWord = blacklistedIterator.next();
			
			while (!stop) {
				if(originalTextIterator.hasNext()) {
					nextOriginalWord = originalTextIterator.next();
					 if (StringUtils.equals(nextOriginalWord, nextBlacklistedWord)) {
						 stop = true;
					 }				 				 
				 } else {
					 //original phrase ended and words in the blacklisted phrase weren't compared
					 return false;
				 }
			}
		}
		 
		 //all words in the phrase were compared and they matched!
		 return true;
	}

	/**
	 * Generate a structure with the blacklist words to be used on the items
	 * validation. Every time the blacklist is updated it should be run.
	 */
	public void generateBlacklistIndex() {
		index = new LinkedList<LinkedList<String>>();
		List<String> descriptions = blackListDao
		.findAllBlackListDescription();
		
		for (String description : descriptions) {
			addPhrase(description);
		}
		
		LOGGER.debug("Blacklist updated");
	}

	private void addPhrase(String description) {
		LinkedList<String> phraseList = new LinkedList<String>();
		
		String[] words = description.split(" ");
	    for (String word : words) {
	        phraseList.add(word);
	    }
	    index.add(phraseList);
	}
}
