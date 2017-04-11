package com.ebay.park.service.user.command;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ebay.park.db.entity.UserSession;
import com.ebay.park.service.device.command.RemoveUserSessionsCmd;
import com.ebay.park.service.user.dto.RemoveInactiveSessionsRequest;
import com.ebay.park.util.DataCommonUtil;

/**
 * Cmd to delete inactive web sessions.
 * @author Julieta Salvadó
 *
 */
@Component
public class RemoveInactiveSessionsCmd extends RemoveUserSessionsCmd<RemoveInactiveSessionsRequest> {

	@Value("${scheduler.daysToDeleteSession}")
	private int numberOfDaysForSessionDelete;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RemoveInactiveSessionsCmd.class);

	@Override
	protected List<UserSession> getUserSessionsToRemove(
			RemoveInactiveSessionsRequest request) {
		//web sessions
		Date limitDate = DataCommonUtil.addDays(
				Calendar.getInstance().getTime(), -numberOfDaysForSessionDelete);

        LOGGER.info("Inactive web sessions with last update older than {} will be deleted", limitDate);

		return userSessionDao.findExpiredWebUserSessionsByUser(limitDate);
	}

}
