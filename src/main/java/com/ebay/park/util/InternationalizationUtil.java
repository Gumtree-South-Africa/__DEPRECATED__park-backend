package com.ebay.park.util;

import com.ebay.park.db.entity.Category;
import com.ebay.park.db.entity.Item;
import com.ebay.park.service.ListedResponse;
import com.ebay.park.service.ParkRequest;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.conversation.dto.Offer;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

@Component
public class InternationalizationUtil {
	@Autowired
	private SessionService sessionService;

	@Autowired
	private MessageUtil messageUtil;

	/**
	 * DO NOT EVER SAVE OR UPDATE THE RETURNED CATEGORIES Changes the categories
	 * names to their internationalized names.
	 * 
	 * @param categories
	 * 			list of categories
	 * @param language
	 * 			target language
	 * @return
	 */
	public List<Category> internationalize(List<Category> categories, String language) {
		for (Category c : categories) {
			internationalize(c, language);
		}

		return categories;
	}

	public Category internationalize(Category c, String langParam) {
		String i18nName = internationalizeCategoryName(c.getKey(), langParam);
		c.setName(i18nName);
		return c;
	}

	/**
	 * It internationalize the given name.
	 * @param name the name to be internationalized
	 * @param langParam language. See rules at {@link LanguageUtil}.
	 * @return the name in the given language
	 */
	public String internationalizeCategoryName(String name, String langParam) {
		Assert.notNull(name, "The name to be internationalized must be not null");
		return internationalize(name, langParam);
	}

	/**
	 * It internationalize the given name.
	 * @param name the name to be internationalized
	 * @param langParam language. See rules at {@link LanguageUtil}.
	 * @return the name in the given language
	 */
	public String internationalize(String name, String langParam) {
		String lang = LanguageUtil.getValidLanguage(langParam);
		return messageUtil.formatMessage(name, null, lang);
	}

	public Offer internationalize(Offer o, String token) {
		String i18nName = internationalizeCategoryName(o.getItem()
				.getCategory().getName(), getLanguage(token));
		o.getItem().getCategory().setName(i18nName);
		return o;
	}

	private String getLanguage(String token) {
		String lang = null;
		if (StringUtils.isNotBlank(token)) {
			try {
				UserSessionCache session = sessionService.getUserSession(token);
				lang = session.getLang();
			} catch(ServiceException se) {
				lang = LanguageUtil.DEFAULT_LANGUAGE;
			}
		} else {
			lang = LanguageUtil.DEFAULT_LANGUAGE;
		}
		return lang;
	}

	public List<Item> internationalizeItems(List<Item> items, ParkRequest request) {
		String lang = request.getLanguage() != null ? request.getLanguage() : getLanguage(request.getToken());
		for (Item item : items) {
			internationalize(item.getCategory(), lang);
			try{
				item.setLocalizedStatus(messageUtil.formatMessage("item." + item.getStatus().toString().toLowerCase(), null, lang));
			}catch(Exception e){
				item.setLocalizedStatus(item.getStatus().toString());
			}
		}
		return items;
	}

	public void internationalizeListedResponse(ListedResponse response,
			String message, String lang) {
		if (response.listIsEmpty()) {
			response.setNoResultsMessage(messageUtil.formatMessage(message,
					null, lang));
			try{
				response.setNoResultsHintMessage(messageUtil.formatMessage(message + "_hint",
						null, lang));
			}catch(Exception e){
				// do noting
			}
		}
	}

	public String internationalizeMessage(String message, String lang) {
		return messageUtil.formatMessage(message, null, lang);

	}
	
	public String internationalizeMessage(String message, String lang, String[] params) {
		return messageUtil.formatMessageWithProps(message, params, lang);
	}

}
