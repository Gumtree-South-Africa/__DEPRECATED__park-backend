package com.ebay.park.service.social;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.SocialDao;
import com.ebay.park.db.entity.Social;

@Component
public class SocialIds {
	@Autowired
	private SocialDao socialDao;
	
	/**
	 * It maps <social-description, social-id>.
	 */
	private HashMap<String, Long> socialIds;
	
	//getting unmodifiable HashMap
    private Map<String, Long> unmodifiableMap;

	@PostConstruct
	public void initializeSocialIds() {
		socialIds = new HashMap<String, Long>();
		socialIds.put(Social.FACEBOOK, 
				socialDao.findByDescription(Social.FACEBOOK).getSocialId());
		socialIds.put(Social.TWITTER, 
				socialDao.findByDescription(Social.TWITTER).getSocialId());
		unmodifiableMap = Collections.unmodifiableMap(socialIds);
	}
	
	public Long getFacebookId() {
		if (unmodifiableMap.containsKey(Social.FACEBOOK)) {
			return unmodifiableMap.get(Social.FACEBOOK);
		}
		return null;
	}
	
	public Long getTwitterId() {
		if (unmodifiableMap.containsKey(Social.TWITTER)) {
			return unmodifiableMap.get(Social.TWITTER);
		}
		return null;
	}
}
