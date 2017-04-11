package com.ebay.park.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * It configures HTTP accesses.
 * @author Julieta Salvadó
 */
@Configuration
public class HTTPAccessConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
