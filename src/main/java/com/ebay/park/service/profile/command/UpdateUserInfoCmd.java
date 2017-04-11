package com.ebay.park.service.profile.command;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.profile.dto.UserInfoRequest;
import com.ebay.park.service.profile.dto.UserInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ebay.park.service.ServiceException.createServiceException;

/**
 * Command to update user basic profile data
 * 
 * @author cbirge
 * 
 */
@Component
public class UpdateUserInfoCmd implements
		ServiceCommand<UserInfoRequest, UserInfoResponse> {

	@Autowired
	private UserDao userDao;

	@Override
	public UserInfoResponse execute(UserInfoRequest request)
			throws ServiceException {
		User user = userDao.findByUsername(request.getUsername());
		if (user == null)
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		user = updateUser(request, user);
		try {
			return new UserInfoResponse(userDao.save(user));
		} catch (Exception e) {
			throw createServiceException(ServiceExceptionCode.INVALID_USERINFO_UPDATE_REQ);
		}

	}

	/**
	 * Method to update user profile information with the ones provided on
	 * request
	 * 
	 * @param request
	 * @param userToUpdate
	 * @return User updated
	 */
	protected User updateUser(UserInfoRequest request, User userToUpdate) {

		if (request.getLocation() != null) {
			int indexOf = request.getLocation().indexOf(",");
			userToUpdate.setLatitude(Double.valueOf(request.getLocation()
					.substring(0, indexOf)));
			userToUpdate.setLongitude(Double.valueOf(request.getLocation()
					.substring(indexOf + 1)));
			userToUpdate.setLocationName(request.getLocationName());
			userToUpdate.setZipCode(request.getZipCode());
		}

		if (request.getPicture() != null) {
			userToUpdate.setPicture(request.getPicture());
		}

		if (request.getMobile() != null) {
			userToUpdate.setMobile(request.getMobile());
		}

		return userToUpdate;
	}
}
