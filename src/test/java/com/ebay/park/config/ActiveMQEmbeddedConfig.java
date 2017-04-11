package com.ebay.park.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.ConnectionFactory;

/**
 * Configuration class to start an embedded ActiveMQ broker, for testing
 *
 * @author gervasio.amy
 * @since 14/09/2016.
 */
@Configuration
public class ActiveMQEmbeddedConfig {

    public static final String JMS_BROKER_URL = "vm://embedded";

    private BrokerService broker;

    @PostConstruct
    public void startActiveMQ() throws Exception {
        broker = new BrokerService();
        // configure the broker
        broker.setBrokerName("activemq-broker");
        //broker.setDataDirectory("target");
        broker.setPersistent(false);
        broker.addConnector(JMS_BROKER_URL);
        broker.setUseJmx(false);
        broker.setUseShutdownHook(false);
        broker.start();
    }

    @PreDestroy
    public void stopActiveMQ() throws Exception {
        broker.stop();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(JMS_BROKER_URL + "?broker.persistent=false");
        // We need to specify which packages are "trusted", for testing propouses let's use *
        // more info: http://activemq.apache.org/objectmessage.html
        factory.setTrustAllPackages(true);
        return factory;
    }
}