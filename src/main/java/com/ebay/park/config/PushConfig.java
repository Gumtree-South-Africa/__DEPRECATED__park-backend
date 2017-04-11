/*
 * Copyright eBay, 2014
 */
package com.ebay.park.config;

import com.ebay.park.push.AndroidPushBatchExecutor;
import com.ebay.park.push.IOSPushBatchExecutor;
import com.google.android.gcm.server.Sender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Beans definition for Android push notification sender.
 *
 * @author jpizarro
 * @author gervasio.amy
 * @see IOSPushBatchExecutor
 * @see AndroidPushBatchExecutor
 */
@Configuration
public class PushConfig {

    @Value("${push_notifications.android.app_server_key}")
    private String gcmKey;

    @Bean
    Sender sender() {
        return new Sender(gcmKey);
    }

}