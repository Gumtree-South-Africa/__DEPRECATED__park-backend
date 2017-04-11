package com.ebay.park.util;

import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;


/**
 * lucia.masola
 */

@Component
public class MessageUtil {
	
	private static Logger logger = LoggerFactory.getLogger(MessageUtil.class);

	protected static final String DEFAULT_LOCALE = "es"; //Current default app locale switch to "es". Previously was "en"
	protected static final String SPANISH_LANGUAGE = "es";
	
	@Autowired
	private MessageSource messageSource;

	/**
	 * Return a formatted message in the language specified in <code>lang</code>. It uses the <code>messageProps</code>
	 * to replace the placeholders in the message obtained from the <code>messageSource</code>
	 * @param messageKey a single String with the key of the message to be obtain from <code>messageSource</code>
	 * @param messageProps a Map<String,String> with the properties to be replaced in the <code>messageTemplate</code>
	 * @param lang a single String representing the language of the message
	 * @return a single String with the formatted message
	 */
	public String formatMessage(String messageKey, Map<String, String> messageProps, String lang){	
		Locale locale = null;
		try{
			locale = Locale.forLanguageTag(lang != null ? lang : DEFAULT_LOCALE); 
			
			if (!(locale.getLanguage().equals(Locale.ENGLISH.getLanguage())
					|| locale.getLanguage().equals(new Locale(SPANISH_LANGUAGE).getLanguage()))) {
				locale = Locale.forLanguageTag(DEFAULT_LOCALE); 
			}

		} catch(Exception e) {
            logger.warn("Issue with locale: {} using default en", lang, e);
			locale = Locale.forLanguageTag(DEFAULT_LOCALE); 
		}
        	    
		
		String messageTemplate = messageSource.getMessage(messageKey, null, locale);
        return StrSubstitutor.replace(messageTemplate, messageProps);
	}
	
	public String formatMessageWithProps(String messageKey, String[] props, String lang) {
		Locale locale = null;
		try{
			locale = Locale.forLanguageTag(lang != null ? lang : DEFAULT_LOCALE); 
			
			if (!(locale.getLanguage().equals(Locale.ENGLISH.getLanguage())
					|| locale.getLanguage().equals(new Locale(SPANISH_LANGUAGE).getLanguage()))) {
				locale = Locale.forLanguageTag(DEFAULT_LOCALE); 
			}

		} catch(Exception e) {
            logger.error("Issue with locale: {} using default en", lang, e);
			locale = Locale.forLanguageTag(DEFAULT_LOCALE); 
		}
		
		return messageSource.getMessage(messageKey, props, locale);
	}
}
