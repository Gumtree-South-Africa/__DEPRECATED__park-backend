package com.ebay.park.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.support.destination.BeanFactoryDestinationResolver;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

/**
 * All spring config needed to connect to ActiveMQ. We are now using SpringBoot's default implementation.
 * the following properties must be defined in order to set this up:
 * <ul>
 * <li>spring.activemq.broker-url= FOR EX: tcp://192.168.1.210:9876
 * <li>spring.activemq.user= FOR EX: admin
 * <li>spring.activemq.password= FOR EX: secret
 * </ul>
 * <p><b>Important:</b> we are using {@link BeanFactoryDestinationResolver} to resolve queues as beans, so, please define queues as
 * beans! otherwise, they will be not recognized</p>
 *
 * @author gervasio.amy
 * @since 05/09/2016.
 * @see org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties for more configuration properties
 * @see ActiveMQQueue
 */
@Configuration
@EnableJms
public class JmsConfig {

    @Autowired
    private BeanFactory springContextBeanFactory;

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDestinationResolver(new BeanFactoryDestinationResolver(springContextBeanFactory));
        //factory.setConcurrency("3-10");
        return factory;
    }

    @Bean
    public JmsMessagingTemplate jmsMessagingTemplate(ConnectionFactory connectionFactory) {
        return new JmsMessagingTemplate(connectionFactory);
    }

    @Bean(name="pushQueueDestination")
    public Destination pushQueueDestination(@Value("${jms.queue.push}") String queueName) {
        return new ActiveMQQueue(queueName);
    }

    @Bean(name="emailQueueDestination")
    public Destination mailQueueDestination(@Value("${jms.queue.email}") String queueName) {
        return new ActiveMQQueue(queueName);
    }

}
