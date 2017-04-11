package com.ebay.park.service.group.validator;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.group.dto.UnsubscribeGroupFollowersRequest;
import com.ebay.park.util.ParkConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * @author gabriel.sideri
 */
@Component
public class UnsubscribeGroupFollowersValidator implements
		ServiceValidator<UnsubscribeGroupFollowersRequest>, ParkConstants {
	
	@Value("${group.unfollow_group_users_max_limit}")
	private int MAX_FOLLOWERS_TO_PROCCESS;
	
	@Override
	public void validate(UnsubscribeGroupFollowersRequest request) {
		//Parse users ids & verify if it are long type. 
		String[] userIds = request.getFollowersIds().split(",");
		
		List <Long> followersIdsValidated = new ArrayList<Long>(userIds.length);
		int maxFollowers = MAX_FOLLOWERS_TO_PROCCESS < userIds.length ? MAX_FOLLOWERS_TO_PROCCESS
				: userIds.length;
				
		for (int i = 0; i < maxFollowers; i++) {
			if (StringUtils.isNumeric(userIds[i].trim()) && !userIds[i].equals("")) {
				followersIdsValidated.add(Long.parseLong(userIds[i].trim()));
			}
		}
		
		if (followersIdsValidated.isEmpty()) {
			throw createServiceException(ServiceExceptionCode.INVALID_USER_IDS);
		}
		
		request.setFollowersIdsValidated(followersIdsValidated);
	}

}
