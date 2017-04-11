package com.ebay.park.service.item.command;

import com.ebay.park.db.entity.UserFollowsItem;
import com.ebay.park.service.ServiceExceptionCode;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * Make user unfollow an item
 * 
 * @author federico.jaite
 * 
 */
@Component
public class UserUnfollowItemCmd extends UserFollowItemCmd {

	@Override
	protected void execute(UserFollowsItem userFollowsItem) {
		UserFollowsItem followed = getUserFollowsItemDao().findOne(
				userFollowsItem.getId());

		if (followed == null) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOLLOWED_BY_USER);
		}

		getUserFollowsItemDao().delete(userFollowsItem);
	}

}