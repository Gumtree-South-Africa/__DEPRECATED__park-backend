package com.ebay.park.service.moderation.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.ebay.park.service.ServiceException;
import com.ebay.park.service.ServiceResponse;
import com.ebay.park.service.moderation.dto.RejectItemRequest;
import com.ebay.park.service.moderation.rejection.RejectItemExecutor;
import com.ebay.park.service.moderation.rejection.RejectItemType;

/**
 * Cmd that manages the delete and the different types of reasons behind it. 
 * @author Julieta Salvadó
 *
 */
@Component
public class RejectItemCmdImpl implements RejectItemCmd {
	
	@Autowired
	ApplicationContext ctx;
		
	@Override
    public ServiceResponse execute(RejectItemRequest request) throws ServiceException {
		RejectItemExecutor executor = (RejectItemExecutor) ctx.getBean(
				RejectItemType.fromId(request.getReasonId()).getExecutorClass());
		executor.execute(request);
		return ServiceResponse.SUCCESS;
	}
}