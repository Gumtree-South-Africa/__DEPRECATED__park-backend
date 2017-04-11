package com.ebay.park.service.user.finder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.UserDao;
import com.ebay.park.db.entity.Device;
import com.ebay.park.db.entity.User;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.session.dto.UserSessionCache;
import com.ebay.park.util.Identifiable;
import com.ebay.park.util.Identifiable.IdentifiableType;
import com.ebay.park.util.UserUtils;

@Component
public class UserInfoUtil {

	@Autowired
	protected SessionService session;

	@Autowired
	protected UserDao userDao;
	
	@Autowired
	private UserUtils userUtils;

	private User findUserInfoByToken(String token) {
		UserSessionCache userSession = session.getUserSession(token);
		if (userSession != null) {
			return userDao.findById(userSession.getUserId());
		} else {
			return userDao.findByToken(token);
		}
	}

	private User findUserInfoByUsername(String username) {
		return userDao.findByUsername(username);
	}

	public User findUser(Identifiable<String> id) {
		String value = id.getIdentityValue();
		IdentifiableType field = id.getIdentityField();
		if (IdentifiableType.TOKEN.equals(field)) {
			return this.findUserInfoByToken(value);
		} else if (IdentifiableType.USERNAME.equals(field)) {
			return this.findUserInfoByUsername(value);
		} else {
			return this.findUserById(Long.valueOf(value));
		}
	}

	public User findUserById(Long id) {
		return userDao.findById(id);
	}

	public List <User> findUsersByIds(List<Long> userIds) {
		return userDao.findByIdIn(userIds);
	}

	/**
	 * It finds the devices linked to a user.
	 * @param userId
	 * 		user to find devices linked to
	 * @param signedIn
	 * 		true for signed-in devices; otherwise, false.
	 * @return
	 * 		list of devices
	 */
	public List<Device> findDevicesByUser(Long userId, boolean signedIn) {
		return userUtils.findDevices(userId, signedIn);
	}
}
