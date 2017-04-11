package com.ebay.park.service.group.validator;

import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.ServiceValidator;
import com.ebay.park.service.group.dto.RemoveGroupItemsRequest;
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
public class RemoveGroupItemsValidator implements
		ServiceValidator<RemoveGroupItemsRequest>, ParkConstants {
	
	@Value("${group.remove_group_items_limit}")
	private int MAX_ITEMS_TO_PROCCESS;
	
	@Value("${group.unfollow_group_users_max_limit}")
	private int MAX_FOLLOWERS_TO_PROCCESS;
	
	@Override
	public void validate(RemoveGroupItemsRequest request) {
				
		//Parse items ids & verify if it are long type. 
		String[] ids = request.getIds().split(",");
		
		List <Long> idsValidated = new ArrayList<Long>(ids.length);
		
		int maxLengthToVerify = request.isDeleteByUsersIds() ? MAX_FOLLOWERS_TO_PROCCESS : MAX_ITEMS_TO_PROCCESS;
		
		maxLengthToVerify = maxLengthToVerify < ids.length ? maxLengthToVerify
				: ids.length;
				
		for (int i = 0; i < maxLengthToVerify; i++) {
			if (StringUtils.isNumeric(ids[i].trim()) && !ids[i].equals("")) {
				idsValidated.add(Long.parseLong(ids[i].trim()));
			}
		}
		
		if (idsValidated.isEmpty()) {

			if (request.isDeleteByUsersIds()) {
				throw createServiceException(ServiceExceptionCode.INVALID_USER_IDS);
			}

			throw createServiceException(ServiceExceptionCode.INVALID_ITEM_IDS);
		}
		
		request.setIdsValidated(idsValidated);
	}

}
