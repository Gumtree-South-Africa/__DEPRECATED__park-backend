package com.ebay.park.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.LocalTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.node.Node;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.elasticsearch.common.settings.Settings.settingsBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 * @author l.marino on 7/21/15.
 *
 *
 */

//@Configuration/*@PropertySource(value = "classpath:elasticsearch.properties")*/
//@EnableElasticsearchRepositories(basePackages = "com.ebay.park.elasticsearch.repository")
//public class ElasticsearchLocalConfig {
//    @Resource
//    private Environment environment;
//    @Bean
//    public Client client() {
////        TransportClient client = new TransportClient();
////        TransportAddress address = new InetSocketTransportAddress(
////              environment.getProperty("elasticsearch.host"),
////              Integer.parseInt(environment.getProperty("elasticsearch.port")));
////        client.addTransportAddress(address);
////        return client;
//
////        Settings settings = settingsBuilder().put("node.local", "true").build();
//
//        // Create a new Client with default options:
//
//        try (TransportClient transportClient = TransportClient.builder()/*.settings(settings)*/.build()) {
//
//            transportClient.addTransportAddress(new InetSocketTransportAddress(
//                    InetAddress.getByName("127.0.0.1"), 9300));
//
////            transportClient.addTransportAddress(new LocalTransportAddress("1"));
//            return transportClient;
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        return null;
//
//    }
//
//        @Bean
//    public ElasticsearchOperations elasticsearchTemplate() {
//        return new ElasticsearchTemplate(client());
//    }
//}

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.ebay.park.elasticsearch.repository")
public class ElasticsearchLocalConfig {

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate() {
        return new ElasticsearchTemplate(localElasticClient());
    }

    @Bean
    public Client localElasticClient(){
        Settings.Builder settings = Settings.settingsBuilder()
                .put("http.enabled", "false")
                .put("path.data", "target/elasticsearch-data")
                .put("path.home", "/path/to/elasticsearch/home/dir");

        Node node = nodeBuilder()
                .local(true)
                .settings(settings.build())
                .node();

        return node.client();
    }
}
//@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.ebay.park.elasticsearch.repository")
//public class ElasticsearchLocalConfig implements InitializingBean, DisposableBean {
//
//    private String clusterName;
//    private Node node;
//    private Client client;
//
//    @Override
//    public void afterPropertiesSet() {
//        node = new NodeBuilder()
//                .clusterName(clusterName)
//                .build().start();
//        client = node.client();
//    }
//
//    @Override
//    public void destroy() {
//        client.close();
//        node.close();
//    }
//
//    @Bean
//    public ElasticsearchTemplate elasticsearchTemplate() {
//        return new ElasticsearchTemplate(node.client());
//    }
//
//}