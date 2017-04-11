package com.ebay.park.service.social.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.park.service.ServiceCommand;
import com.ebay.park.service.ServiceException;
import com.ebay.park.service.group.GroupService;
import com.ebay.park.service.notification.FeedServiceHelper;
import com.ebay.park.service.social.dto.UnreadCountRequest;
import com.ebay.park.service.social.dto.UnreadCountResponse;

/**
 * 
 * @author Julieta Salvadó
 *
 */
@Component
public class UnreadCountCmd implements
ServiceCommand<UnreadCountRequest, UnreadCountResponse>{

	@Autowired
	private FeedServiceHelper feedServiceHelper;
	
	@Autowired
	private GroupService groupService;
	
	@Override
	public UnreadCountResponse execute(UnreadCountRequest request)
			throws ServiceException {
		UnreadCountResponse response = new UnreadCountResponse();
		response.setUnreadFeeds(feedServiceHelper.countUnreadFeeds(request.getToken()));
		response.setUnreadGroupItems(groupService.countUnreadGroupItems(request.getToken()));
		return response;
	}

}
