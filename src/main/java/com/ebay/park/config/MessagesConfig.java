/*
 * Copyright eBay, 2014
 */
package com.ebay.park.config;

import org.apache.commons.compress.utils.CharsetNames;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author juan.pizarro
 */
@Configuration
public class MessagesConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource bundle = new ReloadableResourceBundleMessageSource();
        bundle.setBasenames(new String[] { "classpath:/messages/messages" });
        bundle.setCacheSeconds(1);
        bundle.setFallbackToSystemLocale(false);
        bundle.setDefaultEncoding(CharsetNames.UTF_8);
        return bundle;
    }
}
