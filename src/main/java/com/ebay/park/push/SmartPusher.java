package com.ebay.park.push;

/**
 * A smart pusher must recognize the received {@link PushNotification} platform (Android or iOS) and take actions  regarding that, i.e.,
 * to use GCM or APNS senders for example...
 *
 * @author gervasio.amy
 * @since 15/09/2016.
 */
public interface SmartPusher {

    /**
     * Pushes the received {@link PushNotification} smartly depending on the platform (Android, iOS, etc)
     *
     * @param notification the notification to be pushed
     * @throws IllegalArgumentException if notification is null
     */
    void push(PushNotification notification);

}
