package com.ebay.park.config;

import com.ebay.park.elasticsearch.listeners.ParkPostCommitDeleteEventListener;
import com.ebay.park.elasticsearch.listeners.ParkPostCommitInsertEventListener;
import com.ebay.park.elasticsearch.listeners.ParkPostCommitUpdateEventListener;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

/**
 * @author l.marino on 6/3/15.
 */
@Component
public class HibernateListenersConfigurer {

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private ParkPostCommitInsertEventListener insertEventListener;
    
    @Autowired
    private ParkPostCommitUpdateEventListener updateEventListener;
    
    @Autowired
    private ParkPostCommitDeleteEventListener deleteEventListener;

    @PostConstruct
    public void registerListeners(){
        HibernateEntityManagerFactory hibernateEntityManager = (HibernateEntityManagerFactory) this.entityManagerFactory;
        SessionFactoryImpl sessionFactory = (SessionFactoryImpl) hibernateEntityManager.getSessionFactory();
        EventListenerRegistry eventListenerRegistry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        eventListenerRegistry.getEventListenerGroup(EventType.POST_COMMIT_INSERT).appendListener(insertEventListener);
        eventListenerRegistry.getEventListenerGroup(EventType.POST_COMMIT_UPDATE).appendListener(updateEventListener);
        eventListenerRegistry.getEventListenerGroup(EventType.POST_COMMIT_DELETE).appendListener(deleteEventListener);
    }
    
}
