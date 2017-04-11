package com.ebay.park.service.device.command;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.device.dto.RemoveUserSessionsByDeviceRequest;

/**
 * This command removes user sessions that belongs to the same device.
 * @author Julieta Salvadó
 *
 */
@Component
public class RemoveUserSessionsByDeviceCmd extends RemoveUserSessionsCmd<RemoveUserSessionsByDeviceRequest>{
	
	@Override
	protected List<UserSession> getUserSessionsToRemove(
			RemoveUserSessionsByDeviceRequest request) {
		/*Find usersessions who has the same unique-device-id/device-id,
		 * sign out this token and delete the user-session from DB.*/
		if (StringUtils.isNotBlank(request.getUniqueDeviceId())) {
			return userSessionDao.findUserSessionsByUniqueDeviceId(request.getUniqueDeviceId());

		} else {
			return userSessionDao.findUserSessionsByDeviceId(
					request.getDeviceId(),
					request.getDeviceType());
		}
	}
}
