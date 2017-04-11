package com.ebay.park.util;

import com.ebay.park.db.entity.Item;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.text.Normalizer;
import java.text.Normalizer.Form;

/**
 * Utilities class for processing text.
 *
 */
@Component
public class TextUtils {

	private static final String DOUBLE_QUOTES = "\"";

	@Value("${item.urlForItem}")
	private String itemURL;
	
	@Value("${group.urlForGroup}")
	private  String groupURL;

	@Value("${profile.urlForProfile}")
	private String profileURL;

	@Autowired
	private InternationalizationUtil i18nUtil;
	
	/**
	 * Creates a friendly SEO URL for an item.
	 * @param categoryName
	 * 			name of the category
	 * @param itemName
	 * 			name of the item
	 * @param itemId
	 * 			id of the item
	 * @return
	 * 		URL
	 */
	public String createItemSEOURL(String categoryName, String itemName, Long itemId) {
		return String.format(itemURL, makeURL(categoryName), makeURL(itemName), itemId);
	}

	/**
	 * Creates a friendly SEO URL for a group.
	 * @param groupName
	 * 			name of the group
	 * @param groupId
	 * 			id of the group
	 * @return
	 * 		URL created
	 */
	public String createGroupSEOURL(String groupName, Long groupId) {
		return String.format(groupURL, makeURL(groupName), groupId);
	}

	private String makeURL(String string) {		
		if (StringUtils.isBlank(string)) {
			return string;
		}	
		 return Normalizer.normalize(string.toLowerCase(), Form.NFD) //lowercase and normalization
		    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "") //accented characters conversion
		    .replaceAll("[^0-9a-zA-Z-]", "-") //character conversion to hyphen
		    .replaceAll("[-]+", "-")
			.replaceAll("^-", "")
			.replaceAll("-$", "");
	}

	public String createProfileSEOURL(String username) {
		return String.format(profileURL, makeURL(username));
	}

	/**
	 * It build a new path from a original path and a file name.
	 * @param path
	 * 		original path
	 * @param fileName
	 * 		file name
     * @return
	 * 		built path
     */
	public String buildFullPath(String path, String fileName) {
		Assert.notNull(path, "Path must not be null here");
		Assert.notNull(fileName, "File name must not be null here");

		return String.format("%s/%s", path, fileName);
	}

	public String escapeCharactersForHTML(String text) {
		return StringEscapeUtils.escapeHtml(text);
	}

	/**
	 * It creates the SEO URL for the given item.
	 * @param item	the base item
	 * @param lang the language
	 * @return	url for the item
	 */
	public String createItemSEOURL(Item item, String lang) {
		Assert.notNull(item, "'item' must be not null");
		return createItemSEOURL(
				i18nUtil.internationalizeCategoryName(item.getCategory().getKey(), lang),
				item.getName(),
				item.getId());
	}
	
	/**
	 * It creates a capitalized string.
	 * @param toCapitalize
	 * @return a capitalized string
	 */
	public String capitalize(String toCapitalize) {
		Assert.notNull(toCapitalize, "The string to modifiy cannot be null");
		return toCapitalize.substring(0,1).toUpperCase() + toCapitalize.substring(1).toLowerCase();
	}
	
	/**
	 * It returns a double quoted string.
	 * @param toDoubleQuote
	 * @return a double quoted string
	 */
	public String doubleQuote(String toDoubleQuote) {
		Assert.notNull(toDoubleQuote, "The string to modifiy cannot be null");
		return DOUBLE_QUOTES + toDoubleQuote + DOUBLE_QUOTES;
	}
}
