package com.ebay.park.service.session.command;

import static com.ebay.park.service.ServiceException.createServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ebay.park.db.dao.OrphanedDeviceDao;
import com.ebay.park.db.entity.DeviceType;
import com.ebay.park.db.entity.User;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceExceptionCode;
import com.ebay.park.service.device.command.RemoveUserSessionsByDeviceCmd;
import com.ebay.park.service.device.dto.RemoveUserSessionsByDeviceRequest;
import com.ebay.park.service.session.SessionService;
import com.ebay.park.service.user.UserServiceHelper;
import com.ebay.park.service.user.dto.SignRequest;
import com.ebay.park.util.ParkTokenUtil;

/**
 * Cmd to create sessions and to reorganize other sessions, devices with owner and orphaned devices.
 * @author gabriel.sideri & Julieta Salvadó
 *
 */
@Component
	public class CreateSessionCmd implements ServiceCommand<SignRequest, String>{

	@Autowired
	private SessionService sessionService;

	@Autowired
	private ParkTokenUtil tokenUtil;
	
	@Autowired
	private OrphanedDeviceDao orphanedDeviceDao;
	
	@Autowired
	private RemoveUserSessionsByDeviceCmd removeUserSessionsByDeviceCmd;

	@Autowired
	private UserServiceHelper userServiceHelper;

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public String execute(SignRequest request) throws ServiceException {

		User user = userServiceHelper.findUserByUsernameOrEmail(request.getUsername(), request.getEmail());
		if (user == null) {
			throw createServiceException(ServiceExceptionCode.USER_NOT_FOUND);
		}

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
		
		user.addUserSession(userSession);

		//Create Session Cache
		sessionService.createUserSessionCache(userSession);

		return sessionToken;

	}
}