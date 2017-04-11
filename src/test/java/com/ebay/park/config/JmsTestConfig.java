package com.ebay.park.config;

import com.ebay.park.db.dao.FeedDao;
import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.NotificationConfigDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.email.MailSender;
import com.ebay.park.notification.NotificationDispatcher;
import com.ebay.park.notification.consumer.EmailNotificationConsumer;
import com.ebay.park.notification.consumer.PushNotificationConsumer;
import com.ebay.park.push.SmartPusher;
import com.ebay.park.queue.FeedNotificationHandler;
import com.ebay.park.queue.JmsNotificationQueue;
import com.ebay.park.util.FeedUtils;
import com.ebay.park.util.MessageUtil;
import com.ebay.park.util.TextUtils;
import org.mockito.Mockito;
import org.springframework.context.annotation.*;
import org.springframework.jms.annotation.EnableJms;

import static org.mockito.Mockito.mock;

/**
 * Starts a JMS embedded server non persistent for integration testing proposes
 *
 * @author gervasio.amy
 * @since 13/09/2016.
 */
@Configuration
@ComponentScan(
        basePackages = { "com.ebay.park.push", "com.ebay.park.notification", "com.ebay.park.util", "com.ebay.park.queue" },
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { NotificationDispatcher.class, MessageUtil.class,
                        JmsNotificationQueue.class, PushNotificationConsumer.class, FeedNotificationHandler.class, FeedUtils.class,
                        TextUtils.class, EmailNotificationConsumer.class,  })
        }
)
@PropertySource("classpath:properties/jmsTestEnv.properties")
@EnableJms
@EnableAspectJAutoProxy
public class JmsTestConfig {

    @Bean
    public SmartPusher smartPusher() {
        return mock(SmartPusher.class);
    }

    @Bean
    public MailSender mailSender() {
        return mock(MailSender.class);
    }

    @Bean
    public UserDao createMockUserDao() {
        return mock(UserDao.class);
    }

    @Bean
    public ItemDao createMockItemDao() {
        return mock(ItemDao.class);
    }

    @Bean
    public FeedDao createMockFeedDao() {
        return mock(FeedDao.class);
    }

    @Bean
    public NotificationConfigDao createMockNotificationConfigDao() {
        return mock(NotificationConfigDao.class);
    }



}
