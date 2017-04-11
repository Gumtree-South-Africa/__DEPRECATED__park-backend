package com.ebay.park.config;

import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Created by lmarino on 6/3/15.
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.ebay.park.elasticsearch.repository")
public class ElasticsearchConfig {

    @Value("${elasticsearch.cluster.name}")
    private String clusterName;

    @Value("${elasticsearch.cluster.nodes}")
    private String clusterNodes;

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate(@Qualifier(value = "client")TransportClient client){
        ElasticsearchTemplate template = new ElasticsearchTemplate(client);
        return template;
    }

    @Bean(name = "client")
    public TransportClientFactoryBean client() {
        TransportClientFactoryBean bean = new TransportClientFactoryBean();
        bean.setClusterName(clusterName);
        bean.setClusterNodes(clusterNodes);
        return bean;
    }
}
