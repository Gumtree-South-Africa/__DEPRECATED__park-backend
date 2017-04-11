package com.ebay.park.push.swrve;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * This class is responsible to send push notifications to Swrve "transactional push notifications".
 * It basically knows how deal with Swrve, that's it. Please resolve domain business specific stuff outside (such as decide which
 * push_key should be used)
 *
 * More information: https://docs.swrve.com/swrves-apis/api-guides/swrve-push-api-guide/
 *
 * @author gervasio.amy
 * @since 14/12/2016.
 * @see RestTemplate
 */
@Component
public class SwrvePusher {

    private static Logger logger = LoggerFactory.getLogger(SwrvePusher.class);

    protected static String swrveUrl = "https://service.swrve.com/push";

    @Autowired
    protected RestTemplate restTemplate;

    /**
     * Sends the push though HTTP POST to Swrve using the default Swrve message
     *
     * @param swrvePushKey which Swrve push notication is about to be sent?
     * @param swrveUserId to which Swrve user?
     */
    public SwrvePushResponse sendPush(String swrvePushKey, String swrveUserId) {
        return this.sendPush(swrvePushKey, swrveUserId, null);
    }

    /**
     * Sends the push though HTTP POST to Swrve
     *
     * @param swrvePushKey which Swrve push notication is about to be sent?
     * @param swrveUserId to which Swrve user?
     * @param message if the message is going to be overwritten,
     */
    public SwrvePushResponse sendPush(String swrvePushKey, String swrveUserId, String message) {
        Assert.notNull(swrvePushKey, "swrvePushKey must not be null");
        Assert.notNull(swrveUserId, "swrveUserId must not be null");

        SwrvePushRequest request = new SwrvePushRequest();
        request.setPush_key(swrvePushKey);
        request.setUser(swrveUserId);
        request.setMessage(message);
        try {
            SwrvePushResponse response = restTemplate.postForObject(swrveUrl, request.toMultiValueMap(), SwrvePushResponse.class);
            return this.handleResponse(response);
        } catch (HttpClientErrorException e) {
            logger.warn("An HTTP error happened while trying to POST to Swrve - [SwrveKey: {} / SwrveUserId: {} / Message: {} ]",
                    swrvePushKey, swrvePushKey, message, e);
        }
        return null;
    }

    /**
     * If {@link SwrvePushResponse#code} is 200, there's nothing to do, everything is fine, but if a non 200 code is received, something
     * went wrong, so, it will be logged as WARN
     *
     * @param response
     */
    private SwrvePushResponse handleResponse(SwrvePushResponse response) {
        if (response.getCode() != 200) {
            logger.warn("There was an issue while trying to send a push notification through Swrve. The error received is (and it's all " +
                            "information we have) : {}", response);
        }
        return response;
    }
}