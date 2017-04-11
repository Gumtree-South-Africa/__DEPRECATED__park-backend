package com.ebay.park.service.device.command;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.db.dao.OrphanedDeviceDao;
import com.ebay.park.db.dao.UserSessionDao;
import com.ebay.park.db.entity.OrphanedDevice;
import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.device.dto.DeviceRegistrationRequest;

/**
 * Cmd to store the uuid if it is not registered to any user. Any session stored with the same uuid
 * will be deleted when a new access is registered.
 * @author Julieta Salvadó
 *
 */
@Component
public class DeviceRegistrationCmd implements ServiceCommand <DeviceRegistrationRequest, ServiceResponse>{

	@Autowired
	private UserSessionDao userSessionDao;
	
	@Autowired
	private OrphanedDeviceDao orphanedDeviceDao;
	
	private static Logger logger = LoggerFactory.getLogger(DeviceRegistrationCmd.class);
	
	@Override
	public ServiceResponse execute(DeviceRegistrationRequest request)
			throws ServiceException {

		boolean existingOrphanedDevice =  orphanedDeviceDao.findByUniqueDeviceId(request.getUniqueDeviceId()) != null;
        logger.info("existingOrphanedDevice: {} with uuid {}", existingOrphanedDevice, request.getUniqueDeviceId());

		if (!existingOrphanedDevice) {
			List<UserSession> existingSession = userSessionDao.findUserSessionsByUniqueDeviceId(request.getUniqueDeviceId());

			boolean sessionExists = !existingSession.isEmpty();
            logger.info("sessionExists: {} with uuid {}", sessionExists, request.getUniqueDeviceId());

            logger.info("values before verifying unrecovering. request.getDeviceId(): {}request.getDeviceType(): {} sessions with the same device values {}", request.getDeviceId(), request.getDeviceType(), userSessionDao.findUserSessionsByDeviceId(request.getDeviceId(), request.getDeviceType()).isEmpty());

			/*if the user does not have a previous session then a new orphaned device should be added*/
			if (!sessionExists) {
				logger.info("Saving because session does not exist");
				orphanedDeviceDao.save(new OrphanedDevice(request.getUniqueDeviceId(),
						request.getDeviceId(), request.getDeviceType()));
			} else if (request.getDeviceId() != null && request.getDeviceType() != null  && 
					((request.getNewInstall() != null && request.getNewInstall() == true) || (request.getNewInstall() == null &&
					userSessionDao.findUserSessionsByDeviceId(request.getDeviceId(), request.getDeviceType()).isEmpty()))){
			/*if the user has a previous session, that cannot be recovered
			 * (because it is a new installation and the token is not available in the app)*/
				logger.info("Saving because session does not exist and deleting because of unrecovered session");
				orphanedDeviceDao.save(new OrphanedDevice(request.getUniqueDeviceId(),
						request.getDeviceId(), request.getDeviceType()));
				//delete unrecovered session
				userSessionDao.delete(existingSession);
			}
			/*Otherwise, the previous session should be automatically recovered*/
		}

		return ServiceResponse.SUCCESS;
	}

}
