package com.ebay.park.service.item.command;

import com.ebay.park.db.dao.ItemDao;
import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.dao.UserFollowsItemDao;
import com.ebay.park.db.entity.*;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.item.dto.UserItemRequest;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * Make user follow an item
 * 
 * @author federico.jaite
 * 
 */
@Component
public class UserFollowItemCmd implements ServiceCommand<UserItemRequest, Void> {

	@Autowired
	private UserDao userDao;

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private UserFollowsItemDao userFollowsItemDao;

	@Override
	public Void execute(UserItemRequest request)
			throws ServiceException {

		User user = userDao.findByToken(request.getToken());
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_UNAUTHORIZED);
		}

		Item item = itemDao.findOne(request.getItemId());
		if (item == null) {
			throw createServiceException(ServiceExceptionCode.ITEM_NOT_FOUND);
		}

		if (item.is(StatusDescription.EXPIRED)) {
			throw createServiceException(ServiceExceptionCode.ITEM_EXPIRED);
		}

		UserFollowsItem userFollowsItem = new UserFollowsItem();
		userFollowsItem.setItem(item);
		userFollowsItem.setUser(user);
		userFollowsItem.setDateFollowed(DateTime.now().toDate());
		userFollowsItem.setId(new UserFollowsItemPK(user, item));

		execute(userFollowsItem);
		return null;
	}

	protected void execute(UserFollowsItem userFollowsItem) {
		UserFollowsItem followed = userFollowsItemDao.findOne(userFollowsItem
				.getId());

		if (followed != null) {
			throw createServiceException(ServiceExceptionCode.ITEM_ALREADY_FOLLOWED_BY_USER);
		}

		userFollowsItemDao.save(userFollowsItem);
	}

	protected UserFollowsItemDao getUserFollowsItemDao() {
		return userFollowsItemDao;
	}
}