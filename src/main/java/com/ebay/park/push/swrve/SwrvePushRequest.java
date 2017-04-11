package com.ebay.park.push.swrve;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * This class represents a Swrve push request. The properties contained will be sent as POST parameters
 *
 * More information: https://docs.swrve.com/swrves-apis/api-guides/swrve-push-api-guide/
 *
 * @author gervasio.amy
 * @since 14/12/2016.
 * @see SwrvePusher
 */
public class SwrvePushRequest {

    /**
     * <b>required</b> The Push API key used to authenticate your campaign.
     */
    private String push_key;

    /**
     * <b>required</b> The Swrve user ID of the user who should receive the push notification.
     */
    private String user;

    /**
     * <b>optional</b> The push message text. This overrides the default message, if provided when defining the campaign in the
     * Transactional Push via API wizard
     */
    private String message;

    /**
     * <b>optional</b> The custom sound file name.
     */
    private String sound;


    public String getPush_key() {
        return push_key;
    }

    public void setPush_key(String push_key) {
        this.push_key = push_key;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }


    /**
     * Spring {@link org.springframework.web.client.RestTemplate} needs a body of type {@link MultiValueMap} to convert it to
     * <code>application/x-www-form-urlencoded</code> parameters properly, that's why we need this method here     *
     *
     * @return a {@link MultiValueMap} with push_key, user, message, sound and its values
     * @see org.springframework.http.converter.FormHttpMessageConverter
     */
    public MultiValueMap<String, String> toMultiValueMap() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("push_key", push_key);
        map.add("user", user);
        map.add("message", message);
        map.add("sound", sound);
        return map;
    }
}
