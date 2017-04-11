package com.ebay.park.service.session.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.OrphanedDeviceDao;
import com.ebay.park.db.entity.DeviceType;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.device.command.RemoveUserSessionsByDeviceCmd;
import com.ebay.park.service.device.dto.RemoveUserSessionsByDeviceRequest;
import com.ebay.park.service.user.dto.signin.SignInRequest;
import com.ebay.park.util.ParkTokenUtil;

/**
 * Command to perform user session creation.
 * Once legacy signIn/signUp will be removed, CreateSessionCmd 
 * {@link CreateSessionCmd} should be deprecated.
 * @author scalderon
 * @since 2.0.2
 *
 */
@Component
public class CreateUserSessionCmd implements ServiceCommand<SignInRequest, UserSession> {
	
	@Autowired
	private ParkTokenUtil tokenUtil;
	
	@Autowired
	private OrphanedDeviceDao orphanedDeviceDao;
	
	@Autowired
	private RemoveUserSessionsByDeviceCmd removeUserSessionsByDeviceCmd;

	@Override
	public UserSession execute(SignInRequest request) throws ServiceException {
		
		String sessionToken = tokenUtil.createSessionToken();
		UserSession userSession;
		if (request.getDevice() != null) {
			userSession = new UserSession(sessionToken, request.getDevice().getUniqueDeviceId());
			
			DeviceType platform = request.getDevice().getDeviceType() == null ? null : DeviceType.getDeviceByValue(request.getDevice().getDeviceType());
			
			if (platform != null && request.getDevice().getDeviceId() != null) {
				//Remove the device from the orphaned device table
				orphanedDeviceDao.delete(request.getDevice().getUniqueDeviceId());

				//Remove & sign out users with the same unique device id or device id (if the first does not exist)
				removeUserSessionsByDeviceCmd.execute(
						new RemoveUserSessionsByDeviceRequest(request.getDevice().getUniqueDeviceId(),
								request.getDevice().getDeviceId(), platform));

				userSession.addDevice(request.getDevice().getDeviceId(),platform);
			}
			//Add swrve ID to user session
			userSession.setSwrveId(request.getDevice().getSwrveId());
		} else {
			userSession = new UserSession(sessionToken, null);
		}
		
		return userSession;
	}

}
